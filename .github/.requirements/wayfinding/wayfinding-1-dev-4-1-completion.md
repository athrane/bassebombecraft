# Wayfinding Map — Complete the intent of branch dev-4.1

**Status**: Map document for a multi-session planning effort. Not a PR description — no implementation plan, no file list belongs here. Individual tickets graduate into `.github/.requirements/pr/<issue-number>-<kebab-name>.md` files once resolved (format: [pr-template-generic.md](../../../.claude/skills/00-00-create-pr/templates/pr-template-generic.md)).

## Destination

Branch `dev-4.1` ships mod version `1.18.2-4.1`: the mod compiles, starts on the client and on a dedicated server, and runs on Minecraft 1.18.2 / Forge 40.3.0 with no gameplay changes, and the GitHub Actions pipeline builds, containerises, tests and releases it. At that point tag `v1.18.2-4.1` on `master`.

## Notes

- **Intent confirmed by the user:** shipping `1.18.2-4.1`. Evidence: `version.json` is `{"modVersion":"4.1","minecraftVersion":"1.18.2"}`, `gradle.properties` has `mod_version=1.18.2-4.1`, `bassebombecraft_update.json` promotes `1.18.2-4.1`, and the commits since `master` are the 1.18.2 migration (`#1437`, `#1440`–`#1461`).
- Target is Minecraft 1.18.2, Forge 40.3.0, Java 17, official mappings. Treat these as pinned unless the `forge-version-pin` ticket says otherwise.
- API migration only: replace removed or deprecated Forge/Minecraft APIs with their direct equivalents; no gameplay redesign. (Assumed from the commit history; confirm with the user if a ticket wants to break it.)
- Keep each implementable PR inside one subsystem (registries, networking, rendering, AI, build/CI) so reviews and merges stay independent.
- **Order set by the user:** step 1 is getting a JDK 17 on the PATH (`jdk-on-path`); step 2 is adding unit tests (`test-framework-setup`). Everything else follows. The table of open tickets below lists them in that order.
- **Current compile state is unknown.** Fourteen migration PRs have landed, but no fresh `./gradlew compileJava` was possible when this map was charted: no `java` on the PATH of the session's shell (the reason for step 1), and the working tree has an uncommitted Gradle wrapper change (8.11.1 → 9.7.1). Never plan from an old error list; the `compile-baseline` ticket produces the new one.
- **Tests and compilation interact.** Unit tests compile against `src/main`, so they cannot run until the main code compiles. `test-framework-setup` must decide how step 2 sequences against `compile-baseline`.
- Requirements files already exist for `#1440`–`#1461` and `#1464`; check them before opening a ticket that overlaps one.

## Decisions So Far

| Ticket | Gist | Resolved |
|--------|------|----------|
| [test-framework-scope](./wayfinding-1-tickets/test-framework-scope.md) | Unit tests are in scope for `1.18.2-4.1` and come right after getting Java working (step 2); first targets and setup details are in `test-framework-setup` | user, when revising the map |

## Not Yet Specified

- Remaining compile errors and deprecation warnings — cannot be phrased until `compile-baseline` runs.
- Visual or gameplay regressions in rendering (world-space renderers all filter on `RenderLevelStageEvent.Stage.AFTER_PARTICLES`; HUD placement after the overlay changes) — cannot be phrased until `client-smoke-test` has a result.
- Whether recipes, models, loot and lang files load correctly in game — depends on `resource-format-audit`.
- Release readiness: merging `dev-4.1` to `master`, tagging, the CurseForge upload, README and update JSON — depends on the compile, smoke-test and CI tickets.

## Out of Scope

- Moving to any Minecraft version after 1.18.2 — needs its own map.
- New gameplay features or refactors of the `Registered*` classes beyond what the migration requires — no behavioural change in this effort.
- Branch `origin/copilot/migrate-to-minecraft-1-18-2` — contains only an "Initial plan" commit; nothing to recover.
- Requirements files `#1337`–`#1432` — an earlier 1.17.1 attempt, kept as history.
- The Copilot → Claude Code agent-setup migration (`#1464`) — already done and unrelated to the mod.

## Tickets

| Ticket | Type | Status | Blocked By | File |
|--------|------|--------|------------|------|
| jdk-on-path | task | open | none | [wayfinding-1-tickets/jdk-on-path.md](./wayfinding-1-tickets/jdk-on-path.md) |
| gradle-toolchain-compat | research | open | jdk-on-path | [wayfinding-1-tickets/gradle-toolchain-compat.md](./wayfinding-1-tickets/gradle-toolchain-compat.md) |
| test-framework-setup | grilling | open | jdk-on-path, gradle-toolchain-compat | [wayfinding-1-tickets/test-framework-setup.md](./wayfinding-1-tickets/test-framework-setup.md) |
| compile-baseline | task | open | gradle-toolchain-compat | [wayfinding-1-tickets/compile-baseline.md](./wayfinding-1-tickets/compile-baseline.md) |
| overlay-event-api | research | open | none | [wayfinding-1-tickets/overlay-event-api.md](./wayfinding-1-tickets/overlay-event-api.md) |
| access-transformer-status | research | open | none | [wayfinding-1-tickets/access-transformer-status.md](./wayfinding-1-tickets/access-transformer-status.md) |
| resource-format-audit | research | open | none | [wayfinding-1-tickets/resource-format-audit.md](./wayfinding-1-tickets/resource-format-audit.md) |
| forge-version-pin | grilling | open | none | [wayfinding-1-tickets/forge-version-pin.md](./wayfinding-1-tickets/forge-version-pin.md) |
| ci-version-sources | grilling | open | forge-version-pin | [wayfinding-1-tickets/ci-version-sources.md](./wayfinding-1-tickets/ci-version-sources.md) |
| client-smoke-test | task | open | compile-baseline | [wayfinding-1-tickets/client-smoke-test.md](./wayfinding-1-tickets/client-smoke-test.md) |
| server-smoke-test | task | open | compile-baseline | [wayfinding-1-tickets/server-smoke-test.md](./wayfinding-1-tickets/server-smoke-test.md) |
| ci-test-job-assertions | grilling | open | server-smoke-test, ci-version-sources | [wayfinding-1-tickets/ci-test-job-assertions.md](./wayfinding-1-tickets/ci-test-job-assertions.md) |
