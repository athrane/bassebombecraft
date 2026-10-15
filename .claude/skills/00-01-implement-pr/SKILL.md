---
name: 00-01-implement-pr
description: "Reads a .github/.requirements/pr/ file, builds a dependency-ordered implementation plan, executes it following the project's Forge patterns, then runs the Gradle validation pipeline. Use to implement a BasseBombeCraft PR."
---

# Skill: Implement Pull Request from Requirements

Generate and execute a complete implementation plan for a pull request based on a requirements file created by the [`00-00-create-pr`](../00-00-create-pr/SKILL.md) skill (or by [`create-pr-from-build-errors`](../create-pr-from-build-errors/SKILL.md)).
## When to Use This Skill

- The user asks to implement a PR or execute a `.github/.requirements/pr/[nnn]-[keywords].md` file
- The user is continuing the `00-00-create-pr` → `00-01-implement-pr` workflow

## Prerequisites

| Resource | Purpose |
|----------|---------|
| `.github/.requirements/pr/[nnn]-[keyword1-keyword2].md` | The requirements file to implement (required input) |
| [project-patterns.md](./references/project-patterns.md) | Full list of project patterns (Operator2/Ports, DeferredRegister, event buses, proxies, config, network) |
| [forge-upgrade-pr](../forge-upgrade-pr/SKILL.md) | Forge API migration patterns for upgrade fixes |
| `src/main/java/bassebombecraft/ModConstants.java`, `BassebombeCraft.java` | Naming constants, initialisation and registration entry points |
| Existing files in the affected package | Patterns to follow — read one representative class before writing new code |

## Step-by-Step Workflow

### Phase 1: Requirements Analysis

1. **Read the requirements file** — extract the PR title (type + scope), then parse Summary, Motivation, Changes, Implementation Plan, Testing, and Related Issues.
2. **Analyze scope** — determine the affected packages under `src/main/java/bassebombecraft/` (`item`, `entity`, `event`, `client`, `operator`, `network`, `config`, `potion`, `sound`, …) and read the related files.
3. **Read project standards**:
   - [forge-upgrade-pr](../forge-upgrade-pr/SKILL.md) and its `forge-api-migration-patterns.md` — for any removed/renamed Forge or Minecraft API
   - `ModConstants.java` for shared constants; `BassebombeCraft.java` for initialisation and registration patterns
   - One representative class in the affected package
4. **Assess dependencies** — new entries needed in `build.gradle`, reusable constants in `ModConstants`, new TOML config keys in `ModConfiguration`.

### Phase 2: Implementation Planning

