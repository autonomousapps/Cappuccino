package com.autonomousapps.cappuccino.animations

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Exec
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CappuccinoAnimationsPlugin implements Plugin<Project> {

    private final Logger logger = LoggerFactory.getLogger('cappuccino-logger')

    private final def defaultExclusion = 'release'

    private Project project

    def getAdbExe() {
        project.android.adbExe
    }

    // This code block runs after the build script has been 'evaluated' (i.e., after the Configuration phase)
    // It first gets the list of connected devices by executing a process that runs `adb devices` and parses the result.
    // Then, it iterates through all the "application variants", which are the combination of build type and flavor.
    // It then dynamically generates a task for each combination of variant and connected device. If there are two variants
    // (flavor1Debug and flavor2Debug) and two connected devices (say, with IDs 1000 and 2000), then it will generate four
    // tasks with names grantAnimationPermissionFlavor1DebugWithId1000, .... It then sets those dynamically-created tasks as
    // dependencies for assemble*AndroidTest (e.g., assembleFlavor1DebugAndroidTest).
    @SuppressWarnings("GroovyAssignabilityCheck")
    @Override
    void apply(Project project) {
        this.project = project

        project.extensions.create("cappuccino", CappuccinoAnimationsExtension)

        project.afterEvaluate {
            addDefaultExclusionsIfNecessary()

            project.android.applicationVariants.each { variant ->
                if (variantNotExcluded(variant.name)) {
                    devices().each { device ->
                        def assembleTask = project.tasks.findByName("assemble${variant.name.capitalize()}AndroidTest")
                        if (assembleTask != null) {
                            Task animTask = createGrantAnimationPermissionTask(variant.name, variant.applicationId, device)
                            assembleTask.dependsOn animTask
                        } else {
                            logger.warn("task assemble${variant.name.capitalize()}AndroidTest does not exist! Cannot create GrantAnimationPermission task.")
                        }
                    }
                }
            }
        }
    }

    private void addDefaultExclusionsIfNecessary() {
        if (!project.cappuccino.excludedConfigurations.contains(defaultExclusion)) {
            project.cappuccino.excludedConfigurations.add(defaultExclusion)
        }
    }

    /**
     * Returns true is this is a variant for which we wish to create a grantAnimationPermission task.
     */
    private boolean variantNotExcluded(String variantName) {
        // use #find {} instead?
        for (String config : project.cappuccino.excludedConfigurations) {
            if (variantName.toLowerCase().contains(config.toLowerCase())) {
                return false
            }
        }
        return true
    }

    /**
     * Dynamically creates a task to grant the permission SET_ANIMATION_SCALE via adb. The task name
     * and its dependency is based on variant name, applicationId (package), and device id.
     * Sets the dependency of the new task to install* (e.g., installUsDebug).
     *
     * @param variantName variant name
     * @param applicationId application ID (package name)
     * @param deviceId device ID
     */
    Task createGrantAnimationPermissionTask(String variantName, String applicationId, String deviceId) {
        project.tasks.create("grantAnimationPermission${variantName.capitalize()}WithId$deviceId", Exec) {
            description = 'Grants the SET_ANIMATION_SCALE permission to the app via `adb`'
            group = 'Verification'

            dependsOn "install${variantName.capitalize()}"

            //noinspection GrUnresolvedAccess
            commandLine "${getAdbExe()} -s $deviceId shell pm grant $applicationId android.permission.SET_ANIMATION_SCALE".split(' ')
        }
    }

    /**
     * Executes {@code adb devices} and returns a list of connected devices.
     *
     * @return a list of connected devices.
     */
    def devices() {
        Process p = "${getAdbExe()} devices".execute()
        p.waitFor() // TODO tsr: check exit value and deal with InterruptedException?

        boolean doParse = false
        def devices = []
        p.in.eachLine { line ->                                   // #eachLine() handles resource opening and closing automatically
            if (doParse) {                                        // We're going to wait till we've seen the line 'List of devices attached'
                String[] deviceWithStatus = line.split("\\s+") // get device ID and status
                String device = deviceWithStatus[0]
                String status = deviceWithStatus.length > 1 ? deviceWithStatus[1] : "offline"

                // ignore empty IDs (such as on the last, blank line)
                if (!device.isEmpty() && status.equalsIgnoreCase("device")) {
                    device = device.replaceAll(":", ".") // tasks cannot contain the ':' character in Gradle 5.0
                    devices.add(device)
                }
            }
            if (!doParse && line.contains('List')) { // stop checking once we have set doParse to `true`
                doParse = true
            }
        }

        return devices
    }
}