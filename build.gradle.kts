plugins {
    alias(libs.plugins.compose.compiler) apply false
    id("com.android.application") version "8.6.1" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    id("com.google.devtools.ksp") version "2.0.20-1.0.24" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
}
