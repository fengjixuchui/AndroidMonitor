package com.amlzq.android.monitor.device;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amlzq.android.monitor.BaseRecyclerViewAdapter;
import com.amlzq.android.monitor.R;
import com.amlzq.android.monitor.data.model.CommonInfo;

/**
 * 设备
 */

public class DeviceAdapter extends BaseRecyclerViewAdapter<CommonInfo, DeviceAdapter.ViewHolder> {

    public DeviceAdapter() {
        super();
    }

    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device, parent, false);
        return new DeviceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DeviceAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mView.setOnClickListener(v -> mItemClickListener.onItemClick(null, v,
                holder.getAdapterPosition(),
                holder.getItemId()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public CommonInfo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }

}
