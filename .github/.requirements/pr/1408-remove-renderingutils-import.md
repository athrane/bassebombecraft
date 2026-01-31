# fix(rendering): Remove unresolved RenderingUtils import

## Summary
Fixes build error caused by unresolved static import of `renderTriangleBillboard` from `RenderingUtils` class in `DefaultTeamRenderer`.

## Motivation
The project fails to build with the error: "The import bassebombecraft.client.rendering.RenderingUtils.renderTriangleBillboard cannot be resolved". This import is for a method that is commented out and not being used, causing compilation to fail.

## Changes
- [x] Remove unused static import: `bassebombecraft.client.rendering.RenderingUtils.renderTriangleBillboard`
- [x] Clean up unused import from DefaultTeamRenderer class

## Type of Change
- [x] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Testing
- [x] Build succeeds (`./gradlew build` passes)
- [ ] Manual testing performed (rendering still works correctly)
- [ ] No functional changes - only removed dead import

**Test coverage**: No new tests needed - removing unused import only.

## Related Issues
Closes #123

## Checklist
- [x] Code follows project conventions
- [x] Build succeeds (`./gradlew build` passes)
- [x] No breaking changes
- [x] Commit messages follow Conventional Commits format

## Additional Notes
The `renderTriangleBillboard` method call on line 69 is already commented out, so removing this import has no functional impact. The class is also marked as `@Deprecated`, suggesting it may be removed or refactored in the future.
