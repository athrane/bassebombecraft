---
name: 00-01-implement-pr
description: 'Reads a .github/.requirements/pr/[nnnn]-[slug].md requirements file produced by 00-00-create-pr, builds a dependency-ordered implementation plan, executes it, then runs the validation pipeline. Use to implement a PR for the BasseBombeCraft Minecraft Forge mod.'
argument-hint: '[pr-spec-file]'
---

# Skill: Implement PR

Generate and execute a complete implementation plan for a pull request based on a requirements file created by the [`00-00-create-pr`](../00-00-create-pr/SKILL.md) skill (or graduated from [`wayfinder`](../wayfinder/SKILL.md)). The second step of the `00-00-create-pr` → `00-01-implement-pr` → `00-03-review-pr` workflow.

## When to Use This Skill

- The user asks to implement a PR or execute a `.github/.requirements/pr/[nnnn]-[slug].md` file
- The user is continuing the `00-00-create-pr` → `00-01-implement-pr` → `00-03-review-pr` workflow
- The user has a graduated wayfinder ticket that needs implementation

## Prerequisites

| Resource | Purpose |
|----------|---------|
| `.github/.requirements/pr/[nnnn]-[slug].md` | The requirements file to implement (required input) |
| [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) | The plain PR template (for understanding the spec shape) |
| `src/main/java/bassebombecraft/` | Existing Java source for the affected domain |
| `version.json` | Minecraft + mod version reference |
| [`00-00-create-pr/SKILL.md`](../00-00-create-pr/SKILL.md) | Produces the requirements file this skill implements |
| [`00-03-review-pr/SKILL.md`](../00-03-review-pr/SKILL.md) | Reviews the resulting implementation |

## Project Conventions Snapshot

Standing facts this skill should respect. Derived from existing `.github/.requirements/pr/14XX-*.md` files plus the codebase; do not invent conventions that contradict these.

| Convention | Source of truth |
|------------|-----------------|
| **Language & toolchain** | Java 17 only. `./gradlew build`, `./gradlew test`, `./gradlew compileJava` are the canonical validation commands. |
| **Minecraft version** | Forge 1.17.1 baseline. New code must not use pre-1.17 OpenGL or deprecated Forge APIs. |
| **Architecture patterns** | `RegisteredItems` / `RegisteredEntities` registration pattern; `TypeUtils`-style validation; operator pattern (`src/main/java/bassebombecraft/operator/`); event-driven side effects (`src/main/java/bassebombecraft/event/`) |
| **Java style** | Javadoc on public APIs; one top-level class per file; static factory methods (`create()`); `RegisteredItems.register(...)` style registration; `PlayerUtils` / `EntityUtils` / `BlockUtils` helper classes |
| **Build verification** | `./gradlew compileJava` first (fast), then `./gradlew build` (full), then `./gradlew test` for the affected module |
| **What "done" looks like** | `./gradlew build` succeeds, `./gradlew test` passes for the affected module, manual in-game verification for GUI/rendering changes |

## Step-by-Step Workflow

### Phase 1: Requirements Analysis

1. **Read the requirements file** — extract PR title (type + scope), parse **Summary**, **Motivation**, **Changes**, **Implementation Plan**, **Testing**, and **Related Issues** sections. Note all `[github-issue-number]-[slug].md` references for traceability.
2. **Analyse scope** — determine affected domains (potion, entity, projectile, inventory, client/rendering, client/screen, event, block, item, config, sound, network, world); locate related files in `src/main/java/bassebombecraft/[domain]/`.
3. **Read project context**:
   - [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) for the PR spec shape conventions
   - 2–3 existing `.github/.requirements/pr/14XX-*.md` files in the same domain for phrasing conventions
   - The existing files in `src/main/java/bassebombecraft/[domain]/` for patterns to follow
4. **Assess dependencies** — new registration entries (`RegisteredItems`, `RegisteredEntities`); reusable existing types; `TypeUtils` validation patterns to follow; new `Operator` wiring in the operator graph.

