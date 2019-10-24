package com.amlzq.android.monitor.apps;

import android.app.SearchManager;
import android.app.Service;
import android.content.ComponentName;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;

import com.amlzq.android.monitor.MyBaseAppCompatActivity;
import com.amlzq.android.monitor.R;

public class SearchAppActivity extends MyBaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_app);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SearchManager searchManager = (SearchManager) getSystemService(Service.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(mContext, SearchAppActivity.class)));
        return true;
    }

    @Override
    public void onFragmentInteraction(Bundle bundle) {

    }

}