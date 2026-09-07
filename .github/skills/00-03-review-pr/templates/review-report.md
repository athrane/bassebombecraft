# Code Review Report — PR ${prNumber}

**Generated:** [Current date and time]
**Scope:** [Changed files only / Full codebase]
**Matching PR Spec:** [Path to `.github/.requirements/pr/[nnnn]-[slug].md`, or `_None found._`]
**Changed Files:** [Count]
**Source Files Analyzed:** [Count]
**Documentation Files Analyzed:** [Count]

---

## Executive Summary

[Brief overview of compliance status — one short paragraph.]

**Overall Compliance:** [Percentage or rating]
**Critical Issues:** [Count]
**High Priority Issues:** [Count]
**Recommendations:** [Count]

---

## Compliance Analysis by Category

### 1. Java 17 + Forge 1.17.1 ✅/❌

| Check | Status | Files Affected | Notes |
|-------|--------|----------------|-------|
| No Java 8 patterns | ✅ Pass | — | — |
| No pre-1.17 OpenGL (`GlStateManager.pushMatrix`, etc.) | ✅ Pass | — | — |
| No deprecated Forge APIs (`Capability.writeNBT`, `RenderSystem.color4f`, `TextureManager.bind`) | ❌ Fail | `[ClassName.java](src/main/java/.../ClassName.java#L42)` | Found `RenderSystem.color4f` at line 42 |
| `./gradlew compileJava` succeeds | ✅ Pass | — | — |
| `./gradlew build` succeeds | ✅ Pass | — | — |
| `./gradlew test` passes (where applicable) | ✅ Pass | — | — |

### 2. Architecture Patterns ✅/❌

| Pattern | Status | Files Affected | Notes |
|---------|--------|----------------|-------|
| Registration via `RegisteredItems` / `RegisteredEntities` | ✅ Pass | — | All new items registered via `RegisteredItems.register(...)` |
| `TypeUtils`-style validation on cross-boundary inputs | ⚠️ Partial | `[Operator.java](src/main/java/.../Operator.java#L15)` | Missing validation on `Entity` argument |
| Operator pattern (`Operator` interface + concrete operator) | ✅ Pass | — | New operator wraps the change as expected |
| Event-driven side effects in `event/[domain]/` | ✅ Pass | — | Handler registered in `BassebombeCraft.java` event bus |
| No `Math.random()` (use `RandomSource` for 1.17.1+) | ✅ Pass | — | — |

### 3. Java Style ✅/❌

| Check | Status | Files Affected | Notes |
|-------|--------|----------------|-------|
| Javadoc on every public class | ⚠️ Partial | `[ClassName.java](src/main/java/.../ClassName.java)` | Missing Javadoc on 2 public methods |
| Javadoc on every public method | ✅ Pass | — | — |
| One top-level class per file | ✅ Pass | — | — |
| Static factory methods (`create()`) | ✅ Pass | — | — |
| `Object.freeze(this)` on records / data classes | ⚠️ Partial | `[DataClass.java](src/main/java/.../DataClass.java)` | Missing freeze call in constructor |
| Java 17 features used appropriately (records, sealed types) | ✅ Pass | — | — |

### 4. PR Spec Scope Compliance ✅/❌

(Only populated when a matching `.github/.requirements/pr/[nnnn]-[slug].md` exists.)

| Check | Status | Notes |
|-------|--------|-------|
| All files named in **Changes → Files Updated** are touched in the diff | ✅ Pass | — |
| No files outside the PR spec's scope are touched | ⚠️ Partial | `UnrelatedFile.java` was modified; document justification in PR description |
| Commit messages follow Conventional Commits | ✅ Pass | All 3 commits match `<type>(<scope>): <description>` |
| Implementation Plan phases executed in order | ✅ Pass | — |

### 5. Documentation ✅/❌

| Check | Status | Files Affected | Notes |
|-------|--------|----------------|-------|
| `README.md` / `README.txt` updated (if applicable) | ✅ Pass | — | — |
| Wiki updated (if applicable) | N/A | — | No wiki changes needed |
| Javadoc on public APIs | ✅ Pass | — | — |
| Bracketed markdown links (not bare paths) in any new markdown | ✅ Pass | — | — |

---

## Detailed Findings

### Critical Issues (Must Fix) 🔴

1. **[Title]**
   - **File:** `[ClassName.java](src/main/java/.../ClassName.java#L42)`
   - **Issue:** [Description of the violation]
   - **Required Action:** [Concrete fix]
   - **Reference:** [Convention](path-to-convention-doc)

### High Priority Issues (Should Fix) 🟡

1. **[Title]**
   - **File:** `[ClassName.java](src/main/java/.../ClassName.java#L42)`
   - **Issue:** [Description]
   - **Recommended Action:** [Concrete fix]
   - **Reference:** [Convention](path-to-convention-doc)

### Recommendations (Best Practices) 💡

1. **[Title]**
   - **Files:** `[ClassName.java](src/main/java/.../ClassName.java)`, `[Other.java](src/main/java/.../Other.java)`
   - **Observation:** [Description]
   - **Recommendation:** [Suggested improvement]
   - **Reference:** [Convention](path-to-convention-doc)

---

## Emerging Patterns Analysis

### Identified Patterns (Potential Documentation Candidates)

1. **[Pattern Name]**
   - **Occurrences:** Found in [count] files
   - **Description:** [Brief description of the pattern]
   - **Benefits:** [Why this pattern is useful]
   - **Recommendation:** [Document in which file/section]
   - **Example Files:**
     - `src/main/java/.../File1.java`
     - `src/main/java/.../File2.java`

### No Emerging Patterns Detected

[If no new patterns found, state this clearly.]

---

## Documentation Gap Analysis

### Missing Documentation

1. **[Document name or instruction file]**
   - **Rationale:** [Why this documentation is needed]
   - **Scope:** [Files/patterns it should cover]
   - **Priority:** High / Medium / Low
   - **Suggested Location:** [`.github/.../path.md`](.github/.../path.md)
   - **Key Content:**
     - [Bullet points of what should be covered]

### No Documentation Gaps Detected

[If no gaps found, state this clearly.]

---

## Validation Pipeline Status

| Command | Result | Notes |
|---------|--------|-------|
| `./gradlew compileJava` | ✅ Pass / ❌ Fail | — |
| `./gradlew build` | ✅ Pass / ❌ Fail | — |
| `./gradlew test` (where applicable) | ✅ Pass / ❌ Fail / ⏭️ Skipped | No `test/` source set for this domain |

---

## Action Items (Prioritised)

| # | Priority | File / Section | Action |
|---|-----------|----------------|--------|
| 1 | 🔴 Critical | `[ClassName.java#L42](src/main/java/.../ClassName.java#L42)` | [Concrete fix] |
| 2 | 🟡 High | `[Other.java#L17](src/main/java/.../Other.java#L17)` | [Concrete fix] |
| 3 | 💡 Recommendation | `README.md` | [Improvement] |

---

## Reviewer Sign-off

- [ ] All critical issues resolved
- [ ] All high-priority issues addressed or explicitly deferred
- [ ] Validation pipeline (compileJava, build, test) green
- [ ] Scope matches PR spec's Changes section
- [ ] Commit messages follow Conventional Commits
- [ ] Documentation updated where applicable