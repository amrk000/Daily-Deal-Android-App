plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.amrk000.dailydeal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.amrk000.dailydeal"
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //MVVM
    // ViewModel:
    implementation (libs.androidx.lifecycle.viewmodel)
    // Saved state module:
    implementation (libs.androidx.lifecycle.viewmodel.savedstate)
    // LiveData:
    implementation (libs.androidx.lifecycle.livedata)

    //Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx.v281)
    implementation(libs.androidx.navigation.ui.ktx.v281)

    //Room DB
    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx) //coroutine support

    //Kotlin coroutines
    implementation(libs.kotlinx.coroutines.android)

    //Glide
    implementation (libs.glide)
    kapt (libs.compiler)

    //Gson
    implementation (libs.gson)
}