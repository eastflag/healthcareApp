package kr.co.aura.mtelo.healthcare.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import kr.co.aura.mtelo.healthcare.R;

/**
 * Created by young-kchoi on 16. 3. 3..
 */
public class AnimatedProgressLinear extends LinearLayout {

    private int mProgress = 0;
    private boolean mBidirectionalAnimate = true;
    private int mDrawWidth = 0;

    private ProgressBar mProgressBar;
    private ImageView mAniImage, mUpDownImage;
    private TextView mKcalText;
    private Context mContext;

    public AnimatedProgressLinear(Context context) {
        super(context);
        init(context);
    }

    public AnimatedProgressLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimatedProgressLinear(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }



    private void init(Context context) {

        mContext = context;

        LinearLayout view = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.animated_porgress_linear , null);

        mProgressBar = (ProgressBar) view.findViewById(R.id.ani_progressbar);
        mAniImage = (ImageView) view.findViewById(R.id.ani_move_img);
        mUpDownImage = (ImageView) view.findViewById(R.id.ani_updown_flag_img);

        mKcalText = (TextView) view.findViewById(R.id.ani_user_kcal_text);

    }

    public void startImgAnim(){
        mAniImage.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim_translate_right) );


    }

}
