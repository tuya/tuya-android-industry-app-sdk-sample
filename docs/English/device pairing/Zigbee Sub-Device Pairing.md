## Initialize IActivator

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| `mode` | `ActivatorMode` | Yes | The pairing mode. |

**Example**

```java
ZigbeeActivator zigbeeActivator = (ZigbeeActivator) ActivatorService.activator(ActivatorMode.Zigbee);
```

## Register `IActivatorListener` to listen for pairing result

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| listener | IActivatorListener | Yes | Listen for callback |

**Example**

```java
zigbeeActivator.setListener(new IActivatorListener() {
            @Override
            public void onSuccess(@Nullable IDevice iDevice) {
                Log.d(TAG, "onSuccess: ");
                if (null != iDevice){
                    // The ID of the gateway.
                    iDevice.getDeviceId();
                }
            }

            @Override
            public void onError(@NonNull String s, @NonNull String s1) {
                Log.d(TAG, "onError: " + s);
            }
        });
```

## Initialize pairing parameters

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| gwDeviceId | String | Yes | The ID of the gateway. |
| time | int | Yes | Set the timeout. |

**Example**

```java
ZigbeeActivatorParams zigbeeActivatorParams = new ZigbeeActivatorParams.Builder()
                .setGwDeviceId(gwDeviceId)
                .setTimeout(time)
                .build();
zigbeeActivator.setParams(zigbeeActivatorParams);
```

## Start pairing

This method initiates device pairing.

**Example**

```java
zigbeeActivator.start();
```

## Stop pairing

**Example**

```java
zigbeeActivator.stop();
```
