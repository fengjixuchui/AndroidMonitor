package com.amlzq.android.monitor.apps;

import android.os.Bundle;

import com.amlzq.android.monitor.MyBaseFragment;
import com.amlzq.android.monitor.R;
import com.amlzq.android.monitor.data.model.AppInfo;
import com.amlzq.android.util.FragmentUtil;

/**
 * 应用详情
 */

public class AppDetailFragment extends MyBaseFragment {
    public static final String TAG = "AppDetailFragment";

    private AppInfo mData;

    public AppDetailFragment() {
        // Required empty public constructor
    }

    public static AppDetailFragment newInstance(AppInfo model) {
        AppDetailFragment fragment = new AppDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(FragmentUtil.PARAMETER, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mData = getArguments().getParcelable(FragmentUtil.PARAMETER);
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_app_detail, container, false);
//    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_app_detail;
    }

    @Override
    protected void afterCreate(Bundle bundle) {

    }

}