5. **Create the implementation checklist** — convert each `### Files Deleted` / `### Files Updated` entry into a concrete CREATE/MODIFY/DELETE operation (a path that doesn't exist yet, or a description starting with `New ...`, is a CREATE). Order: enums/interfaces → classes → registration (`Registered*`) → event wiring → tests.
6. **Identify required patterns**:
   - New items/entities/effects/sounds/containers: register with `DeferredRegister` in the matching `Registered*` class; use the class-literal `register(Foo.class)` helper in `RegisteredItems` — **never call a supplier's `.get()` during class initialisation** (that caused the "Registry is already frozen" crash, #1455)
   - Event handlers: `@Mod.EventBusSubscriber(modid = MODID)` with `@SubscribeEvent`; `MinecraftForge.EVENT_BUS` for gameplay events, the mod event bus for lifecycle events
   - Composable logic: `Operator2` + `Ports`, composed via `Operators2.run()`; validate inputs with `Operators2.validateNotNull()`
   - Config: `ForgeConfigSpec` in `ModConfiguration`
   - Network: the `SimpleChannel` packet pattern in the `network/` package (`NetworkChannelHelper`)
   - Client/server split: `ClientProxy` / `ServerProxy`, client-only rendering in `client/`
7. **Plan the file structure** — list new and modified files with full paths; one public type per `.java` file, filename matching the class, package matching the directory.
8. **Optionally stress-test the plan** — for implementations involving significant architectural decisions or ambiguity, invoke the [`00-02-grill-me`](../00-02-grill-me/SKILL.md) skill to interview the user before beginning Phase 3.

### Phase 3: Implementation Execution

9. **Create type definitions** — enums and interfaces first, with Javadoc on the type and its public members; constants into `ModConstants` rather than inline literals.
10. **Implement core logic** — guard clauses and early returns at the top of each method, happy path un-nested; Javadoc on all public classes, methods and fields; static factory methods (`getInstance()`, `create()`) on utility and singleton classes.
11. **Register** — add the `Registered*` entries, creative-tab entries, config keys and network packet registrations the requirements call for.
12. **Write tests** — only if the requirements ask for them *and* a test setup exists. This project currently has no `src/test` directory and no JUnit/Mockito dependency in `build.gradle`; if the requirements file expects unit tests, say so and add the dependency as a separate, declared step instead of writing tests that cannot run.

### Phase 4: Validation & Refinement

13. **Run the validation pipeline**, fixing and re-running until each passes:
    - `./gradlew compileJava`
    - `./gradlew build`
    - `./gradlew test` — only if tests exist
    - For runtime-affecting changes (registration, event handling, rendering, networking), also run `./gradlew runClient` and confirm the mod reaches the main menu with no registry or class-loading errors in `run/logs/debug.log`. If you cannot run the client in this environment, say so explicitly and list the manual checks from the requirements file for the user to perform
14. **Verify conformance** — Javadoc on public APIs, package declarations match directories, constants in `ModConstants`, registration through `DeferredRegister`, correct event bus, and every file operation from the Changes section is complete.
15. **Final review** — all planned operations done, conventions followed, docs updated if applicable, no undocumented breaking changes. Do not tick the requirements file's Testing/Checklist boxes for checks you did not actually run.

See [examples.md](./references/examples.md) for worked scenarios and a quick pattern-to-skill lookup.

## Output Format

```markdown
## Implementation Complete

### Files Created
- `src/main/java/bassebombecraft/[scope]/NewFile.java` - [Brief description]

### Files Modified
- `src/main/java/bassebombecraft/[scope]/ExistingFile.java` - [What changed]

### Tests Added/Updated
- [Number] tests in `src/test/java/bassebombecraft/[scope]/`, or "none — no test setup in the project"

### Validation Results
✅ / ❌ `./gradlew compileJava`
✅ / ❌ `./gradlew build`
✅ / ❌ / ⏭ `./gradlew test` (⏭ = no tests exist)
✅ / ❌ / ⏭ `./gradlew runClient` smoke check (⏭ = not run; manual steps listed)

### Conformance Verification
[Which of the Phase 4 step 14 checks were verified]

### Ready for Commit
Ready to commit with message: `<type>(<scope>): <description>`

### Notes
[Any important notes about implementation choices or deviations]
```

Report any ❌ or ⏭ honestly; do not mark a stage passed unless it ran.

## Guard Rails

- Do not skip the validation pipeline; iterate fix-and-retry until `compileJava` and `build` pass
- Do not deviate from the requirements file's Changes section without documenting the deviation and justification
- Follow existing patterns in the affected package rather than inventing new ones
- Never mutate Forge registries directly; always register through `DeferredRegister`
- Do not commit, push or open the PR unless the user asks

## Troubleshooting

| Situation | Response |
|-----------|----------|
| Cannot read requirements file | Ask the user for the correct path |
| Scope not recognized | Ask the user to clarify the affected packages |
| `compileJava` fails with errors outside the requirements file's scope | Report them; they belong in a separate PR requirements file (see `create-pr-from-build-errors`) unless they are caused by your change |
| Replacement Forge API is not what the requirements file says | Verify against the Forge 40.3.0 / 1.18.2 sources, document the deviation in Notes |
| Breaking changes detected | Document in the summary and ask the user to confirm |
| Requirements file expects tests but no test setup exists | Flag it; add the JUnit dependency only if the requirements include it |

## Validation Checklist

- [ ] Requirements file read successfully
- [ ] All changes from requirements implemented
- [ ] `./gradlew compileJava` passes
- [ ] `./gradlew build` passes
- [ ] Runtime smoke check run, or manual steps handed to the user
- [ ] No breaking changes (or documented)

## Related Documents

- [00-00-create-pr/SKILL.md](../00-00-create-pr/SKILL.md) — produces the requirements file this skill implements
- [00-02-grill-me/SKILL.md](../00-02-grill-me/SKILL.md) — invoked at Step 8 to stress-test an ambiguous plan before Phase 3 execution
- [00-05-wayfinder/SKILL.md](../00-05-wayfinder/SKILL.md) — charts oversized planning work before it reaches this skill
- [forge-upgrade-pr/SKILL.md](../forge-upgrade-pr/SKILL.md) — Forge API migration patterns
- [project-patterns.md](./references/project-patterns.md) — catalogue of the project's code patterns
- [examples.md](./references/examples.md) — worked examples

## Maintenance Notes

Update this skill (and `references/project-patterns.md`) when:
- The project upgrades to a new Forge or Minecraft version
- A test framework is added to `build.gradle`
- New architectural patterns are introduced
- The validation toolchain changes
