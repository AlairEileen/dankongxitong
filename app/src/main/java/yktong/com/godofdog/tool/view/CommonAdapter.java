package yktong.com.godofdog.tool.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Vampire on 16/9/13.
 * 通用的Adapter
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    private List<T> beenList;
    private LayoutInflater inflater;
    private int convertViewId;
    private int position;

    public CommonAdapter(List<T> beenList, Context context, int convertViewId) {
        this.beenList = beenList;
        inflater = LayoutInflater.from(context);
        this.convertViewId = convertViewId;
    }

    public void addList(List<T> beenList) {
        this.beenList.addAll(beenList);
        notifyDataSetChanged();
    }

    public void setList(List<T> beenList) {
        this.beenList = beenList;
        notifyDataSetChanged();
    }


    public void setPostion(int position) {
        this.position = position;
    }

    @Override
    public int getCount() {
        return beenList != null ? beenList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder commonViewHolder = CommonViewHolder.getHolder(convertView, inflater, convertViewId, parent);
        setData(beenList.get(position), position, commonViewHolder);
        return commonViewHolder.getConvertView();
    }


    public abstract void setData(T t, int position, CommonViewHolder viewHolder);


}
