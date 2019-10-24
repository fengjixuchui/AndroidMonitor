package com.amlzq.android.monitor.apps;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.amlzq.android.monitor.ActionActivity;
import com.amlzq.android.monitor.MyApplication;
import com.amlzq.android.monitor.MyBaseRecycleViewFragment;
import com.amlzq.android.monitor.OnItemChildClickListener;
import com.amlzq.android.monitor.R;
import com.amlzq.android.monitor.data.model.AppInfo;
import com.amlzq.android.schedulers.AppExecutors;
import com.amlzq.android.util.ActivityUtil;
import com.amlzq.android.util.FragmentUtil;
import com.amlzq.android.util.Share2;
import com.amlzq.android.util.ShareContentType;
import com.amlzq.android.util.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 应用列表
 */

public class AppListFragment extends MyBaseRecycleViewFragment<AppInfo, AppListAdapter.ViewHolder> {

    private String mTab;
    private TextView mTVCount;
    private AppListAdapter mAdapter;

    public AppListFragment() {
        // Required empty public constructor
    }

    public static AppListFragment newInstance(String tab) {
        AppListFragment fragment = new AppListFragment();
        Bundle args = new Bundle();
        args.putString(FragmentUtil.PARAMETER, tab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTab = getArguments().getString(FragmentUtil.PARAMETER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_list, container, false);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_app_list;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);

//        if (mDatas.isEmpty()) {
//            loadData(false, true);
//        }

        mAdapter = new AppListAdapter();
//        mAdapter.addHeaderView(getHeaderView(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                TextView tv = (TextView) v;
//                showToastShort(tv.getText().toString());
//            }
//        }));
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo model = mAdapter.getItem(position);

                Bundle bundle = new Bundle();
                bundle.putString(ActivityUtil.TITLE, "应用信息");
                bundle.putString(ActivityUtil.FRAGMENT_TAG, AppDetailFragment.TAG);
                bundle.putParcelable(FragmentUtil.PARAMETER, model);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(mContext, ActionActivity.class);
                startActivity(intent);

                // showDialog(view.getContext(), mAdapter.getItem(position));
            }
        });
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(RecyclerView.Adapter adapter, View view, int position) {
                AppInfo item = mAdapter.getItem(position);
                if (item.intent == null) {
                    showToastShort("unable launch app");
                } else {
                    mContext.startActivity(item.intent);
                }
            }
        });
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return null;
    }

    @Override
    protected RecyclerView.ItemAnimator getItemAnimator() {
        return null;
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
    public void onTitleChanged(CharSequence charSequence, int color) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemChildClick(RecyclerView.Adapter adapter, View view, int position) {

    }

    public void refrashListView(List<AppInfo> datas) {
        mAdapter.setNewData(mDatas);
        mTVCount.setText(String.format(getString(R.string.apps_total), mDatas.size()));
    }

    private View getHeaderView(View.OnClickListener clickListener) {
        View view = getLayoutInflater().inflate(R.layout.item_header,
                (ViewGroup) mRecyclerView.getParent(), false);
        mTVCount = (TextView) view;
        view.setOnClickListener(clickListener);
        return view;
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

    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (showLoadingUI) setLoadingIndicator(true);
        // if (forceUpdate) mRepository.refreshData();

        AppExecutors appExecutors = new AppExecutors();
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final ArrayList<AppInfo> datas = new ArrayList<AppInfo>();

                if (((MyApplication) getApplication()).mAllApps != null) {
                    datas.addAll(((MyApplication) getApplication()).mAllApps);
                }
                if (forceUpdate || datas.isEmpty()) {
                    datas.clear();
                    // 获取新数据
                    datas.addAll(((MyApplication) getApplication()).getInstalledPackages());
                }

                int targetType = 0;
                if (getString(R.string.apps_user).equals(mTab)) {
                    targetType = AppInfo.USER;
                } else if (getString(R.string.apps_system).equals(mTab)) {
                    targetType = AppInfo.SYSTEM;
                } else if (getString(R.string.apps_all).endsWith(mTab)) {

                } else {
                    // 渠道标识
                    // 过滤数据，遍历删除
                    Iterator<AppInfo> iterator = datas.iterator();
                    while (iterator.hasNext()) {
                        AppInfo model = iterator.next();
                        if (!model.packageName.contains(mTab)) iterator.remove();//使用迭代器的删除方法删除
                    }
                }

                if (targetType != 0) {
                    // 过滤数据，遍历删除
                    Iterator<AppInfo> iterator = datas.iterator();
                    while (iterator.hasNext()) {
                        AppInfo model = iterator.next();
                        if (model.type != targetType) iterator.remove();//使用迭代器的删除方法删除
                    }
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

    private void showDialog(final Context context, AppInfo model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(model.label);
        builder.setMessage(model.toString());
        builder.setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                new Share2.Builder(mActivity)
                        .setContentType(ShareContentType.TEXT)
                        .setTextContent("")
                        .setTitle(model.label)
                        // .forcedUseSystemChooser(false)
                        .build()
                        .shareBySystem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onDataEmptyClick() {

    }

    @Override
    public void onDataThrowableClick() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
}