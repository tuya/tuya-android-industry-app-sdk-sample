package com.tuya.iotapp.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.RenderProcessGoneDetail;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tuya.iotapp.activitor.config.APConfigImpl;
import com.tuya.iotapp.activitor.config.EZConfigImpl;
import com.tuya.iotapp.activitor.config.IQrCodeActivitorListener;
import com.tuya.iotapp.activitor.config.QRCodeConfigImpl;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.network.IotAppNetWork;
import com.tuya.iotapp.network.api.IApiUrlProvider;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;
import com.tuya.iotapp.network.utils.TimeStampManager;
import com.tuya.iotapp.sample.env.EnvUrlProvider;
import com.tuya.iotapp.sample.env.EnvUtils;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{
    private String mAccessToken;
    private String uid;
    private String mToken; //配网令牌token
    private String mActivitorToken; //mActivitorToken：region + mToken + secret

    private Button mBtnToken;
    private Button mBtnAp;
    private Button mBtnQr;
    private ImageView mIvQrContent;
    private Button mBtnAsset;
    boolean apStatus = false;
    boolean qrStatus = false;

    private String ssid = "Tuya-Test";
    private String password = "Tuya.140616";
    private TestBusiness business;
    private Timer timer;
    private boolean loopExpire = false;
    private long startLoopTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView(this);
        EnvUtils.setEnv(this, EnvUtils.ENV_PRE);
        IApiUrlProvider provider = new EnvUrlProvider(this);
        IotAppNetWork.initialize(getApplicationContext(), "spjyeg4vafhb1ajs63oa", "7fee20315982485295a1dadac6c3fc50", "Android", provider);

        business = new TestBusiness();
        business.login(new ResultListener<com.alibaba.fastjson.JSONObject>() {
            @Override
            public void onFailure(BusinessResponse bizResponse, com.alibaba.fastjson.JSONObject bizResult, String apiName) {
                LogUtils.d("TestBusiness", "====-failure======code: " + bizResponse.getCode()+" msg:" +bizResponse.getMsg());
            }

            @Override
            public void onSuccess(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                LogUtils.d("TestBusiness", "====-onSuccess======"+bizResult);
                mAccessToken = bizResult.getString("access_token");
                uid = bizResult.getString("uid");
                IotAppNetWork.setAccessToken(mAccessToken);
                LogUtils.d("main accesstoken", mAccessToken + "uid ： " +uid);
            }
        });
    }

    private void initView(Context context) {
        mBtnAp = (Button) findViewById(R.id.btn_ap);
        mBtnQr = (Button) findViewById(R.id.btn_qr);
        mIvQrContent = (ImageView) findViewById(R.id.qr_content);
        mBtnAsset = (Button) findViewById(R.id.btn_asset);
        mBtnToken = (Button) findViewById(R.id.btn_register_token);
        mBtnToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                business.getDeviceRegistrationToken("1371854360056766464", uid, new ResultListener<JSONObject>() {
                    @Override
                    public void onFailure(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                        LogUtils.d("registratinToken","=====false====: " + bizResponse.getCode() + " " + bizResponse.getMsg());
                    }

                    @Override
                    public void onSuccess(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                        LogUtils.d("registratinToken","=====onSuccess====: " + bizResult.toJSONString());
                        String region = bizResult.getString("region");
                        mToken = bizResult.getString("token");
                        String secret = bizResult.getString("secret");
                        StringBuilder builder = new StringBuilder();
                        builder.append(region);
                        builder.append(mToken);
                        builder.append(secret);
                        mActivitorToken = builder.toString();

                        Toast.makeText(context, "令牌 拼接 token ：" + mActivitorToken , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        mBtnAp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer == null) {
                    timer = new Timer();
                }
                if (!apStatus) {
                    //APConfigImpl.startConfig(context, ssid, password, mActivitorToken);
                    EZConfigImpl.startConfig(ssid, password, mActivitorToken);
                    mBtnAp.setText("Ap stopConfig");
                    apStatus = true;
                    startLoop();
                } else {
                    //APConfigImpl.stopConfig();
                    EZConfigImpl.stopConfig();
                    mBtnAp.setText("Ap startConfig");
                    apStatus = false;
                    stopLoop();
                }
            }
        });

        mBtnQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QRCodeConfigImpl.createQrCode(context, ssid, password, mActivitorToken, new IQrCodeActivitorListener() {
                    @Override
                    public void onQrCodeSuccess(Bitmap bitmap) {
                        business.queryDevicesByAssetId("1372829753920290816", new ResultListener<JSONObject>() {
                            @Override
                            public void onFailure(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                                LogUtils.d("query devices result", "=====false==="+bizResponse.getCode()+"  " +bizResponse.getCode());
                            }

                            @Override
                            public void onSuccess(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                                LogUtils.d("query devices result", "=====success==="+bizResult.toJSONString());
                            }
                        });

                        if (!qrStatus) {
                            mIvQrContent.setImageBitmap(bitmap);
                            mIvQrContent.setVisibility(View.VISIBLE);
                            qrStatus = true;
                            startLoop();
                        } else {
                            mIvQrContent.setVisibility(View.GONE);
                            qrStatus = false;
                            stopLoop();
                        }
                    }
                });
            }
        });

        mBtnAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                business.testQueryAssets();
            }
        });
    }


    public void startLoop() {
        if (timer == null) {
            timer = new Timer();
        }
        if (!loopExpire) {
            startLoopTime = TimeStampManager.instance().getCurrentTimeStamp();
            loopExpire = true;
        }
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                long endLoopTime = TimeStampManager.instance().getCurrentTimeStamp();
                LogUtils.d("registration result", "=====loop 循环调用==" + mToken + "==expireTime==:" + (endLoopTime - startLoopTime));
                if (endLoopTime - startLoopTime > 100) {
                    stopLoop();
                    loopExpire = false;

                }
                business.getRegistrationResult(mToken, new ResultListener<JSONObject>() {
                    @Override
                    public void onFailure(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                        LogUtils.d("registration result", "=====false==="+bizResponse.getCode()+"  " +bizResponse.getCode());
                    }

                    @Override
                    public void onSuccess(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                        LogUtils.d("registration result","======success===="+bizResult.toJSONString());
                        JSONArray successDevices = bizResult.getJSONArray("successDevices");
                        if(successDevices != null && successDevices.size() > 0) {
                            //APConfigImpl.stopConfig();
                            EZConfigImpl.stopConfig();
                            stopLoop();
                        }
                    }
                });
            }

        }, 0, 2000);
    }

    private void stopLoop() {
        timer.cancel();
        timer = null;
    }
}