# BasseBombeCraft

Java 17 Minecraft Forge mod (Forge 40.3.0, Minecraft 1.18.2, official mappings). Source root `src/main/java/bassebombecraft/`. The project is mid-upgrade to 1.18.2 (branch `dev-4.1`); most work is fixing compile errors caused by removed or renamed Forge/Minecraft APIs.

## Build

Run from the repository root with the Gradle wrapper.

- `./gradlew compileJava` — fastest check that the code compiles
- `./gradlew build` — full build (also what CI runs)
- `./gradlew runClient` — launch the game; logs go to `run/logs/debug.log`
- `./gradlew test` — only meaningful if tests exist; there is currently **no `src/test` directory and no JUnit/Mockito dependency** in `build.gradle`. Do not write tests that cannot run; flag it and add the dependency as its own declared step.

## Code conventions

- One public type per `.java` file; filename matches the class; `package` matches the directory.
- Javadoc on every public class, method and field.
- Guard clauses and early returns at the top of a method; happy path un-nested.
- Static factory methods (`getInstance()`, `create()`) on utility and singleton classes.
- Shared strings and numbers go in `ModConstants` (or `ClientModConstants`), not inline literals.
- Register through `DeferredRegister` in the matching `Registered*` class. Never mutate Forge registries directly, and never call a supplier's `.get()` during class initialisation (that caused the "Registry is already frozen" crash, #1455).
- Operators receive everything through `Ports`; no raw Minecraft server calls inside an `Operator2`.
- Follow the patterns already used in the affected package: read one representative class before writing new code.

The detailed pattern catalogue (operators, event buses, proxies, config, network) is in [.claude/skills/00-01-implement-pr/references/project-patterns.md](.claude/skills/00-01-implement-pr/references/project-patterns.md).

## PR workflow

Work is tracked as numbered requirements files in `.github/.requirements/pr/<issue-number>-<kebab-name>.md`, one per GitHub issue. Commit messages and PR titles use Conventional Commits: `<type>(<scope>): <description>`.

Skills in `.claude/skills/` drive the workflow:

| Skill | Use for |
|-------|---------|
| `00-05-wayfinder` | Charting work too large or uncertain for one PR into a map of tickets |
| `00-02-grill-me` | Stress-testing a plan with a one-question-at-a-time interview |
| `00-00-create-pr` | Writing one requirements file for a single, understood change |
| `forge-upgrade-pr` | Ranking Gradle build errors from the upgrade and writing requirements files |
| `create-pr-from-build-errors` | Slash command: run the build and write the top three requirements files |
| `00-01-implement-pr` | Implementing a requirements file and running the validation pipeline |
| `00-06-sync-issue-description` | Pushing a requirements file to its GitHub issue |

Do not commit, push or open a PR unless asked.

## Skill authoring

Rules for writing or editing skills are in [.claude/rules/skills.md](.claude/rules/skills.md) and load automatically when a `SKILL.md` is touched.
