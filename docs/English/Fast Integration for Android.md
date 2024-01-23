This topic describes how to quickly integrate **Tuya Industry App SDK** for Android into your development environment, such as Android Studio. It also sheds light on the initialization method and how to enable the debugging mode with a few simple steps. This allows you to run the demo app and get started with your app development by using the Industry App SDK.

## Prerequisites

Before you start, consult with the project manager (PM) about account registration and requesting the key.

## Integrate with the SDK

- Before you start, make sure that you have performed the steps in **Prerequisites**.
- If you have not installed Android Studio, visit the Android Studio [official website](https://developer.android.com/studio) to download Android Studio.

### Step 1: Create an Android project

1. Create a project in Android Studio.

2. Copy your key file `xxx.jks` to the root directory of the project.

   :::important
   `xxx.jks` is the key file generated when creating a project with the `applicationId` provided by the PM.
   :::

### Step 2: Configure `build.gradle`

1. Add `dependencies` to the `build.gradle` file of the Android project.

	```groovy
	android {
		defaultConfig {
			ndk {
				abiFilters "armeabi-v7a", "arm64-v8a"
			}
			packagingOptions {
				pickFirst 'lib/*/libc++_shared.so' // This `.so` file exists in multiple `.aar` files. Select the first one.
			}
		}

		// Configure the sign key information for the project.
		signingConfigs {
			release {
				storeFile file("../xxx.jks")
				storePassword "xxxx"
				keyAlias "xxxx"
				keyPassword "xxxx"
				v1SigningEnabled true
				v2SigningEnabled true
			}

			debug {
				storeFile file("../xxx.jks")
				storePassword "xxxx"
				keyAlias "xxxx"
				keyPassword "xxxx"
				v1SigningEnabled true
				v2SigningEnabled true
			}
		}
	}

	dependencies {
		implementation 'com.alibaba:fastjson:1.2.32'
		// The latest Industry App SDK for Android
		implementation 'com.thingclips.smart:IndustryLinkSDK:2.0.0-beta.1'
    	implementation 'com.squareup.moshi:moshi:1.13.0'
	}
	```

2. Add the Tuya IoT Maven repository URL to the `build.gradle` file in the root directory.

	```groovy
	repositories {
		mavenCentral()
		google()
		// The URL to the Tuya IoT repository
		maven {
		// Contact the project manager to get the private URL.
            url ""
		}
	}
	```

### Step 3: Obfuscate the code

Configure obfuscation in `proguard-rules.pro`.

```groovy
#fastJson
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**

#mqtt
-keep class com.tuya.smart.mqttclient.mqttv3.** { *; }
-dontwarn com.tuya.smart.mqttclient.mqttv3.**

#OkHttp3
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

-keep class okio.** { *; }
-dontwarn okio.**

-keep class com.thingclips.**{*;}
-dontwarn com.thingclips.**
```

<a id="bmp&keySetting"></a>


### Step 4: Initialize the SDK

Initialize the SDK in `Application`. Make sure that all processes are initialized. 

Example:

```java
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialization
        new IndustryLinkSDK.Builder(this)
                .initialize("clientId","clientSecret")
                .setHost("cloud project's Host")
                .setDeviceSDKKey("SDKKey")
                .setDeviceSDKSecret("SDKSecret")
                .setDebugMode(true)
                .build();
    }
}

```

:::important
- Obtain the `clientId`, `clientSecret`, `cloud project's Host`, `SDKKey`, and `SDKSecret` from your PM.
- In debug mode, you can enable SDK logging to facilitate troubleshooting.
- We recommend that you disable logging in release mode.
:::

## Run the demo app

In the following example, a demo app is used to describe the process of app development with the Industry App SDK. Before the development of your app, we recommend that you run the demo app.

### Feature overview

The demo app provides basic features such as device pairing, registration, login, and asset management. Tuya Industry App SDK forms the backbone of the Tuya SaaS Development Framework. For more information, see the GitHub project [tuya-android-iot-app-sdk-sample](https://github.com/tuya/tuya-android-iot-app-sdk-sample).

The demo app supports the following features:

- User management: log in to and log out of the app
- Asset management: query and select assets
- Device pairing: Wi-Fi access point (AP) mode, Wi-Fi Easy Connect (EZ) mode, wired mode, Zigbee sub-device pairing, QR code mode, and QR code scanning to pair NB-IoT devices
- Device management: query and unbind devices

	<img src="https://images.tuyacn.com/rms-static/81555860-b98f-11ee-9eac-b120705c4c0c-1705973649638.jpg?tyName=2845d185e8646f7deb47e56765a334a5.jpg" width="250">

### Run the demo

1. According to the signature file of your project, configure `signingConfigs` in the `build.gradle` file of the project. The `storeFile` file indicates the location where the `xxx.jks` file is stored.

   ![image.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/16474114699aef5417291.png)

2. Change the package name. Replace the package name with the **one provided by your PM**, as indicated below.

   ![image.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/16474114456ebd43b9ace.png)

3. Copy the values of `clientId`, `clientSecret`, `cloud project's Host`, `SDKKey`, and `SDKSecret` provided by your PM and paste them into the corresponding fields in the application, as indicated below.

   ![image.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/16854138931da6aa8743c.png)

4. Run the demo.