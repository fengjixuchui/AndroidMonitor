package com.amlzq.android.monitor;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amlzq.android.monitor.apps.AppDetailFragment;
import com.amlzq.android.monitor.data.model.AppInfo;
import com.amlzq.android.ui.BaseContainerDialogFragment;
import com.amlzq.android.util.ActivityUtil;
import com.amlzq.android.util.FragmentUtil;

/**
 * 详情页面，二级页
 * 是容器
 */

public class ActionDialogFragment extends BaseContainerDialogFragment {

    public static final String TAG = "ActionDialogFragment";

    private ImageButton mIBBack;
    private TextView mTVTitle;
    private FrameLayout mFLMenu;

    public ActionDialogFragment() {
        // Required empty public constructor
    }

    public static ActionDialogFragment newInstance(Bundle args) {
        ActionDialogFragment fragment = new ActionDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTargetFragmentTag = getArguments().getString(ActivityUtil.FRAGMENT_TAG);
            mTitle = getArguments().getString(ActivityUtil.TITLE);
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_action, container, false);
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_action;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

        setCancelable(true);

        mIBBack = getView().findViewById(R.id.ib_back);
        mIBBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mTVTitle = getView().findViewById(R.id.tv_title);
        mTVTitle.setText(mTitle);

        startFragment(AppDetailFragment.TAG);

//        AppInfo model = getArguments().getParcelable(FragmentUtil.PARAMETER);
//        AppDetailFragment fragment = AppDetailFragment.newInstance(model);
    }

}