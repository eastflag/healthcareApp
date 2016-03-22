package kr.co.aura.mtelo.healthcare.addinfo.exercise;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;
import kr.co.aura.mtelo.healthcare.util.AnimatedProgressLinear;

/**
 * Created by young-kchoi on 2016. 2. 24..
 */
public class ExerciseDetailTabActivity extends Activity{
    private SegmentTabLayout mTabLayout;
    private String[] mTitles = {"반","학년", "학교", "전국"};
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private final int PAGE_ONE = 0, PAGE_TWO= 1, PAGE_THREE=2, PAGE_FOUL= 3;
    private String mUser, mAll, mAverageCnt, mAverageType, mBodyType, mBodyType1, mBodyType1Max, mBodyType2, mBodyType2Max, mBodyType3, mBodyType3Max, mBodyType4,
            mBodyType4Max, mBodyType5, mBodyType5Max, mBodyType6, mBodyType6Max, mGroupType;

    private String UserId, ExerciseId, AverageType , GroupType;
    private int SEND_MASSAGE = 100;



    private ArrayList<ExerciseData> mExerciseDataList = new ArrayList<ExerciseData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exerics_detail_tab);

        mTabLayout = (SegmentTabLayout) findViewById(R.id.exercise_detail_tab);
        mFragments.add(new TabItemFragment());
        mFragments.add(new TabItemFragment());
        mFragments.add(new TabItemFragment());
        mFragments.add(new TabItemFragment());


        Intent intent = getIntent();

        UserId ="7001";
        ExerciseId = "137";
        AverageType = intent.getStringExtra("averageType");
        GroupType = "class";
        getDate(UserId, ExerciseId , AverageType , GroupType);
        getDate(UserId, ExerciseId , AverageType , "grade");
        getDate(UserId, ExerciseId , AverageType , "school");
        getDate(UserId, ExerciseId, AverageType, "all");

    }


    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(mExerciseDataList.size() == 4){
                Log.e("!!!!!!", "!!!! 모든 리스트데이터 " + mExerciseDataList);

                //레이아웃 초기화
                initTabLayout();
            }

        }
    };


    private void getDate(String userId, String exerciseId, String averageType , String groupType){
        JSONNetWork_Manager.request_Get_User_Exercise_Data(userId, exerciseId, averageType, groupType, this, new NetWork.Call_Back() {
            @Override
            public void onError(String error) {
            }

            @Override
            public void onGetResponsData(byte[] data) {
            }

            @Override
            public void onGetResponsString(String data) {
                try {
                    if (data != null) {
                        if (data.startsWith("[")) {
                            data = data.substring(1, data.length());
                            data = data.substring(0, data.length() - 1);
                        }
                        Log.e("!!!!", "!!! request_Get_User_Exercise_Data()\n " + data);

                        JSONObject object = new JSONObject(data);

                        ExerciseData exdata = new ExerciseData(
                                object.optString("user"),
                                object.optString("all"),
                                object.optString("averageCnt"),
                                object.optString("averageType"),
                                object.optString("bodyType"),
                                object.optString("bodyType1"),
                                object.optString("bodyType1Max"),
                                object.optString("bodyType2"),
                                object.optString("bodyType2Max"),
                                object.optString("bodyType3"),
                                object.optString("bodyType3Max"),
                                object.optString("bodyType4"),
                                object.optString("bodyType4Max"),
                                object.optString("bodyType5"),
                                object.optString("bodyType5Max"),
                                object.optString("bodyType6"),
                                object.optString("bodyType6Max"),
                                object.optString("groupType")
                        );

                        Log.e( "!!!! " , "!!!"+exdata.toString());
                        mExerciseDataList.add(exdata);
                        mHandler.sendEmptyMessage(SEND_MASSAGE);
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRoaming(String message) {
            }
        });
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
//                    v.resetAni();
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("!!!!!!", "!!!!!! 현재 페이지 " + position);
                mTabLayout.setCurrentTab(position);
                TabItemFragment v = (TabItemFragment) mFragments.get(position);
                v.setData(mExerciseDataList.get(position));
                v.startAni();
                mTabLayout.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.setCurrentTab(1);
        mTabLayout.notifyDataSetChanged();
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
            Log.e("!!!!", "getItem!!!!!! " + position);
            TabItemFragment v = (TabItemFragment) mFragments.get(position);

            return v;
        }
    }



    class TabItemFragment extends Fragment{
        private String mTitle;
        private AnimatedProgressLinear mAniPro;
        private ExerciseData mData;
        private ProgressBar pro1, pro3, pro4, pro5;

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

            pro1 = (ProgressBar) v.findViewById(R.id.detail_tab_item_pro1);
            pro3 = (ProgressBar) v.findViewById(R.id.detail_tab_item_pro3);
            pro4 = (ProgressBar) v.findViewById(R.id.detail_tab_item_pro4);
            pro5 = (ProgressBar) v.findViewById(R.id.detail_tab_item_pro5);

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

        private void setData(ExerciseData data) {
            if (data != null) {

                //저체중
                pro1.setProgress(Integer.parseInt(data.bodyType1));
                pro1.setMax(Integer.parseInt(data.bodyType1Max));

                //과체중
                pro3.setProgress(Integer.parseInt(data.bodyType1));
                pro3.setMax(Integer.parseInt(data.bodyType3Max));

                //중도비만
                pro4.setProgress(Integer.parseInt(data.bodyType1));
                pro4.setMax(Integer.parseInt(data.bodyType5Max));

                //고도비만
                pro5.setProgress(Integer.parseInt(data.bodyType1));
                pro5.setMax(Integer.parseInt(data.bodyType6Max));

                Log.e("!!!!!!", "!!!! progressbar 설정 " + mTabLayout.getCurrentTab() + ", " + data.bodyType1 +", "+ data.bodyType1Max);
            }
        }
    }



    private class ExerciseData{
        private String user;
        private String all;
        private String averageCnt;
        private String averageType;
        private String bodyType;
        private String bodyType1;
        private String bodyType1Max;
        private String bodyType2;
        private String bodyType2Max;
        private String bodyType3;
        private String bodyType3Max;
        private String bodyType4;
        private String bodyType4Max;
        private String bodyType5;
        private String bodyType5Max;
        private String bodyType6;
        private String bodyType6Max;

        @Override
        public String toString() {
            return "ExerciseData{" +
                    "user='" + user + '\'' +
                    ", all='" + all + '\'' +
                    ", averageCnt='" + averageCnt + '\'' +
                    ", averageType='" + averageType + '\'' +
                    ", bodyType='" + bodyType + '\'' +
                    ", bodyType1='" + bodyType1 + '\'' +
                    ", bodyType1Max='" + bodyType1Max + '\'' +
                    ", bodyType2='" + bodyType2 + '\'' +
                    ", bodyType2Max='" + bodyType2Max + '\'' +
                    ", bodyType3='" + bodyType3 + '\'' +
                    ", bodyType3Max='" + bodyType3Max + '\'' +
                    ", bodyType4='" + bodyType4 + '\'' +
                    ", bodyType4Max='" + bodyType4Max + '\'' +
                    ", bodyType5='" + bodyType5 + '\'' +
                    ", bodyType5Max='" + bodyType5Max + '\'' +
                    ", bodyType6='" + bodyType6 + '\'' +
                    ", bodyType6Max='" + bodyType6Max + '\'' +
                    ", groupType='" + groupType + '\'' +
                    '}';
        }

        public ExerciseData(String user, String all, String averageCnt, String averageType, String bodyType, String bodyType1, String bodyType1Max, String bodyType2, String bodyType2Max, String bodyType3, String bodyType3Max, String bodyType4, String bodyType4Max,
                            String bodyType5, String bodyType5Max, String bodyType6, String bodyType6Max, String groupType) {
            this.user = user;
            this.all = all;
            this.averageCnt = averageCnt;
            this.averageType = averageType;
            this.bodyType = bodyType;
            this.bodyType1 = bodyType1;
            this.bodyType1Max = bodyType1Max;
            this.bodyType2 = bodyType2;
            this.bodyType2Max = bodyType2Max;
            this.bodyType3 = bodyType3;
            this.bodyType3Max = bodyType3Max;
            this.bodyType4 = bodyType4;
            this.bodyType4Max = bodyType4Max;
            this.bodyType5 = bodyType5;
            this.bodyType5Max = bodyType5Max;
            this.bodyType6 = bodyType6;
            this.bodyType6Max = bodyType6Max;
            this.groupType = groupType;
        }

        private String groupType;
    }
}
