plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.raksha"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.raksha"
        minSdk = 24
        targetSdk = 36
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
    // ========== CORE ANDROID LIBRARIES ==========
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // ========== GOOGLE PLAY SERVICES ==========
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // ========== UNIT TESTING ==========
    testImplementation(libs.junit)
    testImplementation("junit:junit:4.13.2")

    // ========== INSTRUMENTATION TESTING (AndroidTest) ==========
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
}