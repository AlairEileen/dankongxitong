package yktong.com.godofdog.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.List;

import yktong.com.godofdog.R;
import yktong.com.godofdog.tool.view.CommonAdapter;

/**
 * Created by vampire on 2017/7/4.
 */

public class PikerPopup extends PopupWindow {
    private Context mContext;
    private View view;
    private List list;


    public <T> PikerPopup(Context mContext, PickerListener listener, List<T> list, CommonAdapter<T> adapter) {
        this.mContext = mContext;
        this.view = LayoutInflater.from(mContext).inflate(R.layout.picker_layout, null);
        this.list = list;
        ListView listView = (ListView) view.findViewById(R.id.picker_lv);
        Log.d("PikerPopup", "listView == null:" + (listView == null));
        Log.d("PikerPopup", "adapter == null:" + (adapter == null));
        listView.setAdapter(adapter);
        listView.setFocusable(true);
        listView.setDividerHeight(0);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            listener.SeletInfo(position);
            this.dismiss();
        });
        pickerInfo();
    }

    private void pickerInfo() {
        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener((v, event) -> {

            int height = view.findViewById(R.id.picker_lv).getTop();

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

    public interface PickerListener {
        void SeletInfo(int position);
    }
}
