plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  id("org.jetbrains.kotlin.plugin.serialization") version "2.4.21"
}

android {
  namespace = "edu.ws2027.mc01.am"
  compileSdk {
    version = release(37)
  }

  defaultConfig {
    applicationId = "edu.ws2027.mc01.am"
    minSdk = 24
    targetSdk = 37
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      optimization {
        enable = true
        packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
      }
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)



  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.navigation.compose)
  testImplementation(libs.junit)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)


  implementation("androidx.compose.material:material-icons-extended-android:1.7.8")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")

  implementation("io.coil-kt.coil3:coil-svg:3.0.4")

  implementation("com.google.android.gms:play-services-mlkit-barcode-scanning:18.3.1")
  implementation("com.google.mlkit:barcode-scanning:17.3.0")
  implementation(libs.androidx.camera.view)
  implementation("androidx.camera:camera-camera2:1.6.2")
  implementation(libs.androidx.camera.lifecycle)
}