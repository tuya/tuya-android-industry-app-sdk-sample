package com.tuya.iotapp.assets.business;

import com.tuya.iotapp.assets.bean.AssetBean;
import com.tuya.iotapp.network.business.Business;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;

/**
 * AssetBusinessImpl
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 4:48 PM
 */
public class AssetBusiness extends Business {

    private static final String ASSETS_API = "/v1.0/iot-03/users/assets";

    public AssetBusiness() {

    }

    public void queryAssets(String assetId,
                            ResultListener<AssetBean> listener) {
        IotApiParams params = new IotApiParams(ASSETS_API, "1.0", "GET");
        params.addParam("page_no", "0");
        params.addParam("page_size", "10");
        params.addParam("parent_asset_id", assetId);
        asyncRequest(params, AssetBean.class, listener);
    }
}
