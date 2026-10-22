# Ticket — How to add unit tests to the project

**Type**: grilling
**Status**: open
**Blocked By**: jdk-on-path, gradle-toolchain-compat

## Question

Step 2 of the map: unit tests are in scope for `1.18.2-4.1` (see `test-framework-scope`). Settle the setup before any test is written:

1. Which framework and versions go into `build.gradle` (JUnit 5, and whether Mockito is needed at all), given the ForgeGradle and Gradle versions chosen by `gradle-toolchain-compat`.
2. Where the tests live and how they are named (`src/test/java/bassebombecraft/...`, mirroring the source packages, `[ClassName]Test.java`), and whether `.github/workflows/build.yml` already runs them through `./gradlew build`.
3. Which first targets, preferring code that needs no running Minecraft: for example the `Operator2` / `Ports` operators and plain utility classes. Name the first three to five.
4. How step 2 sequences against `compile-baseline`: tests compile against `src/main`, so they only run once the main code compiles. Options include writing the setup and the first tests now and running them after the compile fixes, or doing the compile fixes first.

## Resolution

_(filled in when Status becomes resolved — interview recap; then graduate it into a PR requirements file with `00-00-create-pr`)_
