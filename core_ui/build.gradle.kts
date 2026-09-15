plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("com.google.devtools.ksp")
    id("kotlin-parcelize")

    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.core_ui"
    compileSdk = 34

    defaultConfig {
        minSdk = 31

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.material3)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)


//    //LazyColumn animation
//    implementation("androidx.compose.foundation:foundation:1.7.0-beta03")

    //Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    ksp("com.google.dagger:hilt-compiler:2.49")

    // Hilt navigation-compose (for hiltViewModel())
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //lottie animation
    implementation ("com.airbnb.android:lottie-compose:6.0.0")

    //database module
    implementation(project(":database"))

    //commonResources
    implementation(project(":commonResources"))

    //navigation module
    implementation(project(":navigation"))

    //wifi module
    implementation(project(":wifi"))

    //mqtt module
    implementation(project(":mqtt"))

    //intro showcase
    implementation ("com.canopas.intro-showcase-view:introshowcaseview:2.0.1")

    //preference datastore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    //reorder
    implementation("sh.calvin.reorderable:reorderable:2.3.0")
//    implementation("sh.calvin.reorderable:reorderable:3.1.0")

    //coil
//    implementation("io.coil-kt:coil-compose:2.7.0")
//    implementation("io.coil-kt:coil-gif:2.7.0")
}