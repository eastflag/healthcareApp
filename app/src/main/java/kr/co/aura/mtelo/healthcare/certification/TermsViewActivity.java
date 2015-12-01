package kr.co.aura.mtelo.healthcare.certification;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import kr.co.aura.mtelo.healthcare.R;
import kr.co.aura.mtelo.healthcare.util.Popup_Manager;

public class TermsViewActivity extends SherlockActivity {

    private Context mContext;
    private PopupWindow mPopup;
    private Button btn_Join_StarRing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms);

        mContext = TermsViewActivity.this;

        init();
    }


    @Override
    public void onBackPressed() {
        if(mPopup !=  null && mPopup.isShowing() )
            mPopup.dismiss();
        else
            super.onBackPressed();
    }

    private  void init()
    {
        init_ACtionBar(R.string.terms_top_text);
        CheckBox box_Thrms_of_Service = (CheckBox)findViewById(R.id.cb_I_agree_to_Terms_of_Service);
        CheckBox box_Privacy = (CheckBox)findViewById(R.id.cb_I_agree_to_Privacy);
        CheckBox box_Use_of_Privacy = (CheckBox)findViewById(R.id.cb_I_agree_to_CONSENT_TO_USE_OF_Privacy);

        box_Thrms_of_Service.setChecked(true);
        box_Privacy.setChecked(true);
        box_Use_of_Privacy.setChecked(true);

        box_Thrms_of_Service.setEnabled(false);
        box_Privacy.setEnabled(false);
        box_Use_of_Privacy.setEnabled(false);

        btn_Join_StarRing = (Button) findViewById(R.id.btn_Join_StarRing);
        btn_Join_StarRing.setText("돌아가기");
    }

    private ActionBar mActionBar ;
    //액션바 설정
    private void init_ACtionBar(int title)
    {
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setCustomView(R.layout.custom_actionbar);
        ((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_center_tx1)).setText(title);
        ((ImageButton)mActionBar.getCustomView().findViewById(R.id.actionbar_left_image1)).setVisibility(View.GONE);
        ((TextView)mActionBar.getCustomView().findViewById(R.id.actionbar_right_tx1)).setVisibility(View.GONE);
    }

    private void showSelectDialog(int resid) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_terms, null);
        TextView tx = (TextView)v.findViewById(R.id.terms_text);
        tx.setText(resid);

        mPopup = new PopupWindow(v, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT, false);
        mPopup.setAnimationStyle(R.style.DialogAnimation);
        mPopup.showAsDropDown(mActionBar.getCustomView());

        LinearLayout linear = (LinearLayout)v.findViewById(R.id.outer_layout);
        linear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPopup.dismiss();
            }
        });
    }


    public void btnDetailTermsOfServiceClick(View v )
    {
        showSelectDialog(R.string.join__terms_of_service);
    }


    public void btnDetailPrivacyClick(View v)
    {
        showSelectDialog(R.string.join__privacy_policy);
    }


    public void btnConsentToUseOfPrivacyClick(View v)
    {
        showSelectDialog(R.string.join__consent_to_use_of_privacy);
    }

    public void btnJoinStarRing(View v)
    {
        finish();
    }

}
