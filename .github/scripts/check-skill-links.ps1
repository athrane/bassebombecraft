<#
.SYNOPSIS
  SessionStart hook script: scans .github/skills/**/*.md for broken cross-references.

.DESCRIPTION
  Walks every markdown file under .github/skills/ (skipping any `*/copy/` folder,
  which holds upstream Herodotus reference material) and validates each relative
  markdown link. Reports broken links via the VS Code hooks SessionStart output
  contract (systemMessage on stdout, exit 0 on success).

  Designed to be invoked from .github/hooks/session-start-check-links.json.

.OUTPUT
  JSON on stdout of the form:
    { "continue": true, "systemMessage": "...optional non-blocking advisory..." }
#>

$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest

# -------------------------------------------------------------------
# Locate repo root: this script lives at .github/scripts/, so its
# parent's parent is the repo root.
# -------------------------------------------------------------------
$scriptDir = Split-Path -Path $PSCommandPath -Parent
$repoRoot  = Resolve-Path (Join-Path $scriptDir '..\..')

$skillsRoot = Join-Path $repoRoot '.github\skills'
if (-not (Test-Path -LiteralPath $skillsRoot)) {
    # Skills folder absent -- nothing to check. Emit clean exit.
    @{ continue = $true } | ConvertTo-Json -Compress
    exit 0
}

# -------------------------------------------------------------------
# Link categories we intentionally ignore as "not real" links:
#   *   template placeholders containing angle brackets  e.g. <slug>, <n>
#   *   literal placeholder text shown in template bodies  e.g. path-to-convention-doc,
#       your-doc-file.md, foo-bar-conventions.md
#   *   paths containing "..." as a classpath placeholder, e.g. src/main/java/.../Foo.java
# -------------------------------------------------------------------
$placeholderPattern = '<[a-zA-Z][a-zA-Z0-9_-]*>'
$ellipsisPlaceholderPattern = '/\.\.\./'
# Plain-text placeholder identifiers commonly used in template bodies. Add new ones here.
$literalPlaceholders = @(
    'path-to-convention-doc',
    'path-to-coding-instructions',
    'path-to-testing-instructions'
)

# Folders to skip: the Herodotus `copy/` folders are upstream reference material.
$skipFolders = @(
    '\00-00-create-pr copy\',
    '\00-01-implement-pr copy\',
    '\00-02-grill-me copy\',
    '\00-03-review-pr copy\'
)

function Should-SkipFile {
    [OutputType([bool])]
    param(
        [string]$RelPath
    )
    # Skip any file under a `*/copy/` subfolder (case-insensitive). These are upstream
    # Herodotus reference material, not part of the adopted skill set.
    foreach ($sf in $skipFolders) {
        if ($RelPath.IndexOf($sf, [System.StringComparison]::OrdinalIgnoreCase) -ge 0) {
            return $true
        }
    }
    return $false
}

function Test-Link {
    [OutputType([bool])]
    param(
        [string]$FileDir,
        [string]$RepoRoot,
        [string]$Link
    )

    # Strip fragment for filesystem check; remember it so we can report full link.
    $path = ($Link -split '#')[0]
    if (-not $path) { return $true }   # pure fragment -- internal anchor, not a file

    # Decide base dir: './' or '../' is relative to file; anything else is repo-root.
    # Important: do NOT use PowerShell's Join-Path here -- its .NET Path.Combine semantics
    # collapse '..' segments relative to the *last* segment of the base, not against the
    # absolute root, which gives wrong results for paths like '../../foo' joined to a
    # deeply-nested base. Build the combined string and let [System.IO.Path]::GetFullPath
    # normalise it against the root.
    if ($path.StartsWith('.\') -or $path.StartsWith('..\') -or $path.StartsWith('./') -or $path.StartsWith('../')) {
        $combined = $FileDir + [System.IO.Path]::DirectorySeparatorChar + $path
    } else {
        $combined = $RepoRoot + [System.IO.Path]::DirectorySeparatorChar + $path
    }
    $candidate = [System.IO.Path]::GetFullPath($combined)

    return ([System.IO.File]::Exists($candidate) -or [System.IO.Directory]::Exists($candidate))
}

function Get-BrokenLinksForFile {
    param(
        [string]$FilePath,
        [string]$RepoRoot
    )
    $fileDir   = Split-Path -Path $FilePath -Parent
    $relPath   = $FilePath.Substring($RepoRoot.Length).TrimStart('\', '/')
    $content   = Get-Content -LiteralPath $FilePath -Raw
    $matches   = [regex]::Matches($content, '\]\(([^)]+)\)')

    if ($env:CHECK_LINKS_DEBUG -eq '1') {
        Write-Debug ("Get-BrokenLinksForFile: fileDir='$fileDir' relPath='$relPath'")
    }

    $broken = @()
    foreach ($m in $matches) {
        $link = $m.Groups[1].Value
        if ($link -match '^(https?:|mailto:|#)') { continue }

        # Skip template placeholders (angle-bracket form, classpath-ellipsis form, and known literal placeholders)
        if ($link -match $placeholderPattern) { continue }
        if ($link -match $ellipsisPlaceholderPattern) { continue }
        if ($literalPlaceholders -contains $link) { continue }

        if (-not (Test-Link -FileDir $fileDir -RepoRoot $RepoRoot -Link $link)) {
            $broken += [pscustomobject]@{
                file = $relPath
                link = $link
            }
        }
    }
    return ,$broken
}

# -------------------------------------------------------------------
# Walk all .md files under .github/skills/, skipping `copy/` folders.
# -------------------------------------------------------------------
$brokenAll = New-Object System.Collections.Generic.List[object]

$stack = New-Object System.Collections.Generic.Queue[string]
$stack.Enqueue($skillsRoot)
while ($stack.Count -gt 0) {
    $dir = $stack.Dequeue()
    $mdFiles = @(Get-ChildItem -LiteralPath $dir -Filter '*.md' -ErrorAction SilentlyContinue |
        Where-Object { -not $_.PSIsContainer })
    foreach ($file in $mdFiles) {
        $rel = $file.FullName.Substring($repoRoot.Path.Length)
        if (Should-SkipFile -RelPath $rel) { continue }
        $found = Get-BrokenLinksForFile -FilePath $file.FullName -RepoRoot $repoRoot.Path
        foreach ($b in $found) { $brokenAll.Add($b) }
    }
    $subdirs = @(Get-ChildItem -LiteralPath $dir -Directory -ErrorAction SilentlyContinue)
    foreach ($sub in $subdirs) {
        $stack.Enqueue($sub.FullName)
    }
}

# -------------------------------------------------------------------
# Emit SessionStart contract output.
# -------------------------------------------------------------------
if ($brokenAll.Count -eq 0) {
    # Clean -- silent continue.
    @{ continue = $true } | ConvertTo-Json -Compress
    exit 0
}

# Build a compact advisory. Cap at 10 entries to keep systemMessage small.
$lines = @()
$lines += "Skill-link check found $($brokenAll.Count) broken cross-reference(s) in .github/skills/."
$lines += "This is a non-blocking advisory -- investigate with: pwsh .github/scripts/check-skill-links.ps1"
$lines += ""
$i = 0
foreach ($b in $brokenAll) {
    if ($i -ge 100) { $lines += "  …and $($brokenAll.Count - 100) more"; break }
    $lines += ("  {0}  ->  {1}" -f $b.file, $b.link)
    $i++
}

$out = @{
    continue      = $true
    systemMessage = ($lines -join "`n")
}
$out | ConvertTo-Json -Compress
exit 0