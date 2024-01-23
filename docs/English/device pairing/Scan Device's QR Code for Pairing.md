## Initialize IActivator

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| `mode` | `ActivatorMode` | Yes | The pairing mode. |

**Example**

```
QRScanActivator qrScanActivator = (QRScanActivator) ActivatorService.activator(ActivatorMode.QRScan);
```

## Example of scanning QR code to get code

Example: depends on `implementation 'com.journeyapps:zxing-android-embedded:3.6.0'`

### Start scanning

**Example**

```java
IntentIntegrator integrator = new IntentIntegrator(activity);
integrator.initiateScan();
```

### Get scanning result

**Example**

```java
 @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String code = result.getContents();
        }
    }
```

## Initialize pairing parameters

**Parameter description**

| Parameter | Type | Required | Description |
|-------|------|----------|------|
| assetId | String | Yes | The asset ID of the device. |
| code | String | Yes | The code extracted from the QR code. |


```java
QRScanActivatorParams qrScanActivatorParams = new QRScanActivatorParams.Builder()
                .setAssetId("assetId")
                .setCode("code")
                .build();

qrScanActivator.setParams(qrScanActivatorParams);

```

## Register `IActivatorListener` to listen for pairing result

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| listener | IActivatorListener | Yes | The device information returned by the callback. |


**Example**

```java
qrScanActivator.setListener(new IActivatorListener() {
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
qrScanActivator.start();
```

## Stop pairing

**Example**

```java
qrScanActivator.stop();
```
