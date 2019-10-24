package com.amlzq.android.monitor.screen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amlzq.android.monitor.BaseRecyclerViewAdapter;
import com.amlzq.android.monitor.R;
import com.amlzq.android.monitor.data.model.ScreenInfo;

/**
 * 屏幕
 */

public class ScreenAdapter extends BaseRecyclerViewAdapter<ScreenInfo, ScreenAdapter.ViewHolder> {

    public ScreenAdapter() {
        super();
    }

    @Override
    public ScreenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutResId = -1;
        if (viewType == ScreenInfo.TEXT) {
            layoutResId = R.layout.item_screen;
        } else {
            layoutResId = R.layout.item_image;
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutResId, parent, false);
        return new ScreenAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ScreenAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mView.setOnClickListener(v -> mItemClickListener.onItemClick(null, v,
                holder.getAdapterPosition(),
                holder.getItemId()));
        switch (holder.getItemViewType()) {
            case ScreenInfo.TEXT:
//                holder.setText(R.id.id, item.id);
//                holder.setText(R.id.content, item.content);
                break;
            case ScreenInfo.IMAGE:
//                holder.setText(R.id.id, item.id);
//                holder.setImageResource(R.id.icon,
//                        RUtil.drawable(item.content));
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mIconView;
        public ScreenInfo mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mIconView = (ImageView) view.findViewById(R.id.icon);
        }
    }

}
