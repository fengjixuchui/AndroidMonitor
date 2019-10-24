package com.amlzq.android.monitor.apps;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amlzq.android.monitor.BaseRecyclerViewAdapter;
import com.amlzq.android.monitor.R;
import com.amlzq.android.monitor.data.model.AppInfo;

public class AppListAdapter extends BaseRecyclerViewAdapter<AppInfo, AppListAdapter.ViewHolder> {

    public AppListAdapter() {
        super();
    }

    @Override
    public AppListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);
        return new AppListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AppListAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mView.setOnClickListener(v -> mItemClickListener.onItemClick(null, v,
                holder.getAdapterPosition(),
                holder.getItemId()));

        holder.mImgIcon.setImageDrawable(holder.mItem.icon);
        holder.mTVId.setText(holder.mItem.label);
        holder.mTVContent.setText(holder.mItem.packageName);
        holder.mTVDesc.setText(holder.mItem.signatureByMD5);

        holder.mView.findViewById(R.id.ib_action).setOnClickListener(v -> {
            mItemChildClickListener.onItemChildClick(this, v, holder.getAdapterPosition());
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImgIcon;
        public final TextView mTVId;
        public final TextView mTVContent;
        public final TextView mTVDesc;
        public final ImageButton mBtnAction;
        public AppInfo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImgIcon = (ImageView) view.findViewById(R.id.img_icon);
            mTVId = (TextView) view.findViewById(R.id.tv_id);
            mTVContent = (TextView) view.findViewById(R.id.tv_content);
            mTVDesc = (TextView) view.findViewById(R.id.tv_desc);
            mBtnAction = (ImageButton) view.findViewById(R.id.ib_action);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.toString() + "'";
        }
    }

}
