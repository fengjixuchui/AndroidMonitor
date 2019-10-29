package com.amlzq.android.monitor;

import com.amlzq.android.app.BaseFragment;
import com.amlzq.android.util.Share2;
import com.amlzq.android.util.ShareContentType;

/**
 * 定制BaseFragment
 */

public abstract class MyBaseFragment extends BaseFragment {

    public void goShare(String title, String url) {
        new Share2.Builder(mActivity)
                .setContentType(ShareContentType.TEXT)
                .setTextContent(url)
                .setTitle(title)
                .build()
                .shareBySystem();
    }

}
