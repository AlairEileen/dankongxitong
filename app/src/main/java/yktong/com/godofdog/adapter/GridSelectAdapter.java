package yktong.com.godofdog.adapter;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yktong.com.godofdog.R;
import yktong.com.godofdog.bean.BaseViewBean;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;

/**
 * Created by Eileen on 2017/9/6.
 */

public class GridSelectAdapter<T extends BaseViewBean> extends CommonAdapter<T> {
    boolean[] checkList;
    GridView gridView;
    private int textId;
    private int imgId;
    private Context context;
    private List<T> beanList;

    public GridSelectAdapter(List<T> beenList, Context context, int convertViewId, GridView gridView, int textId, int imgId) {
        super(beenList, context, convertViewId);
        this.gridView = gridView;
        this.textId = textId;
        this.imgId = imgId;
        this.context = context;
        this.beanList = beenList;
        init();
    }

    @Override
    public void setData(T t, int position, CommonViewHolder viewHolder) {
        viewHolder.setText(textId, t.getName());
        if (checkList!= null && checkList.length!= 0) {
            viewHolder.getConvertView().findViewById(imgId).setVisibility(checkList[position] ? View.VISIBLE : View.GONE);
            ((TextView) viewHolder.getConvertView().findViewById(textId))
                    .setTextColor(context.getResources().getColor(checkList[position] ? R.color.theme_blue : R.color.text_black));
        }
    }

    protected void init() {
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            checkList[position]= !checkList[position];
            view.findViewById(imgId).setVisibility(checkList[position]? View.VISIBLE : View.GONE);
            ((TextView) view.findViewById(textId))
                    .setTextColor(context.getResources().getColor(checkList[position] ? R.color.theme_blue : R.color.text_black));
        });
    }

    public void selectAllOrNone() {
        boolean hasNoCheck = false;
        for (boolean bool : checkList) {
            if (!bool) {
                hasNoCheck = true;
                break;
            }
        }
        for (int i = 0; i < checkList.length; i++) {
            int position = i;
            checkList[position]=hasNoCheck;
        }
        notifyDataSetChanged();
    }

    @Override
    public void setList(List<T> beenList) {
        checkList=new boolean[beenList.size()];
        for (int i = checkList.length; i < beenList.size(); i++) {
            checkList[i]=false;
        }
        super.setList(beenList);
    }

    public List<T> getSelectedBeans() {
        List<T> tList = new ArrayList<>();
        for (int i = 0; i < checkList.length; i++) {
            if (checkList[i])
            tList.add(beanList.get(i));
        }
        return tList;
    }
}
