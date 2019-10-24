package com.amlzq.android.monitor;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public interface OnItemChildClickListener {

    void onItemChildClick(RecyclerView.Adapter adapter, View view, int position);
}
