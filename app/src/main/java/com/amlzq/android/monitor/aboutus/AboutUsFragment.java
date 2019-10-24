package com.amlzq.android.monitor.aboutus;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.amlzq.android.monitor.MyBaseFragment;
import com.amlzq.android.monitor.R;

/**
 * 关于我们
 */

public class AboutUsFragment extends MyBaseFragment {

    public static final String TAG = "AboutUsFragment";

    private WebView mWebView;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AboutUsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_about_us, container, false);
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_about_us;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        FrameLayout fl = findViewById(R.id.fl);
    }

}