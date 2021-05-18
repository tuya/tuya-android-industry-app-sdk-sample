package com.tuya.dev.iotos.devices.bean;

import java.util.List;

/**
 * Device List Bean
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/5/17 11:15 AM
 */
public class DeviceListBean {


    /**
     * total : 2
     * hasMore : false
     * list : [{"sub":false,"productId":"EJhDpP6rc0lPKVt9","ip":"115.236.167.102","activeTime":1621045056,"icon":"smart/icon/1545805678h5zpw0yo2k4_0.png","timeZone":"+08:00","lon":"","updateTime":1621213107,"uuid":"53733405840d8e8ea7af","productName":"sleepace唤醒灯 TEW201","createTime":1544705745,"assetId":"1385867549996081152","name":"sleepace唤醒灯 TEW201","online":false,"model":"TEW201","id":"53733405840d8e8ea7af","category":"dj","lat":"","localKey":"7b3c17ec52d70497"},{"sub":false,"productId":"ylr9R01cMWnMRqEB","ip":"115.236.167.98","activeTime":1619255556,"icon":"smart/icon/1526276264k8q8l44swr2ennf6zx5u92j4i_0.png","timeZone":"+08:00","lon":"","updateTime":1619402051,"uuid":"51658673cc50e3c60223","productName":"Light-LE7","createTime":1548207469,"assetId":"1385867549996081152","name":"D","online":false,"model":"LE7","id":"51658673cc50e3c60223","category":"dj","lat":"","localKey":"45761af41a597f86"}]
     */

    private Integer total;
    private Boolean hasMore;
    private List<ListBean> list;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * sub : false
         * productId : EJhDpP6rc0lPKVt9
         * ip : 115.236.167.102
         * activeTime : 1621045056
         * icon : smart/icon/1545805678h5zpw0yo2k4_0.png
         * timeZone : +08:00
         * lon :
         * updateTime : 1621213107
         * uuid : 53733405840d8e8ea7af
         * productName : sleepace唤醒灯 TEW201
         * createTime : 1544705745
         * assetId : 1385867549996081152
         * name : sleepace唤醒灯 TEW201
         * online : false
         * model : TEW201
         * id : 53733405840d8e8ea7af
         * category : dj
         * lat :
         * localKey : 7b3c17ec52d70497
         */

        private Boolean sub;
        private String productId;
        private String ip;
        private Integer activeTime;
        private String icon;
        private String timeZone;
        private String lon;
        private Integer updateTime;
        private String uuid;
        private String productName;
        private Integer createTime;
        private String assetId;
        private String name;
        private Boolean online;
        private String model;
        private String id;
        private String category;
        private String lat;
        private String localKey;

        public Boolean getSub() {
            return sub;
        }

        public void setSub(Boolean sub) {
            this.sub = sub;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Integer getActiveTime() {
            return activeTime;
        }

        public void setActiveTime(Integer activeTime) {
            this.activeTime = activeTime;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public Integer getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Integer updateTime) {
            this.updateTime = updateTime;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Integer getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Integer createTime) {
            this.createTime = createTime;
        }

        public String getAssetId() {
            return assetId;
        }

        public void setAssetId(String assetId) {
            this.assetId = assetId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean getOnline() {
            return online;
        }

        public void setOnline(Boolean online) {
            this.online = online;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLocalKey() {
            return localKey;
        }

        public void setLocalKey(String localKey) {
            this.localKey = localKey;
        }
    }
}
