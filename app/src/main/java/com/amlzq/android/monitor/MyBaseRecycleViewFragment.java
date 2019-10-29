package com.amlzq.android.monitor;

import android.widget.AdapterView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public abstract class MyBaseRecycleViewFragment
        extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        OnItemChildClickListener {

}
