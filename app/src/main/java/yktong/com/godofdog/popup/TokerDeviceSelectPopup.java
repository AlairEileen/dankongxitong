package yktong.com.godofdog.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yktong.com.godofdog.R;
import yktong.com.godofdog.adapter.TokerDeviceAdapter;
import yktong.com.godofdog.bean.UserListBean;

/**
 * Created by Eileen on 2017/6/26.
 */
public class TokerDeviceSelectPopup extends PopupWindow {

    private final TextView btn_select_all;
    private Context mContext;
    private TokerDeviceAdapter tokerDeviceAdapter;
    private View view;

    private TextView btn_cancel;
    private TextView btn_ok;

    private GridView gv_devices;
    private boolean isMulti;

    public TokerDeviceSelectPopup(Context mContext, ArrayList<UserListBean.UserBean> beanList, boolean isMulti, NumberListener numberListener) {
        this.isMulti = isMulti;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.popup_toker_device_select, null);
        this.mContext = mContext;
        btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        btn_select_all = (TextView) view.findViewById(R.id.btn_select_all);
        btn_ok = (TextView) view.findViewById(R.id.btn_ok);
        gv_devices = (GridView) view.findViewById(R.id.gv_devices);
        initDevices(beanList);
        if (isMulti) {
            btn_select_all.setVisibility(View.VISIBLE);
            btn_select_all.setOnClickListener(v -> {
                tokerDeviceAdapter.selectedAll();
            });
        }
        // 取消按钮
        btn_cancel.setOnClickListener(v -> {
            // 销毁弹出框
            dismiss();
        });
        btn_ok.setOnClickListener(v -> {
            // 销毁弹出框
            numberListener.selectDevices(tokerDeviceAdapter.getSelectItems());
            dismiss();
        });

        // 设置按钮监听
//        btn_pick_photo.setOnClickListener(itemsOnClick);
//        btn_take_photo.setOnClickListener(itemsOnClick);

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

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
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.anim_time_picker);

    }

    private void initDevices(ArrayList<UserListBean.UserBean> beanList) {
        if (tokerDeviceAdapter == null) {
            tokerDeviceAdapter = new TokerDeviceAdapter(beanList, isMulti, mContext);
        }
        gv_devices.setAdapter(tokerDeviceAdapter);
    }


    public interface NumberListener {
        void selectDevices(List<UserListBean.UserBean> arg);
    }
}
