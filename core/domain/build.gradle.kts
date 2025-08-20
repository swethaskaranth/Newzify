plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(project(":core:common"))
    testImplementation(libs.junit)
    testImplementation(libs.mockk)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.coroutines.android)
    testImplementation(libs.kotlin.coroutines.test)

    testImplementation(libs.turbine)
    testImplementation(kotlin("test"))

    implementation(libs.javax.inject)
}
