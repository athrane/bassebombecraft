# Ticket — Is a unit-test framework part of 4.1

**Type**: grilling
**Status**: resolved
**Blocked By**: none

## Question

The project has no `src/test` directory and no JUnit or Mockito dependency (`#1454` fixed a Gradle failure around the test task), while `.vscode/settings.json` already lists `src/test/java` as a source path. Is adding a test framework in scope for `1.18.2-4.1`, deferred to a later release, or dropped? If in scope, which first targets (for example the pure-logic operators)?

## Resolution

Resolved by the user when revising the map: unit tests are **in scope** for `1.18.2-4.1`, and adding them is **step 2**, right after getting a JDK 17 on the PATH. The "which framework, where, which first targets, and how to sequence against the compile fixes" parts are carried by the new ticket `test-framework-setup`.
