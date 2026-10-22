# Ticket — Is the access transformer still needed and valid

**Type**: research
**Status**: open
**Blocked By**: none

## Question

`src/main/resources/META-INF/accesstransformer.cfg` declares two entries: `GoalSelector f_25345_ # goals`, and a `Projectile <init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V` constructor written with the pre-1.17 package names. `build.gradle` has the `accessTransformer = ...` line commented out, and `#1445` replaced the code's direct access to the private `GoalSelector` field. Is either entry still used by the source, will the stale entry make Forge fail or warn when it loads the file from the jar, and should the file be deleted, corrected, or enabled in `build.gradle`?

## Resolution

_(filled in when Status becomes resolved)_
