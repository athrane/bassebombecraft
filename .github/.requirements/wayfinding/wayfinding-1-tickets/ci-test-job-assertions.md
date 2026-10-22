# Ticket — What the CI Test job must prove

**Type**: grilling
**Status**: open
**Blocked By**: server-smoke-test, ci-version-sources

## Question

The `test_job` in `build.yml` starts the `bassebombecraft/minecraft` image, prints `docker logs` twice and reads the container health status, but asserts nothing about the mod. What should make it fail: the container is unhealthy, the log never shows the server "Done" line, or the log contains a mod-loading error? The answer decides whether the job can gate the `release_job` that runs after it.

## Resolution

_(filled in when Status becomes resolved — interview recap)_
