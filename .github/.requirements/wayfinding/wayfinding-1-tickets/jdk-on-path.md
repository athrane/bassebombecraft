# Ticket — A working JDK 17 on the PATH

**Type**: task
**Status**: open
**Blocked By**: none

## Question

Step 1 of the map. The shell used by the agent has no `java` on the PATH and no `JAVA_HOME` (`./gradlew compileJava` stops with "JAVA_HOME is not set and no 'java' command could be found in your PATH"), and the standard Windows install locations (`C:\Program Files\Java`, `C:\Program Files\Eclipse Adoptium`) are empty. Install a JDK 17 (CI uses Temurin 17, `actions/setup-java@v4` with `distribution: 'temurin'`), make it visible to the shells used for this project, and confirm with `java -version` (17.x) and `./gradlew --version` from the repository root.

A human step: it installs software on the user's machine. Record the install path, the exact `java -version` output, and which shells were checked (PowerShell, Git Bash, the VS Code terminal) and whether the Claude Code session needed a restart to see the new PATH.

## Resolution

_(filled in when Status becomes resolved)_
