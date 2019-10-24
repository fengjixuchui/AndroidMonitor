package com.amlzq.android.monitor;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.amlzq.android.ui.BaseRecycleViewFragment;

import java.util.ArrayList;
import java.util.List;

public abstract class MyBaseRecycleViewFragment<M, VH extends RecyclerView.ViewHolder>
        extends BaseRecycleViewFragment<M, VH>
        implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        OnItemChildClickListener {

    protected SwipeRefreshLayout mSwipeRefresh;

    /**
     * 下一个请求数据的页码
     */
    protected int mNextRequestPage = 1;
    protected RecyclerView mRecyclerView;
    // protected RecyclerView.Adapter<H> mAdapter;
    protected List<M> mDatas = new ArrayList<M>();

    protected View mVEmpty;
    protected View mVThrowable;
    protected View mVLoad;

}
