package com.amlzq.android.monitor;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.amlzq.android.app.BaseAppCompatActivity;
import com.amlzq.android.app.BaseFragment;
import com.amlzq.android.log.Log;
import com.amlzq.android.monitor.aboutus.AboutUsFragment;
import com.amlzq.android.monitor.apps.AppDetailFragment;
import com.amlzq.android.monitor.data.model.AppInfo;
import com.amlzq.android.util.FragmentUtil;

/**
 * 详情页面，二级页
 * 是容器
 */

public class ActionActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startFragment(mTargetFragmentTag);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);

    }

    @Override
    protected BaseFragment fragmentProvider(String fragmentTag, String... args) {
        Log.d("args:" + fragmentTag);

        BaseFragment fragment;
        if (AboutUsFragment.TAG.equals(mTargetFragmentTag)) {
            fragment = AboutUsFragment.newInstance();
            return fragment;

        } else if (AppDetailFragment.TAG.endsWith(mTargetFragmentTag)) {
            AppInfo model = mBundle.getParcelable(FragmentUtil.PARAMETER);
            fragment = AppDetailFragment.newInstance(model);
            return fragment;

        } else {
            return super.fragmentProvider(fragmentTag, args);
        }
    }

    @Override
    public void onFragmentInteraction(Bundle args) {
//        Log.d("args:" + Arrays.toString(args));
//        String tag = args[0];
//
//        mTargetFragmentTag = args[0];
//        if (AboutUsFragment.TAG.equals(mTargetFragmentTag)) {
//
//        } else if (AppDetailFragment.TAG.endsWith(mTargetFragmentTag)) {
//
//        }
    }

}