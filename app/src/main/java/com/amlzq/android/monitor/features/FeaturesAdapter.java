package com.amlzq.android.monitor.features;

import androidx.recyclerview.widget.RecyclerView;
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

public class FeaturesAdapter extends BaseRecyclerViewAdapter<CommonInfo, FeaturesAdapter.ViewHolder> {

    public FeaturesAdapter() {
        super();
    }

    @Override
    public FeaturesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feature, parent, false);
        return new FeaturesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FeaturesAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mView.setOnClickListener(v -> mItemClickListener.onItemClick(null, v,
                holder.getAdapterPosition(),
                holder.getItemId()));

        holder.mIdView.setText(holder.mItem.id);
        holder.mContentView.setText(holder.mItem.content);

        holder.mView.findViewById(R.id.ib_action).setOnClickListener(v -> {
            mItemChildClickListener.onItemChildClick(this, v, holder.getAdapterPosition());
        });
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
