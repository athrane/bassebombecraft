---
description: 'Generate implementation plan from PR description requirements file'
agent: 'agent'
model: Claude Sonnet 4.6 (copilot)
---

# Implement Pull Request from Requirements

Generate and execute a complete implementation plan for a pull request based on a requirements file created by the `create-pr-from-build-errors.prompt.md` prompt.

## Mission

Read a PR requirements file from `.github/.requirements/pr/`, analyze the requested changes, create a detailed implementation plan, and execute the plan to complete all changes described in the requirements.

## Scope & Preconditions

- Input: PR requirements markdown file in `.github/.requirements/pr/` directory
- Target: BasseBombeCraft — a Java 17 Minecraft Forge mod (Forge 40.x, MC 1.18.2)
- Source root: `src/main/java/bassebombecraft/`
- Test root: `src/test/java/bassebombecraft/`
- Output: Fully implemented changes ready for commit
- Standards: Follow Minecraft Forge best practices and existing codebase conventions
- Validation: All checks must pass (compile, test, build)

## Inputs

**Required from user:**
- Requirements file path: Which `.github/.requirements/pr/[nnn]-[keyword1-keyword2].md` file to implement?

**Retrieved automatically:**
- PR requirements from the specified file
- Project structure from workspace
- Related source files based on scope
- Existing patterns in the affected package(s)
- Testing patterns from existing test files (if any)
- Forge registration patterns from `RegisteredItems.java`, `RegisteredEntities.java`, etc.

## Workflow

### Phase 1: Requirements Analysis

1. **Read Requirements File**
   - Read the specified `.github/.requirements/pr/[nnn]-[keyword1-keyword2].md` file
   - Extract PR title to determine type and scope
   - Parse Summary, Motivation, and Changes sections
   - Identify the type of change (feat, fix, docs, refactor, test, etc.)

2. **Analyze Scope**
   - Determine affected packages from scope (item, entity, block, event, operator, player, world, config, network, projectile, potion, sound, structure, etc.)
   - Identify related source files in `src/main/java/bassebombecraft/[scope]/`
   - Identify related test files in `src/test/java/bassebombecraft/[scope]/`
   - Check for existing patterns in the module (e.g. `RegisteredItems`, `ModConstants`, event handler classes)

3. **Read Project Standards**
   - Load the **forge-upgrade-pr** skill if available — it contains common Forge/NeoForge migration patterns
   - Review `ModConstants.java` for naming constants and mod-wide values
   - Review `BassebombeCraft.java` for initialization and registration patterns
   - Read one representative class in the affected package before writing new code
   - Identify applicable patterns:
     - **Operator/Ports pattern** (`Operator2` / `Ports` / `Operators2`) for composable logic
     - **Forge DeferredRegister** for items, blocks, entities, effects, sounds, containers
     - **@Mod.EventBusSubscriber** for event handler classes
     - **Proxy pattern** (`ClientProxy` / `ServerProxy`) for dist-specific code
     - **Static factory / getInstance()** for utility and singleton classes
     - **Javadoc** on all public APIs

4. **Assess Dependencies**
   - Check if changes require new entries in `build.gradle` (dependencies section)
   - Identify existing constants in `ModConstants.java` that can be reused
   - Determine if new TOML config keys are needed in `bassebombecraft-common.toml`
   - Check for relevant Forge API changes in the Forge 40.x changelog if migrating APIs

### Phase 2: Implementation Planning

5. **Create Implementation Checklist**
   - Convert each item from the Changes section into specific file operations
   - Order operations by dependency (interfaces/enums → implementation classes → registration → tests)
   - One public type per `.java` file, filename must match class name
   - For modifications: identify the exact methods to update
   - Plan test files that mirror the source package structure

