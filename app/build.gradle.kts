plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // FIrebase Plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.genni"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.genni"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.animation.core.lint)

    implementation("androidx.compose.material:material:1.4.0")
    implementation ("androidx.compose.material:material-icons-core:1.4.0")
    implementation ("androidx.compose.material:material-icons-extended:1.4.0")

    implementation("io.coil-kt:coil-compose:2.4.0")

    // FireBase Dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.12.0")) // FireBase BOM
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.firebase.firestore.ktx) // FireBase Analytics
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation(libs.firebase.auth.ktx) // FireBase Storage Dependency


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}