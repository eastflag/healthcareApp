package kr.co.aura.mtelo.healthcare.addinfo.exercise;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.util.AnimatedProgressLinear;

/**
 * Created by young-kchoi on 2016. 2. 24..
 */
public class ExerciseDetailTabActivity extends Activity{
    private SegmentTabLayout mTabLayout;
    private String[] mTitles = {"반","학년", "학교", "전국"};
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private final int PAGE_ONE = 0, PAGE_TWO= 1, PAGE_THREE=2, PAGE_FOUL= 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exerics_detail_tab);

        mTabLayout = (SegmentTabLayout) findViewById(R.id.exercise_detail_tab);
        mFragments.add(new TabItemFragment());
        mFragments.add(new TabItemFragment());
        mFragments.add(new TabItemFragment());
        mFragments.add(new TabItemFragment());

        initTabLayout();

    }





    private void initTabLayout() {
        final ViewPager vp_3 = (ViewPager) findViewById(R.id.exercise_detail_tab_pager);
        vp_3.setAdapter(new MyPagerAdapter(getFragmentManager()));

        mTabLayout.setTabData(mTitles);
        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp_3.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        vp_3.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e("!!!!" , "onPageScrolled!!!!!! "+ position);
                if (position == mTabLayout.getCurrentTab()) {
                    TabItemFragment v = (TabItemFragment) mFragments.get(position);
                    v.resetAni();
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("!!!!!!", "!!!!!! 현재 페이지 " + position);
                mTabLayout.setCurrentTab(position);
                TabItemFragment v = (TabItemFragment) mFragments.get(position);
                v.startAni();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public TabItemFragment getItem(int position) {
            Log.e("!!!!" , "getItem!!!!!! "+ position);
            TabItemFragment v = (TabItemFragment) mFragments.get(position);
            return v;
        }
    }



    class TabItemFragment extends Fragment{
        private String mTitle;
        private AnimatedProgressLinear mAniPro;

        public TabItemFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v =   inflater.inflate(R.layout.exercise_detail_tab_item, null);
            mAniPro = (AnimatedProgressLinear) v.findViewById(R.id.ani_layout_2);
            return v;
        }


        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            if(mAniPro != null) mAniPro.aniReset();
        }

        private void startAni() {
            if (mAniPro != null) {
                mAniPro.setXValue(800);
                mAniPro.setProgress(80);
                mAniPro.setAveProgressbar(90);
                mAniPro.startImgAnim();
            }
        }

        private void resetAni(){
            mAniPro.aniReset();
        }

    }
}
