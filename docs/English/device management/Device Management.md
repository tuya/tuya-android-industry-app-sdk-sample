## MQTT service

The MQTT service protocol defines the basic operations of the MQTT service.

**Methods**

- `connect()`: Connect to the MQTT service.
- `disconnect()`: Disconnect from the MQTT service.
- `subscribe(topic: String, callback: IMQTTSubscribeCallback)`: Subscribe to a specific topic.
- `unsubscribe(topic: String, callback: IMQTTSubscribeCallback)`: Unsubscribe from a specific topic.

**Description**

### `connect()`

Connect to the MQTT service. This method has no parameters.

### `disconnect()`

Disconnect from the MQTT service. This method has no parameters.

### Subscribe to a topic

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| topic | String | Yes | The topic to subscribe to. |
| callback | IMQTTSubscribeCallback | Yes | The subscription callback. |

**Example**

```java
MQTTService.subscribe("topic", new IMQTTSubscribeCallback() {
            @Override
            public void onError(String s, String s1) {
                Log.d(TAG, "onError: " + s);
                
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: ");

            }
        });
```

## Bluetooth tool

### Connect to an offline device

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| beans | List<BleConnectBean> | Yes | The list of `BleConnectBean` objects. |
| bleConnectBean | BleConnectBean | Yes | Create a `BleConnectBean` object. |
| devId | String | Yes | The device ID. |
| directConnect | boolean | Yes | Indicates whether a direct connection is used. |
| level | int | Yes | The level. |
| scanTimeout | int | Yes | The scan timeout. |
| autoConnect | boolean | Yes | Indicates whether an automatic connection is used. |
| extInfo | ExtInfo | No | The additional information. |

**Example**

```java
List<BleConnectBean> beans = new ArrayList<>();
BleConnectBean bleConnectBean = new BleConnectBean(
        "your_dev_id", // Set devId
        false, // Set directConnect
        0, // Set level
        30000, // Set scanTimeout
        false, // Set autoConnect
        null // Set extInfo (optional, passing in null indicates no extInfo)
);
beans.add(bleConnectBean);
BleToolService.connectBleDevices(beans);
```
### Disconnect a device

**Example**

```java
List<BleConnectBean> beans = new ArrayList<>();
BleConnectBean bleConnectBean = new BleConnectBean(
        "your_dev_id", // Set devId
        false, // Set directConnect
        0, // Set level
        30000, // Set scanTimeout
        false, // Set autoConnect
        null // Set extInfo (optional, passing in null indicates no extInfo)
);
beans.add(bleConnectBean);
BleToolService.disconnectBleDevices(beans);
```

### Unsubscribe from a topic

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| topic | String | Yes | The topic to unsubscribe from. |
| callback | IMQTTSubscribeCallback | Yes | The subscription callback. |

**Example**

```java
MQTTService.unsubscribe("topic", new IMQTTSubscribeCallback() {
            @Override
            public void onError(String s, String s1) {
                Log.d(TAG, "onError: " + s);
                
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: ");

            }
        });
```

## Get device details

Load the device object by device ID.

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| deviceId | String | Yes | The device ID. |

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

```java
DeviceService.load("deviceId", new IndustryValueCallBack<IDevice>() {
            @Override
            public void onSuccess(IDevice iDevice) {
                Log.d(TAG, "onSuccess: ");
                
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: " + s);

            }
        });
```


## Rename a device

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| deviceId | String | Yes | The ID of the target device. |
| newName | String | Yes | The new name of the device. |
| callback | IndustryCallBack | Yes | The callback for renaming a device. |

**Example**

```java
DeviceService.rename("deviceId", "newName", new IndustryCallBack() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: " + s);

            }
        });
```

## Remove a device

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| deviceId | String | Yes | The ID of the target device. |
| callback | IndustryCallBack | Yes | The callback for removing a device. |

**Example**

```java
DeviceService.remove("deviceId", new IndustryCallBack() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: " + s);

            }
        });
```

## Factory reset

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| deviceId | String | Yes | The device ID. |
| callback | IndustryCallBack | Yes | The callback. |

**Example**

```java
DeviceService.resetFactory("deviceId", new IndustryCallBack() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: " + s);

            }
        });
```