6. **Identify Required Patterns**
   - **For new Items**: Implement via `Item` subclass, register with `DeferredRegister<Item>` in `RegisteredItems`, add to `ModConstants` as needed
   - **For new Blocks**: Implement via `Block` subclass, register with `DeferredRegister<Block>` in `RegisteredBlocks`
   - **For new Entities**: Register via `DeferredRegister<EntityType<?>>` in `RegisteredEntities`; add AI goals in `FMLCommonSetupEvent`
   - **For Event Handlers**: Annotate the class with `@Mod.EventBusSubscriber(modid = MODID)`; use `@SubscribeEvent` on handler methods; choose the correct bus (`EVENT_BUS` for game events, `MOD_EVENT_BUS` for lifecycle events)
   - **For Operators**: Implement `Operator2` functional interface; compose via `Operators2.run()`; use `Ports` for all input/output; validate with `Operators2.validateNotNull()`
   - **For Config**: Add keys to `ModConfiguration` via `ForgeConfigSpec` builder; reflect defaults in `bassebombecraft-common.toml`
   - **For Network**: Use the `SimpleChannel` packet registration pattern from the `network/` package
   - **For Tests**: JUnit 5 unit tests (`@Test`, `@BeforeEach`, `@ExtendWith(MockitoExtension.class)`); mock Minecraft objects with Mockito where needed
   - **For All Methods**: Guard clauses / early returns for edge cases at the top; happy path un-nested at the lowest indentation level

7. **Plan File Structure**
   - List all new files to create with full paths under `src/main/java/bassebombecraft/`
   - List all existing files to modify
   - One public class/interface/enum per `.java` file
   - Verify test files mirror source package locations under `src/test/java/bassebombecraft/`

### Phase 3: Implementation Execution

8. **Create Type Definitions**
   - Create enum or interface files first (one type per file, PascalCase filename)
   - Add Javadoc on the type and all public members
   - Add constants to `ModConstants.java` for any magic strings or numeric values

9. **Implement Core Logic**
   - New classes must:
     - Declare a `package` statement matching their directory path
     - Use static factory methods (`getInstance()`, `create()`) on utility/singleton classes
     - Add Javadoc on all public methods, constructors, and fields
     - Follow the guard-clause / early-return style — no deeply nested if-else blocks
   - Operator2 implementations must:
     - Extract all inputs from `Ports` at the top of `run()`
     - Validate inputs with `Operators2.validateNotNull()` before use
     - Return results via `ports.set*()` methods
   - Event handler classes must:
     - Be annotated `@Mod.EventBusSubscriber(modid = MODID)`
     - Use `@SubscribeEvent` on each handler method
     - Select the correct bus (`EVENT_BUS` for game events; `MOD_EVENT_BUS` for lifecycle events)

10. **Registration**
    - Register new items/blocks/entities/effects/sounds via the appropriate `Registered*.java` class using `DeferredRegister`
    - Add creative tab entries in `RegisteredItems` where applicable
    - If adding new config keys, update `ModConfiguration` and the default TOML comments
    - If adding network packets, register them in the network channel setup

11. **Write Tests**
    - Create test files under `src/test/java/bassebombecraft/[scope]/` mirroring source packages
    - Write JUnit 5 tests (`@Test`, `@BeforeEach`, `@ExtendWith(MockitoExtension.class)`)
    - Use Mockito to mock Minecraft/Forge objects that require a running game instance
    - Test pure-logic classes (operators, utilities, config helpers) without mocks where possible
    - Naming convention: `[ClassName]Test.java`

### Phase 4: Validation & Refinement

12. **Run Validation Pipeline**
    - Run `./gradlew compileJava` — fix all compilation errors
    - Run `./gradlew test` — fix any test failures
    - Run `./gradlew build` — fix any remaining build errors
    - Iterate until all three steps succeed

13. **Verify Conformance**
    - Javadoc present on all public APIs
    - Package declarations match directory structure
    - Constants in `ModConstants.java` — no inline magic literals
    - Guard clauses used for validation; no deeply nested blocks
    - Forge registration completed in the appropriate `Registered*.java` class
    - Event bus selection is correct (game vs. mod lifecycle)
    - Tests exist for all new non-trivial logic

14. **Final Review**
    - All items from the Changes checklist completed
    - Code follows project conventions
    - Tests provide adequate coverage
    - Documentation updated (if applicable)
    - No breaking changes (or documented)
    - All validation steps passing

## Output Expectations

