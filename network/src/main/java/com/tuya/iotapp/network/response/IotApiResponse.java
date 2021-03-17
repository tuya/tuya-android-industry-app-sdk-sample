package com.tuya.iotapp.network.response;

/**
 *  IotApiResponse
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 4:58 PM
 */
public class IotApiResponse implements IResponse{

    //错误码
    protected Integer code;

    //错误描述
    protected String msg;

    //是否返回成功
    protected Boolean success;

    //返回结果
    protected Object result;

    //过期时间
    protected Long expires;

    //时间戳
    protected long timestamp;

    //接口返回时间
    protected long t;

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean isSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    @Override
    public String getData() {
        return null;
    }
}
