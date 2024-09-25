import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    linuxX64()
    linuxArm64()

    targets.withType<KotlinNativeTarget>().configureEach {
        compilations.getByName(KotlinCompilation.MAIN_COMPILATION_NAME) {
            cinterops {
                create("myinterop")
            }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":lib"))
            }
        }
    }
}

abstract class TaskInstantiationTrackingBuildService : BuildService<BuildServiceParameters.None>, AutoCloseable {
    var startedExecution = false
    val tasksExecutedAtExecutionTime = mutableListOf<String>()

    override fun close() {
        require(tasksExecutedAtExecutionTime.isEmpty()) {
            "The following tasks were instantiated at execution time: $tasksExecutedAtExecutionTime"
        }
    }
}

val trackingService = gradle.sharedServices.registerIfAbsent(
    "trackingService",
    TaskInstantiationTrackingBuildService::class.java
) {}

allprojects {
    tasks.configureEach {
        usesService(trackingService)
        if (trackingService.get().startedExecution) {
            trackingService.get().tasksExecutedAtExecutionTime.add(path)
        }
        doFirst {
            trackingService.get().startedExecution = true
        }
    }
}