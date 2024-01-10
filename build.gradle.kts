// Top-level build file where you can add configuration options common to all sub-projects/modules.

ext {
    extra["hilt_version"] = "1.1.0"
    extra["dagger_version"] = "2.48.1"
    extra["room_version"] = "2.6.1"
    extra["compose_navigation_version"] = "2.7.5"
    extra["compose_activity_version"] = "1.8.1"
    extra["compose_boom_version"] = "2023.06.00"
    extra["compose_animation_version"] = "1.5.4"
    extra["lifecycle_version"] = "2.6.2"
    extra["core_version"] = "1.12.0"
    extra["junit_version"] = "4.13.2"
    extra["material3_version"] = "1.1.2"
    extra["datastore_version"] = "1.0.0"

    extra["compile_sdk"] = 34
    extra["target_sdk"] = 33
    extra["min_sdk"] = 24

    extra["version_code"] = 1
    extra["version_name"] = "1.0"
}
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    // Ajout
    id("com.google.dagger.hilt.android") version "2.44" apply false
    kotlin("plugin.serialization") version "1.8.10"
}