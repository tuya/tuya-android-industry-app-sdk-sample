package com.tuya.iotapp.network.http;

import java.util.concurrent.ExecutorService;

/**
 * 网络执行器配置
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 7:59 PM
 */
public class IotAppNetWorkConfig {

    private final ExecutorService businessExecutor;

    private final ExecutorService netWorkExecutor;

    public IotAppNetWorkConfig() {
        this.businessExecutor = null;
        this.netWorkExecutor = null;
    }

    public IotAppNetWorkConfig(Builder builder) {
        this.businessExecutor = builder.businessExecutor;
        this.netWorkExecutor = builder.netWorkExecutor;
    }
    public ExecutorService getBusinessExecutor() {
        return businessExecutor;
    }
    public ExecutorService getNetWorkExecutor() {
        return netWorkExecutor;
    }

    public static class Builder {
        ExecutorService businessExecutor;
        ExecutorService netWorkExecutor;

        public Builder() {
            this.businessExecutor = null;
            this.netWorkExecutor = null;
        }

        public Builder(IotAppNetWorkConfig config) {
            this.businessExecutor = config.businessExecutor;
            this.netWorkExecutor = config.netWorkExecutor;
        }

        public Builder businessExecutor(ExecutorService businessExcutor) {
            this.businessExecutor = businessExcutor;
            return this;
        }

        public Builder netWorkExecutor(ExecutorService netWorkExecutor) {
            this.netWorkExecutor = netWorkExecutor;
            return this;
        }

        public IotAppNetWorkConfig build() {
            return new IotAppNetWorkConfig(this);
        }
    }

}
