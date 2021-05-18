package com.tuya.dev.iotos.assets;

import com.tuya.dev.iotos.kv.KvManager;

/**
 * AssetsManager
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/23 9:58 AM
 */
public enum AssetsManager {
    INSTANCE;
    private final static String ASSET_ID = "assetId";
    private final static String ASSET_NAME = "assetName";
    private String assetId;

    public void saveAssets(String assetId, String assetName) {
        this.assetId = assetId;
        KvManager.set(ASSET_ID, assetId);
        KvManager.set(ASSET_NAME, assetName);
    }

    public String getAssetId() {
        if (assetId == null) {
            assetId = KvManager.getString(ASSET_ID);
        }
        return assetId;
    }

    public String getAssetName() {
        return KvManager.getString(ASSET_NAME);
    }
}
