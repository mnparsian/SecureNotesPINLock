plugins {

    id("com.android.application") version "8.11.1"
    id("org.jetbrains.kotlin.android") version "1.9.10"
    id("org.jetbrains.kotlin.kapt") version "1.9.10"
}

android {
    namespace = "bluebirdstudio.app.securenotespinlock"
    compileSdk = 35

    defaultConfig {
        applicationId = "bluebirdstudio.app.securenotespinlock"
        minSdk = 24
        targetSdk = 35
        versionCode = 3
        versionName = "1.0.1"
        resourceConfigurations += setOf("en", "it", "fa")

        // اضافه کردن زبان‌های مورد نیاز
    }

    // تنظیم Bundle Language Splitting
    bundle {
        language {
            enableSplit = true
            // در Kotlin DSL نیازی به include نیست، همه زبان‌ها از resConfigs لحاظ می‌شن
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
}




//dependencies {
//    implementation(platform("androidx.compose:compose-bom:2024.04.00"))
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.foundation:foundation")
//    implementation("androidx.compose.material3:material3")
//    implementation("androidx.compose.material:material-icons-extended")
//    implementation("androidx.activity:activity-compose")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
//    implementation("androidx.activity:activity-compose:1.8.2")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
//    implementation(libs.androidx.navigation.compose.jvmstubs)
//
//    // Test
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//
//
//    // Room components
//    implementation("androidx.room:room-runtime:2.6.1")
//    kapt("androidx.room:room-compiler:2.6.1")
//    implementation("androidx.room:room-ktx:2.6.1")
//
//// Kotlin Extensions and Coroutines support for Room
//    implementation("androidx.room:room-ktx:2.6.1")
//
//
//}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui:1.5.3")
    implementation("androidx.compose.material3:material3:1.2.0")

    // نسخه هماهنگ برای Compose + ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")

    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.androidx.navigation.runtime.android)
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("androidx.compose.material:material-icons-extended:1.5.3")
    testImplementation(kotlin("test"))


//    implementation("com.github.halilozercan:compose-richtext:0.17.0")
    implementation("com.github.halilozercan.compose-richtext:richtext-commonmark-android:0.17.0")






}