### Phase 2: Implementation Planning

5. **Create the implementation checklist** — convert **Changes → Files Deleted / Files Updated** entries into concrete CREATE/MODIFY operations:
   - A path that doesn't yet exist, or a description starting with `New ...`, is a CREATE
   - An existing path with a description of changes is a MODIFY
   - Order CREATE operations: enums → records/data classes → interfaces → classes → registration calls → event wiring → tests
6. **Identify required patterns** — apply the architectural patterns from the Conventions Snapshot above:
   - Registration → look for `RegisteredItems.register(...)`, `RegisteredEntities.register(...)` patterns
   - Validation → look for `TypeUtils.validate(...)` patterns
   - Operator → look at how other operators in `src/main/java/bassebombecraft/operator/` are structured
   - Event-driven → look at how existing handlers in `src/main/java/bassebombecraft/event/` register
7. **Plan file structure** — apply one-top-level-class-per-file; one static factory per class.
8. **Optionally stress-test the plan** — for implementations involving significant ambiguity (e.g. choosing between two valid Forge APIs), invoke either [`00-02-grill-me`](../00-02-grill-me/SKILL.md) for a one-shot interview, or [`wayfinder`](../wayfinder/SKILL.md) if the ambiguity is large enough to warrant a multi-session map. Do not ask the user clarifying questions inline.

### Phase 3: Implementation Execution

9. **Create enums and records** — apply the operator pattern's data model; one enums/constants per file.
10. **Implement core logic** — apply the operator pattern; static factory method; `TypeUtils.validate(...)` on cross-boundary inputs; `Object.freeze(this)` on records where appropriate.
11. **Wire registration** — call `RegisteredItems.register(...)` / `RegisteredEntities.register(...)` from the appropriate bootstrap method; mirror how existing items/entities register.
12. **Wire event handlers** — register in the mod entrypoint's event bus setup; mirror how existing handlers in `src/main/java/bassebombecraft/event/` are wired.
13. **Update Javadoc** — single-line `/** ... */` on every public class and every public method; class-level Javadoc explaining the role and any parallel to sibling classes.

### Phase 4: Validation & Refinement

14. **Run the validation pipeline** in order, fix-and-retry until all pass:
    1. `./gradlew compileJava` — must succeed with no new errors
    2. `./gradlew build` — full build
    3. `./gradlew test` — for the affected module (most PRs in this mod have no `test/` source set; skip if `test/` does not exist for the affected domain)
15. **Verify scope** — `git diff --stat` shows only files named in the PR spec's **Changes** section are touched (excluding pre-existing failures in unrelated files like `PlayerUtils.java`/`GenericCompositeProjectileEntity.java`).
16. **Final review** — every planned CREATE/MODIFY operation done; Javadoc on every new public class; Conventional Commits commit message; `./gradlew build` green; `./gradlew test` green (or skipped with rationale); manual in-game verification done for GUI/rendering changes.

See [references/examples.md](./references/examples.md) for two fully worked scenarios (new feature implementation and bug fix implementation).

## Output Format

After successful implementation, emit a markdown summary to the chat:

```markdown
## Implementation Complete

### Files Created
- `src/main/java/bassebombecraft/[scope]/NewFile.java` — [Brief description]

### Files Modified
- `src/main/java/bassebombecraft/[scope]/ExistingFile.java` — [What changed]

### Registration / Wiring
- Registered `NewItem` via `RegisteredItems.bootstrap()`
- Wired event handler in `BassebombeCraft.java`

### Javadoc Added
- [ClassName] — [Brief description]
- [MethodName] — [Brief description]

### Validation Results
✅ `./gradlew compileJava` succeeded
✅ `./gradlew build` succeeded
✅ `./gradlew test` passed ([total] tests, or "skipped — no test/ source set for this domain")

### Conformance Verification
✅ Java 17 + Forge 1.17.1 patterns only (no pre-1.17 OpenGL, no deprecated Forge APIs)
✅ Static factory methods used on every new class
✅ `TypeUtils`-style validation on cross-boundary inputs
✅ Javadoc on every public class and method
✅ Registration calls in the correct bootstrap method
✅ File scope matches the PR spec's Changes section

### Ready for Commit
Commit message: `<type>(<scope>): <description>`

### Notes
[Any deviations from the PR spec, pre-existing build failures worked around, manual in-game verification notes.]
```

