package com.dision.android.rtlviewpager.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.dision.android.rtlviewpager.DisionApp;
import com.dision.android.rtlviewpager.R;
import com.dision.android.rtlviewpager.adapters.TabsAdapter;
import com.dision.android.rtlviewpager.fragments.NewsFragment;
import com.dision.android.rtlviewpager.models.Tab;
import com.dision.android.rtlviewpager.rest.model.Feed;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    // constants
    public static final String BASIC_TAG = MainActivity.class.getName();

    private static final int TAB_NEWS = 1;
    private static final int TAB_PHOTOS = 2;
    private static final int TAB_VIDEOS = 3;

    // variables
    private TabsAdapter mTabsAdapter;
    private Tab[] mTabs;
    private Feed mFeed;

    // UI variables
    @Bind(R.id.toolbar_activity_main)
    Toolbar toolbar;
    @Bind(R.id.tl_activity_main)
    TabLayout tl;
    @Bind(R.id.vp_activity_main)
    ViewPager vp;

    // methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setToolbarUiSettings();
        getRssFeed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();

                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }



    private void initTabs() {
        mTabs = new Tab[] {
                new Tab(TAB_NEWS, getString(R.string.tab_news)) {

                    @Override
                    public Fragment getFragment() {
                        return NewsFragment.getInstance(TAB_NEWS, mFeed);
                    }
                },
                new Tab(TAB_PHOTOS, getString(R.string.tab_photos)) {

                    @Override
                    public Fragment getFragment() {
                        return NewsFragment.getInstance(TAB_PHOTOS, mFeed);
                    }

                },
                new Tab(TAB_VIDEOS, getString(R.string.tab_videos)) {

                    @Override
                    public Fragment getFragment() {
                        return NewsFragment.getInstance(TAB_VIDEOS, mFeed);
                    }

                }
        };
    }

    private void setToolbarUiSettings() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setAdapters() {
        // initialize adapter
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager(), mTabs, true);
        // set adapter to ViewPager
        vp.setAdapter(mTabsAdapter);
        // set ViewPager to TabLayout
        tl.setupWithViewPager(vp);
        // if RTL orientation, go to last item
        vp.setCurrentItem(vp.getAdapter().getCount() - 1, false);
    }

    private void getRssFeed() {
        DisionApp.getRestClient().getAppService().getRssFeed(new Callback<Feed>() {
            @Override
            public void success(Feed apiResponse, Response response) {
                Log.d("Rss feed success", "News count " + apiResponse.getChannel().getFeedItems().size());

                mFeed = apiResponse;

                initTabs();
                setAdapters();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
