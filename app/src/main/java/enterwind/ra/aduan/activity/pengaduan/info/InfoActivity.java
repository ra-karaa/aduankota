package enterwind.ra.aduan.activity.pengaduan.info;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.activity.BaseActivity;
import enterwind.ra.aduan.activity.pengaduan.info.fragment.AgendaFragment;
import enterwind.ra.aduan.activity.pengaduan.info.fragment.BeritaFragment;

public class InfoActivity extends BaseActivity {

    private static final int ACTIVITY_NUM = 1;
    private Context mContext = InfoActivity.this;
    private static final String TAG = InfoActivity.class.getSimpleName();

    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        setupBottomNavigationView(mContext, ACTIVITY_NUM);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BeritaFragment(), "Berita");
        adapter.addFragment(new AgendaFragment(), "Agenda");
        viewPager.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 6) {
                return null;
            }
            return mFragmentTitleList.get(position);
        }
    }

}
