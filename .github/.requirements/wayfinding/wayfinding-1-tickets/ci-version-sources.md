# Ticket — Single source of truth for versions in CI

**Type**: grilling
**Status**: open
**Blocked By**: forge-version-pin

## Question

Versions are held in several places: `gradle.properties` (`minecraft_version`, `forge_version`, `mod_version`), `version.json` (`modVersion`, `minecraftVersion`, read by `build.yml` to name the jar), `bassebombecraft_update.json`, and a hard-coded `FORGE_VERSION: 37.1.1` in the `create_container_job` of `.github/workflows/build.yml`, which is a 1.17.1 build passed alongside `MC_VERSION` 1.18.2 to the Docker image. Which file is authoritative for each value, and should `build.yml` read the Forge version from it instead of hard-coding it? Past CI breakages (`#1457`–`#1461`) all came from these copies drifting.

## Resolution

_(filled in when Status becomes resolved — interview recap)_
