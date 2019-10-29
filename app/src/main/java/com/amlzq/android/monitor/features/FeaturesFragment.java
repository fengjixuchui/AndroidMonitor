package com.amlzq.android.monitor.features;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.amlzq.android.content.ContextHolder;
import com.amlzq.android.log.Log;
import com.amlzq.android.monitor.MyBaseRecycleViewFragment;
import com.amlzq.android.monitor.R;
import com.amlzq.android.monitor.data.model.CommonInfo;
import com.amlzq.android.monitor.device.DeviceAdapter;
import com.amlzq.android.schedulers.AppExecutors;
import com.amlzq.android.util.ClipboardUtil;
import com.amlzq.android.util.DeviceUtil;
import com.amlzq.android.util.PermissionChecker;
import com.amlzq.android.util.Share2;
import com.amlzq.android.util.ShareContentType;
import com.amlzq.android.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 特征
 */

public class FeaturesFragment
        extends MyBaseRecycleViewFragment
        implements EasyPermissions.PermissionCallbacks {

    public static final String TAG = "FeaturesFragment";

    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private DeviceAdapter mAdapter;

    public FeaturesFragment() {
        // Required empty public constructor
    }

    public static FeaturesFragment newInstance() {
        return new FeaturesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_features, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new DeviceAdapter();
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        StringBuilder sb = new StringBuilder();
        for (CommonInfo model : mAdapter.getData()) {
            sb.append(model.share());
        }

        if (id == R.id.action_share) {
            new Share2.Builder(getActivity())
                    .setContentType(ShareContentType.TEXT)
                    .setTextContent(sb.toString())
                    .setTitle(String.format(getString(R.string.title_share_to), ""))
                    // .forcedUseSystemChooser(false)
                    .build()
                    .shareBySystem();
        } else if (id == R.id.action_copy) {
            ClipboardUtil.putText(getContext(), sb.toString());
            showToastShort(R.string.already_copied_to_the_clipboard);
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isActive() {
        return isAdded();
    }

    public void setLoadingIndicator(boolean active) {
        mSwipeRefresh.setRefreshing(active);
    }

    public void showLoadingError(String message) {
        if (StringUtil.notEmpty(message)) showToastShort(message);
    }

    public void refreshListView(List<CommonInfo> items) {
        mAdapter.setNewData(items);
    }

    private void askPhonePermission(CommonInfo model) {
        EasyPermissions.requestPermissions(this,
                String.format(getString(R.string.grant_permission_to_display_data),
                        Manifest.permission.READ_PHONE_STATE, model.id),
                PermissionChecker.REQUEST_CODE_PHONE, Manifest.permission.READ_PHONE_STATE);

//        if (EasyPermissions.hasPermissions(mContext, Manifest.permission.READ_PHONE_STATE)) {
//            // Have permission, do the thing!
//            item.content = DeviceUtil.getDeviceId();
//            mAdapter.notifyDataSetChanged();
//        } else {
//            // Request one permission
//            EasyPermissions.requestPermissions(this, item.content,
//                    PermissionChecker.REQUEST_CODE_PHONE, Manifest.permission.READ_PHONE_STATE);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == PermissionChecker.REQUEST_CODE_PHONE) {

        }
        loadData(true, true);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    private void showDialog(final Context context, CommonInfo model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(model.id);
        builder.setMessage(model.content);
        builder.setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                new Share2.Builder(getActivity())
                        .setContentType(ShareContentType.TEXT)
                        .setTextContent(model.share())
                        .setTitle(model.id)
                        // .forcedUseSystemChooser(false)
                        .build()
                        .shareBySystem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        builder.setNeutralButton(R.string.copy, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ClipboardUtil.putText(getContext(), model.share());
                showToastShort(R.string.already_copied_to_the_clipboard);
            }
        });
        builder.show();
    }

    public void loadData(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) setLoadingIndicator(true);
        // if (forceUpdate) mRepository.refreshData();

        AppExecutors appExecutors = new AppExecutors();
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<CommonInfo> datas = new ArrayList<CommonInfo>();
                Context cxt = ContextHolder.getContext();

                datas.add(new CommonInfo(cxt.getString(R.string.device_id),
                        StringUtil.get(DeviceUtil.getDeviceId(),
                                cxt.getString(R.string.granted_permission)), ""));

                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (!isActive()) return;
                        if (showLoadingUI) setLoadingIndicator(false);
                        refreshListView(datas);
                    }
                });
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData(true, true);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommonInfo item = mAdapter.getItem(position);
        Log.d(item.toString());
        showDialog(getContext(), item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemChildClick(RecyclerView.Adapter adapter, View view, int position) {
        CommonInfo item = mAdapter.getItem(position);
        Log.d(item.toString());
        if (getString(R.string.granted_permission).endsWith(item.content)) {
            if (getString(R.string.device_id).endsWith(item.id)) {
                askPhonePermission(item);
            }
        } else {
            showDialog(getContext(), item);
        }
    }

}