## Listen for devices

Listen for device status.

### Register a device listener

`IDevice` listens for device information, such as:

- The DP data.
- The name of the device.
- The online status of the device.

**Parameter description**

| Parameters | Description |
| ---- | --- |
| listener | The listener for device status. |

**Example**

```java
iDevice.addDeviceListener(new IDeviceListener() {
                    @Override
                    public void onDpUpdate(String s, String s1) {
                        Log.d(TAG, "onDpUpdate: ");
                    }

                    @Override
                    public void onRemoved(String s) {
                        Log.d(TAG, "onRemoved: ");
                    }

                    @Override
                    public void onStatusChanged(String s, boolean b) {
                        Log.d(TAG, "onStatusChanged: ");
                    }

                    @Override
                    public void onNetworkStatusChanged(String s, boolean b) {
                        Log.d(TAG, "onNetworkStatusChanged: ");
                    }

                    @Override
                    public void onDevInfoUpdate(String s) {

                    }
                });
```

### Remove a device listener

**Parameter description**

| Parameters | Description |
| ---- | --- |
| listener | Remove a listener for device status. |

**Example**

```java
iDevice.removeDeviceListener(new IDeviceListener() {
                    @Override
                    public void onDpUpdate(String s, String s1) {
                        Log.d(TAG, "onDpUpdate: ");
                    }

                    @Override
                    public void onRemoved(String s) {
                        Log.d(TAG, "onRemoved: ");
                    }

                    @Override
                    public void onStatusChanged(String s, boolean b) {
                        Log.d(TAG, "onStatusChanged: ");
                    }

                    @Override
                    public void onNetworkStatusChanged(String s, boolean b) {
                        Log.d(TAG, "onNetworkStatusChanged: ");
                    }

                    @Override
                    public void onDevInfoUpdate(String s) {

                    }
                });
```

## OTA update

`IDeviceOtaManager` provides methods used to update the device firmware via OTA. You can get the update information, initiate or cancel an update, and register a listener for an update.

### Get the update information

```java
iDevice.newOtaManager().fetchFirmwareUpgradeInfo(new IndustryDataCallBack<List<FirmwareUpgradeInfo>>() {
                    @Override
                    public void onSuccess(List<FirmwareUpgradeInfo> firmwareUpgradeInfos) {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
```

**`FirmwareUpgradeInfo` object**

| Parameter | Type | Description |
| ------------------ | -------- | -------------------------------------------------- |
| desc | String | The description of the update. |
| upgradeStatus | Int | The update status.<ul><li>`0`: No update available</li><li>`1`: An update available</li><li>`2`: Updating</li><li>`5`: Wait for the device to wake up</li></ul> |
| version | String | The new version of the firmware. |
| currentVersion | String | The current version of the firmware. |
| timeout | Int | The timeout period. |
| upgradeType | Int | The type of updates.<ul><li>`0`: Update notification</li><li>`2`: Force update</li><li>`3`: Check for update</li></ul> |
| type | Int | The type of firmware update channel.<ul><li>Generic firmware. <li>`0`: Main module, Wi-Fi module, and Bluetooth module.</li><li>`1`: Bluetooth module</li><li>`2`: GPRS module</li><li>`3`: Zigbee module</li><li>`5`: 433 MHz module</li><li>`6`: NB-IoT module</li><li>`9`: MCU module</li><li>`10` to `19`: For extension</li></ul> |
| typeDesc | String | The description of the firmware update channel. |
| lastUpgradeTime | Long | The last update time. |
| firmwareDeployTime | Long | The firmware deployment time. |
| fileSize | Long | The size of the firmware update. Unit: bytes. |
| controlType | Int | Indicates whether device control is allowed during the update.<ul><li>`YES`: Allowed.</li><li>`NO`: Not allowed.</li></ul> |
| upgradingDesc | String | The description shown when the firmware is being updated. |
| downloadingDesc | String | The description shown when the update is being downloaded. |
| remind | String | The description shown when the update verification fails. |
| canUpgrade | Boolean? | Indicates whether the update verification succeeds.<ul><li>`null`: No verification is required to start an update.</li><li>`false`: The update verification fails, with the update being denied. You can show `remind` to inform the user about the failure.</li><li>`true`: The update verification succeeds, with the update being accepted.</li></ul> |
| devType | Int | The type of the device.<ul><li>`0`: Ordinary device</li><li>`1`: Low-power device</li></ul> |
| waitingDesc | String | The description for waiting for the device to wake up. |
| upgradeMode | Int | The type of firmware update, available starting from v3.35.5.<br /><ul><li>`0`: Generic firmware update</li><li>`1`: PID-specific firmware update</li></ul> |


