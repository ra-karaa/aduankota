package enterwind.ra.aduan.activity.pengaduan;

import android.content.Context;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;
import com.viewpagerindicator.CirclePageIndicator;

import butterknife.ButterKnife;
import enterwind.ra.aduan.R;
import enterwind.ra.aduan.activity.BaseActivity;
import enterwind.ra.aduan.activity.PagerFragment;
import enterwind.ra.aduan.adapter.PagerFragmentAdapter;

public class TutorialActivity extends BaseActivity {

    private static final int ACTIVITY_NUM = 3;
    private Context mContext = TutorialActivity.this;

    private PagerFragmentAdapter mSimpleFragmentAdapter;
    private ViewPager mViewPager;
    private ExtensiblePageIndicator extensiblePageIndicator;

    @Override
    protected void onCreate(Bundle tes){
        super.onCreate(tes);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.bind(this);
        setupBottomNavigationView(mContext, ACTIVITY_NUM);

        extensiblePageIndicator = (ExtensiblePageIndicator) findViewById(R.id.flexibleIndicator);
        mSimpleFragmentAdapter = new PagerFragmentAdapter(getSupportFragmentManager());

        mSimpleFragmentAdapter.addFragment(PagerFragment.newInstance(R.color.colorPrimary, R.drawable.ic_login));
        mSimpleFragmentAdapter.addFragment(PagerFragment.newInstance(R.color.colorPrimary, R.drawable.ic_register));
        mSimpleFragmentAdapter.addFragment(PagerFragment.newInstance(R.color.colorPrimary, R.drawable.ic_dahsboard));
        mSimpleFragmentAdapter.addFragment(PagerFragment.newInstance(R.color.colorPrimary, R.drawable.ic_pengaduan));
        mSimpleFragmentAdapter.addFragment(PagerFragment.newInstance(R.color.colorPrimary, R.drawable.ic_buataduan));
        mSimpleFragmentAdapter.addFragment(PagerFragment.newInstance(R.color.colorPrimary, R.drawable.ic_history));

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSimpleFragmentAdapter);
        extensiblePageIndicator.initViewPager(mViewPager);
    }

}
