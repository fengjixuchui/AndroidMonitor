package com.amlzq.android.monitor.screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.amlzq.android.content.ContextHolder;
import com.amlzq.android.log.Log;
import com.amlzq.android.monitor.MyBaseRecycleViewFragment;
import com.amlzq.android.monitor.R;
import com.amlzq.android.monitor.data.model.ScreenInfo;
import com.amlzq.android.schedulers.AppExecutors;
import com.amlzq.android.util.ClipboardUtil;
import com.amlzq.android.util.DisplayUtil;
import com.amlzq.android.util.Share2;
import com.amlzq.android.util.ShareContentType;
import com.amlzq.android.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 屏幕
 */

public class ScreenFragment
        extends MyBaseRecycleViewFragment<ScreenInfo, ScreenAdapter.ViewHolder> {

    public static final String TAG = "ScreenFragment";

    private ScreenAdapter mAdapter;

    public ScreenFragment() {
        // Required empty public constructor
    }

    public static ScreenFragment newInstance() {
        ScreenFragment fragment = new ScreenFragment();
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
//        return inflater.inflate(R.layout.fragment_screen, container, false);
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_screen;
    }

    @Override
    protected void afterCreate(Bundle bundle) {
        super.afterCreate(bundle);

        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        setActivityTitle(R.string.title_screen);
    }

    @Override
    protected RecyclerView.Adapter<ScreenAdapter.ViewHolder> getAdapter() {
        mAdapter = new ScreenAdapter();
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

    protected void onSwipeRefreshListener() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        StringBuilder sb = new StringBuilder();
        for (ScreenInfo model : mAdapter.getData()) {
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
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onTitleChanged(CharSequence charSequence, int ResId) {

    }

    public void refrashListView(List<ScreenInfo> datas) {
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

    private void showDialog(final Context context, ScreenInfo model) {
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

    public void loadData(boolean forceUpdate, final boolean showLoadingUI, Activity activity) {
        if (showLoadingUI) setLoadingIndicator(true);
        // if (forceUpdate) mRepository.refreshData();

        AppExecutors appExecutors = new AppExecutors();
        appExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                List<ScreenInfo> datas = new ArrayList<ScreenInfo>();
                Context cxt = ContextHolder.getContext();

                // 显示指标
                DisplayMetrics dm = cxt.getResources().getDisplayMetrics();

                datas.add(new ScreenInfo(cxt.getString(R.string.drawable_path), "display", ScreenInfo.IMAGE));
                datas.add(new ScreenInfo(cxt.getString(R.string.screen_orientation),
                        DisplayUtil.getOrientationName(activity.getResources().getConfiguration().orientation)));

                datas.add(new ScreenInfo(cxt.getString(R.string.screen_dpi), dm.xdpi + "x" + dm.ydpi));
                datas.add(new ScreenInfo(cxt.getString(R.string.screen_pixels), dm.widthPixels + "x" + dm.heightPixels));
                datas.add(new ScreenInfo(cxt.getString(R.string.screen_density), "" + dm.density));
                datas.add(new ScreenInfo(cxt.getString(R.string.screen_density_dpi), "" + dm.densityDpi));
                datas.add(new ScreenInfo(cxt.getString(R.string.screen_scaled_density), "" + dm.scaledDensity));
                datas.add(new ScreenInfo(cxt.getString(R.string.density_default), "" + DisplayMetrics.DENSITY_DEFAULT));

                // 屏幕的默认分辨率
                Display dp = activity.getWindowManager().getDefaultDisplay();
                datas.add(new ScreenInfo(cxt.getString(R.string.display_size), dp.getWidth() + "x" + dp.getHeight()));
                datas.add(new ScreenInfo(cxt.getString(R.string.display_size_default), "" + Display.DEFAULT_DISPLAY));

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
        loadData(true, true, mActivity);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ScreenInfo item = mAdapter.getItem(position);
        showDialog(mContext, item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemChildClick(RecyclerView.Adapter adapter, View view, int position) {
        ScreenInfo item = mAdapter.getItem(position);
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