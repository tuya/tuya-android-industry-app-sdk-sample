## Initialize IActivator

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| `mode` | `ActivatorMode` | Yes | The pairing mode. |

**Example**

```java
WiredActivator wiredActivator = (WiredActivator) ActivatorService.activator(ActivatorMode.Wired);
```

## Get a pairing token

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| assetId | String | Yes | The ID of the specified asset. |
| callback | IndustryDataCallBack | Yes | The callback. |


**Example**

```java
// Get asset activation token.
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

## Register `IDiscoveryListener` to listen for scanning result

**Parameter description**

**`IWiredDevice` data model**

| Parameter | Type | Required | Description |
|-----------|-------|---------|--------------------|
| getIP() | String | Yes | Get the IP address of the device. |
| getGWId() | String | Yes | Get the ID of the gateway. |
| getProductKey() | String | Yes | Get the product key for the device. |


```java
IDiscovery iDiscovery = ActivatorService.discovery(DiscoveryMode.WIRED);
iDiscovery.setListener(new IDiscoveryListener() {
            @Override
            public void didDiscover(@NonNull IDiscoveryDevice iDiscoveryDevice) {
                if (iDiscoveryDevice instanceof IWiredDevice){
                    ((IWiredDevice) iDiscoveryDevice).getIP();
                    ((IWiredDevice) iDiscoveryDevice).getGWId();
                    ((IWiredDevice) iDiscoveryDevice).getProductKey();
                }
            }
        });
```

## Initialize pairing parameters

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| gwDevice | IWiredDevice | Yes | Pass in the gateway object. |
| time | int | Yes | Set the timeout. |
| token | String | Yes | Set the token. |

**Example**

```java
WiredActivatorParams wiredActivatorParams = new WiredActivatorParams.Builder()
                .setGWDevice(gwDevice)
                .setTimeout(time)
                .setToken("token")
                .build();
wiredActivator.setParams(wiredActivatorParams);
```

## Register `IActivatorListener` to listen for pairing result

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| listener | IActivatorListener | Yes | Listen for the callback. |

**Example**

```java
wiredActivator.setListener(new IActivatorListener() {
            @Override
            public void onSuccess(@Nullable IDevice iDevice) {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError(@NonNull String s, @NonNull String s1) {
                Log.d(TAG, "onError: " + s);

            }
        });
```


## Start pairing

This method initiates device pairing.

**Example**

```java
wiredActivator.start();
```

## Stop pairing

**Example**

```java
wiredActivator.stop();
```
