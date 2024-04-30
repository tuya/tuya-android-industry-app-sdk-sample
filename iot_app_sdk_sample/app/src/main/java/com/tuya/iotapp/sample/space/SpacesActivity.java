package com.tuya.iotapp.sample.space;

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

import com.thingclips.iotapp.common.IndustryCallBack;
import com.thingclips.iotapp.common.IndustryValueCallBack;
import com.thingclips.iotapp.space.api.ISpace;
import com.thingclips.iotapp.space.api.SpaceService;
import com.tuya.iotapp.sample.MainManagerActivity;
import com.tuya.iotapp.sample.R;
import com.tuya.iotapp.sample.assets.AssetsManager;
import com.tuya.iotapp.sample.space.adapter.SpacesAdapter;

import java.util.List;

/**
 * Assets Activity
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/18 3:27 PM
 */
public class SpacesActivity extends AppCompatActivity {
    private static final String TAG = "SpacesActivity";
    private static final String SPACE_ID = "spaceId";
    private static final String SPACE_NAME = "spaceName";

    private RecyclerView rvSpace;
    private SpacesAdapter adapter;
    private Button btnDone, btnUpdate;


    private boolean hasMore = true;
    private boolean loading = false;
    private int mPageNo = 0;

    private String spaceId = "";

    public static void launch(Context context,
                              String spaceId,
                              String spaceName) {
        Intent intent = new Intent(context, SpacesActivity.class);
        intent.putExtra(SPACE_ID, spaceId);
        intent.putExtra(SPACE_NAME, spaceName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);

        spaceId = getIntent().getStringExtra(SPACE_ID);

        findViewById(R.id.btnAdd).setOnClickListener(v -> showInputDialog());

        findViewById(R.id.btnDelete).setOnClickListener(v -> onDeleteSpace());

        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle(getIntent().getStringExtra(SPACE_NAME));
        toolbar.setNavigationOnClickListener(v -> finish());

        btnDone = findViewById(R.id.btnDone);
        btnUpdate = findViewById(R.id.btn_update);
        if (!TextUtils.isEmpty(spaceId)) {
            btnUpdate.setVisibility(View.VISIBLE);
            btnUpdate.setOnClickListener(v -> onRenameSpace());
            btnDone.setVisibility(View.VISIBLE);
            btnDone.setOnClickListener(v -> {
                AssetsManager.INSTANCE.saveAssets(spaceId);
                v.getContext().startActivity(new Intent(v.getContext(), MainManagerActivity.class));
            });
        }


        rvSpace = findViewById(R.id.rvSpace);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvSpace.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        rvSpace.setLayoutManager(linearLayoutManager);
        rvSpace.addOnScrollListener(new RecyclerView.OnScrollListener() {

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

        adapter = new SpacesAdapter();
        rvSpace.setAdapter(adapter);

        loadMore();
    }

    private void onRenameSpace() {
        final EditText inputServer = new EditText(SpacesActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(SpacesActivity.this);
        builder.setTitle(R.string.spaces_input_space_name).setView(inputServer)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
            String name = inputServer.getText().toString();
            SpaceService.update(spaceId, name, new IndustryCallBack() {
                @Override
                public void onSuccess() {
                    Toast.makeText(SpacesActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(int i, @NonNull String s) {
                    Toast.makeText(SpacesActivity.this, getString(R.string.failure) + ": " + s, Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.show();
    }

    private void onDeleteSpace() {
        SpaceService.remove(spaceId, new IndustryCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(SpacesActivity.this, R.string.spaces_del_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int i, @NonNull String s) {
                Toast.makeText(SpacesActivity.this, getString(R.string.spaces_del_failure) + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMore() {
        if (loading) {
            return;
        }
        loading = true;
        SpaceService.subSpaces(spaceId, new IndustryValueCallBack<List<ISpace>>() {
            @Override
            public void onSuccess(List<ISpace> spaces) {
                if (hasMore) {
                    mPageNo++;
                }
                adapter.setData(spaces);
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
        final EditText inputServer = new EditText(SpacesActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(SpacesActivity.this);
        builder.setTitle(R.string.spaces_input_space_name).setView(inputServer)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
            String name = inputServer.getText().toString();
            SpaceService.create(name, spaceId, new IndustryCallBack() {
                @Override
                public void onSuccess() {
                    loadMore();
                }

                @Override
                public void onError(int i, @NonNull String s) {
                    Log.e(TAG, "code: " + i + "; msg: " + s);
                    Toast.makeText(SpacesActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.show();
    }
}
