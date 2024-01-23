## Initialize IActivator

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| `mode` | `ActivatorMode` | Yes | The pairing mode. |

**Example**

```java
QRActivator qrActivator = (QRActivator) ActivatorService.activator(ActivatorMode.QR);
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
| token | String | The token obtained. |

**Example**

```java
QRActivatorParams qrActivatorParams = new QRActivatorParams.Builder()
                .setToken("token")
                .setWifi("ssid","password")
                .setTimeout(time)
                .build();
qrActivator.setParams(qrActivatorParams);
```

## Register `IActivatorListener` to listen for pairing result

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| listener | IQRActivatorListener | Yes | The callback. |

**Example**

```java
qrActivator.setListener(new IQRActivatorListener() {
            @Override
            public void onQRCodeSuccess(@NonNull String s) {
                // Generate a QR code by yourself based on the return result. You need to integrate the ZXing library.
                Log.d(TAG, "onQRCodeSuccess: ");

            }

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

This method initiates device pairing.

**Example**

```java
qrActivator.start();
```

## Stop pairing

**Example**

```java
qrActivator.stop();
```
