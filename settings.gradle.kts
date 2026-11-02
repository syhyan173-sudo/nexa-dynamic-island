pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NexaIsland"

// Modular Architecture
include(":app")
include(":core:common")
include(":core:design")
include(":core:datastore")
include(":core:permissions")
include(":core:overlay")
include(":domain")
include(":data")
include(":feature:dashboard")
include(":feature:island")
