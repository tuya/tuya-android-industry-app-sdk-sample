package com.tuya.dev.iotos.assets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.tuya.dev.iotos.MainManagerActivity;
import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.assets.adapter.AssetsAdapter;
import com.tuya.dev.iotos.assets.bean.AssetBean;
import com.tuya.dev.iotos.assets.business.AssetBusiness;
import com.tuya.dev.network.business.BusinessResponse;
import com.tuya.dev.network.request.ResultListener;

import java.util.ArrayList;

/**
 * Assets Activity
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/18 3:27 PM
 */
public class AssetsActivity extends AppCompatActivity {
    private static final String ASSET_ID = "assetId";
    private static final String ASSET_NAME = "assetName";


    private AssetBusiness business = new AssetBusiness();

    private RecyclerView rvAsset;
    private AssetsAdapter adapter;
    private Button btnDone;

    private boolean hasMore = true;
    private boolean loading = false;

    private String assetId = "";

    public static void launch(Context context,
                              String assetId,
                              String assetName) {
        Intent intent = new Intent(context, AssetsActivity.class);
        intent.putExtra(ASSET_ID, assetId);
        intent.putExtra(ASSET_NAME, assetName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);

        assetId = getIntent().getStringExtra(ASSET_ID);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle(getIntent().getStringExtra(ASSET_NAME));
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        btnDone = findViewById(R.id.btnDone);
        if (!TextUtils.isEmpty(assetId)) {
            btnDone.setVisibility(View.VISIBLE);
            btnDone.setOnClickListener(v -> {
                AssetsManager.INSTANCE.saveAssets(assetId);
                v.getContext().startActivity(new Intent(v.getContext(), MainManagerActivity.class));
            });
        }


        rvAsset = findViewById(R.id.rvAsset);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvAsset.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        rvAsset.setLayoutManager(linearLayoutManager);
        rvAsset.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    if (((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() > adapter.getItemCount() - 10
                            && hasMore) {
                        loadMore();
                    }
                }
            }
        });

        adapter = new AssetsAdapter();
        rvAsset.setAdapter(adapter);

        loadMore();
    }

    private void loadMore() {
        if (loading) {
            return;
        }
        loading = true;
        business.queryAssets(assetId,
                new ResultListener<AssetBean>() {
                    @Override
                    public void onFailure(BusinessResponse bizResponse, AssetBean bizResult, String apiName) {
                        loading = false;
                    }

                    @Override
                    public void onSuccess(BusinessResponse bizResponse, AssetBean bizResult, String apiName) {
                        if (bizResult.getAssets().size() < 10) {
                            hasMore = false;
                        }
                        adapter.setData((ArrayList) bizResult.getAssets());
                        adapter.notifyDataSetChanged();
                        loading = false;
                    }
                });
    }
}
