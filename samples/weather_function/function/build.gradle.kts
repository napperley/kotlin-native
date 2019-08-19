group = "org.example"
version = "0.1-SNAPSHOT"

plugins {
    kotlin("multiplatform") version "1.3.41"
}

repositories {
    mavenCentral()
}

kotlin {
    linuxX64("linux") {
        binaries {
            executable("weather") {
                entryPoint = "org.example.weather_func.main"
            }
        }
        compilations.getByName("main") {
            @Suppress("UNUSED_VARIABLE") val curl by cinterops.creating {
                includeDirs("-L/usr/lib/x86_64-linux-gnu/curl")
                defFile(project.file("curl.def"))
            }
            @Suppress("UNUSED_VARIABLE") val cjson by cinterops.creating {
                includeDirs("-I/lib/cjson-1.7.12/include/cjson")
                defFile(project.file("cjson.def"))
            }
        }
    }
    sourceSets {
        linuxX64("linux").compilations["main"].defaultSourceSet {}
    }
}
