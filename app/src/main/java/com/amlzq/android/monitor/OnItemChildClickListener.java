package com.amlzq.android.monitor;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public interface OnItemChildClickListener {

    void onItemChildClick(RecyclerView.Adapter adapter, View view, int position);
}
