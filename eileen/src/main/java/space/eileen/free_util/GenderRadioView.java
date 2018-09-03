package space.eileen.free_util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import space.eileen.R;

/**
 * Created by Eileen on 2017/8/21.
 */

public class GenderRadioView extends LinearLayout {

    ImageView checkViews[];

    Gender genderChecked=Gender.male;

    public GenderRadioView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        View view = LayoutInflater.from(context).inflate(R.layout.view_gender_radio, this);
        LinearLayout ll_male = (LinearLayout) view.findViewById(R.id.ll_male);
        LinearLayout ll_female = (LinearLayout) view.findViewById(R.id.ll_female);
        LinearLayout ll_null = (LinearLayout) view.findViewById(R.id.ll_null);

        ImageView iv_male = (ImageView) view.findViewById(R.id.iv_male);
        ImageView iv_female = (ImageView) view.findViewById(R.id.iv_female);
        ImageView iv_null = (ImageView) view.findViewById(R.id.iv_null);
        checkViews = new ImageView[]{iv_male, iv_female, iv_null};


        ll_male.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                genderChecked = Gender.male;
                changeView(0);
            }
        });
        ll_female.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                genderChecked = Gender.female;
                changeView(1);
            }
        });
        ll_null.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                genderChecked = Gender.noGender;
                changeView(2);
            }
        });


    }

    private void changeView(int position) {
        for (int i = 0; i < checkViews.length; i++) {
            checkViews[i].setImageResource(i == position ? R.mipmap.icon_s : R.mipmap.icon_g);
        }
    }

    public Gender getGenderChecked() {
        return genderChecked;
    }

    public static enum Gender {
        male, female, noGender;
    }


}
