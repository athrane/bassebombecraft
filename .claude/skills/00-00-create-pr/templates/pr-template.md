# GitHub Pull Request Template

Template for PR requirements files in the BasseBombeCraft project. It extends the shorter generic template in [pr-template-generic.md](./pr-template-generic.md) with an Implementation Plan and a Documentation Plan.

## PR Title Format

Use [Conventional Commits](https://www.conventionalcommits.org/) format:

```
<type>(<scope>): <description>
```

**Types**:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `refactor`: Code refactoring (no functional changes)
- `test`: Adding or updating tests
- `chore`: Build process, dependency updates, tooling
- `perf`: Performance improvements
- `style`: Code style/formatting changes

**Scopes** are the affected package or area: `item`, `entity`, `block`, `event`, `rendering`, `operator`, `network`, `registry`, `config`, `potion`, `sound`, `structure`, `build`, `ci`, and so on.

**Examples**:
- `feat(item): Add levitation book`
- `fix(rendering): replace removed getMatrixStack() with getPoseStack()`
- `fix(registry): Replace deprecated fmllegacy RegistryObject import`
- `chore(ci): Upgrade build.yml to resolve deprecated set-output command`
- `refactor(operator): Simplify target selection operators`

## PR Description Template

The generated file's first line is always the PR title as a level-1 heading, in the same `<type>(<scope>): <description>` format described above — every section below follows it in the same file. Do not wrap the file in a code fence.

```markdown
# <type>(<scope>): <description>

## Summary
Brief description of what this PR accomplishes (1-3 sentences).

## Motivation
Why is this change needed? What problem does it solve? For a build or runtime failure, quote the exact error.

## Changes

### Files Deleted

- **Delete `[path/to/file]`** — [Reason for deletion]

### Files Updated

- **`[path/to/file]`** — [Description of changes]

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Implementation Plan

Describe the ordered execution phases for this PR:

### Phase 1 — [Phase name]

[Description of steps]

### Phase 2 — [Phase name]

[Description of steps]

## Testing

[Describe which test files are added/updated, or note that no test changes are planned and why.
The project currently has no src/test directory and no JUnit/Mockito dependency in build.gradle.]

- [ ] Unit tests added/updated
- [ ] Manual testing performed in-game (`./gradlew runClient`)
- [ ] `./gradlew compileJava` passes
- [ ] `./gradlew build` passes

**Test coverage**: [If applicable, note new/changed test coverage]

### Manual validation steps

| # | Check | How to verify |
|---|-------|---------------|
| 1 | [Check description] | [Verification method, e.g. "Launch client; mod reaches main menu with no error in run/logs/debug.log"] |

## Documentation Plan

_Omit this section if no documentation changes are required._

| File | Changes |
|------|---------|
| `[path/to/doc.md]` | [Description of changes] |

## Related Issues
Closes #[issue-number]
Related to #[issue-number]

## Checklist
- [ ] Code follows project conventions and Minecraft Forge best practices
- [ ] Code compiles without errors (`./gradlew compileJava` passes)
- [ ] Build succeeds (`./gradlew build` passes)
- [ ] Javadoc comments added for public APIs
- [ ] Updated documentation (if applicable)
- [ ] No breaking changes (or documented in PR description)
- [ ] Commit messages follow Conventional Commits format

## Additional Notes
Any additional context, screenshots, or information that reviewers should know.
```

## PR Review Checklist (for Reviewers)

### Code Quality
- [ ] Code follows project architectural patterns and Minecraft Forge conventions
- [ ] One public type per `.java` file; package matches directory
- [ ] Constants live in `ModConstants` — no inline magic strings or numbers
- [ ] Guard clauses / early returns; no deeply nested blocks
- [ ] No deprecated API usage without justification

### Forge Specifics
- [ ] Registrables go through `DeferredRegister` in the matching `Registered*` class — no eager `.get()` during class initialisation
- [ ] Correct event bus (`MinecraftForge.EVENT_BUS` for gameplay, mod event bus for lifecycle)
- [ ] Client-only code stays on the client side (`ClientProxy` / `Dist.CLIENT`)
- [ ] Network packets registered on the `SimpleChannel`

### Documentation
- [ ] Javadoc comments on public APIs
- [ ] README or other documentation updated (if applicable)

### Build & Validation
- [ ] `./gradlew compileJava` passes
- [ ] `./gradlew build` succeeds
- [ ] Mod starts via `./gradlew runClient` without registry or class-loading errors (for runtime-affecting changes)