## Guard Rails

- Do not skip the validation pipeline; iterate fix-and-retry until `./gradlew compileJava`, `./gradlew build`, and `./gradlew test` (when applicable) all pass
- Do not deviate from the requirements file's **Changes** subsections without documenting the deviation in the output's **Notes** section
- Do not modify files outside the PR spec's scope — if a pre-existing build error in `PlayerUtils.java` or `GenericCompositeProjectileEntity.java` blocks `./gradlew build`, document it and skip those files rather than touching them
- Follow existing patterns in the affected domain rather than inventing new ones
- Use `Object.freeze(this)` on records/data classes; never mutate fields after construction
- Never use `Math.random()` if randomness is needed — use `RandomSource` (Minecraft 1.17.1+ API)
- Do not invent a new registration pattern — match `RegisteredItems.register(...)` / `RegisteredEntities.register(...)` exactly

## Troubleshooting

| Situation | Response |
|-----------|----------|
| Cannot read requirements file | Ask the user for the correct path |
| Scope not recognised | Ask the user to clarify affected domains |
| `./gradlew build` fails due to pre-existing unrelated errors (e.g. `PlayerUtils.java`, `GenericCompositeProjectileEntity.java`) | Document them in the output's **Notes**; treat as out-of-scope; verify only the targeted file changed |
| `./gradlew test` does not exist (no `test/` source set for the affected domain) | Skip the test step; note "no `test/` source set for this domain" in the output |
| Ambiguous Forge API choice | Invoke [`wayfinder`](../wayfinder/SKILL.md) to chart a `grilling` ticket, not inline Q&A |
| Breaking changes detected (e.g. removed public API) | Document in the output's **Notes** and ask the user to confirm |

## Validation Checklist

- [ ] Requirements file read successfully
- [ ] All changes from **Files Deleted / Files Updated** implemented
- [ ] `./gradlew compileJava` succeeded
- [ ] `./gradlew build` succeeded
- [ ] `./gradlew test` passed (or skipped with documented rationale)
- [ ] Java 17 + Forge 1.17.1 patterns only (no pre-1.17 OpenGL, no deprecated Forge APIs)
- [ ] Static factory methods on every new class
- [ ] `TypeUtils`-style validation on cross-boundary inputs
- [ ] Javadoc on every public class and method
- [ ] Registration in the correct bootstrap method
- [ ] File scope matches the PR spec's Changes section
- [ ] No breaking changes (or documented with user confirmation)

## Related Documents

- [`00-00-create-pr/SKILL.md`](../00-00-create-pr/SKILL.md) — produces the requirements file this skill implements
- [`00-03-review-pr/SKILL.md`](../00-03-review-pr/SKILL.md) — reviews the resulting implementation for compliance
- [`wayfinder/SKILL.md`](../wayfinder/SKILL.md) — the source of multi-ticket graduations that produce PR spec files
- [`.github/prompts/pr-template.md`](../../prompts/pr-template.md) — the plain PR template (for understanding the spec shape)
- [`.github/.requirements/pr/`](../../.requirements/pr/) — past PR specs to absorb phrasing conventions from
- [references/examples.md](./references/examples.md) — worked examples

## Maintenance Notes

Update this skill when:
- New validation tools are added (e.g. SpotBugs, Checkstyle)
- Java or Minecraft version upgrades change the build pipeline
- New registration patterns emerge (`RegisteredItems`-style additions for new domains)
- Javadoc conventions evolve