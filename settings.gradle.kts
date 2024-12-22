pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // JitPack Maven Repository
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "Su ve İlaç Takipçisi"
include(":app")
