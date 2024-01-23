## Initialize IActivator

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| `mode` | `ActivatorMode` | Yes | The pairing mode. |

**Example**

```java
EZActivator ezActivator = (EZActivator) ActivatorService.activator(ActivatorMode.EZ);
```

## Get a pairing token

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| assetId | String | Yes | The ID of the specified asset. |
| callback | IndustryDataCallBack | Yes | The callback. |


**Example**

```java
// Get the asset activation token.
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

## Initialize pairing parameters

**Parameter description**

| Parameter | Type | Description |
|-------|------|------|
| ssid | String | The SSID of the Wi-Fi network. |
| password | String | The password of the Wi-Fi network. |
| token | String | The token. |


**Example**

```java
WiFiActivatorParams params = new WiFiActivatorParams.Builder()
                .setWifi("your_ssid", "your_password")
                .setToken("your_token")
                .build();
```

## Register `IActivatorListener` to listen for pairing result

**`IDevice` object**

| Parameter | Type | Description |
| --- | --- | --- |
| getDeviceId() | String | The device ID. |
| getUUID() | String | The universally unique identifier (UUID) of the device. |
| getName() | String | The name of the device. |
| getIcon() | String | The device icon. |
| getProductId() | String | The product ID. |
| getCategory() | String | The device category. |
| getTimezoneId() | String | The ID of the device's time zone. |
| isCloudOnline | Boolean | Indicates whether the device is online over the internet. |
| isLocalOnline | Boolean | Indicates whether the device is online over the local network. |
| isOnline | Boolean | Indicates whether the device is online. |
| getLatitude() | String | The latitude of the device. |
| getLongitude() | String | The longitude of the device. |
| getDps() | Map<String, Any> | The data point (DP). |
| publishDps(dps: DpCommand, callback: IndustryCallBack) | \ | DP control. |
| getSchemas() | Map<String, DpSchema> | The DP schema. |
| addDeviceListener(listener: IDeviceListener) | \ | The device listener. |
| removeDeviceListener(listener: IDeviceListener) | \ | Remove a device listener. |
| getWifiSignalStrength(callback: IndustryValueCallBack<String>) | \ | Get the Wi-Fi signal strength. |
| getDevAttribute() | Long | The device flag. |
| newOtaManager() | IDeviceOtaManager | Device OTA update. |
| newBackupManager() | IDeviceWifiBackupManager | The device's alternative network. The support for this feature depends on the value returned by `getDevAttribute()`. Bit12 indicates the support. |

**Example**

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

## Start pairing

**Example**

```java
ezActivator.start();
```

## Stop pairing

**Example**

```java
ezActivator.stop();
```
