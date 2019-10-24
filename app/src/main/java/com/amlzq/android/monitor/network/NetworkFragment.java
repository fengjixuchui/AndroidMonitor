package com.amlzq.android.monitor.network;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.amlzq.android.content.ContextHolder;
import com.amlzq.android.log.Log;
import com.amlzq.android.monitor.MyBaseRecycleViewFragment;
import com.amlzq.android.monitor.OnItemChildClickListener;
import com.amlzq.android.monitor.R;
import com.amlzq.android.monitor.data.model.CommonInfo;
import com.amlzq.android.schedulers.AppExecutors;
import com.amlzq.android.util.ClipboardUtil;
import com.amlzq.android.util.NetworkUtil;
import com.amlzq.android.util.PermissionChecker;
import com.amlzq.android.util.Share2;
import com.amlzq.android.util.ShareContentType;
import com.amlzq.android.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 网络
 */

public class NetworkFragment
        extends MyBaseRecycleViewFragment<CommonInfo, NetworkAdapter.ViewHolder>
        implements EasyPermissions.PermissionCallbacks {

    public static final String TAG = "NetworkFragment";

    private NetworkAdapter mAdapter;

    public NetworkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NetworkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NetworkFragment newInstance() {
        NetworkFragment fragment = new NetworkFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_network_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new NetworkAdapter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_network_list;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);

        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonInfo item = mAdapter.getItem(position);
                showDialog(mContext, item);
            }
        });
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
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
        });
        setActivityTitle(R.string.title_network);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter<NetworkAdapter.ViewHolder> getAdapter() {

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

    public void refrashListView(List<CommonInfo> datas) {
        mDatas.clear();
        mDatas.addAll(datas);
        mAdapter.setNewData(datas);
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
        if (requestCode == PermissionChecker.REQUEST_CODE_PHONE) {

        }
        setLoadingIndicator(true);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

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
                datas.add(new CommonInfo(cxt.getString(R.string.local_ip_address_v4),
                        StringUtil.get(NetworkUtil.getIPAddress(NetworkUtil.IPv4),
                                cxt.getString(R.string.not_found))));

                datas.add(new CommonInfo(cxt.getString(R.string.local_ip_address_v6),
                        NetworkUtil.getIPAddress(NetworkUtil.IPv6)));

                datas.add(new CommonInfo(cxt.getString(R.string.wifi_mac_address),
                        NetworkUtil.getWiFiAddress()));

                datas.add(new CommonInfo(cxt.getString(R.string.bluetooth_mac_address),
                        ""));

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

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemChildClick(RecyclerView.Adapter adapter, View view, int position) {

    }

    @Override
    public void onDataEmptyClick() {

    }

    @Override
    public void onDataThrowableClick() {

    }
}