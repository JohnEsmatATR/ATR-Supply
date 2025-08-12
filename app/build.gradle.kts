plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.akhnaton.atrapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.akhnaton.atrapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.5"

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
        dataBinding = true
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:deprecation")
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.libraries.places:places:3.3.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.activity:activity:1.10.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.activity:activity-ktx:1.10.1")
    implementation("androidx.fragment:fragment-ktx:1.6.0")

    //Sdp & Ssp
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.0.6")

    // Google maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")


    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")

    //OkHttp
    val OkHttp_version = "4.9.3"
    implementation("com.squareup.okhttp3:okhttp:$OkHttp_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$OkHttp_version")

    //Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    //ViewModel and livedata
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Lottie
    implementation("com.airbnb.android:lottie:6.2.0")

    // Coil image loader
    implementation("io.coil-kt:coil:2.5.0")

    // otpView
    implementation("com.github.aabhasr1:OtpView:v1.1.2-ktx") // kotlin
    implementation("com.github.aabhasr1:OtpView:v1.1.2")

//    // zoom imageView
//    implementation("com.github.sheetalkumar105:ZoomImageView-android:1.02")

    implementation("com.github.ome450901:SimpleRatingBar:1.5.1")

    //Pick Image From Camera And Gallery
    implementation("com.github.dhaval2404:imagepicker:2.1")

    //recyclerview
    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    implementation ("com.github.bumptech.glide:glide:4.16.0")

}