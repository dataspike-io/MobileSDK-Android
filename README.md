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
    implementation 'com.github.dataspike-io:MobileSDK-Android:1.0.0-rc10'
}
```
3. In your app's activity or fragment implement VerificationCompletedCallback interface.
4. Initialize SDK in your project.
```
val dataspikeDependencies = DataspikeDependencies(
    isDebug = true/false,
    dsApiToken = your_api_token,
    shortId = verification_url_id,
)

fun startDataspikeFlow(
        dataspikeDependencies: DataspikeDependencies,
        callback: VerificationCompletedCallback,
        context: Context,
    )
```
5. Receive and handle verification result in your app after user completed verification process in SDK.
```
override fun onVerificationCompleted(verificationSucceeded: DataspikeVerificationStatus?) {
    ...
} 
```

## Integrate via .aar

1.  In your app module's ```build.gradle``` file, add the following dependencies.
```
implementation 'androidx.core:core-ktx:1.12.0'
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.10.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.fragment:fragment-ktx:1.6.1'
implementation 'androidx.camera:camera-core:1.4.0-alpha01'
implementation 'androidx.camera:camera-camera2:1.2.3'
implementation 'androidx.camera:camera-lifecycle:1.2.3'
implementation 'androidx.camera:camera-view:1.2.3'
implementation 'com.google.mlkit:face-detection:16.1.5'
implementation 'com.google.mlkit:object-detection:17.0.0'
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:okhttp:4.10.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.10.0'
implementation 'com.github.bumptech.glide:glide:4.16.0'
```
2. Put [dataspike_mobile_sdk.aar](https://github.com/dataspike-io/MobileSDK-Android/releases/download/1.0.0-rc10/mobile_sdk-release.aar) in ```libs``` folder in your project.
3. In your app module's ```build.gradle``` file, add the dependency for the Dataspike Mobile SDK.
```
implementation files(‘libs/dataspike_mobile_sdk.aar’)
```
4. Implement callback and initialize SDK in your project as described above.
