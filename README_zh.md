Tuya Android Smart Industry App SDK Sample
===
[中文版](README_zh.md) | [English](README.md)

## 开始

[准备工作](https://developer.tuya.com/zh/docs/app-development/smart-industry-sdk-preparation?id=Kdljngk28zthi)

> 注意：智慧行业 App SDK 做了安全校验的升级。您需要在[IoT平台根据说明文档](https://developer.tuya.com/cn/docs/app-development/iot_app_sdk_core_sha1?id=Kao7c7b139vrh)来获取SHA256，然后在IoT平台绑定您的SHA256，否则会报错非法客户端。如果您需要本地dubug运行Sample，您需要在app模块的build.gradle下，android闭包中配置您的签名信息：
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

[集成](https://developer.tuya.com/zh/docs/app-development/smart-industry-sdk-preparation?id=Kdljngk28zthi)

[SDK功能](https://developer.tuya.com/zh/docs/app-development/intro-tutorial-of-iot-app-sdk-core-for-android?id=Kdljfr19dbo6b)

## App 图片

![app_sdk_sample1_zh](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/162443855370a008a89d3.png)

## 问题反馈

您可以通过**Github Issue** 或通过[**工单**](https://service.console.tuya.com)来进行反馈您所碰到的问题


## LICENSE

Tuya Android Smart Life App SDK Sample是在MIT许可下提供的。更多信息请参考[LICENSE](LICENSE)文件
