plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.carelanka"
    compileSdk = 36 // සාමාන්‍යයෙන් 35 භාවිතා කිරීම ප්‍රමාණවත්

    defaultConfig {
        applicationId = "com.example.carelanka"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    // Version Catalog (libs.versions.toml) හරහා ලබාගන්නා dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.activity)
    implementation(libs.recyclerview)
    implementation(libs.cardview)

    // Firebase (BOM භාවිතා කර අලුත්ම අනුවාද ලබාගැනීම)
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // Realtime Database දෝෂය නිවැරදි කිරීමට මෙය අනිවාර්යයෙන්ම අවශ්‍ය වේ
    implementation("com.google.firebase:firebase-database")

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}