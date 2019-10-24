package com.amlzq.android.monitor.apps;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.amlzq.android.monitor.MyBaseFragment;
import com.amlzq.android.monitor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用列表
 */

public class AppsFragment extends MyBaseFragment {

    public static final String TAG = "AppsFragment";

    private FloatingActionButton mFaBtn;

    private TabLayout mTLIndicator;
    private List<String> mTabIndicators;

    private ViewPager mVPContent;
    private ContentPagerAdapter mContentAdapter;
    private List<Fragment> mTabFragments;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AppsFragment() {
    }

    // TODO: Customize parameter initialization
    public static AppsFragment newInstance() {
        AppsFragment fragment = new AppsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_apps, container, false);
//        return view;
//    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_apps;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        mTLIndicator = findViewById(R.id.tl_indicator);
        mTabIndicators = new ArrayList<String>();
        mVPContent = findViewById(R.id.vp_content);
        mTabFragments = new ArrayList<Fragment>();
        mFaBtn = (FloatingActionButton) findViewById(R.id.fab);

        // 初始化3个tab
        mTabIndicators.add(getString(R.string.apps_all));
        mTabIndicators.add(getString(R.string.apps_system));
        mTabIndicators.add(getString(R.string.apps_user));

        mTLIndicator.setTabMode(TabLayout.MODE_SCROLLABLE);
        // mTLIndicator.setTabTextColors(getColor(R.color.gray), getColor( R.color.white));
        // mTLIndicator.setSelectedTabIndicatorColor(getColor(R.color.white));
        ViewCompat.setElevation(mTLIndicator, 8);
        mTLIndicator.setupWithViewPager(mVPContent);

        mContentAdapter = new ContentPagerAdapter(getChildFragmentManager());
        mVPContent.setAdapter(mContentAdapter);

        // ViewPager默认加载页面的左右两页，此方法设置屏幕外左右加载页数
        // mVPContent.setOffscreenPageLimit(2);

        for (String id : mTabIndicators) {
            mTLIndicator.addTab(mTLIndicator.newTab());
            mTabFragments.add(AppListFragment.newInstance(id));
        }
        mContentAdapter.notifyDataSetChanged();

        mFaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText etInput = new EditText(mContext);
                etInput.setHint(R.string.input_channel_id);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(R.string.add_tab);
                builder.setView(etInput);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String id = etInput.getText().toString().trim();
                        if (TextUtils.isEmpty(id)) return;

                        mTabIndicators.add(id);
                        mTLIndicator.addTab(mTLIndicator.newTab());
                        mTabFragments.add(AppListFragment.newInstance(id));
                        mContentAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        setActivityTitle(R.string.title_apps);
    }

    @Override
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void onTitleChanged(CharSequence charSequence, int color) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.apps, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                break;
            case R.id.action_search:
                startActivity(new Intent(mContext, SearchAppActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 片段适配器
     */
    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public Fragment getItem(int position) {
            return mTabFragments.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return super.isViewFromObject(view, object);
        }

        @Override
        public int getCount() {
            return mTabFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabIndicators.get(position);
        }

    }

}