**Deliverables:**
1. All source files created/modified as specified in requirements
2. All test files created/modified to validate changes
3. All validation steps passing (`compileJava`, `test`, `build`)
4. Implementation summary showing:
   - Files created (with paths)
   - Files modified (with paths)
   - Tests added/updated
   - Validation results
   - Any deviations from original plan (with justification)

**Success Criteria:**
- All changes from requirements file are implemented
- Code follows all project conventions
- All tests pass (`./gradlew test`)
- Code compiles without errors (`./gradlew compileJava`)
- Build succeeds (`./gradlew build`)
- Changes are ready for commit

**Format:**
Provide a final summary in this format:

```markdown
## Implementation Complete

### Files Created
- `src/main/java/bassebombecraft/[scope]/NewClass.java` - [Brief description]
- `src/test/java/bassebombecraft/[scope]/NewClassTest.java` - [Brief description]

### Files Modified
- `src/main/java/bassebombecraft/[scope]/ExistingClass.java` - [What changed]
- `src/main/java/bassebombecraft/ModConstants.java` - [New constants added]
- `src/main/java/bassebombecraft/[scope]/Registered*.java` - [Registration points]

### Tests Added/Updated
- [Number] unit tests in `src/test/java/bassebombecraft/[scope]/`

### Validation Results
✅ Compilation passed (`./gradlew compileJava`)
✅ Tests passed (`./gradlew test`)
✅ Build succeeded (`./gradlew build`)

### Conformance Verification
✅ Javadoc on all public APIs
✅ Constants in ModConstants.java (no magic literals)
✅ Guard clauses / early returns used
✅ Forge registration completed
✅ Correct event bus used
✅ Test coverage adequate

### Ready for Commit
Changes are complete and validated. Ready to commit with message:
```
<type>(<scope>): <description>
```

### Notes
[Any important notes about implementation choices or deviations]
```

## Quality Assurance

**Validation Checklist:**
- [ ] Requirements file read successfully
- [ ] All changes from requirements implemented
- [ ] New files use correct naming (PascalCase, matches class name)
- [ ] One public type per `.java` file
- [ ] Package declarations match directory structure
- [ ] Static factory / getInstance() on utility and singleton classes
- [ ] Javadoc on all public APIs
- [ ] Constants declared in `ModConstants.java`
- [ ] Guard clauses used; no deep nesting
- [ ] Forge DeferredRegister used for new registrables
- [ ] Correct event bus selected (game vs. mod lifecycle)
- [ ] Operator2 / Ports pattern followed (where applicable)
- [ ] Test files mirror source package structure
- [ ] JUnit 5 tests written; Mockito used for Minecraft objects
- [ ] `./gradlew compileJava` passes
- [ ] `./gradlew test` passes
- [ ] `./gradlew build` passes
- [ ] No breaking changes (or documented)

**Failure Triggers:**
- Cannot read requirements file → Ask user for correct path
- Scope not recognized → Ask user to clarify affected packages
- Validation steps fail → Fix errors and re-run until passing
- Breaking changes detected → Document in summary and ask user to confirm
- Insufficient test coverage → Add more tests until adequate
- Pattern violations detected → Refactor to conform to standards

## Guard Rails

- **Never skip validation steps** — all three (`compileJava`, `test`, `build`) must pass before completion
- **Always follow project conventions** — review an existing class in the same package before writing new code
- **Maintain backwards compatibility** — avoid breaking changes unless explicitly required
- **Test thoroughly** — JUnit 5 unit tests required for all new non-trivial logic
- **Document deviations** — explain any departures from standard patterns
- **Use existing patterns** — search codebase for similar implementations before introducing new ones
- **One type per file** — never combine multiple top-level public types in one `.java` file
- **Constants in ModConstants** — never use inline magic strings or numbers
- **No raw Minecraft server calls in operators** — operators receive everything via `Ports`
- **Registration via DeferredRegister** — never mutate Forge registries directly
- **Javadoc on public APIs** — every public class, method, and field must have a Javadoc comment

## Example Scenarios

### Example 1: New Feature — Operator

