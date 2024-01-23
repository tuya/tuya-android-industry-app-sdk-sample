# Method 1

`edgeGatewayActivatorToken` generates a token that is then uploaded to the backend of the edge gateway for device pairing.

## Generate a token

**Parameter description**

| Parameter | Type | Required | Description |
| ------ | ---- | -------- | ---- |
| assetId | String | Yes | The asset ID. |
| callback | IndustryValueCallBack<String> | No | The callback, returning the activation token. |

**Example**

```java
ActivatorService.edgeGatewayActivatorToken("assetId", new IndustryValueCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG, "onSuccess: " + s);
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Log.d(TAG, "onError: " + s);

            }
        });
```

# Method 2

Generate a QR code on the backend of the edge gateway and pair the device by scanning the QR code.

