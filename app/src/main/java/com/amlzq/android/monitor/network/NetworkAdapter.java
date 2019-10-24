package com.amlzq.android.monitor.network;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amlzq.android.monitor.BaseRecyclerViewAdapter;
import com.amlzq.android.monitor.R;
import com.amlzq.android.monitor.data.model.CommonInfo;

/**
 * 网络
 */

public class NetworkAdapter extends BaseRecyclerViewAdapter<CommonInfo, NetworkAdapter.ViewHolder> {

    public NetworkAdapter() {
        super();
    }

    @Override
    public NetworkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_network, parent, false);
        return new NetworkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NetworkAdapter.ViewHolder holder, int position) {
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
