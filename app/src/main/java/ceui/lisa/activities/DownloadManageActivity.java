package ceui.lisa.activities;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ceui.lisa.R;
import ceui.lisa.database.AppDatabase;
import ceui.lisa.fragments.BaseFragment;
import ceui.lisa.fragments.FragmentDownload;
import ceui.lisa.fragments.FragmentDownloadFinish;
import ceui.lisa.fragments.FragmentHasDownload;
import ceui.lisa.fragments.FragmentNowDownload;
import ceui.lisa.utils.Common;

public class DownloadManageActivity extends BaseActivity {

    private static final String[] CHINESE_TITLES = new String[]{"正在下载", "已完成"};
    private BaseFragment[] allPages = new BaseFragment[]{new FragmentNowDownload(), new FragmentDownloadFinish()};
    private ViewPager mViewPager;

    @Override
    protected void initLayout() {
        mLayoutID = R.layout.activity_download_manage;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("下载管理");
        toolbar.setNavigationOnClickListener(v -> finish());
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return allPages[i];
            }

            @Override
            public int getCount() {
                return CHINESE_TITLES.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return CHINESE_TITLES[position];
            }


        });
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mViewPager != null){
            if(mViewPager.getCurrentItem() == 0){
                return false;
            }else {
                getMenuInflater().inflate(R.menu.delete_all, menu);
                return true;
            }
        }else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_delete){
            AppDatabase.getAppDatabase(mContext).downloadDao().deleteAll();
            Common.showToast("下载记录清除成功");
            if(allPages[1] instanceof FragmentDownloadFinish) {
                ((FragmentDownloadFinish) allPages[1]).getFirstData();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
