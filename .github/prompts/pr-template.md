# GitHub Pull Request Template

Generic template for creating pull requests in the BasseBombeCraft project.

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

**Examples**:
- `feat(entity): Add new mob effect system`
- `fix(potion): Correct effect duration calculation`
- `docs(readme): Update installation instructions`
- `refactor(event): Simplify event handler registration`
- `test(block): Add tests for custom block behavior`

## PR Description Template

```markdown
## Summary
Brief description of what this PR accomplishes (1-3 sentences).

## Motivation
Why is this change needed? What problem does it solve?

## Changes
List the key changes made:
- [ ] Change 1: [Description]
- [ ] Change 2: [Description]
- [ ] Change 3: [Description]

## Type of Change
- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Testing
Describe how the changes were tested:
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing performed in-game
- [ ] All tests passing (`./gradlew test`)

**Test coverage**: [If applicable, note new/changed test coverage]

## Related Issues
Closes #[issue-number]
Related to #[issue-number]

## Checklist
- [ ] Code follows project conventions and Minecraft Forge best practices
- [ ] Code compiles without errors (`./gradlew build` passes)
- [ ] Code follows Java style guidelines
- [ ] All tests pass (`./gradlew test` passes)
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
- [ ] Proper null checking and validation
- [ ] Static factory methods used consistently where appropriate
- [ ] Design patterns implemented correctly
- [ ] Proper use of Minecraft/Forge APIs
- [ ] No deprecated API usage without justification

### Testing
- [ ] Adequate test coverage for new/changed code
- [ ] Tests follow established patterns
- [ ] Integration tests added for system interactions (if applicable)
- [ ] Tests are deterministic and reproducible

### Documentation
- [ ] Javadoc comments for public APIs
- [ ] Inline comments for complex logic
- [ ] README or documentation updated (if applicable)
- [ ] Coding instructions updated (if new patterns introduced)

### Build & Validation
- [ ] `./gradlew build` succeeds
- [ ] `./gradlew test` passes
- [ ] `./gradlew check` passes (if applicable)
- [ ] No compilation errors or warnings
- [ ] No runtime errors in test environment

### Architecture
- [ ] Changes align with existing architectural patterns
- [ ] No unnecessary dependencies introduced
- [ ] Proper separation of concerns
- [ ] Minecraft event handlers registered correctly
- [ ] Resource locations and registry entries properly defined
