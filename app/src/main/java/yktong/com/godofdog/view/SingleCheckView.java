package yktong.com.godofdog.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import yktong.com.godofdog.R;

/**
 * Created by vampire on 2017/7/5.
 */

public class SingleCheckView extends LinearLayout implements Checkable {


    private View v;
    private CheckBox checkBox;

    public SingleCheckView(Context context) {
        super(context);
        initView(context);
    }

    public SingleCheckView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SingleCheckView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SingleCheckView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }


    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.date_item, this, true);
        checkBox = (CheckBox) v.findViewById(R.id.checkbox_item);
    }

    public void setText(int id, String content) {
        TextView textView = (TextView) v.findViewById(id);
        textView.setText(content);
    }


    @Override
    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);

    }

    @Override
    public boolean isChecked() {
        return checkBox.isChecked();
    }

    @Override
    public void toggle() {
        checkBox.toggle();
    }
}
