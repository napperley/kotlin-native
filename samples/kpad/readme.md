# KPad (kpad)

Basic text editor written in Kotlin (via Kotlin Native) which can be thought of as a cut down version of [Geany](https://www.geany.org/). This Kotlin Native program shows how to do the following:

- Using callbacks
- Structuring a desktop UI
- Handling the Gtk lifecycle
- Using managed layouts
- Dealing with stable references/callback data
- Window management
- Managing C pointers
- Handling file IO
- Using dialogs

## Building Program

In order to build KPad do the following:

1. Install the Gtk 3 development library (on Linux Mint, Ubuntu, and Debian install the **libgtk-3-dev** package)
2. Clone the Kotlin Native repository
3. Change working directory to *samples/kpad* in the cloned repository
4. Run the **linkDebugExecutableLinux** Gradle task, eg `./gradlew linkDebugExecutableLinux`

After completing the steps above you can now run the program (*kpad.kexe*) which is located in *build/bin/linux/debugExecutable*. If you want KPad without the debug symbols run the **linkReleaseExecutableLinux** Gradle task, eg `./gradlew linkReleaseExecutableLinux`. The release executable (kpad.kexe) when it is generated is located in *build/bin/linux/releaseExecutable*.