**Input**: `.github/.requirements/pr/347-add-levitate-operator.md`
```markdown
## Summary
Adds a new Operator2 that applies the Levitation mob effect to a target entity...

## Changes
- [ ] Create LevitateEntityOp.java implementing Operator2
- [ ] Register operator usage in relevant composite item
- [ ] Add unit tests
```

**Execution**:
1. Read requirements, identify scope: `operator/entity`
2. Read `src/main/java/bassebombecraft/operator/entity/` for existing operator patterns
3. Note how `Ports` input/output is used and how `Operators2.validateNotNull()` is called
4. Create `LevitateEntityOp.java` implementing `Operator2`:
   - Extract target entity from `Ports`
   - Validate with `Operators2.validateNotNull()`
   - Apply `MobEffects.LEVITATION` via `LivingEntity.addEffect()`
5. Wire into the appropriate composite item in `RegisteredItems` or item action class
6. Create `LevitateEntityOpTest.java` under `src/test/java/bassebombecraft/operator/entity/`
7. Run validation pipeline, fix any issues
8. Generate implementation summary

### Example 2: Bug Fix — Forge API Migration

**Input**: `.github/.requirements/pr/582-fix-renderlastevent.md`
```markdown
## Summary
Replaces removed RenderWorldLastEvent with RenderLevelStageEvent...

## Changes
- [ ] Replace RenderWorldLastEvent import and handler signature in StructureRenderEventHandler
- [ ] Adapt handler logic to new RenderLevelStageEvent.Stage enum
```

**Execution**:
1. Read requirements, identify scope: `event` / `client`
2. Read `StructureRenderEventHandler.java` to locate the old handler
3. Replace `RenderWorldLastEvent` with `RenderLevelStageEvent`
4. Add stage guard: only execute when `event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL`
5. Update import statements
6. Run `./gradlew compileJava` — fix any remaining errors
7. Run full validation pipeline
8. Generate implementation summary

### Example 3: New Config Key

**Input**: `.github/.requirements/pr/123-config-effect-duration.md`
```markdown
## Summary
Adds a configurable duration for the Levitation effect...

## Changes
- [ ] Add LEVITATION_DURATION constant to ModConstants
- [ ] Add ForgeConfigSpec entry in ModConfiguration
- [ ] Read config value in LevitateEntityOp
```

**Execution**:
1. Read requirements, identify scope: `config`
2. Read `ModConstants.java` for constant naming conventions
3. Add `LEVITATION_DURATION_DEFAULT = 100` to `ModConstants`
4. Add `ForgeConfigSpec.IntValue levitationDuration` to `ModConfiguration`
5. Update `LevitateEntityOp` to read `ModConfiguration.levitationDuration.get()`
6. Run validation pipeline
7. Generate implementation summary

## Additional Context

- **Build tool**: `./gradlew` (Gradle wrapper, run from project root)
- **Java version**: Java 17
- **Forge version**: 40.3.0 (MC 1.18.2)
- **Package root**: `bassebombecraft` — all source lives under `src/main/java/bassebombecraft/`
- **Mod entry point**: `BassebombeCraft.java` — handles lifecycle events and proxy dispatch
- **Operator pattern**: `Operator2` functional interface composed by `Operators2.run()`; all I/O goes through `Ports`
- **Registration pattern**: `DeferredRegister<T>` fields in `Registered*.java`; deferred objects accessed via `RegistryObject<T>.get()`
- **Event buses**: `MinecraftForge.EVENT_BUS` for gameplay events; `FMLJavaModLoadingContext.get().getModEventBus()` for mod lifecycle events
- **Proxy pattern**: `DistExecutor.runForDist()` selects `ClientProxy` or `ServerProxy` at runtime
- **Config**: `ForgeConfigSpec` builder in `ModConfiguration`; TOML file at `run/config/bassebombecraft-common.toml`

## Related Prompts

- `.github/prompts/create-pr-from-build-errors.prompt.md` - Creates the requirements file this prompt implements
- `.github/prompts/pr-template.md` - Template structure that requirements files follow

## Maintenance Notes

This prompt should be updated when:
- Project upgrades to a new Forge or Minecraft version
- New architectural patterns are introduced (e.g. new operator types)
- Build / validation toolchain changes
- Testing conventions evolve
- New coding standards are established
