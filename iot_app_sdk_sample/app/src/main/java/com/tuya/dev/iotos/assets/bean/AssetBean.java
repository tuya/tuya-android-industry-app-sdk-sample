package com.tuya.dev.iotos.assets.bean;

import java.util.List;

/**
 * Asset Bean
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/22 4:52 PM
 */

public class AssetBean {

    /**
     * assets : [{"asset_id":"1372813829490122752","asset_name":"测试资产下设备转移"},{"asset_id":"1372829753920290816","asset_name":"SDK测试项目资产"}]
     * has_more : false
     * project_name : SDK测试项目
     */

    private List<AssetsBean> assets;
    private Boolean has_more;
    private String project_name;

    public List<AssetsBean> getAssets() {
        return assets;
    }

    public void setAssets(List<AssetsBean> assets) {
        this.assets = assets;
    }

    public Boolean getHas_more() {
        return has_more;
    }

    public void setHas_more(Boolean has_more) {
        this.has_more = has_more;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public static class AssetsBean {
        /**
         * asset_id : 1372813829490122752
         * asset_name : 测试资产下设备转移
         */

        private String asset_id;
        private String asset_name;

        public String getAsset_id() {
            return asset_id;
        }

        public void setAsset_id(String asset_id) {
            this.asset_id = asset_id;
        }

        public String getAsset_name() {
            return asset_name;
        }

        public void setAsset_name(String asset_name) {
            this.asset_name = asset_name;
        }
    }
}
