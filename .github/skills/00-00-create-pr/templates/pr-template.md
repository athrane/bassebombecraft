# GitHub Pull Request Template — BasseBombeCraft (extended)

Bundled by [`.github/skills/00-00-create-pr/SKILL.md`](../SKILL.md). This is a **richer** variant of [`.github/prompts/pr-template.md`](../../../prompts/pr-template.md) — it adds **Implementation Plan** and **Documentation Plan** sections for PR specs produced by the `00-00-create-pr` skill. Existing in-repo PR files (e.g. `14XX-*.md`) use the plain template; new PR specs produced by this skill should use this richer one.

## PR Title Format

Use [Conventional Commits](https://www.conventionalcommits.org/) format:

```
<type>(<scope>): <description>
```

**Types**: `feat`, `fix`, `docs`, `refactor`, `test`, `chore`, `perf`, `style`.

**Common scopes** (BasseBombeCraft): `potion`, `entity`, `projectile`, `inventory`, `rendering`, `screen`, `event`, `block`, `item`, `config`, `sound`, `network`, `world`, `ci`, `docker`, `readme`.

**Examples**:
- `feat(potion): Add new mob effect system`
- `fix(rendering): Correct horizontal distance calculation in GenericEggProjectile`
- `docs(readme): Update installation instructions`
- `chore(ci): Update build pipeline to use Java 17`

## PR Description Template

```markdown
## Summary
Brief description of what this PR accomplishes (1–3 sentences).

## Motivation
Why is this change needed? What problem does it solve? Reference affected files via bracketed markdown links, e.g. `[MobPrimingEffect.java](src/main/java/bassebombecraft/potion/effect/MobPrimingEffect.java#L87)`.

## Changes

### Files Deleted
_None._  (or `- **[path/to/file]**` with reason)

### Files Updated
- **`[path/to/file]`** — [Description of changes]
- **`[path/to/new/file]`** — New ... [description]

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Implementation Plan

Describe the ordered execution phases for this PR. Each phase should be small enough to land as a single commit and reviewable on its own.

### Phase 1 — [Phase name]

[Steps]

### Phase 2 — [Phase name]

[Steps]

## Testing

### Java build & tests

[Describe which `./gradlew` tasks are run, or note if no test changes are required and why.]

- [ ] `./gradlew build` succeeds
- [ ] `./gradlew test` passes
- [ ] Affected module compiles cleanly

**Test coverage**: [If applicable, note new/changed test coverage. Most PRs in this mod have no unit tests; manual in-game validation is the norm.]

### Manual validation steps

| # | Check | How to verify |
|---|-------|---------------|
| 1 | [Check description] | [Verification method — typically "launch dev client, do X, observe Y"] |

## Documentation Plan

_Omit this section entirely if no documentation changes are required._

Describe the documentation changes needed:

| File | Changes |
|------|---------|
| `README.md` | [Description] |
| `README.txt` | [Description] |
| Javadoc on `[ClassName.java](src/main/java/.../ClassName.java)` | [Description] |

## Related Issues
Closes #[issue-number]
Related to #[issue-number]
[If graduated from a wayfinder map: link the source ticket file(s).]

## Checklist
- [ ] Code follows project conventions (static factory methods, `RegisteredItems`/`RegisteredEntities` registration, operator pattern, etc.)
- [ ] Code compiles without errors (`./gradlew build` passes)
- [ ] Code follows Java style guidelines (one top-level class per file, Javadoc on public APIs)
- [ ] All tests pass (`./gradlew test` passes for the affected module)
- [ ] Build succeeds (`./gradlew build` passes)
- [ ] Javadoc comments added for public APIs
- [ ] Updated documentation (if applicable — `README.md`, `README.txt`, wiki, Javadoc)
- [ ] No breaking changes (or documented in PR description)
- [ ] Commit messages follow Conventional Commits format

## Additional Notes
Any additional context, screenshots, root-cause analyses, or information that reviewers should know.
```

## PR Review Checklist (for Reviewers)

### Code Quality
- [ ] Code follows project architectural patterns (operator pattern, registered-items pattern, event-driven handlers)
- [ ] Java 17 + Forge 1.17.1 patterns only (no pre-1.17 OpenGL, no deprecated Forge APIs)
- [ ] Static factory methods used consistently (`create()` methods on operators, items, entities)
- [ ] Proper use of `TypeUtils`-style validation where types cross boundaries
- [ ] Proper use of Minecraft/Forge APIs (no direct GL state calls; `PoseStack`-based rendering)

### Testing
- [ ] Affected files compile cleanly (`./gradlew build` passes)
- [ ] `./gradlew test` passes
- [ ] Manual in-game testing performed for GUI/rendering/entity changes

### Documentation
- [ ] Javadoc comments for public APIs and class members
- [ ] Inline comments for non-obvious logic
- [ ] `README.md` / `README.txt` updated (if applicable)
- [ ] Wiki updated (if applicable)

### Build & Validation
- [ ] `./gradlew build` succeeds
- [ ] `./gradlew test` passes
- [ ] No new compilation errors or warnings
- [ ] No runtime errors in test environment

### Architecture
- [ ] Changes align with existing architectural patterns (`RegisteredItems.register(...)`, `TypeUtils.validate(...)`, event-driven side effects)
- [ ] No unnecessary dependencies introduced
- [ ] Proper separation of concerns (operators, events, items, entities, projectiles)
- [ ] Forge registry entries and resource locations correctly defined