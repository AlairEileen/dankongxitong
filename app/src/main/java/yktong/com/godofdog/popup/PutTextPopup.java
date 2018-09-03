package yktong.com.godofdog.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import yktong.com.godofdog.R;

/**
 * Created by Eileen on 2017/9/7.
 */

public class PutTextPopup extends PopupWindow {

    private Activity activity;
    private WindowManager.LayoutParams params;

    private View view;

    private TextView btn_cancel;
    private TextView btn_ok;

    private int showAt;

    private PutTextPopup(Activity activity, String title, int showAt, OnOkTextPut onOkTextPut) {
        this.activity = activity;
        this.showAt = showAt;
        this.view = LayoutInflater.from(activity).inflate(R.layout.popup_text_put, null);
        btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        btn_ok = (TextView) view.findViewById(R.id.btn_ok);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        EditText et_content = (EditText) view.findViewById(R.id.et_content);
        tv_title.setText(title);

        // 取消按钮
        btn_cancel.setOnClickListener(v -> {
            // 销毁弹出框
            dismiss();
        });
        btn_ok.setOnClickListener(v -> {
            // 销毁弹出框
            onOkTextPut.onPut(et_content.getText().toString());
            dismiss();
        });

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener((v, event) -> {

            int height = activity.findViewById(showAt).getTop();

            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height) {
                    dismiss();
                }
            }
            return true;
        });

        setWindow();

    }

    public static PutTextPopup build(Activity activity, String title, int showAt, OnOkTextPut onOkTextPut) {

        PutTextPopup putTextPopup = new PutTextPopup(activity, title, showAt, onOkTextPut);
        putTextPopup.show();
        return putTextPopup;
    }

    private void show() {
        showAtLocation(activity.findViewById(showAt), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        params = activity.getWindow().getAttributes();
        //当弹出Popupwindow时，背景变半透明
        params.alpha = 0.7f;
        activity.getWindow().setAttributes(params);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        setOnDismissListener(() -> {
            params = activity.getWindow().getAttributes();
            params.alpha = 1f;
            activity.getWindow().setAttributes(params);
        });
        InputMethodManager inputMethodManager =(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void setWindow() {
          /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.anim_time_picker);
    }

    public static interface OnOkTextPut {
        void onPut(String text);
    }
}
