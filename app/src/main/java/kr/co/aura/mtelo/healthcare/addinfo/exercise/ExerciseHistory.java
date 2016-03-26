package kr.co.aura.mtelo.healthcare.addinfo.exercise;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_history);


        init_ACtionBar();

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        getDate(userId,""); //userId, ExerciseId


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerAdapter = new RecyclerAdapter(ExerciseHistory.this, mHistoryItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
//        ArrayList<HistoryListItem> items = testCode();

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

    private void getDate(String userId, String exerciseId){

        JSONNetWork_Manager.request_Get_Exercise_History(userId, exerciseId, this, new NetWork.Call_Back() {
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
                MLog.write(Log.ERROR, this.toString(), "array="+ i +", " + array.get(i));
                JSONObject object = array.getJSONObject(i);  // JSONObject 추출

                HistoryListItem item = new HistoryListItem(
                        object.getString("exerciseId"),
                        object.getString("date"),
                        object.getString("exerciseName"),
                        object.getString("calorie"),
                        object.getString("step"),
                        object.getString("distance"),
                        object.getString("img")
                );
                mHistoryItems.add(item);
//                item.toString();
            }
            mHandler.sendEmptyMessage(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
          mRecyclerAdapter.notifyDataSetChanged();
        }
    };

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        Context context;
        ArrayList<HistoryListItem> items;

        public RecyclerAdapter(Context context, ArrayList<HistoryListItem> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_history_item, parent, false);
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
        private String id, date, name, calorie, step, distance, image;

        @Override
        public String toString() {
            return "HistoryListItem{" +
                    "id='" + id + '\'' +
                    ", date='" + date + '\'' +
                    ", name='" + name + '\'' +
                    ", calorie='" + calorie + '\'' +
                    ", step='" + step + '\'' +
                    ", distance='" + distance + '\'' +
                    ", image='" + image + '\'' +
                    '}';
        }

        public HistoryListItem(String id, String date, String name, String calorie, String step, String distance, String image) {
            this.id = id;
            this.date = date;
            this.name = name;
            this.calorie = calorie;
            this.step = step;
            this.distance = distance;
            this.image = image;
        }
    }


}
