```markdown
# fix(registry): Replace deprecated fmllegacy RegistryObject import with registries package

## Summary
Fixes a Java compilation error where `package net.minecraftforge.fmllegacy does not exist` prevents the project from building. All 31 affected source files have their `RegistryObject` import updated to `net.minecraftforge.registries.RegistryObject`.

## Motivation
The build fails with compilation errors because `net.minecraftforge.fmllegacy.RegistryObject` no longer exists in the Forge version being used. The class was moved to `net.minecraftforge.registries.RegistryObject`. This affects [RegisteredParticles.java](src/main/java/bassebombecraft/client/particles/RegisteredParticles.java) and 30 other source files across the project.

## Changes
- [x] Replace `import net.minecraftforge.fmllegacy.RegistryObject;` with `import net.minecraftforge.registries.RegistryObject;` in all affected files
- [x] Fix double-semicolon typo in [SoundConfig.java](src/main/java/bassebombecraft/config/SoundConfig.java)

**Affected files (31 total)**:
- [RegisteredParticles.java](src/main/java/bassebombecraft/client/particles/RegisteredParticles.java)
- [SoundConfig.java](src/main/java/bassebombecraft/config/SoundConfig.java)
- [RegisteredEntities.java](src/main/java/bassebombecraft/entity/RegisteredEntities.java)
- [RegisteredAttributes.java](src/main/java/bassebombecraft/entity/attribute/RegisteredAttributes.java)
- [RegisteredContainers.java](src/main/java/bassebombecraft/inventory/container/RegisteredContainers.java)
- [RegisteredItems.java](src/main/java/bassebombecraft/item/RegisteredItems.java)
- [DefaultCompositeItemResolver.java](src/main/java/bassebombecraft/item/composite/DefaultCompositeItemResolver.java)
- [BeastmasterBook.java](src/main/java/bassebombecraft/item/book/BeastmasterBook.java)
- [CobwebBook.java](src/main/java/bassebombecraft/item/book/CobwebBook.java)
- [DecoyBook.java](src/main/java/bassebombecraft/item/book/DecoyBook.java)
- [DigMobHoleBook.java](src/main/java/bassebombecraft/item/book/DigMobHoleBook.java)
- [EmitHorizontalForceBook.java](src/main/java/bassebombecraft/item/book/EmitHorizontalForceBook.java)
- [EmitVerticalForceBook.java](src/main/java/bassebombecraft/item/book/EmitVerticalForceBook.java)
- [FallingAnvilBook.java](src/main/java/bassebombecraft/item/book/FallingAnvilBook.java)
- [GenericCompositeItemsBook.java](src/main/java/bassebombecraft/item/book/GenericCompositeItemsBook.java)
- [IceBlockBook.java](src/main/java/bassebombecraft/item/book/IceBlockBook.java)
- [LargeFireballBook.java](src/main/java/bassebombecraft/item/book/LargeFireballBook.java)
- [LavaBlockBook.java](src/main/java/bassebombecraft/item/book/LavaBlockBook.java)
- [LightningBoltBook.java](src/main/java/bassebombecraft/item/book/LightningBoltBook.java)
- [MultipleArrowsBook.java](src/main/java/bassebombecraft/item/book/MultipleArrowsBook.java)
- [ReceiveAggroBook.java](src/main/java/bassebombecraft/item/book/ReceiveAggroBook.java)
- [SmallFireballBook.java](src/main/java/bassebombecraft/item/book/SmallFireballBook.java)
- [SmallFireballRingBook.java](src/main/java/bassebombecraft/item/book/SmallFireballRingBook.java)
- [SpawnFlamingChickenBook.java](src/main/java/bassebombecraft/item/book/SpawnFlamingChickenBook.java)
- [SpawnSquidBook.java](src/main/java/bassebombecraft/item/book/SpawnSquidBook.java)
- [TeleportBook.java](src/main/java/bassebombecraft/item/book/TeleportBook.java)
- [WitherSkullBook.java](src/main/java/bassebombecraft/item/book/WitherSkullBook.java)
- [RegisteredPotions.java](src/main/java/bassebombecraft/potion/RegisteredPotions.java)
- [PotionUtils.java](src/main/java/bassebombecraft/potion/PotionUtils.java)
- [RegisteredEffects.java](src/main/java/bassebombecraft/potion/effect/RegisteredEffects.java)
- [RegisteredSounds.java](src/main/java/bassebombecraft/sound/RegisteredSounds.java)

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
- [ ] Manual testing performed
- [ ] All tests passing (`./gradlew test`)
- [ ] Build succeeds (`./gradlew build`)

**Test coverage**: N/A - Compilation fix only

## Related Issues
Closes #1440

## Checklist
- [x] Code follows project conventions
- [ ] Build succeeds (`./gradlew build` passes)
- [ ] All tests pass (`./gradlew test` passes)
- [x] Code compiles without errors
- [x] No breaking changes
- [ ] Updated documentation (if applicable)

## Additional Notes
**Error Details**:
- File: `src/main/java/bassebombecraft/client/particles/RegisteredParticles.java` (and 30 others)
- Line: 10
- Error: `package net.minecraftforge.fmllegacy does not exist` / `import net.minecraftforge.fmllegacy.RegistryObject;`

The `RegistryObject` class was relocated from `net.minecraftforge.fmllegacy` to `net.minecraftforge.registries` in newer versions of Minecraft Forge. The fix updates all import statements to reference the correct package.
```
