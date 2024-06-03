Tuya Android Industry App SDK Sample
===
[中文版](README_zh.md)|[English](README.md)

This sample demonstrates the use of Tuya Android Industry App SDK to build an IoT App from scratch. It divides into several function groups to give developers a clear insight into the implementation for different features, includes the user login process, asset management for different users, device network configuration, and controls. For device network configuration, EZ mode and AP mode are implemented, which let developers pair devices over Wi-Fi, as well as control them via LAN and MQTT. For device control, it supplies a common panel for sending and receiving any kind types of data points.


## Self-developed Smart Industry App Service
Self-Developed Smart Industry App is one of Tuya’s IoT app development solutions. This solution provides the services that enable connections between the app and the cloud. It also supports a full range of services and capabilities that customers can use to independently develop mobile apps. The Smart Life App SDK used in this sample is included in the Self-developed Smart Industry App Service.

## Get Started

- [Preparation for Integration](https://developer.tuya.com/en/docs/app-development/smart-industry-sdk-preparation?id=Kdljngk28zthi)

> Note：Smart Industry App SDK has done the security checksum。You need to get SHA256 in[Tuya IoT platform](https://developer.tuya.com/en/docs/app-development/iot_app_sdk_core_sha1?id=Kao7c7b139vrh),then bind your SHA256,otherwise it will report an illegal client error. If you need a local dubug to run Sample, you need to configure your signature information in the app module under build.gradle, android closures at：
```groovy
signingConfigs {
        debug {
            storeFile file('../xxx.jks')
            storePassword 'xxx'
            keyAlias 'xxx'
            keyPassword 'xxx'
        }
    }
```

- [Integration](https://developer.tuya.com/en/docs/app-development/smart-industry-sdk-preparation?id=Kdljngk28zthi)

- [SDK Features](https://developer.tuya.com/en/docs/app-development/intro-tutorial-of-iot-app-sdk-core-for-android?id=Kdljfr19dbo6b)

## App Images

<img src="https://images.tuyacn.com/rms-static/81555860-b98f-11ee-9eac-b120705c4c0c-1705973649638.jpg?tyName=2845d185e8646f7deb47e56765a334a5.jpg" alt="main_page" width="30%" />

## Issue Feedback

You can provide feedback on your issue via **Github Issue** or [Technical Ticket](https://service.console.tuya.com).

## License

Tuya Android Smart Life App SDK Sample is available under the MIT license. Please see the [LICENSE](LICENSE) file for more info.
