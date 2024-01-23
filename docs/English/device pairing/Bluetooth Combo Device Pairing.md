## Initialize IActivator

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| `mode` | `ActivatorMode` | Yes | The pairing mode. |

**Example**

```java
BLEWIFIActivator blewifiActivator = (BLEWIFIActivator) ActivatorService.activator(ActivatorMode.BLE_WIFI);
```

## Initialize device scanning `IDiscovery`

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| `mode` | `DiscoveryMode` | Yes | The scanning mode. |

**Example**

```java
IDiscovery iDiscovery = ActivatorService.discovery(DiscoveryMode.BLE_WIFI);
```

## Device scanning parameters

**Example**

```java
DiscoveryParams.Builder builder = new DiscoveryParams.Builder();
builder.setTimeout(600_000);
DiscoveryParams discoveryParams = new DiscoveryParams(builder);
iDiscovery.setParams(discoveryParams);
```

## Register `IDiscoveryListener` to listen for scanning result

**Parameter description**

**`IBluetoothDevice` data model**

| Parameter | Type | Description |
|-------|------|------|
| getId | Function | Get the ID of the Bluetooth device. |
| getName | Function | Get the name of the Bluetooth device. |
| getData | Function | Get the device data. |
| getConfigType | Function | Get the configuration type. |
| getAddress | Function | Get the Bluetooth address of the device. |
| getDeviceType | Function | Get the type of the device. |
| getUUID | Function | Get the UUID of the device. |
| getMAC | Function | Get the MAC address of the device. |
| getProductId | Function | Get the product ID for the device. |
| isBind | Function | Determine if the device has been bound. |


**Example**

```java
 IDiscovery iDiscovery = ActivatorService.discovery(DiscoveryMode.BLE_WIFI);
        iDiscovery.setListener(new IDiscoveryListener() {
            @Override
            public void didDiscover(@NonNull IDiscoveryDevice iDiscoveryDevice) {
                if (iDiscoveryDevice instanceof IBluetoothDevice){
                    ((IBluetoothDevice) iDiscoveryDevice).getAddress();
                    ((IBluetoothDevice) iDiscoveryDevice).getId();
                    ((IBluetoothDevice) iDiscoveryDevice).getDeviceType();
                }

            }
        });
```

## Device scanning

```java
// Start scanning
iDiscovery.startDiscovery();
// Stop scanning
iDiscovery.stopDiscovery();
```

## Initialize pairing parameters

Get the parameters from the result of the scanning listener.

**Parameter description**

| Parameter | Type | Required | Description |
|-------|------|----------|------|
| address | String | Yes | The address of the Bluetooth device. |
| assetId | String | Yes | The asset ID of the device. |
| token | String | Yes | The token of the device. |
| pwd | String | Yes | The password of the Wi-Fi network. |
| uuid | String | Yes | The UUID of the device. |
| ssid | String | Yes | The SSID of the Wi-Fi network. |
| mac | String | No | The MAC address of the device. |
| time | long | No | The activation timeout, in milliseconds. |


```java
BLEWIFIActivatorParams params = new BLEWIFIActivatorParams.Builder()
                .setAddress("address")
                .setAssetId("assetId")
                .setToken("token")
                .setPwd("pwd")
                .setUuid("uuid")
                .setSsid("ssid")
                .setMac("mac")
                .setTimeout(time)
                .build();
blewifiActivator.setParams(params);
```


## Register `IActivatorListener` to listen for pairing result

**Example**

```java
blewifiActivator.setListener(new IActivatorListener() {
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
blewifiActivator.start();
```

## Stop pairing

**Example**

```java
blewifiActivator.stop();
```

## Destroy pairing instance

**Example**

```java
blewifiActivator.destroy();
```
