<p align="center">
  <img src="https://admin.pngme.com/logo.png" alt="Pngme" width="100" height="100">
  <h3 align="center">Pngme Android (Java) SDK & Sample App</h3>
</p>

This documentation covers how to use the Pngme SDK with Java.

You can find similar documentation for [Expo](https://github.com/pngme/sample-android-app-react-native-expo), [Flutter-Kotlin](https://github.com/pngme/sample-android-app-flutter), [Flutter-Java](https://github.com/pngme/sample-android-app-flutter_java) and [React Native](https://github.com/pngme/sample-android-app-react-native).

## Setup

1. The SDK supports Android API version 16+
1. The SDK enables your app to:
   1. Register a mobile phone user with Pngme
   1. Periodically send data to Pngme to analyze financial events
1. Using the SDK requires an **SDK Token**
   - [**Sign up for a free Pngme Dashboard account**](https://admin.pngme.com) then access your SDK token from the [Keys page](https://admin.pngme.com/keys)
   - Use the `test` SDK token during development but replace with the `production` SDK token before deploying your app to the Google Play store
   - A custom consent dialog for requesting SMS permissions from the user. You can follow the design guide [here](https://drive.google.com/file/d/1SAc4Wt62mYUleDfSme3GMG9yyDT8omwP/view) to create a custom dialog.

<p align="center">
  <img src="https://raw.githubusercontent.com/pngme/sample-android-app-flutter/main/.docs/webconsole_keys.png" width=450 height=300/>
</p>

After integrating the SDK, financial data will be accessible in the [Pngme Dashboard](https://admin.pngme.com/users) and via the [Pngme REST APIs](https://developers.api.pngme.com/reference/).

## Integrating the SDK

### Step 1

Add the JitPack package manager to `/app/build.gradle`.

```groovy
    allprojects {
        repositories {
            maven { url 'https://jitpack.io' }
        }
    }
```

### Step 2

Add the following dependencies to `/app/build.gradle`.

```groovy
    dependencies {
        implementation 'com.github.pngme:android-sdk:v7.0.1'
    }
```

### Step 3

Call the `PngmeSdk.goWithCustomDialog()` method in your app after the user has given consent to access SMS data.

## PngmeSDK API


```kotlin
 fun goWithCustomDialog(
     activity: AppCompatActivity,
     clientKey: String, // pass the SDK token here
     firstName: String,
     lastName: String,
     email: String,
     phoneNumber: String,
     externalId: String,
     companyName: String,
     hasAcceptedTerms: Boolean, // default is false
     onComplete: Callback? = null
 )
```

The `goWithCustomDialog` method performs three tasks.

1. register a `user` in Pngme's system using an Android Onetime Worker
2. check for new SMS messages and send them to Pngme's system every 30 minutes using an Android Background Worker

| Field           | Description                                                                                                        |
| --------------- | ------------------------------------------------------------------------------------------------------------------ |
| activity        | a reference to the current Activity                                                                                |
| sdkToken        | the SDK Token from the [Pngme Dashboard Keys page](https://admin.pngme.com/keys)                                   |
| firstName       | the mobile phone user's first name                                                                                 |
| lastName        | the mobile phone user's last name                                                                                  |
| email           | the mobile phone user's email address                                                                              |
| phoneNumber     | the mobile phone user's phone number, example `"23411234567"`                                                      |
| externalId      | a unique identifier provided by your app (if none available, pass an empty string `""`)                            |
| companyName     | your company's name; this is used in the display header of the [Permission Dialog Flow](.docs/permission_flow.gif) |
| hasAcceptedTerms | Set the value to 'true' if the user has accepted the terms and conditions when invoking the 'goWithCustomDialog' method. Defaults to false                                                                      |
| onComplete      | a callback function that is called when the `goWithCustomDialog` method has completed                                              |


### `isPermissionGranted()`

```kotlin
fun isPermissionGranted(context: Context): Boolean
```

| Field   | Description             |
| ------- | ----------------------- |
| context | the current app Context |

This indicates if the user has accepted the SMS permissions request:

- Returns `true` if the user has accepted the SMS permission request.
- Returns `false` if the user has denied the SMS permission request.

### Sending test data

This can be tested in a sample app running in the local emulator,
assuming the emulated app is running with a valid SDK token.

Android Emulator can simulate incoming SMS messages, and we can use this to test the Pngme SDK locally.

The following text message is of a recognized format for the Stanbic bank sender: `Stanbic`.

```text
Acc:XXXXXX1111
CR:NGN4,000.00
Desc:HELLO WORLD! SAMPLE MESSAGE
Bal:NGN50,000.00
```

You can inject this fake SMS into the emulated phone by following these steps.

> Once the app gets the permissions form the user it will instantly start sending existing SMS messages to the Pngme system. This results in messages being seen much sooner than SMS received after the app was installed.
>
> The background worker processes new messages every 30 minutes, so new messages will take at least 30 minutes to appear in the webconsole.

![Inject Fake SMS](.docs/inject_fake_sms.png)

1. Open the `more` window in the emulator settings
2. Navigate to the `phone` section
3. Set the sender to the string `Stanbic` or one of the senders from our [supported institutions](https://developers.api.pngme.com/reference/supported-institutions)
4. Copy/Paste the above same message into the message box
5. Hit `Send Message`

After following the above steps to send a fake SMS, run the sample app.
The fake SMS will be sent to the Pngme system using the SDK token from your Pngme account.
If the sample app runs successfully, the financial data in the text message will be accessible
via the [Pngme REST APIs](https://developers.api.pngme.com/reference/getting-started-with-your-api) or in the [Pngme webconsole](https://admin.pngme.com).

## Next steps

See [Going Live with the SDK](https://developers.api.pngme.com/docs/going-live-with-the-sdk) to learn more about the whitelisting process with the Google Play store.
