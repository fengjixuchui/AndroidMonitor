package com.amlzq.android.monitor;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * 因为是个人项目所以使用非广泛使用的API
 * 商业项目多人协作建议使用https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 *
 * @param <M>  数据模型
 * @param <VH> 视图持有者
 */
public abstract class BaseRecyclerViewAdapter<M, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected List<M> mValues;
    protected RecyclerView mRecyclerView;
    protected AdapterView.OnItemClickListener mItemClickListener;
    protected AdapterView.OnItemLongClickListener mItemLongClickListener;
    protected OnItemChildClickListener mItemChildClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener itemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener itemChildClickListener) {
        this.mItemChildClickListener = itemChildClickListener;
    }

    public BaseRecyclerViewAdapter() {
        mValues = new ArrayList<>();
    }

    public void onAttachedToRecyclerView(RecyclerView view) {
        mRecyclerView = view;
    }

    public void addData(List<M> items) {
        mValues.addAll(items);
    }

    public List<M> getData() {
        return mValues;
    }

    public void setNewData(List<M> items) {
        mValues.clear();
        mValues.addAll(items);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

    }

    public M getItem(int position) {
        return mValues.get(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}