### Start update

**Parameter description**

| Parameter | Type | Description |
| --- | --- | --- |
| upgradeInfoList | List<FirmwareUpgradeInfo> | The update information. |

**Example**

```java
iDevice.newOtaManager().startOTA(upgradeInfoList);
```


### Continue with update

**Parameter description**

| Parameter | Type | Description |
| --- | --- | --- |
| isContinue | Boolean | Indicates whether to continue with the update. |

**Example**

```java
iDevice.newOtaManager().continueOTA(isContinue)
```


### Cancel update

**Parameter description**

| Parameter | Type | Description |
| --- | --- | --- |
| firmwareType | Int | The type of firmware. |
| callback | IndustryNormalCallback | The callback. |

**Example**

```java
iDevice.newOtaManager().cancelOTA(0, new IndustryNormalCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
```

### Get update status

**Example**

```java
iDevice.newOtaManager().fetchUpgradingInfo(new IndustryDataCallBack<DeviceUpgradeStatusBean>() {
                    @Override
                    public void onSuccess(DeviceUpgradeStatusBean deviceUpgradeStatusBean) {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
```

**`DeviceUpgradeStatusBean` parameters**

| Parameter | Type | Description |
| --------------- | -------------------- | ------------------------------------------------------------ |
| devId | String | The device ID. |
| firmwareType | int | The type of firmware. |
| statusText | String | The description of the status. |
| statusTitle | String | The title of the status. |
| progress | int | The update progress. In certain conditions, the value might be less than 0. If any, ignore this type of value. |
| status | DevUpgradeStatusEnum | The update status.<br /><ul><li> `2`: Updating</li><li> `3`: Update succeeded</li><li> `4`: Update failed</li><li> `5`: Wait for the device to wake up</li><li> `6`: The update has been downloaded</li><li> `7`: Update timeout</li><li> `100`: The app is preparing, for example, connecting to a Bluetooth device, or downloading the update.</li></ul> |
| upgradeMode | Int | <ul><li>`0`: Generic firmware update</li><li>`1`: PID-specific firmware update, available starting from v3.35.5 |
| errorMsg | String | The error message. |
| errorCode | Int | The error code returned on failure. |


### Listen for callback

**Example**

```java
iDevice.newOtaManager().registerOTAListener(new IDeviceOtaListener() {
                    @Override
                    public void firmwareUpgradeStatus(DeviceUpgradeStatusBean deviceUpgradeStatusBean) {
                        Log.d(TAG, "firmwareUpgradeStatus: ");
                    }
                });
```


## Device control

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| dpCommand | DpCommand | Yes | The DP command. |
| callback | IndustryCallBack | Yes | The success callback. |

**`DpCommand` parameters**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| publishMode | DpsPublishMode | Yes | The data transfer type. |
| dps | List<Dp> | Yes | The DP data set. |

**Example of creating `dpCommand`**

```java
DpCommand dpCommand = new DpCommand.Builder()
                        .addDp("key","value")
                        .publishMode(DpsPublishMode.AUTO)
                        .build();
```

**Example**

```java
iDevice.publishDps(dpCommand, new IndustryCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.d(TAG, "onError: ");

                    }
                });
```

## Alternative network

:::important
You can call `iDevice.getDevAttribute() & (1 << 12)` to check whether a specific device supports the alternative network. `1` indicates supported and `0` indicates not supported.
:::

### Query current Wi-Fi information

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| callback | IndustryDataCallBack | Yes | The callback. |

**`ConnectWifiInfoBean` object**

**Parameter description**

