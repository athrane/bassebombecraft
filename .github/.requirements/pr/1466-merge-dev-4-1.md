# chore(build): merge dev-4.1 into master

## Summary

`dev-4.1`'s 21 migration-fix commits come into `master` through one pull request with a merge commit. It closes the 19 issues whose fixes are already on `dev-4.1`. After it, every remaining item of the dev-4.1 map is an `athrane/issue<N>` pull request into `master`, and `dev-4.1` is deleted. There are no more `dev-<version>` branches.

## Motivation

Loopy's map 2 ticket `bassebombecraft-flow-switch` (D1) decided to merge `dev-4.1` now rather than finish it in the old flow:

- **`master` is already the 1.18.2 base.** It is #1439's merge plus two commits. `dev-4.1` is the fixes on top of that base.
- **Issues stay open although their fixes are committed.** A closing keyword acts only when a commit reaches the default branch, so #1440–#1461 are still open today.
- **Per-item pull requests need `master` to be current.** Until the merge, every new item branch would start from the old base.

This is the third of the flow switch's PRs. Its order:
1. commit the dev-4.1 map;
2. CI (#1465);
3. this PR;
4. move to `.requirements/` (#1467).

## Changes

### Files Deleted

_None. The `dev-4.1` branch is deleted on GitHub after the merge._

### Files Updated

- **Every file `dev-4.1` changes.** No new edits: the pull request brings `dev-4.1` in as it is. That includes `gradle/wrapper/gradle-wrapper.properties` (8.11.1 → 9.7.1, see Additional Notes) and `.github/workflows/build.yml` (#1457, #1459 and #1461's step fixes, which merge cleanly with #1465).
- **`.github/.requirements/wayfinding/`** — The dev-4.1 map and its 13 tickets, untracked today, are committed on `dev-4.1` before the pull request is opened, so they arrive with the merge (flow-switch D3). So are any of these requirements files that are still untracked.

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [x] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Implementation Plan

### Phase 1 — Prepare `dev-4.1`

1. Commit the dev-4.1 map and its tickets on `dev-4.1`. Its Destination ends with "`dev-4.1` is ready to merge to `master`"; revise it to "tag `v1.18.2-4.1` on `master`" (flow-switch risk).
2. Push `dev-4.1`.

### Phase 2 — The pull request

1. Open a pull request from `dev-4.1` into `master`, titled as this file.
2. In the body, put one closing line per issue whose fix is on `dev-4.1` and still open:

   ```
   Closes #1440
   Closes #1441
   Closes #1442
   Closes #1443
   Closes #1444
   Closes #1445
   Closes #1446
   Closes #1447
   Closes #1448
   Closes #1449
   Closes #1450
   Closes #1452
   Closes #1453
   Closes #1455
   Closes #1457
   Closes #1458
   Closes #1459
   Closes #1460
   Closes #1461
   Closes #1466
   ```

   - **#1454 is left out.** No commit on `dev-4.1` fixes it, so it stays open as an item of the dev-4.1 map. Do not write the list as a range.
   - **#1464 is already closed** (by hand), so it is not listed.
3. Merge with **Create a merge commit**, not squash or rebase, so each fix commit keeps its `closes #N` and its history.
4. Delete `dev-4.1`.

### Phase 3 — Check

1. Check that the 19 issues and #1466 are closed, and that #1454 is open.

## Testing

No test changes: the project has no test sources.

- [ ] Unit tests added/updated
- [ ] Manual testing performed in-game (`./gradlew runClient`)
- [ ] `./gradlew compileJava` passes
- [ ] `./gradlew build` passes

**Test coverage**: unchanged.

### Manual validation steps

| # | Check | How to verify |
|---|-------|---------------|
| 1 | The fixes are on `master` | `git log origin/master --oneline` shows the 21 `dev-4.1` commits and one merge commit |
| 2 | The right issues closed | `gh issue view <N> --json state,closedByPullRequestsReferences` for each listed issue: `CLOSED`, closed by this pull request |
| 3 | #1454 is still open | `gh issue view 1454 --json state` gives `OPEN` |
| 4 | The map arrived | `.github/.requirements/wayfinding/wayfinding-1-dev-4-1-completion.md` is tracked on `master` |
| 5 | No release was made | With #1465 merged, the push to `master` builds but creates no release |
| 6 | No dev branch is left | `git ls-remote --heads origin dev-*` prints nothing |

## Related Issues
Closes #1466, plus the 19 issues listed in Phase 2
Related to #1454

## Checklist
- [ ] Code follows project conventions and Minecraft Forge best practices
- [ ] Code compiles without errors (`./gradlew compileJava` passes)
- [ ] Build succeeds (`./gradlew build` passes)
- [ ] Javadoc comments added for public APIs
- [ ] Updated documentation (if applicable)
- [ ] No breaking changes (or documented in PR description)
- [ ] Commit messages follow Conventional Commits format

## Additional Notes

- **`master` will not build after the merge, and that is accepted** (flow-switch risk). `dev-4.1`'s wrapper is Gradle 9.7.1, which ForgeGradle 6 refuses. With 8.11.1, `dev-4.1`'s code compiles cleanly (Loopy's `gradle-validate-in-worktree`, 2026-10-15). `master`'s own code never compiled on 1.18.2, so nothing that worked breaks. The wrapper is the dev-4.1 map's `gradle-toolchain-compat` ticket, and its fix is an ordinary `athrane/issue<N>` pull request after this one. Nothing releases from `master` pushes once #1465 is in.
- **Loopy reads this merge later, not now.** The requirements files have no frontmatter until #1467, so there is nothing for merge sync to mark. #1467's migration reads these issues from GitHub as closed by this pull request, and marks their files `merged`.
- **Issues without a requirements file** (#1446, #1450, #1457, #1458, #1460) close normally and are never Loopy items.
