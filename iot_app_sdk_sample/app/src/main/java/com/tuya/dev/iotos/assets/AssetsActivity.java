package com.tuya.dev.iotos.assets;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuya.dev.iotos.MainManagerActivity;
import com.tuya.dev.iotos.R;
import com.tuya.dev.iotos.assets.adapter.AssetsAdapter;
import com.tuya.dev.iotos.view.DividerDecoration;
import com.tuya.iotapp.asset.api.TYAssetManager;
import com.tuya.iotapp.asset.bean.AssetsBean;
import com.tuya.iotapp.network.response.ResultListener;

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

    private RecyclerView rvAsset;
    private AssetsAdapter adapter;

    private boolean hasMore = true;
    private boolean loading = false;

    private String assetId = "";
    private int pageNum = 0;

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

        ((TextView)findViewById(R.id.tvTitle)).setText(getIntent().getStringExtra(ASSET_NAME));
        findViewById(R.id.ivBack).setOnClickListener(v->{
            finish();
        });

        if (!TextUtils.isEmpty(assetId)) {
            findViewById(R.id.flAsset).setVisibility(View.VISIBLE);
            findViewById(R.id.tvDone).setOnClickListener(v -> {
                AssetsManager.INSTANCE.saveAssets(assetId, getIntent().getStringExtra(ASSET_NAME));
                v.getContext().startActivity(new Intent(v.getContext(), MainManagerActivity.class));
            });
        }


        rvAsset = findViewById(R.id.rvAsset);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvAsset.setLayoutManager(linearLayoutManager);
        DividerDecoration dividerItemDecoration = new DividerDecoration(new InsetDrawable(ContextCompat.getDrawable(this, R.drawable.bg_tuya_divider),
                48,
                0,
                48,
                0));
        rvAsset.addItemDecoration(dividerItemDecoration);
        rvAsset.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                    if (((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition() > adapter.getItemCount() - 10
                            && hasMore) {
                        pageNum += 1;
                        loadMore(pageNum);
                    }
                }
            }
        });

        adapter = new AssetsAdapter();
        rvAsset.setAdapter(adapter);

        loadMore(pageNum);
    }

    private void loadMore(int pageNum) {
        if (loading) {
            return;
        }
        loading = true;
        TYAssetManager.Companion.getAssetBusiness().queryAssets(
                assetId,
                pageNum,
                20,
                new ResultListener<AssetsBean>() {
                    @Override
                    public void onFailure(String s, String s1) {
                        loading = false;
                        Toast.makeText(AssetsActivity.this,
                                s1,
                                Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onSuccess(AssetsBean assetsBean) {
                        if (assetsBean.getAssets().size() < 10) {
                            hasMore = false;
                        }
                        adapter.setData((ArrayList) assetsBean.getAssets());
                        adapter.notifyDataSetChanged();
                        loading = false;
                    }
                }
        );
    }
}
