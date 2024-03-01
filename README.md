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
    implementation 'com.github.dataspike-io:MobileSDK-Android:1.0.0-rc12'
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
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.fragment:fragment-ktx:1.6.2'
implementation 'androidx.camera:camera-core:1.4.0-alpha04'
implementation 'androidx.camera:camera-camera2:1.3.1'
implementation 'androidx.camera:camera-lifecycle:1.3.1'
implementation 'androidx.camera:camera-view:1.3.1'
implementation 'com.google.mlkit:face-detection:16.1.6'
implementation 'com.google.mlkit:object-detection:17.0.1'
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:okhttp:4.10.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.10.0'
implementation 'com.github.bumptech.glide:glide:4.16.0'
```
2. Put [dataspike_mobile_sdk.aar](https://github.com/dataspike-io/MobileSDK-Android/releases/download/1.0.0-rc13/dataspike_mobile_sdk.aar) in ```libs``` folder in your project.
3. In your app module's ```build.gradle``` file, add the dependency for the Dataspike Mobile SDK.
```
implementation files(‘libs/dataspike_mobile_sdk.aar’)
```
4. Implement callback and initialize SDK in your project as described above.

## UI Customization
You can customize the appearance of most elements in the SDK, as well as separately define the color scheme for light and dark themes.
<br/><br/>
1. Make a call to [/update-verification-profile](https://docs.dataspike.io/api/#tag/Verification-profiles/operation/update-verification-profile) and pass the ```ui_settings``` in the request body.
```
"ui_settings": "{\"themes\":{\"light\":{\"palette\":{\"background_color\":\"#FFFFFF\",\"main_color\":\"#FF5387\",\"success_color\":\"#9900bf\",\"error_color\":\"#00bfb6\"},\"typography\":{\"header\":{\"font\":\"roboto_bold\",\"text_color\":\"#000000\",\"text_size\":30},\"body_one\":{\"font\":\"roboto_regular\",\"text_color\":\"#000000\",\"text_size\":16},\"body_two\":{\"font\":\"roboto_regular\",\"text_color\":\"#70000000\",\"text_size\":14}}},\"dark\":{\"palette\":{\"background_color\":\"#323331\",\"main_color\":\"#33bf00\",\"success_color\":\"#52C27F\",\"error_color\":\"#FF5387\"},\"typography\":{\"header\":{\"font\":\"roboto_bold\",\"text_color\":\"#FFFFFF\",\"text_size\":30},\"body_one\":{\"font\":\"roboto_regular\",\"text_color\":\"#FFFFFF\",\"text_size\":16},\"body_two\":{\"font\":\"roboto_regular\",\"text_color\":\"#70FFFFFF\",\"text_size\":14}}}},\"components\":{\"button\":{\"style\":{\"margin\":16,\"corner_radius\":100}}},\"messages\":{\"verification_successful\":\"You have successfully uploaded all required documents\",\"verification_expired\":\"Your verification link has expired\",\"verification_failed\":\"Something went wrong this time\"},\"links\":{\"onboarding\":{\"poi\":\"https://static.dataspike.io/images/sdk/verification/document.png\",\"liveness\":\"https://static.dataspike.io/images/sdk/verification/selfie.png\",\"poa\":\"https://static.dataspike.io/images/sdk/verification/poa.png\"},\"verification_result\":{\"verification_successful\":\"https://static.dataspike.io/images/sdk/verification/success-icon.png\",\"verification_expired\":\"https://static.dataspike.io/images/sdk/verification/time-expired.png\",\"verification_failed\":\"https://static.dataspike.io/images/sdk/verification/error-icon.png\"},\"intro\":{\"poi\":\"https://static.dataspike.io/images/sdk/verification/main-page.png\",\"poa\":\"https://static.dataspike.io/images/sdk/verification/poa-document.png\"},\"requirements\":{\"poi\":\"https://dash.dataspike.io/widget/requirements/docs/\",\"liveness\":\"https://dash.dataspike.io/widget/requirements/selfie/\",\"poa\":\"https://dash.dataspike.io/widget/requirements/poa/\"}},\"options\":{\"show_timer\":false,\"show_steps\":false,\"disable_dark_mode\":false}}"
```
2. The appearance of the SDK should reflect the changes after restarting.
<br/><br/>
> [!CAUTION]
> Please check, that the SDK looks as intended after applying any changes.

> [!NOTE]
> You can reset all changes by passing an empty string in the ```ui_settings```.

> [!NOTE]
> If you don't pass a key in the ```ui_settings```, it's value will be set to default in the SDK.

<br/><br/>
Possible font values:
- "mont_bold"
- "mont_semi_bold"
- "mont_regular"
- "roboto_bold"
- "roboto_semi_bold"
- "roboto_regular"
