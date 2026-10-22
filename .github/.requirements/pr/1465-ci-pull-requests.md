# chore(ci): validate pull requests and release only on v* tags

## Summary

`build.yml` runs today on every push to every branch, and every green run creates a draft release and pushes the `latest` server image. This PR makes CI run on pull requests into `master` and on pushes to `master`. Only a pushed `v*` tag releases. Only `master` and tags move the `latest` image. Issue branches can then be pushed freely, which the per-item pull request flow needs.

## Motivation

Bassebombecraft is moving to the flow herodotus and Loopy use: one issue, one `athrane/issue<N>` branch and one pull request into `master`, with versions as `v*` tags. That was decided in Loopy's map 2 ticket `bassebombecraft-flow-switch` (D2). Today's workflow fights that flow:

- **`on: [push]`.** Every push to any branch builds, containerises, tests and releases.
- **`release_job` has no condition.** Every green push creates a draft release. The two 1.17.1 releases are drafts made exactly this way.
- **`create_container_job` always pushes `bassebombecraft/minecraft:latest`.** A build from an issue branch would replace the image that `test_job` and anyone pulling `latest` use.
- **Pull requests get no checks,** because the workflow does not listen to `pull_request`.

This is the second of the flow switch's PRs. Its order:
1. commit the dev-4.1 map;
2. this PR;
3. merge `dev-4.1` (#1466);
4. move to `.requirements/` (#1467).

It is based on `master`. `dev-4.1`'s `build.yml` edits (#1457, #1459, #1461) touch only `build_job`'s steps, so the merge in #1466 does not conflict with this PR.

## Changes

### Files Deleted

_None._

### Files Updated

- **`.github/workflows/build.yml`**:
  - **Triggers.** `on: [push]` becomes:
    ```yaml
    on:
      push:
        branches: [master]
        tags: ['v*']
      pull_request:
        branches: [master]
    ```
  - **`create_container_job`.** The image is always tagged and pushed as `<final_version>-<sha>`. The `latest` tag is added and pushed only when `github.event_name == 'push'`, on `master` or on a `v*` tag.
  - **`test_job`.** The service image becomes `bassebombecraft/minecraft:${{ needs.build_job.outputs.final_version }}-${{ github.sha }}`, so a pull request tests its own image, not `latest`.
  - **`release_job`.** It gains `if: startsWith(github.ref, 'refs/tags/v')`. `tag_name` stays `github.ref`, which is now always a version tag.

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [x] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Implementation Plan

### Phase 1 — Triggers and release condition

1. Branch `athrane/issue1465` from `master`.
2. Replace `on: [push]` with the triggers above.
3. Add the `if:` to `release_job`.

### Phase 2 — Image tags

1. Split `create_container_job`'s pushes:
   - always push the sha tag;
   - push `latest` only under `if: github.event_name == 'push'`.
2. Point `test_job`'s service at the sha-tagged image.

### Phase 3 — Check the run

1. Open the pull request. Its own run is the first `pull_request` run.
2. Check that the run does not reach `release_job` and does not push `latest`.

## Testing

No test changes: the project has no test sources, and this PR changes only the workflow.

- [ ] Unit tests added/updated
- [ ] Manual testing performed in-game (`./gradlew runClient`)
- [ ] `./gradlew compileJava` passes
- [ ] `./gradlew build` passes

**Test coverage**: unchanged.

### Manual validation steps

| # | Check | How to verify |
|---|-------|---------------|
| 1 | Pull requests are checked | The pull request for this PR shows the workflow's checks |
| 2 | A pull request does not release | In that run, `Release` is skipped and the repo's Releases page has no new draft |
| 3 | A pull request does not move `latest` | Docker Hub shows a new `<final_version>-<sha>` tag for the run, and `latest` is unchanged |
| 4 | An issue branch push alone runs nothing | Pushing the branch before opening the pull request starts no workflow run |
| 5 | The YAML is valid | The Actions tab shows no "invalid workflow file" error |

## Related Issues
Closes #1465

## Checklist
- [ ] Code follows project conventions and Minecraft Forge best practices
- [ ] Code compiles without errors (`./gradlew compileJava` passes)
- [ ] Build succeeds (`./gradlew build` passes)
- [ ] Javadoc comments added for public APIs
- [ ] Updated documentation (if applicable)
- [ ] No breaking changes (or documented in PR description)
- [ ] Commit messages follow Conventional Commits format

## Additional Notes

- **The checks may be red, and that is not this PR's failure.** `master` has not been built in CI since #1439, and the compile baseline is unknown (`compile-baseline` on the dev-4.1 map). This PR is judged by what runs and what does not: Release skipped, `latest` untouched.
- **A tag releases from now on.** `1.18.2-4.1` is released by pushing `v1.18.2-4.1` on `master` once the dev-4.1 map's Destination is met. The workflow still creates a draft, so publishing stays a manual step.
- **Out of scope.** The deprecated `actions/create-release@v1` and `actions/checkout@v2`, and Dependabot's #1403. The validate entry point and running it in CI come with flow-switch D4.
