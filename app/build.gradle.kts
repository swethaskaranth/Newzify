import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.detekt)
    alias(libs.plugins.room)
}

// Load local.properties
val localProperties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}


val apiKey: String = localProperties.getProperty("API_KEY") ?: ""
val baseUrl: String = localProperties.getProperty("BASE_URL") ?: ""

android {
    namespace = "com.kaizencoder.newzify"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kaizencoder.newzify"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    room{
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.retrofit)
    implementation(libs.moshi.converter)
    implementation(libs.moshi.kotlin)
    implementation(libs.http.logging.interceptor)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.coroutines.android)
    testImplementation(libs.kotlin.coroutines.test)

    implementation(libs.navigation.compose)

    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    implementation(libs.coil)
    implementation(libs.coil.network)
    implementation(libs.gilde)

    testImplementation(libs.konsist)

    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    testImplementation(libs.room.testing)

    testImplementation(libs.mockk)

    implementation(libs.timber)

}

detekt {
    // Path to your generated config
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))

    // Keep default rules and just override in your config
    buildUponDefaultConfig = true

    parallel = true
}
