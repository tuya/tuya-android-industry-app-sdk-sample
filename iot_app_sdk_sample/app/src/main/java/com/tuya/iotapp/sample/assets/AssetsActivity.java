package com.tuya.iotapp.sample.assets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thingclips.iotapp.asset.api.AssetService;
import com.thingclips.iotapp.asset.api.IAsset;
import com.thingclips.iotapp.common.IndustryCallBack;
import com.thingclips.iotapp.common.IndustryValueCallBack;
import com.tuya.iotapp.sample.MainManagerActivity;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.assets.adapter.AssetsAdapter;

import java.util.List;

/**
 * Assets Activity
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/18 3:27 PM
 */
public class AssetsActivity extends AppCompatActivity {
    private static final String TAG = "AssetsActivity";
    private static final String ASSET_ID = "assetId";
    private static final String ASSET_NAME = "assetName";

    private RecyclerView rvAsset;
    private AssetsAdapter adapter;
    private Button btnDone, btnUpdate;


    private boolean hasMore = true;
    private boolean loading = false;
    private int mPageNo = 0;

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

        findViewById(R.id.btnAdd).setOnClickListener(v -> showInputDialog());

        findViewById(R.id.btnDelete).setOnClickListener(v -> onDeleteAsset());

        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle(getIntent().getStringExtra(ASSET_NAME));
        toolbar.setNavigationOnClickListener(v -> finish());

        btnDone = findViewById(R.id.btnDone);
        btnUpdate = findViewById(R.id.btn_update);
        if (!TextUtils.isEmpty(assetId)) {
            btnUpdate.setVisibility(View.VISIBLE);
            btnUpdate.setOnClickListener(v -> onRenameAsset());
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

    private void onRenameAsset() {
        final EditText inputServer = new EditText(AssetsActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(AssetsActivity.this);
        builder.setTitle(R.string.asset_update_title).setView(inputServer)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
            String name = inputServer.getText().toString();
            AssetService.update(assetId, name, new IndustryCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AssetsActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(int i, @NonNull String s) {
                    Toast.makeText(AssetsActivity.this, getString(R.string.failure) + ": " + s, Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.show();
    }

    private void onDeleteAsset() {
        AssetService.remove(assetId, new IndustryCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(AssetsActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Toast.makeText(AssetsActivity.this, getString(R.string.failure) + ": " + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMore() {
        if (loading) {
            return;
        }
        loading = true;
        AssetService.subAssets(assetId, new IndustryValueCallBack<List<IAsset>>() {
            @Override
            public void onSuccess(List<IAsset> iAssets) {
                if (hasMore) {
                    mPageNo++;
                }
                adapter.setData(iAssets);
                adapter.notifyDataSetChanged();
                loading = false;

            }

            @Override
            public void onError(int i, String s) {
                loading = false;

            }
        });
    }

    private void showInputDialog() {
        final EditText inputServer = new EditText(AssetsActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(AssetsActivity.this);
        builder.setTitle(R.string.asset_input_asset_name).setView(inputServer)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
            String name = inputServer.getText().toString();
            String parentAssetId = assetId;
            AssetService.create(name, parentAssetId, new IndustryCallBack() {
                @Override
                public void onSuccess() {
                    loadMore();
                }

                @Override
                public void onError(int i, @NonNull String s) {
                    Log.e("AssetManager", "code: " + i + "; msg: " + s);

                }
            });
        });
        builder.show();
    }
}