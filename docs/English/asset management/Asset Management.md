## Add asset

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| name | String | Yes | The name of the asset. |
| parentAssetId | String | Yes | The ID of the parent asset. |
| callback | IndustryCallBack | Yes | The callback. |

**Example**

```java
AssetService.create("name", "parentAssetId", new IndustryCallBack() {
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

## Rename asset

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| assetId | String | Yes | The ID of the target asset. |
| name | String | Yes | The new name of the asset. |
| callback | IndustryCallBack | Yes | The callback. |

**Example**

```java
AssetService.update("assetId", "name", new IndustryCallBack() {
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

## Delete asset

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| assetId | String | Yes | The ID of the target asset. |
| callback | IndustryCallBack | Yes | The callback. |

**Example**

```java
AssetService.remove("assetId", new IndustryCallBack() {
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

## Get the property of a single asset

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| assetId | String | Yes | The ID of the target asset. |
| callback | IndustryValueCallBack<IAsset> | Yes | The callback. |

**Example**

```java
AssetService.asset("assetId", new IndustryValueCallBack<IAsset>() {
            @Override
            public void onSuccess(IAsset iAsset) {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: ");

            }
        });
```

## Get the list of sub-asset properties

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| assetId | String? | No | The ID of the asset to be queried. If the value is `nil`, all sub-assets will be queried. |
| callback | IndustryValueCallBack<List<IAsset>> | Yes | The callback, returning the list of sub-assets. |

**Example**

```java
AssetService.subAssets("assetId", new IndustryValueCallBack<List<IAsset>>() {
            @Override
            public void onSuccess(List<IAsset> iAssets) {
                Log.d(TAG, "onSuccess: ");
                
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: ");

            }
        });
```

## Query device list by asset ID

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| assetId | String | Yes | The ID of the target asset. |
| lastRowKey | String? | No | The `RowKey` of the last row returned from the previous query, used for pagination. |
| callback | IndustryValueCallBack<IAssetDeviceListResult> | Yes | The callback, returning the list of devices. |

**Example**

```java
AssetService.devices("assetId", "lastRowKey", new IndustryValueCallBack<IAssetDeviceListResult>() {
            @Override
            public void onSuccess(IAssetDeviceListResult iAssetDeviceListResult) {
                Log.d(TAG, "onSuccess: ");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: ");

            }
        });
```

## Get the management instance of a single asset

**Parameter description**

| Parameter | Type | Required | Description |
| --- | --- | --- | --- |
| assetId | String | Yes | The ID of the target asset. |


**Example**

```java
IAssetManager manager = AssetService.assetManager("assetId");
```
