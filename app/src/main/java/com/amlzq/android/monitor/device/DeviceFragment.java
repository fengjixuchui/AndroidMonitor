package com.amlzq.android.monitor.device;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
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
import com.amlzq.android.schedulers.AppExecutors;
import com.amlzq.android.util.ClipboardUtil;
import com.amlzq.android.util.DeviceUtil;
import com.amlzq.android.util.PermissionChecker;
import com.amlzq.android.util.Share2;
import com.amlzq.android.util.ShareContentType;
import com.amlzq.android.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 设备
 */

public class DeviceFragment
        extends MyBaseRecycleViewFragment<CommonInfo, DeviceAdapter.ViewHolder>
        implements EasyPermissions.PermissionCallbacks {

    public static final String TAG = "DeviceFragment";

    private DeviceAdapter mAdapter;

    public DeviceFragment() {
        // Required empty public constructor
    }

    public static DeviceFragment newInstance() {
        DeviceFragment fragment = new DeviceFragment();
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
//        return inflater.inflate(R.layout.fragment_device_list, container, false);
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_device_list;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        setActivityTitle(R.string.title_device);
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
    public void onTitleChanged(CharSequence charSequence, int resId) {

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

        if (getString(R.string.granted_permission).endsWith(item.content)) {

            if (getString(R.string.device_id).endsWith(item.id)
                    || getString(R.string.subscriber_id).endsWith(item.id)
                    || getString(R.string.sim_serial_number).endsWith(item.id)) {
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

    public void loadData(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) setLoadingIndicator(true);
        // if (forceUpdate) mRepository.refreshData();

        AppExecutors appExecutors = new AppExecutors();
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<CommonInfo> datas = new ArrayList<CommonInfo>();
                Context cxt = ContextHolder.getContext();

                datas.add(new CommonInfo(cxt.getString(R.string.manufacturer), Build.MANUFACTURER, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.brand), Build.BRAND, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.model), Build.MODEL, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.board), Build.BOARD, ""));

                datas.add(new CommonInfo(cxt.getString(R.string.device_id),
                        StringUtil.get(DeviceUtil.getDeviceId(),
                                cxt.getString(R.string.granted_permission)), ""));

                datas.add(new CommonInfo(cxt.getString(R.string.gsf_id),
                        StringUtil.get(DeviceUtil.getGSFAndroidId(),
                                cxt.getString(R.string.granted_permission)), ""));

                datas.add(new CommonInfo(cxt.getString(R.string.subscriber_id),
                        StringUtil.get(DeviceUtil.getSubscriberId(),
                                cxt.getString(R.string.granted_permission)), ""));

                datas.add(new CommonInfo(cxt.getString(R.string.sim_serial_number),
                        StringUtil.get(DeviceUtil.getSimSerialNumber(),
                                cxt.getString(R.string.granted_permission)), ""));

                datas.add(new CommonInfo(cxt.getString(R.string.build_hardware), Build.HARDWARE, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.build_radio), Build.RADIO, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.build_serial), Build.SERIAL, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.build_display), Build.DISPLAY, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.bootloader), Build.BOOTLOADER, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.fingerprint), Build.FINGERPRINT, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.build_id), Build.ID, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.build_user), Build.USER, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.build_host), Build.HOST, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.build_time), "" + Build.TIME, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.build_tags), Build.TAGS, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.build_type), Build.TYPE, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.product), Build.PRODUCT, ""));
                datas.add(new CommonInfo(cxt.getString(R.string.build_device), Build.DEVICE, ""));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    datas.add(new CommonInfo(cxt.getString(R.string.supported_abis), "" + Arrays.toString(Build.SUPPORTED_ABIS), ""));
                    datas.add(new CommonInfo(cxt.getString(R.string.supported_32_bit_abis), "" + Arrays.toString(Build.SUPPORTED_32_BIT_ABIS), ""));
                    datas.add(new CommonInfo(cxt.getString(R.string.supported_64_bit_abis), "" + Arrays.toString(Build.SUPPORTED_64_BIT_ABIS), ""));
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

}