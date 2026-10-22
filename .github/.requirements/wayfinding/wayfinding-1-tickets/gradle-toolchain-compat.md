# Ticket — Which Gradle, JDK and plugin versions the build script supports

**Type**: research
**Status**: open
**Blocked By**: jdk-on-path

## Question

`build.gradle` uses ForgeGradle `[6.0,6.2)`, the Shadow plugin `7.1.2` and `jcenter()`; the committed wrapper is Gradle 8.11.1, but the working tree has an uncommitted change to 9.7.1; and no JDK 17 was on the PATH of the shell used for charting (fixed by `jdk-on-path`). Which Gradle version range and which JDK does this plugin combination actually support, which of `jcenter()` / Shadow 7.1.2 must change, and should the uncommitted wrapper change be reverted?

## Resolution

_(filled in when Status becomes resolved)_
