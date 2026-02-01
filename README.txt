-------------------------------------------
Source installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "unrenamed" MCP source code (aka
srgnames) - this means that you will not be able to read them directly against
normal code.

Source pack installation information:

Standalone source installation
==============================

See the Forge Documentation online for more detailed instructions:
http://mcforge.readthedocs.io/en/latest/gettingstarted/

Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: You're left with a choice.
If you prefer to use Eclipse:
1. Run the following command: "gradlew genEclipseRuns" (./gradlew genEclipseRuns if you are on Mac/Linux)
2. Open Eclipse, Import > Existing Gradle Project > Select Folder 
   or run "gradlew eclipse" to generate the project.
(Current Issue)
4. Open Project > Run/Debug Settings > Edit runClient and runServer > Environment
5. Edit MOD_CLASSES to show [modid]%%[Path]; 2 times rather then the generated 4.

If you prefer to use IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Run the following command: "gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.

If you prefer to use VS Code:
1. Install the "Extension Pack for Java" extension in VS Code
2. Open the project folder in VS Code
3. If using a Linux/Mac codespace or environment, comment out the Windows-specific 
   Java path in gradle.properties:
   #org.gradle.java.home=C:\\Program Files\\Eclipse Adoptium\\jdk-17.0.4.101-hotspot
4. Make gradlew executable (Linux/Mac only): chmod +x gradlew
5. Run the following command: "./gradlew genVSCodeRuns" (or "gradlew genVSCodeRuns" on Windows)
6. Wait for Gradle to download dependencies and set up the workspace
7. Reload the Java workspace: Press Ctrl+Shift+P (or Cmd+Shift+P on Mac), 
   type "Java: Clean Java Language Server Workspace", then reload VS Code
8. Ensure the Java source path is correctly set to "src/main/java" in .vscode/settings.json:
   "java.project.sourcePaths": ["src/main/java"]
9. If you see import errors for third-party libraries (net.minecraft, net.minecraftforge, etc.),
   run: "./gradlew --refresh-dependencies build --no-daemon" to download all dependencies
10. Open any .java file - it should now show full IntelliSense and no "non-project file" errors

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not affect your code} and then start the processs again.

Should it still not work, 
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.
or the Forge Project Discord discord.gg/UvedJ9m

Forge source installation
=========================
MinecraftForge ships with this code and installs it as part of the forge
installation process, no further action is required on your part.

LexManos' Install Video
=======================
https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be

For more details update more often refer to the Forge Forums:
http://www.minecraftforge.net/forum/index.php/topic,14048.0.html
