# MobileSDK-Android
Dataspike Android Mobile SDK is a powerful and efficient development kit for conducting Anti-Money Laundering (AML), Know Your Customer (KYC), and Document Verification processes.

# Usage

## Integrate via JitPack
https://jitpack.io/#dataspike-io/MobileSDK-Android

1. In your project's ```build.gradle``` or ```settings.gradle``` file, add JitPack repository url to the list of repositories.
```
allprojects {
    repositories {
        // ... Other repositories ...

        maven { url 'https://jitpack.io' }
    }
}
```
2. In your app module's ```build.gradle``` file, add the dependency for the Dataspike Mobile SDK.
```
dependencies {
    implementation 'com.github.dataspike-io:MobileSDK-Android:1.0.0-rc6'
}
```
3. Initialize SDK in your project.
```
val dataspikeDependencies = DataspikeDependencies(
    isDebug = true/false,
    dsApiToken = your_api_token,
    shortId = verification_url_id
)

DataspikeManager.startDataspikeFlow(
    dataspikeDependencies,
    context
)
```

## Integrate via .aar

1.  In your app module's ```build.gradle``` file, add the following dependencies.
```
implementation 'androidx.core:core-ktx:1.12.0'
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.9.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.fragment:fragment-ktx:1.6.1'
implementation 'androidx.camera:camera-core:1.3.0-rc01'
implementation 'androidx.camera:camera-camera2:1.2.3'
implementation 'androidx.camera:camera-lifecycle:1.2.3'
implementation 'androidx.camera:camera-view:1.2.3'
implementation 'com.google.mlkit:face-detection:16.1.5'
implementation 'com.google.mlkit:object-detection:17.0.0'
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:okhttp:4.10.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.10.0'
```
2. Put [dataspike_mobile_sdk.aar](https://github.com/dataspike-io/MobileSDK-Android/releases/download/1.0.0-rc6/dataspike_mobile_sdk.aar) in ```libs``` folder in your project.
3. In your app module's ```build.gradle``` file, add the dependency for the Dataspike Mobile SDK.
```
implementation files(‘libs/dataspike_mobile_sdk.aar’)
```
4. Initialize SDK in your project as described above.
