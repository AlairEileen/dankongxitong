package yktong.com.godofdog.popup;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yktong.com.godofdog.R;
import yktong.com.godofdog.bean.BaseViewBean;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;

/**
 * Created by Eileen on 2017/9/7.
 */

public class SelectListPopup<T extends BaseViewBean> extends PopupWindow {

    List<View> convertViewList = new ArrayList<>();
    private Activity activity;
    private WindowManager.LayoutParams params;
    private View view;
    private TextView btn_cancel;
    private TextView btn_ok;
    private int showAt;
    private Class<T> tClass;
    private List<T> tList;
    private T selectBean;
    private boolean[] checkeds;
    private boolean isMulti;

    public SelectListPopup(Activity activity, String title, int showAt, Class<T> tclass, List<T> tList, T defaultSelected, OnOkBeanSelected<T> onOkBeanSelected) {
        this.activity = activity;
        this.showAt = showAt;
        this.tClass = tclass;
        this.tList = tList;
        this.view = LayoutInflater.from(activity).inflate(R.layout.popup_list_select, null);
        selectBean = defaultSelected;
        btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        btn_ok = (TextView) view.findViewById(R.id.btn_ok);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(title);
        initCheckeds();
        initList();
        // 取消按钮
        btn_cancel.setOnClickListener(v -> {
            // 销毁弹出框
            dismiss();
        });
        btn_ok.setOnClickListener(v -> {
            // 销毁弹出框
            onOkBeanSelected.onPut(selectBean);
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
        show();
    }

    private void initCheckeds() {
        checkeds = new boolean[tList.size()];
        for (int i = 0; i < tList.size(); i++) {
            checkeds[i] = tList.get(i).equals(selectBean)?true:false;
        }
    }

//    public static SelectListPopup build(Activity activity, String title, int showAt, Class<T> tclass, List<T> tList, OnOkBeanSelected<T> onOkBeanSelected) {
//
//        SelectListPopup putTextPopup = new SelectListPopup(activity, title, showAt, tclass, tList, onOkBeanSelected);
//        putTextPopup.show();
//        return putTextPopup;
//    }

    private void initList() {
        ListView lv_list = (ListView) view.findViewById(R.id.lv_list);

        CommonAdapter<T> adapter = new CommonAdapter<T>(tList, activity, R.layout.item_gv_jurisdiction_right) {
            @Override
            public void setData(T t, int position, CommonViewHolder viewHolder) {
                TextView textView = viewHolder.getView(R.id.tv_gv_item_jurisdiction_right);
                textView.setText(t.getName());
                changeChecked(viewHolder.getConvertView(), position, checkeds[position]);
                convertViewList.add(viewHolder.getConvertView());
            }

            @Override
            public void notifyDataSetChanged() {
                convertViewList.clear();
                super.notifyDataSetChanged();
            }
        };
        lv_list.setAdapter(adapter);
        lv_list.setOnItemClickListener((parent, view1, position, id) -> {
            if (!isMulti) resetCheckeds();
            changeChecked(view1, position, true);
            selectBean = tList.get(position);
        });
    }

    private void resetCheckeds() {
        for (int i = 0; i < checkeds.length; i++) {
            changeChecked(convertViewList.size() > i ? convertViewList.get(i) : null, i, false);
        }
    }

    private void changeChecked(View view, int position, boolean checked) {
        checkeds[position] = checked;
        if (view == null) return;
        ((TextView) view.findViewById(R.id.tv_gv_item_jurisdiction_right))
                .setTextColor(activity.getResources().getColor(checked ? R.color.text_blue_light : R.color.text_black));
        view.findViewById(R.id.iv_gv_item_jurisdiction_right).setVisibility(checked ? View.VISIBLE : View.GONE);
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

    public static interface OnOkBeanSelected<T> {
        void onPut(T bean);
    }
}
