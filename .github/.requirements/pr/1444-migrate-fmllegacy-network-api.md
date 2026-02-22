## Summary
Migrate all network layer code away from the removed `net.minecraftforge.fmllegacy.network` package to the current Forge 40.x+ networking API, fixing compilation failures in `NetworkChannelHelper`, `CompositeMagicItem`, `GenericCompositeProjectileEntity`, and all six network packet classes.

## Motivation
The `net.minecraftforge.fmllegacy` package was a temporary compatibility shim that no longer exists in Forge 40.x. All usages of `NetworkRegistry.newSimpleChannel`, `NetworkHooks.openGui`, `NetworkHooks.getEntitySpawningPacket`, and the `Context` class from this package must be replaced with the current equivalents from `net.minecraftforge.network`. Without this fix, the entire networking subsystem fails to compile, breaking charm delivery, graphical effects, particle rendering, potion effect delivery, and custom entity spawning.

## Changes
- [ ] Change 1: Replace `import static net.minecraftforge.fmllegacy.network.NetworkRegistry.newSimpleChannel` with the correct import from `net.minecraftforge.network.NetworkRegistry` in `NetworkChannelHelper.java`
- [ ] Change 2: Replace `SimpleChannel` type reference with `SimpleChannel` from `net.minecraftforge.network.simple.SimpleChannel` in `NetworkChannelHelper.java`
- [ ] Change 3: Replace `import static net.minecraftforge.fmllegacy.network.NetworkHooks.openGui` with the correct import from `net.minecraftforge.network.NetworkHooks` in `CompositeMagicItem.java`
- [ ] Change 4: Replace `NetworkHooks.getEntitySpawningPacket(this)` call — update import to `net.minecraftforge.network.NetworkHooks` in `GenericCompositeProjectileEntity.java`
- [ ] Change 5: Replace `Context ctx = context.get()` — update import to `net.minecraftforge.network.NetworkEvent.Context` in `AddCharm.java`
- [ ] Change 6: Replace `Context ctx = context.get()` — update import to `net.minecraftforge.network.NetworkEvent.Context` in `AddGraphicalEffect.java`
- [ ] Change 7: Replace `Context ctx = context.get()` — update import to `net.minecraftforge.network.NetworkEvent.Context` in `AddParticleRendering.java`
- [ ] Change 8: Replace `Context ctx = context.get()` — update import to `net.minecraftforge.network.NetworkEvent.Context` in `AddPotionEffect.java`
- [ ] Change 9: Replace `Context ctx = context.get()` — update import to `net.minecraftforge.network.NetworkEvent.Context` in `RemoveEffect.java`
- [ ] Change 10: Replace `Context ctx = context.get()` — update import to `net.minecraftforge.network.NetworkEvent.Context` in `RemoveParticleRendering.java`

## Type of Change
- [x] Bug fix (non-breaking change which fixes an issue)
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

**Test coverage**: Compilation fix. Manual in-game testing required to verify that charm application, graphical effects, particle rendering, and potion effect delivery across client/server boundaries still function correctly after the network API migration.

## Related Issues
Closes #1444

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

## PR Title
`fix(network): Migrate fmllegacy network imports to net.minecraftforge.network API`

## Additional Notes
Affected files:
- `src/main/java/bassebombecraft/network/NetworkChannelHelper.java` — channel registration
- `src/main/java/bassebombecraft/item/composite/CompositeMagicItem.java` — GUI opening
- `src/main/java/bassebombecraft/entity/projectile/GenericCompositeProjectileEntity.java` — entity spawning packet
- `src/main/java/bassebombecraft/network/packet/AddCharm.java`
- `src/main/java/bassebombecraft/network/packet/AddGraphicalEffect.java`
- `src/main/java/bassebombecraft/network/packet/AddParticleRendering.java`
- `src/main/java/bassebombecraft/network/packet/AddPotionEffect.java`
- `src/main/java/bassebombecraft/network/packet/RemoveEffect.java`
- `src/main/java/bassebombecraft/network/packet/RemoveParticleRendering.java`

Check if `NetworkHooks.getEntitySpawningPacket` was also renamed — in Forge 40.x it may be `IEntityAdditionalSpawnData.getAddEntityPacket()` that is the preferred way, or the method name may differ. Verify with Forge 40.x changelog before applying.
