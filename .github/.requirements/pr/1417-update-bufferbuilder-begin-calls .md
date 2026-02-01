# fix(rendering): Update BufferBuilder.begin() calls to use VertexFormat.Mode

## Summary
Fixed compilation error in RenderingUtils.java by updating BufferBuilder.begin() method calls to use VertexFormat.Mode enum instead of deprecated GL11 integer constants.

## Motivation
The Minecraft/Forge rendering API has changed the BufferBuilder.begin() method signature from accepting integer constants (GL11.GL_LINES, GL11.GL_QUADS) to requiring VertexFormat.Mode enum values. This change causes build failures with the error:

```
The method begin(VertexFormat.Mode, VertexFormat) in the type BufferBuilder is not applicable for the arguments (int, VertexFormat)
```

## Changes
- [x] Updated all BufferBuilder.begin() calls in RenderingUtils.java
- [x] Replaced GL11.GL_LINES with VertexFormat.Mode.LINES
- [x] Replaced GL11.GL_QUADS with VertexFormat.Mode.QUADS
- [x] Verified all rendering methods use correct API

## Type of Change
- [x] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update
- [ ] Refactoring (no functional changes)
- [ ] Performance improvement
- [ ] Test coverage improvement

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [x] Manual testing performed
- [x] All tests passing (build compiles successfully)

**Test coverage**: Fix resolves compilation error; existing rendering functionality preserved.

## Related Issues
Fixes build error: BufferBuilder.begin() API incompatibility

## Checklist
- [x] Code follows project conventions
- [ ] Code lints without errors
- [x] All tests pass (build succeeds)
- [ ] Build succeeds
- [ ] JSDoc comments added for public APIs (N/A - Java project)
- [ ] Updated documentation (if applicable)
- [x] No breaking changes (or documented in PR description)
- [x] Commit messages follow Conventional Commits format

## Additional Notes
This is a necessary update to maintain compatibility with the current Minecraft/Forge rendering API. The change affects multiple rendering methods:
- renderWireframeBox() - GL_LINES → Mode.LINES
- renderSolidBox() and variants - GL_QUADS → Mode.QUADS  
- renderLine() - GL_LINES → Mode.LINES

All affected methods use the immediate-mode rendering pattern with Tesselator/BufferBuilder.
