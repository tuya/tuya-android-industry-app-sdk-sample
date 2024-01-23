## IActivator初始化

**参数说明**

| 参数名 | 类型 | 是否必填 | 说明 |
| --- | --- | --- | --- |
| `mode` | `ActivatorMode` | 是 | 配网模式 |

**代码示例**

```java
EZActivator ezActivator = (EZActivator) ActivatorService.activator(ActivatorMode.EZ);
```

## 获取配网token

**参数说明**

| 参数名 | 类型 | 是否必填 | 说明 |
| --- | --- | --- | --- |
| assetId | String | 是 | 指定的资产 ID |
| callback | IndustryDataCallBack | 是 | 回调函数 |


**代码示例**

```java
// 获取资产激活令牌
ActivatorService.activatorToken("assetId", new IndustryDataCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG, "onSuccess: " + s);
            }

            @Override
            public void onFailure(@NonNull String s, @NonNull String s1) {
                Log.d(TAG, "onFailure: " + s);
            }
        });
```

## 初始化配网参数

**参数说明**

| 参数名 | 类型 | 说明 |
|-------|------|------|
| ssid | String | Wi-Fi的SSID |
| password | String | Wi-Fi的密码 |
| token | String | 令牌 |


**代码示例**

```java
WiFiActivatorParams params = new WiFiActivatorParams.Builder()
                .setWifi("your_ssid", "your_password")
                .setToken("your_token")
                .build();
```

## 注册IActivatorListener监听配网结果

**IDevice对象说明**

| 参数名 | 类型 | 说明 |
| --- | --- | --- |
| getDeviceId() | String | 设备ID |
| getUUID() | String | 设备UUID |
| getName() | String | 设备名称 |
| getIcon() | String | 否 | 设备图标 |
| getProductId() | String | 产品ID |
| getCategory() | String | 设备分类 |
| getTimezoneId() | String | 设备时区ID |
| isCloudOnline | Boolean | 设备是否在云端在线 |
| isLocalOnline | Boolean | 设备是否在本地在线 |
| isOnline | Boolean | 设备是否在线 |
| getLatitude()| String | 设备所在纬度 |
| getLongitude() | String | 设备所在经度 |
| getDps() | Map<String, Any> | 设备数据点 |
| publishDps(dps: DpCommand, callback: IndustryCallBack) | \ | DP控制 |
| getSchemas() | Map<String, DpSchema> | 设备数据点模式 |
| addDeviceListener(listener: IDeviceListener) | \ | 设备监听 |
| removeDeviceListener(listener: IDeviceListener) | \ | 移除设备监听 |
| getWifiSignalStrength(callback: IndustryValueCallBack<String>) | \ | 获取WiFi信号强度 |
| getDevAttribute() | Long | 设备标志位 |
| newOtaManager() | IDeviceOtaManager | 设备OTA升级 |
| newBackupManager() | IDeviceWifiBackupManager | 设备备用网络，具体支不支持此功能，需要参考`getDevAttribute()`返回的值,bit12支持 |

**代码示例**

```
ezActivator.setListener(new IActivatorListener() {
            @Override
            public void onSuccess(@Nullable IDevice iDevice) {
                Log.d(TAG, "onSuccess: ");

            }

            @Override
            public void onError(@NonNull String s, @NonNull String s1) {
                Log.d(TAG, "onError: ");

            }
        });
```

## 开始配网方法调用

**代码示例**

```java
ezActivator.start();
```

## 停止方法

**代码示例**

```java
ezActivator.stop();
```