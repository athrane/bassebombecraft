# Ticket — Does a dedicated server start

**Type**: task
**Status**: open
**Blocked By**: compile-baseline

## Question

Run `./gradlew runServer` (or start the built `-server` jar on a Forge 1.18.2 server). Does it reach the "Done" line without class-loading errors from client-only classes (the `ClientProxy` / `ServerProxy` split, renderers, screens)? Record the exact log line that marks success; `ci-test-job-assertions` will reuse it.

## Resolution

_(filled in when Status becomes resolved — pass/fail, the success log line, any client-class loading errors)_
