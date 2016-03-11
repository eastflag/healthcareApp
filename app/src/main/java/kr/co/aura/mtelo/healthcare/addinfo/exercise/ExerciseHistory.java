package kr.co.aura.mtelo.healthcare.addinfo.exercise;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.network.JSONNetWork_Manager;
import kr.co.aura.mtelo.healthcare.network.NetWork;
import kr.co.aura.mtelo.healthcare.preferences.CPreferences;
import kr.co.aura.mtelo.healthcare.util.MLog;


/**
 * Created by young-kchoi on 16. 3. 10..
 */
public class ExerciseHistory extends SherlockActivity {

    private ArrayList<HistoryListItem> mHistoryItems = new ArrayList<HistoryListItem>();
    private String mNextYN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_history);


        init_ACtionBar();
        getDate();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


//        ArrayList<HistoryListItem> items = testCode();
        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), mHistoryItems));
    }


    private void init_ACtionBar() {
        ActionBar mActionBar;

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.custom_actionbar);

        String str = new CPreferences(this).getUserName();
        ((TextView) mActionBar.getCustomView().findViewById(R.id.actionbar_center_tx1)).setText(str);        //타이틀

        String date = new CPreferences(this).getTitleLastDate();
        ((TextView) mActionBar.getCustomView().findViewById(R.id.actionbar_right_tx1)).setText(date);    //날짜
        ((ImageButton) mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private ArrayList<HistoryListItem> testCode() {

        HistoryListItem item = new HistoryListItem("20160310123", "2016.03.10", "축국", "50", "240", "6000", "4.05", "http://210.127.55.205/exercise_contents/soccer.png");
        HistoryListItem item2 = new HistoryListItem("20160310123", "2016.03.03", "농구", "750", "340", "6500", "4.05", "http://210.127.55.205/exercise_contents/soccer.png");
        HistoryListItem item3 = new HistoryListItem("20160310123", "2016.03.04", "탁구", "550", "200", "2000", "4", "http://210.127.55.205/exercise_contents/soccer.png");
        HistoryListItem item4 = new HistoryListItem("20160310123", "2016.03.06", "배구", "250", "1140", "8000", "1.05", "http://210.127.55.205/exercise_contents/soccer.png");
        HistoryListItem item5 = new HistoryListItem("20160310123", "2016.03.08", "당구", "150", "640", "1000", "5", "http://210.127.55.205/exercise_contents/soccer.png");

        HistoryListItem item6 = new HistoryListItem("20160310123", "2016.03.10", "축국", "50", "240", "6000", "4.05", "http://210.127.55.205/exercise_contents/soccer.png");
        HistoryListItem item7 = new HistoryListItem("20160310123", "2016.03.03", "농구", "750", "340", "6500", "4.05", "http://210.127.55.205/exercise_contents/soccer.png");
        HistoryListItem item8 = new HistoryListItem("20160310123", "2016.03.04", "탁구", "550", "200", "2000", "4", "http://210.127.55.205/exercise_contents/soccer.png");
        HistoryListItem item9 = new HistoryListItem("20160310123", "2016.03.06", "배구", "250", "1140", "8000", "1.05", "http://210.127.55.205/exercise_contents/soccer.png");
        HistoryListItem item10 = new HistoryListItem("20160310123", "2016.03.08", "당구", "150", "640", "1000", "5", "http://210.127.55.205/exercise_contents/soccer.png");

        mHistoryItems.add(item);
        mHistoryItems.add(item2);
        mHistoryItems.add(item3);
        mHistoryItems.add(item4);
        mHistoryItems.add(item5);

        mHistoryItems.add(item6);
        mHistoryItems.add(item7);
        mHistoryItems.add(item8);
        mHistoryItems.add(item9);
        mHistoryItems.add(item10);

        return mHistoryItems;
    }



    private void getDate(){
        //학교 정보 추출
//        String url = Define.getNetUrl() + Define.MENTAL_LIST+"?" +JSONNetWork.KEY_USER_ID+ "=123";;
        String url = "http://210.127.55.205:82/HealthCare/simli/type_list?userId=123";
        Log.e("LDK", "############# url: " + url);

        JSONNetWork_Manager.request_Get_Exercise_History("123", "", this, new NetWork.Call_Back() {
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
                        Log.e("!!!!", "!!! request_Get_Exercise_History()\n " + data);

                        JSONObject object = new JSONObject(data);
                        JSONArray array = object.optJSONArray("history");

                        mNextYN = object.optString("nextYN");
                        buildData(array);
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


    //JSON데이터를 가공
    public void buildData(JSONArray array) {
        Log.e("!!!!!!!!!" , "!!!!!!! buildDate "+ array.toString());
        try {
            if (array.length() == 0) return;
            for (int i = 0; i < array.length(); i++) {
                MLog.write(Log.ERROR, this.toString(), "array= i " + array.get(i));
                JSONObject object = array.getJSONObject(i);  // JSONObject 추출

                HistoryListItem item = new HistoryListItem(
                        object.getString("exerciseId"),
                        object.getString("date"),
                        object.getString("exerciseName"),
                        object.getString("time"),
                        object.getString("calorie"),
                        object.getString("step"),
                        object.getString("distance"),
                        object.getString("img")
                );
                mHistoryItems.add(item);
                item.toString();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        Context context;
        ArrayList<HistoryListItem> items;

        public RecyclerAdapter(Context context, ArrayList<HistoryListItem> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_history_item, null);
            return new ViewHolder(v);
        }

        //        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final HistoryListItem item = items.get(position);
            holder.date.setText(item.date + "  <" + item.name + ">");
            holder.calorie.setText(item.calorie + " Kcal");
            holder.step.setText(item.step + " 보");
            holder.distance.setText(item.distance + " Km");
            Picasso.with(context).load(item.image).into(holder.image);

        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView date, calorie, step, distance;
            ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                date = (TextView) itemView.findViewById(R.id.history_date);
                calorie = (TextView) itemView.findViewById(R.id.history_calorie_text);
                step = (TextView) itemView.findViewById(R.id.history_step_text);
                distance = (TextView) itemView.findViewById(R.id.history_distance_text);
                image = (ImageView) itemView.findViewById(R.id.history_image);
            }
        }
    }


    private class HistoryListItem {
        private String id, date, name, time, calorie, step, distance, image;

        @Override
        public String toString() {
            return "HistoryListItem{" +
                    "id='" + id + '\'' +
                    ", date='" + date + '\'' +
                    ", name='" + name + '\'' +
                    ", time='" + time + '\'' +
                    ", calorie='" + calorie + '\'' +
                    ", step='" + step + '\'' +
                    ", distance='" + distance + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }

        public HistoryListItem(String id, String date, String name, String time, String calorie, String step, String distance, String image) {
            this.id = id;
            this.date = date;
            this.name = name;
            this.time = time;
            this.calorie = calorie;
            this.step = step;
            this.distance = distance;
            this.image = image;
        }
    }


}
