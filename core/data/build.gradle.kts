plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
android{

    namespace = "com.kaizencoder.newzify.core.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}
dependencies{
    implementation(project(":core:common"))
    implementation(project(":core:domain"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))

    implementation(libs.javax.inject)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.coroutines.android)
    testImplementation(libs.kotlin.coroutines.test)

    implementation(libs.room.runtime)

    implementation(libs.retrofit)
    implementation(libs.moshi.converter)
}
