import dependencies.AppDependencies
import versions.SharedVersions

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = Config.NAME
    compileSdk = Config.TARGET_SDK

    defaultConfig {
        applicationId = Config.NAME
        minSdk = Config.MIN_SDK
        targetSdk = Config.TARGET_SDK
        versionCode = Config.VERSION
        versionName = Config.VERSION_NAME

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isShrinkResources = false
            enableUnitTestCoverage = false

            matchingFallbacks.addAll(arrayOf("qa", "debug"))
        }

        release {
            isMinifyEnabled =  true
            isShrinkResources = false

            matchingFallbacks.add("release")
        }

        register("qa") {
            isDebuggable = true
            enableUnitTestCoverage = true

            matchingFallbacks.add("qa")
            initWith(getByName("debug"))
        }
    }

    compileOptions {
        sourceCompatibility = Config.JAVA_TARGET
        targetCompatibility = Config.JAVA_TARGET
    }

    kotlinOptions {
        jvmTarget = Config.JAVA_VERSION
    }

    kapt {
        correctErrorTypes = true
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = SharedVersions.KOTLIN_COMPILER_VERSION
    }

    sourceSets {
        getByName("debug") { java.srcDirs("src/main/java") }
        getByName("release") { java.srcDirs("src/main/java") }
        getByName("qa") { java.srcDirs("src/main/java") }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":domain"))

    AppDependencies.getImplementation().map { implementation(it) }
    AppDependencies.debugImplementation().map { debugImplementation(it) }
    AppDependencies.getKapt().map { kapt(it) }
}