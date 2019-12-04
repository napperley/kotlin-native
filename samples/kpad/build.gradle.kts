group = "org.example"
version = "0.1-SNAPSHOT"

plugins {
    kotlin("multiplatform") version "1.3.61"
}

kotlin {
    linuxX64("linux") {
        binaries {
            executable {
                entryPoint = "org.example.kpad.main"
            }
        }
    }
}