package yktong.com.godofdog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yktong.com.godofdog.R;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.UserListBean;

import static yktong.com.godofdog.R.id.iv_device;
import static yktong.com.godofdog.R.id.tv_device;
import static yktong.com.godofdog.R.id.tv_device_id;

/**
 * Created by Eileen on 2017/6/26.
 */

public class TokerDeviceAdapter extends BaseAdapter {
    ClickListener clickListener;
    List<TextView> textList = new ArrayList<>();
    List<ImageView> imgList = new ArrayList<>();
    List<DeviceView> deviceViews = new ArrayList<>();
    private boolean isMulti;
    private ArrayList<UserListBean.UserBean> devices;
    private boolean isChice[];
    private Context context;

    public TokerDeviceAdapter(ArrayList<UserListBean.UserBean> devices, boolean isMulti, Context context) {
        this.devices = devices;
        this.isMulti = isMulti;
        isChice = new boolean[devices.size()];
        for (int i = 0; i < devices.size(); i++) {
            isChice[i] = false;
        }
        clickListener = new ClickListener();
        this.context = context;
    }

    @Override
    public void notifyDataSetChanged() {
        textList.clear();
        imgList.clear();
        deviceViews.clear();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return devices.size();
    }


    @Override
    public Object getItem(int arg0) {
        return devices.get(arg0);
    }


    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        View view = arg1;
        DeviceView deviceView = null;
//        if (view == null) {
        view = LayoutInflater.from(context).inflate(R.layout.item_grid_toker_device, null);
        deviceView = new DeviceView();
        deviceView.tv_device_id = (TextView) view.findViewById(tv_device_id);
        deviceView.tv_device = (TextView) view.findViewById(tv_device);
        deviceView.iv_device = (ImageView) view.findViewById(iv_device);
//            view.setTag(deviceView);
//        } else {
//            deviceView = (DeviceView) view.getTag();
//        }
        deviceView.tv_device_id.setText(arg0 + "");

        deviceView.tv_device.setText(devices.get(arg0).getCName());
        deviceView.tv_device.setTextColor(context.getResources().getColor(devices.get(arg0).getCId() == MyApp.userId ? R.color.text_green_light:R.color.text_gray));
        textList.add(deviceView.tv_device);
        imgList.add(deviceView.iv_device);
        if (isChice[arg0]) {
            doSelected(deviceView.tv_device_id, deviceView.tv_device, deviceView.iv_device, true);
        }
        view.setOnClickListener(clickListener);
        deviceViews.add(deviceView);
        return view;
    }

    public List<UserListBean.UserBean> getSelectItems() {
        List<UserListBean.UserBean> deviceList = new ArrayList<>();
        for (int i = 0; i < isChice.length; i++) {
            if (isChice[i]) {
                deviceList.add(devices.get(i));
            }
        }
        return deviceList;
    }

    public void selectedAll() {
        boolean hasNo = false;
        for (boolean aa : isChice) {
            if (!aa) {
                hasNo = true;
                break;
            }
        }
        for (int i = 0; i < isChice.length; i++) {
            isChice[i] = hasNo;
        }
        notifyDataSetChanged();
    }

    private void doNoMulti(TextView tv_device_id, TextView tv_device, ImageView iv_device) {
        boolean isSelected = isChice[Integer.parseInt(tv_device_id.getText().toString())];
        if (!isSelected) {
            for (DeviceView deviceView : deviceViews) {
                int color=devices.get(Integer.valueOf( deviceView.tv_device_id.getText().toString())).getCId()==MyApp.userId?R.color.text_green_light:R.color.text_gray;
                deviceView.tv_device.setTextColor(context.getResources().getColor(color));
            }
            for (ImageView imageView : imgList) {
                imageView.setImageResource(R.mipmap.icon_g);
            }
            for (int i = 0; i < isChice.length; i++) {
                isChice[i] = false;
            }
//            tv_device.setTextColor(activity.getResources().getColor(R.color.text_blue_light));
//            iv_device.setImageResource(R.mipmap.icon_s);
//            isChice[Integer.parseInt(tv_device_id.getText().toString())] = true;
            doSelected(tv_device_id, tv_device, iv_device, true);
        }
    }

    private void doMulti(TextView tv_device_id, TextView tv_device, ImageView iv_device) {
        boolean isSelected = isChice[Integer.parseInt(tv_device_id.getText().toString())];
        if (isSelected) {
            doSelected(tv_device_id, tv_device, iv_device, false);
        } else {
            doSelected(tv_device_id, tv_device, iv_device, true);

        }
    }

    private void doSelected(TextView tv_device_id, TextView tv_device, ImageView iv_device, boolean selected) {
        tv_device.setTextColor(context.getResources().getColor(selected ? R.color.text_blue_light : devices.get(Integer.valueOf(tv_device_id.getText().toString())).getCId() == MyApp.userId ? R.color.text_green_light : R.color.text_gray));
        iv_device.setImageResource(selected ? R.mipmap.icon_s : R.mipmap.icon_g);
        isChice[Integer.parseInt(tv_device_id.getText().toString())] = selected;
    }

    class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TextView tv_device_id = (TextView) v.findViewById(R.id.tv_device_id);
            TextView tv_device = (TextView) v.findViewById(R.id.tv_device);
            ImageView iv_device = (ImageView) v.findViewById(R.id.iv_device);
            if (isMulti) {
                doMulti(tv_device_id, tv_device, iv_device);
            } else {
                doNoMulti(tv_device_id, tv_device, iv_device);
            }
        }


    }

//    private void doNoSelected(TextView tv_device_id, TextView tv_device, ImageView iv_device) {
//        tv_device.setTextColor(activity.getResources().getColor(R.color.text_gray));
//        iv_device.setImageResource(R.mipmap.icon_g);
//        isChice[Integer.parseInt(tv_device_id.getText().toString())] = false;
//    }

    private class DeviceView {
        TextView tv_device_id;
        TextView tv_device;
        ImageView iv_device;
    }
}

