package com.amlzq.android.monitor.os;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.amlzq.android.content.ContextHolder;
import com.amlzq.android.log.Log;
import com.amlzq.android.monitor.MyBaseRecycleViewFragment;
import com.amlzq.android.monitor.R;
import com.amlzq.android.monitor.data.model.CommonInfo;
import com.amlzq.android.schedulers.AppExecutors;
import com.amlzq.android.util.ClipboardUtil;
import com.amlzq.android.util.Share2;
import com.amlzq.android.util.ShareContentType;
import com.amlzq.android.util.StringUtil;
import com.amlzq.android.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 系统
 */

public class OSFragment extends MyBaseRecycleViewFragment<CommonInfo, OSAdapter.ViewHolder>
        implements EasyPermissions.PermissionCallbacks {

    public static final String TAG = "OSFragment";

    private OSAdapter mAdapter;

    public OSFragment() {
        // Required empty public constructor
    }

    public static OSFragment newInstance() {
        OSFragment fragment = new OSFragment();
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
//        return inflater.inflate(R.layout.fragment_os, container, false);
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_os;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        setActivityTitle(R.string.title_os);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter<OSAdapter.ViewHolder> getAdapter() {
        if (mAdapter == null) mAdapter = new OSAdapter();
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

    public void refrashListView(List<CommonInfo> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        mAdapter.setNewData(datas);
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

    public boolean isActive() {
        return isAdded();
    }

    public void setLoadingIndicator(boolean active) {
        mSwipeRefresh.setRefreshing(active);
    }

    public void showLoadingError(String message) {
        if (StringUtil.notEmpty(message)) showToastShort(message);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

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
                        .setTitle(String.format(getString(R.string.title_share_to), model.id))
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
        appExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                List<CommonInfo> datas = new ArrayList<CommonInfo>();
                Context cxt = ContextHolder.getContext();

                // 需要在UI线程才能获取到值
                datas.add(new CommonInfo(cxt.getString(R.string.build_version_sdk_int),
                        StringUtil.get("" + Build.VERSION.SDK_INT,
                                cxt.getString(R.string.not_found))));

                datas.add(new CommonInfo(cxt.getString(R.string.build_version_code_name),
                        Build.VERSION.CODENAME));

                datas.add(new CommonInfo(cxt.getString(R.string.build_version_incremental),
                        Build.VERSION.INCREMENTAL));

                datas.add(new CommonInfo(cxt.getString(R.string.build_version_release),
                        Build.VERSION.RELEASE));

                if (SystemUtil.has16()) {
                    datas.add(new CommonInfo(cxt.getString(R.string.build_version_security_patch),
                            Build.VERSION.SECURITY_PATCH));

                    datas.add(new CommonInfo(cxt.getString(R.string.build_version_preview_sdk_int),
                            "" + Build.VERSION.PREVIEW_SDK_INT));
                }

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

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommonInfo item = mAdapter.getItem(position);
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

//                if (getString(R.string.granted_permission).endsWith(item.content)) {
//
//                    if (getString(R.string.local_ip_address_v4).endsWith(item.id)
//                            || getString(R.string.wifi_mac_address).endsWith(item.id)
//                            || getString(R.string.bluetooth_mac_address).endsWith(item.id)) {
//                        askPhonePermission(item);
//                    }
//
//                } else {
//                    showDialog(mContext, item);
//                }
    }

    @Override
    public void onDataEmptyClick() {

    }

    @Override
    public void onDataThrowableClick() {

    }

}