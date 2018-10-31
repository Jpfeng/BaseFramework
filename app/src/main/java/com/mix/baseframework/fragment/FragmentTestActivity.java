package com.mix.baseframework.fragment;

import com.google.android.material.tabs.TabLayout;
import com.mix.baseframework.R;
import com.mix.framework.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentTestActivity extends BaseActivity {
// transaction
/*    @BindView(R.id.rg_activity)
    RadioGroup mRG;

    private List<OuterFragment> mFragments;
    private FragmentManager mFragmentManager;
    private int mCurrent;

    @Override
    protected int getPageLayoutId() {
        return R.layout.activity_fragment_test;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mFragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OuterFragment fragment = OuterFragment.newInstance(i);
            mFragments.add(fragment);
        }
        mCurrent = 0;
        mRG.check(R.id.rb_activity_0);
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager
                .beginTransaction()
                .add(R.id.fl_container_activity, mFragments.get(mCurrent), String.valueOf(mCurrent))
                .commit();

        mRG.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_activity_0:
                    to(0);
                    break;
                case R.id.rb_activity_1:
                    to(1);
                    break;
                case R.id.rb_activity_2:
                    to(2);
                    break;
                case R.id.rb_activity_3:
                    to(3);
                    break;
                case R.id.rb_activity_4:
                    to(4);
                    break;
            }
        });
    }

    private void to(int index) {
        if (index == mCurrent) {
            return;
        }

        Fragment current = mFragments.get(mCurrent);
        Fragment target = mFragments.get(index);
        mCurrent = index;

        if (target.isAdded()) {
            mFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .hide(current)
                    .show(target)
                    .commit();

        } else {
            mFragmentManager
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .hide(current)
                    .add(R.id.fl_container_activity, target, String.valueOf(index))
                    .commit();
        }
    }*/

    // Viewpager
    @BindView(R.id.tl_tab)
    TabLayout mTAbs;
    @BindView(R.id.vp_test)
    ViewPager mPages;

    private List<OuterFragment> mFragments;

    @Override
    protected int getPageLayoutId() {
        return R.layout.activity_fragment_test;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mFragments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            OuterFragment fragment = OuterFragment.newInstance(i);
            mFragments.add(fragment);
        }

        mPages.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "PAGE #" + position;
            }
        });
        mTAbs.setupWithViewPager(mPages);
    }
}
