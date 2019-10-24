package com.amlzq.android.monitor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amlzq.android.app.BaseAppCompatActivity;
import com.amlzq.android.app.BaseFragment;
import com.amlzq.android.monitor.aboutus.AboutUsFragment;
import com.amlzq.android.monitor.apps.AppsFragment;
import com.amlzq.android.monitor.device.DeviceFragment;
import com.amlzq.android.monitor.features.FeaturesFragment;
import com.amlzq.android.monitor.network.NetworkFragment;
import com.amlzq.android.monitor.os.OSFragment;
import com.amlzq.android.monitor.screen.ScreenFragment;
import com.amlzq.android.monitor.setting.SettingsActivity;
import com.amlzq.android.util.ActivityUtil;
import com.amlzq.android.util.AppUtil;
import com.amlzq.android.util.FragmentUtil;
import com.amlzq.android.util.Share2;
import com.amlzq.android.util.ShareContentType;

public class MainActivity
        extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        View headerView = mNavigationView.getHeaderView(0);
        TextView mTVName = headerView.findViewById(R.id.tv_id);
        TextView mTVDesc = headerView.findViewById(R.id.tv_desc);
        mTVName.setText(Build.MANUFACTURER);
        mTVDesc.setText(Build.MODEL);

        // 展开
        mDrawer.openDrawer(GravityCompat.START);
        mNavigationView.getMenu().getItem(0).setChecked(true);
        startFragment(FeaturesFragment.TAG);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(mContext, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_features) {
            startFragment(FeaturesFragment.TAG);

        } else if (id == R.id.nav_os) {
            startFragment(OSFragment.TAG);

        } else if (id == R.id.nav_apps) {
            startFragment(AppsFragment.TAG);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_device) {
            startFragment(DeviceFragment.TAG);

        } else if (id == R.id.nav_screen) {
            startFragment(ScreenFragment.TAG);

        } else if (id == R.id.nav_cpu) {

        } else if (id == R.id.nav_storage) {

        } else if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_battery) {

        } else if (id == R.id.nav_network) {
            startFragment(NetworkFragment.TAG);

        } else if (id == R.id.nav_sensors) {

        } else if (id == R.id.nav_share) {

            new Share2.Builder(mContext)
                    .setContentType(ShareContentType.TEXT)
                    // 设置要分享的文本内容
                    .setTextContent("")
                    .setTitle(String.format(getString(R.string.title_share_to), getString(R.string.app_name)))
                    .build()
                    .shareBySystem();

        } else if (id == R.id.nav_about_us) {
            Intent intent = new Intent(this, ActionActivity.class);
            intent.putExtra(ActivityUtil.FRAGMENT_TAG, AboutUsFragment.TAG);
            startActivityFromFragment(AboutUsFragment.newInstance(), intent, 0);

            // startActivity(intent);
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
            return;
        }

        //检查当前Fragment内部是否有待处理的回退逻辑
        if (mTargetFragment != null && mTargetFragment.onBackPressed()) {
            return;
        }

        //检查当前Fragment的自维护的回退栈是否需要回退
        if (FragmentUtil.onBackPressed(getSupportFragmentManager())) {
            return;
        }

        showAbortDialog(this);
    }

    /**
     * 退出事件对话框
     *
     * @param context Context
     */
    private void showAbortDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.tips);
        builder.setMessage(R.string.whether_quit_application);
        builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                AppUtil.exit();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        // builder.show();
        showDialog(builder.create(), null);
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {
        String tag = bundle.getString("");

    }

    @Override
    protected BaseFragment fragmentProvider(String fragmentTag, String... args) {
        if (FeaturesFragment.TAG.equals(fragmentTag)) {

            FeaturesFragment fragment = FeaturesFragment.newInstance();
            return fragment;

        } else if (OSFragment.TAG.equals(fragmentTag)) {

            OSFragment fragment = OSFragment.newInstance();
            return fragment;

        } else if (AppsFragment.TAG.equals(fragmentTag)) {

            AppsFragment fragment = AppsFragment.newInstance();
            return fragment;

        } else if (DeviceFragment.TAG.equals(fragmentTag)) {

            DeviceFragment fragment = DeviceFragment.newInstance();
            return fragment;

        } else if (NetworkFragment.TAG.equals(fragmentTag)) {

            NetworkFragment fragment = NetworkFragment.newInstance();
            return fragment;

        } else if (ScreenFragment.TAG.equals(fragmentTag)) {

            ScreenFragment fragment = ScreenFragment.newInstance();
            return fragment;

        } else {
            return null;
        }
    }

}