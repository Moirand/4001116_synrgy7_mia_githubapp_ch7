plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.safeArgs)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.example.githubapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.githubapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".dev"
        }
        create("staging") {
            isMinifyEnabled = true
            isDebuggable = true
            applicationIdSuffix = ".stg"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    flavorDimensions += "mode"
    productFlavors {
        create("free") {
            dimension = "mode"
            applicationIdSuffix = ".free"
        }
        create("paid") {
            dimension = "mode"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "${JavaVersion.VERSION_21}"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    lint {
        baseline = file("lint.xml")
        abortOnError = false
        checkReleaseBuilds = false
    }
}

dependencies {
    implementation(project(":di"))
    implementation(project(":domain"))
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    testImplementation(libs.org.mockito.kotlin)
    testImplementation(libs.org.mockito.coroutine)
    testImplementation(libs.org.mockito.inline)
    testImplementation(libs.androidx.core.testing)
    implementation(libs.io.chochanaresh.filepicker)
    implementation(libs.androidx.work)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.com.airbnb.lottie)
    implementation(libs.com.facebook.shimmer)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.com.github.bumptech.glide.glide)
    implementation(libs.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    ksp(libs.com.github.bumptech.glide.glide.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}