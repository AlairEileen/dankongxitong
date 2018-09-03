package yktong.com.godofdog.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;

import space.eileen.tools.XString;
import yktong.com.godofdog.R;

import static space.eileen.tools.XString.convertText;

/**
 * Created by Eileen on 2017/6/26.
 */
public class TimePickerPopup extends PopupWindow {

    private DatePicker dp_date;


    private Context mContext;

    private View view;

    private TextView btn_cancel;
    private TextView btn_ok;
    private TimePicker tp_time;


    public TimePickerPopup(Context mContext, boolean isFuture, TimerListener timerListener) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.popup_time_picker, null);

//        btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
//        btn_pick_photo = (Button) view.findViewById(R.id.btn_pick_photo);
        btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        btn_ok = (TextView) view.findViewById(R.id.btn_ok);
        tp_time = (TimePicker) view.findViewById(R.id.tp_time);
        dp_date = (DatePicker) view.findViewById(R.id.dp_date);
        tp_time.setIs24HourView(true);
        // 取消按钮
        btn_cancel.setOnClickListener(v -> {
            // 销毁弹出框
            dismiss();
        });
        btn_ok.setOnClickListener(v -> {
            // 销毁弹出框
            String startTime = dp_date.getYear() + "-" + convertText(dp_date.getMonth() + 1) + "-" + convertText(dp_date.getDayOfMonth()) + " " + convertText(tp_time.getCurrentHour()) + ":" + convertText(tp_time.getCurrentMinute()) + ":00";

            if (isFuture) {
                try {
                    if (!XString.isFutureTime(startTime)) {
                        Toast.makeText(mContext, "时间只能为将来时间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (ParseException e) {
                    return;
                }
            }
            timerListener.selectTime(dp_date.getYear(), dp_date.getMonth() + 1, dp_date.getDayOfMonth(), tp_time.getCurrentHour(), tp_time.getCurrentMinute());
            dismiss();
        });

        // 设置按钮监听
//        btn_pick_photo.setOnClickListener(itemsOnClick);
//        btn_take_photo.setOnClickListener(itemsOnClick);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener((v, event) -> {

            int height = view.findViewById(R.id.btn_cancel).getTop();

            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss();
                }
            }
            return true;
        });


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.anim_time_picker);

    }

    public interface TimerListener {
        void selectTime(int year, int month, int dayOfMonth, int hour, int minute);
    }
}
