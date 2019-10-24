package com.amlzq.android.monitor.features;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

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
        extends MyBaseRecycleViewFragment<CommonInfo, DeviceAdapter.ViewHolder>
        implements EasyPermissions.PermissionCallbacks {

    public static final String TAG = "FeaturesFragment";

    private DeviceAdapter mAdapter;

    public FeaturesFragment() {
        // Required empty public constructor
    }

    public static FeaturesFragment newInstance() {
        FeaturesFragment fragment = new FeaturesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_features, container, false);
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_features;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        setActivityTitle(R.string.title_features);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        StringBuilder sb = new StringBuilder();
        for (CommonInfo model : mAdapter.getData()) {
            sb.append(model.share());
        }

        if (id == R.id.action_share) {
            new Share2.Builder(mActivity)
                    .setContentType(ShareContentType.TEXT)
                    .setTextContent(sb.toString())
                    .setTitle(String.format(getString(R.string.title_share_to), getActivityTitle()))
                    // .forcedUseSystemChooser(false)
                    .build()
                    .shareBySystem();
        } else if (id == R.id.action_copy) {
            ClipboardUtil.putText(mContext, sb.toString());
            showToastShort(R.string.already_copied_to_the_clipboard);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTitleChanged(CharSequence charSequence, int i) {

    }

    @Override
    protected RecyclerView.Adapter<DeviceAdapter.ViewHolder> getAdapter() {
        mAdapter = new DeviceAdapter();
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        return mAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mRecyclerView.getContext());
    }

    @Override
    protected RecyclerView.ItemAnimator getItemAnimator() {
        return new DefaultItemAnimator();
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
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

    public void refrashListView(List<CommonInfo> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        mAdapter.setNewData(mDatas);
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
                new Share2.Builder(mActivity)
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
                ClipboardUtil.putText(mContext, model.share());
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
                        refrashListView(datas);
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
        showDialog(mContext, item);
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
            showDialog(mContext, item);
        }
    }

    @Override
    public void onDataEmptyClick() {

    }

    @Override
    public void onDataThrowableClick() {

    }
}