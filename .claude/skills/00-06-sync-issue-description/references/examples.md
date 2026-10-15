# Worked Examples

Two sessions of the [`00-06-sync-issue-description`](../SKILL.md) skill. The agent never reads the PR file; it only sees the script's output. `SYNC` stands for `node .claude/skills/00-06-sync-issue-description/scripts/sync-issue.mjs`.

## Example 1: Dry run → click → apply

User: `/00-06-sync-issue-description .github/.requirements/pr/1441-iforgecontainertype-import-fails.md`

### Step 2 — Dry run

This is real output from a dry run against `athrane/bassebombecraft`; nothing was written.

```
$ SYNC .github/.requirements/pr/1441-iforgecontainertype-import-fails.md --dry-run
issue:    #1441 OPEN https://github.com/athrane/bassebombecraft/issues/1441
title:    unchanged
body:     2,508 → 5,060 chars (fits; markers appended)
overflow: 0 create, 0 update, 0 stub
plan:     1887fe8c
```

`title: unchanged` is expected: this file has no top-level `# ` heading. `markers appended` with a non-empty current body (2,508 chars) means the issue already has a description without `pr-sync` markers, and the new block would be appended below it. The agent must check what that text is: if it is an earlier copy of the plan it re-runs with `--replace-unmarked` (the summary then reads `unmarked body replaced`); if it is hand-written text worth keeping, it applies as is.

### Step 3 — `AskUserQuestion`

- Question (after the `--replace-unmarked` re-run): `Sync #1441? Title unchanged; body 2,508 → N chars (fits; unmarked body replaced); no overflow comments.`
- Options: **Apply (Recommended)** · **Cancel** (the "keep current title" option is dropped because the title is unchanged)

### Step 4 — The user clicks Apply

This step assumes the existing text was an earlier copy of the plan. `<plan>` is the hash printed by the `--replace-unmarked` dry run; this output is illustrative.

```
$ SYNC .github/.requirements/pr/1441-iforgecontainertype-import-fails.md --replace-unmarked --apply --expect <plan>
issue:    https://github.com/athrane/bassebombecraft/issues/1441
title:    unchanged
body:     updated
verified: true
```

A large file adds one line per overflow comment, such as `part 1:   https://github.com/…/issues/N#issuecomment-…`. Surplus comments from an earlier, longer sync show as `stub N:` lines; they are edited to a superseded stub, never deleted.

If the issue changed between the preview and the click, the apply exits 3:

```
error: plan-changed — plan is now 0c4e9d21, expected 1887fe8c; the issue or file changed after the preview
```

The agent re-runs the dry run and asks again.

---

## Example 2: Ambiguous issue

The PR file's `## Related Issues` section says `Closes #1444` and `Closes #1445`. The output below is illustrative, showing the shape of the error.

```
$ SYNC .github/.requirements/pr/1444-migrate-fmllegacy-network-api.md --dry-run
candidates: #1444 #1445
error: ambiguous-issue — found 2 candidate issues in ## Related Issues; pass --issue N
```

`AskUserQuestion`: `Which issue should the PR file sync to?` with the options **#1444** and **#1445**. The user clicks #1444, and the agent re-runs with `--issue 1444 --dry-run`. From there it follows Example 1.
