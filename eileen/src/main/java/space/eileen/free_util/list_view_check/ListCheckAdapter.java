package space.eileen.free_util.list_view_check;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import space.eileen.R;

/**
 * Created by Eileen on 2017/8/10.
 */

public class ListCheckAdapter<T extends ListCheckBean> extends BaseAdapter {
    private final Context context;
    private final int maxSelected;
    private final boolean hasSequence;
    boolean[] isChecked;
    List<T> list;
    ClickListener clickListener = new ClickListener();

    List<TextView> textList = new ArrayList<>();
    List<ImageView> imgList = new ArrayList<>();

    public ListCheckAdapter(Context mContext, List<T> list, int maxSelected, boolean hasSequence) {
        this.list = list;
        this.context = mContext;
        this.maxSelected = maxSelected;
        this.hasSequence = hasSequence;
        if (list == null || list.size() == 0) return;
        isChecked = new boolean[list.size()];
        for (int i = 0; i < list.size(); i++) {
            isChecked[i] = false;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ListBeanView listBeanView = null;
//        if (view == null) {
        view = LayoutInflater.from(context).inflate(R.layout.item_list_check, null);
        listBeanView = new ListBeanView();
        listBeanView.tv_position = (TextView) view.findViewById(R.id.tv_position);
        listBeanView.tv_text = (TextView) view.findViewById(R.id.tv_text);
        listBeanView.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
//            view.setTag(listBeanView);
//        } else {
//            listBeanView = (ListBeanView) view.getTag();
//        }
        String text = "";
        if (hasSequence) {
            text = 1+position + ".";
        }
        text += list.get(position).getText();

        listBeanView.tv_position.setText(position + "");
        listBeanView.tv_text.setText(text);
        textList.add(listBeanView.tv_text);
        imgList.add(listBeanView.iv_icon);
        if (isChecked[position]) {
            doSelected(listBeanView.tv_position, listBeanView.tv_text, listBeanView.iv_icon, true);
        }
        return view;
    }

    public List<T> getSelectItems() {
        List<T> deviceList = new ArrayList<>();
        for (int i = 0; i < isChecked.length; i++) {
            if (isChecked[i]) {
                deviceList.add(list.get(i));
            }
        }
        return deviceList;
    }

    private int getCheckLength() {
        int length = 0;
        for (boolean cc : isChecked) {
            if (cc) length++;
        }
        return length;
    }

    private void doMulti(TextView tv_device_id, TextView tv_device, ImageView iv_device) {
        boolean isSelected = isChecked[Integer.parseInt(tv_device_id.getText().toString())];
        if (isSelected) {
            doSelected(tv_device_id, tv_device, iv_device, false);
        } else {
            doSelected(tv_device_id, tv_device, iv_device, true);
        }
    }

    private void doSelected(TextView tv_device_id, TextView tv_device, ImageView iv_device, boolean selected) {
        tv_device.setTextColor(context.getResources().getColor(selected ? R.color.text_blue_light : R.color.text_gray));
        iv_device.setImageResource(selected ? R.mipmap.icon_s : R.mipmap.icon_g);
        isChecked[Integer.parseInt(tv_device_id.getText().toString())] = selected;
    }

    class ClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tv_device_id = (TextView) view.findViewById(R.id.tv_position);
            if (!isChecked[Integer.valueOf(tv_device_id.getText().toString())]) {
                if (maxSelected == getCheckLength()) {
                    Toast.makeText(context, "最多只能选择" + maxSelected + "项", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            TextView tv_device = (TextView) view.findViewById(R.id.tv_text);
            ImageView iv_device = (ImageView) view.findViewById(R.id.iv_icon);
            doMulti(tv_device_id, tv_device, iv_device);
        }
    }

//    private void doNoSelected(TextView tv_position, TextView tv_text, ImageView iv_icon) {
//        tv_text.setTextColor(activity.getResources().getColor(R.color.text_gray));
//        iv_icon.setImageResource(R.mipmap.icon_g);
//        isChice[Integer.parseInt(tv_position.getText().toString())] = false;
//    }

    private class ListBeanView {
        TextView tv_position;
        TextView tv_text;
        ImageView iv_icon;
    }


}
