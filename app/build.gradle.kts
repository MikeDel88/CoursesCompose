plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Ajout
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "1.8.10"
}

android {
    namespace = "fr.course.compose"
    compileSdk = rootProject.extra["compile_sdk"] as Int

    defaultConfig {
        applicationId = "fr.course.compose"
        minSdk = rootProject.extra["min_sdk"] as Int
        targetSdk = rootProject.extra["target_sdk"] as Int
        versionCode = rootProject.extra["version_code"] as Int
        versionName = rootProject.extra["version_name"] as String

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // CORE
    implementation("androidx.core:core-ktx:${rootProject.extra["core_version"]}")

    // LIFECYCLE
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra["lifecycle_version"]}")

    // COMPOSE UI
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.activity:activity-compose:${rootProject.extra["compose_activity_version"]}")
    implementation(platform("androidx.compose:compose-bom:${rootProject.extra["compose_boom_version"]}"))
    androidTestImplementation(platform("androidx.compose:compose-bom:${rootProject.extra["compose_boom_version"]}"))
    implementation("androidx.compose.animation:animation:${rootProject.extra["compose_animation_version"]}")

    // MATERIAL3
    implementation("androidx.compose.material3:material3:${rootProject.extra["material3_version"]}")
    implementation("androidx.compose.material3:material3-window-size-class:${rootProject.extra["material3_version"]}")

    // SERIALISATION
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // NAVIGATION
    implementation ("androidx.navigation:navigation-compose:${rootProject.extra["compose_navigation_version"]}")

    // HILT AND DAGGER
    implementation("com.google.dagger:hilt-android:${rootProject.extra["dagger_version"]}")
    kapt("com.google.dagger:hilt-android-compiler:${rootProject.extra["dagger_version"]}")
    androidTestImplementation("com.google.dagger:hilt-android-testing:${rootProject.extra["dagger_version"]}")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${rootProject.extra["dagger_version"]}")
    implementation("androidx.hilt:hilt-navigation-compose:${rootProject.extra["hilt_version"]}")
    implementation("androidx.hilt:hilt-navigation-fragment:${rootProject.extra["hilt_version"]}")

    // ROOM
    //noinspection GradleDependency
    implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
    annotationProcessor("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
    kapt("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
    implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")

    // DATASTORE
    implementation("androidx.datastore:datastore-preferences:${rootProject.extra["datastore_version"]}")

    // TEST CORE
    implementation("androidx.test:core-ktx:1.5.0")

    // TEST ESPRESSO
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // TEST JUNIT
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    testImplementation("junit:junit:${rootProject.extra["junit_version"]}")

    // TEST COMPOSE
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
}
