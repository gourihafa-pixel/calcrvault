plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Optional signing configuration from keystore.properties (injected by CI; never commit).
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = java.util.Properties()
val hasKeystore = keystorePropertiesFile.exists().also {
    if (it) keystoreProperties.load(keystorePropertiesFile.inputStream())
} && listOf("storeFile", "storePassword", "keyAlias", "keyPassword").all { keystoreProperties.containsKey(it) }

android {
    namespace = "com.calcvault.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.calcvault.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        create("release") {
            if (hasKeystore) {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = if (hasKeystore) signingConfigs.getByName("release") else signingConfigs.getByName("debug")
        }
        getByName("debug") {
            isMinifyEnabled = false
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
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.documentfile:documentfile:1.0.1")
    implementation("net.objecthunter:exp4j:0.4.8")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
}
