package yktong.com.godofdog.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import yktong.com.godofdog.R;
import yktong.com.godofdog.map.amap.LineMapActivity;


/**
 * Created by Eileen on 2017/8/29.
 */

public class MarkerInfoPopup extends PopupWindow {


    private final TextView tv_title;
    private final TextView tv_address;
    private final TextView tv_time;
    private final TextView tv_time_text;
    private Context mContext;

    private View view;


    public MarkerInfoPopup(Context mContext, LineMapActivity.MapMarker mapMarker) {
        this.mContext = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.popup_marker_info, null);

        tv_title = (TextView) view.findViewById((R.id.tv_title));
        tv_time_text = (TextView) view.findViewById((R.id.tv_time_text));
        tv_time = (TextView) view.findViewById((R.id.tv_time));
        tv_address = (TextView) view.findViewById((R.id.tv_address));
        setContent(mapMarker);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.tv_title).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(false);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.anim_time_picker);

    }

    public void setContent(LineMapActivity.MapMarker mapMarker) {

        tv_address.setText(mapMarker.address);
        tv_time.setText(mapMarker.time);
        tv_time_text.setText(mapMarker.time_text);
        tv_title.setText(mapMarker.title);
    }
}
