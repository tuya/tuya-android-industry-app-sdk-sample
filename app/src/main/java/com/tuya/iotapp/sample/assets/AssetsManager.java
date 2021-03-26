package com.tuya.iotapp.sample.assets;

import com.tuya.iotapp.common.kv.KvManager;

/**
 * AssetsManager
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/23 9:58 AM
 */
public enum AssetsManager {
    INSTANCE;
    private final static String ASSET_ID = "assetId";
    private String assetId;

    public void saveAssets(String assetId) {
        this.assetId = assetId;
        KvManager.set(ASSET_ID, assetId);
    }

    public String getAssetId() {
        if (assetId == null) {
            assetId = KvManager.getString(ASSET_ID);
        }
        return assetId;
    }
}