| Parameter | Type | Description |
| -------- | ------ | ---------------------------- |
| devId | String | The device ID. |
| tId | String | The message ID. |
| ssid | String | The SSID of the Wi-Fi network. |
| signal | Int | The Wi-Fi signal strength. |
| network | Int | The network status. |
| version | Int | The protocol version. |
| hash | String | The hash value of the connected Wi-Fi network. |


**Example**

```java
iDevice.newBackupManager().fetchConnectWifiInfo(new IndustryDataCallBack<ConnectWifiInfoBean>() {
                    @Override
                    public void onSuccess(ConnectWifiInfoBean connectWifiInfoBean) {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
```

### Query alternative Wi-Fi networks

**`BackupWifiInfoListBean` object**

**Parameter description**

| Parameter | Type | Description |
| ------------ | ------------------------ | ---------------------------------------- |
| devId | String | The device ID. |
| tId | String | The message ID. |
| maxNum | String | The maximum number of SSIDs that the device can store. |
| backupList | MutableList<BackupWifiInfoBean> | The list of alternative Wi-Fi networks. The type is [BackupWifiInfoBean] |

**`BackupWifiInfoBean` parameters**

| Parameter | Type | Description |
| ------- | ------ | ------------------------------------------------- |
| ssid | String | The SSID of the Wi-Fi network. |
| hash | String | The hash value of the Wi-Fi network. |
| passwd | String | The password of the target Wi-Fi network. |


**Example**

```java
iDevice.newBackupManager().fetchBackupWifiInfoList(new IndustryDataCallBack<BackupWifiInfoListBean>() {
                    @Override
                    public void onSuccess(BackupWifiInfoListBean backupWifiInfoListBean) {
                        Log.d(TAG, "onSuccess: ");
                        
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Log.d(TAG, "onFailure: ");

                    }
                });
```

### Set the list of alternative Wi-Fi networks

**Parameter description**

| Parameters | Description |
| ---- | ---- |
| backupWifiList | The bean of the alternative Wi-Fi network. |
| callback | The callback. |

**`BackupWifiResultBean` data model**

**Parameter description**

| Parameter | Type | Description |
| --------- | ------------------------ | ---------------------- |
| devId | String | The device ID. |
| tId | String | The message ID. |
| ssid | String | The SSID of the Wi-Fi network. |
| resCode | Int | The response code. |
| ssidList | MutableList<String> | The list of Wi-Fi SSIDs. |


**Example**

```java
ArrayList<BackupWifiInfoBean> backupWifiList = new ArrayList<>();
                // Set the password of the newly added Wi-Fi network.
                BackupWifiInfoBean backupWifiBean = new BackupWifiInfoBean();
                backupWifiBean.setSsid("test1");
                backupWifiBean.setPasswd("12345678");
                backupWifiList.add(backupWifiBean);

                iDevice.newBackupManager().setBackupWifiInfoList(backupWifiList, new IndustryDataCallBack<BackupWifiResultBean>() {
                    @Override
                    public void onSuccess(BackupWifiResultBean backupWifiResultBean) {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
```

### Switch to a new Wi-Fi network

**Parameter description**

| Parameters | Type | Description |
| ---- | ---- | ---- |
| ssid | String | The name of the target Wi-Fi network. |
| password | String | The password of the target Wi-Fi network. |
| callback | IndustryDataCallBack | The callback. |

**Example**

```java
iDevice.newBackupManager().switchToTargetWifi("ssid", "password", new IndustryDataCallBack<SwitchWifiResultBean>() {
                    @Override
                    public void onSuccess(SwitchWifiResultBean switchWifiResultBean) {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Log.d(TAG, "onFailure: ");

                    }
                });
```
### Switch to an alternative Wi-Fi network

**Parameter description**

| Parameters | Type | Description |
| ---- | ---- | ---- |
| hash | String | The hash value of the target Wi-Fi password. |
| callback | IndustryDataCallBack | The callback. |

**Example**

```java
iDevice.newBackupManager().switchToBackupWifi("hash", new IndustryDataCallBack<SwitchWifiResultBean>() {
                    @Override
                    public void onSuccess(SwitchWifiResultBean switchWifiResultBean) {
                        Log.d(TAG, "onSuccess: ");
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
```

### Exit page and destroy listener

**API description**

```java
iDevice.newBackupManager().onDestroy();
```
