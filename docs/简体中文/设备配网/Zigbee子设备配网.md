## IActivator初始化

**参数说明**

| 参数名 | 类型 | 是否必填 | 说明 |
| --- | --- | --- | --- |
| `mode` | `ActivatorMode` | 是 | 配网模式 |

**代码示例**

```java
ZigbeeActivator zigbeeActivator = (ZigbeeActivator) ActivatorService.activator(ActivatorMode.Zigbee);
```

## 注册IActivatorListener监听配网结果

**参数说明**

| 参数名 | 类型 | 是否必填 | 说明 |
| --- | --- | --- | --- |
| listener | IActivatorListener | 是 | 回调监听 |

**代码示例**

```java
zigbeeActivator.setListener(new IActivatorListener() {
            @Override
            public void onSuccess(@Nullable IDevice iDevice) {
                Log.d(TAG, "onSuccess: ");
                if (null != iDevice){
                    //GW设备的ID 
                    iDevice.getDeviceId();
                }
            }

            @Override
            public void onError(@NonNull String s, @NonNull String s1) {
                Log.d(TAG, "onError: " + s);
            }
        });
```

## 初始化配网参数

**参数说明**

| 参数名 | 类型 | 是否必填 | 说明 |
| --- | --- | --- | --- |
| gwDeviceId | String | 是 | GW设备的ID |
| time | int | 是 | 设置超时时间 |

**代码示例**

```java
ZigbeeActivatorParams zigbeeActivatorParams = new ZigbeeActivatorParams.Builder()
                .setGwDeviceId(gwDeviceId)
                .setTimeout(time)
                .build();
zigbeeActivator.setParams(zigbeeActivatorParams);
```

## 开始配网

该方法用于开始设备配对操作。

**代码示例**

```java
zigbeeActivator.start();
```

## 停止配网

**代码示例**

```java
zigbeeActivator.stop();
```