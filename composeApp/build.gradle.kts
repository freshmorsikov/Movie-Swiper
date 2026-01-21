import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.googleServices)
}

buildConfig {
    val themoviedbToken = project.findProperty("MOVIE_SWIPER_THEMOVIEDB_TOKEN") as String
    buildConfigField("THEMOVIEDB_TOKEN", themoviedbToken)

    val supabaseUrl = project.findProperty("MOVIE_SWIPER_SUPABASE_URL") as String
    buildConfigField("SUPABASE_URL", supabaseUrl)

    val supabaseApiKey = project.findProperty("MOVIE_SWIPER_SUPABASE_API_KEY") as String
    buildConfigField("SUPABASE_API_KEY", supabaseApiKey)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.okhttp)
            implementation(libs.sqldelight.android.driver)
            implementation(project.dependencies.platform(libs.firebase.bom))
        }
        iosMain.dependencies {
            implementation(libs.ktor.darwin)
            implementation(libs.sqldelight.native.driver)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.navigation.compose)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.bundles.ktor)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.bundles.sqldelight)
            implementation(libs.bundles.coil)
            implementation(libs.bundles.koin)
            implementation(libs.bundles.datastore)
            implementation(libs.firebase.analytics)

            implementation(project.dependencies.platform(libs.supabase.bom))
            implementation(libs.supabase.realtime)
            implementation(libs.supabase.postgrest)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.github.freshmorsikov.moviematcher"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.github.freshmorsikov.moviematcher"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 30
        versionName = "0.3.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        buildConfig = true
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("com.github.freshmorsikov.moviematcher")
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
