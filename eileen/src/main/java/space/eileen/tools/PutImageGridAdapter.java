package space.eileen.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;

import java.util.ArrayList;

import space.eileen.R;

import static com.foamtrace.photopicker.PhotoPickerActivity.EXTRA_RESULT;

/**
 * Created by Eileen on 2017/6/28.
 */

public class PutImageGridAdapter extends BaseAdapter {
    ClickListener clickListener;
    private GridView gridView;
    private ArrayList<String> paths;
    //    private boolean isChice[];
    private Context context;
    private int selectPicCode = 8000;
    private int selectedPicsNum;

    public PutImageGridAdapter(Context context, int selectedPicsNum, GridView gridView) {
        this.selectedPicsNum = selectedPicsNum;
        paths = new ArrayList<>();
        paths.add("+");
        clickListener = new ClickListener();
        this.context = context;
        this.gridView = gridView;
        init();
    }


    @Override
    public int getCount() {
//        Toast.makeText(activity,"getCount:"+paths.size(),Toast.LENGTH_SHORT).show();
        return paths.size();
    }

    @Override
    public Object getItem(int arg0) {
        return paths.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        View view = arg1;
        SelectView getView = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_grid_select_pics, null);
            getView = new SelectView();
            getView.iv_select_pic = (ImageView) view.findViewById(R.id.iv_select_pic);
            getView.iv_select_pic_del = (ImageView) view.findViewById(R.id.iv_select_pic_del);
            getView.iv_select_pic_id = (TextView) view.findViewById(R.id.iv_select_pic_id);
            view.setTag(getView);
        } else {
            getView = (SelectView) view.getTag();
        }
        getView.iv_select_pic_id.setText(arg0 + "");
        if (!paths.get(arg0).equals("+")) {
            getView.iv_select_pic.setImageBitmap(ImageTools.decodeSampleBitmapFromFile(paths.get(arg0), 140, 140));
            getView.iv_select_pic_del.setImageResource(R.mipmap.icon_del);
        } else {
            getView.iv_select_pic_del.setImageResource(0);
            getView.iv_select_pic.setImageResource(R.mipmap.icon_addpic);
        }
        view.setOnClickListener(clickListener);
        return view;
    }

    /**
     * 刷新已选择图片列表
     *
     * @param pic_paths
     */
    public void refreshData(ArrayList<String> pic_paths) {
        paths = pic_paths;
        if (paths.size() < selectedPicsNum) {
            paths.add("+");
        }
    }

    @Override
    public void notifyDataSetChanged() {
//        Toast.makeText(activity,"notifyDataSetChanged",Toast.LENGTH_SHORT).show();
        if (paths.size() < selectedPicsNum && !paths.get(paths.size() - 1).equals("+")) {
            paths.add("+");
        }
        super.notifyDataSetChanged();
        refreshGridLength();

    }

    /**
     * 获取已选择图片列表
     *
     * @return
     */
    public ArrayList<String> getSelectedPicsList() {
        ArrayList<String> oldPaths = new ArrayList<>();
        oldPaths.addAll(paths);
        if (oldPaths.get(oldPaths.size() - 1).equals("+")) {
            oldPaths.remove(oldPaths.size() - 1);
        }
        return oldPaths;
    }

    /**
     * 获取已选择图片数量
     *
     * @return
     */
    public int getSelectedPicsSize() {

        return paths.size() - 1;
    }

    private void showAddPicsView() {
        PhotoPickerIntent intent = new PhotoPickerIntent(context);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照， 默认false
        intent.setMaxTotal(selectedPicsNum); // 最多选择照片数量，默认为9
        intent.setSelectedPaths(getSelectedPicsList()); // 已选中的照
        ((Activity) context).startActivityForResult(intent, selectPicCode);
    }

    public void refreshGridLength() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int rows = 0;
        if (getSelectedPicsSize() / 4 == 0) {
            rows = 1;
        } else if (getSelectedPicsSize() / 4 == 1 || (getSelectedPicsSize() / 4 == 1 && getSelectedPicsSize() % 4 == 0)) {
            rows = 2;
        } else if (getSelectedPicsSize() / 4 == 2 || (getSelectedPicsSize() / 4 == 2 && getSelectedPicsSize() % 4 == 0)) {
            rows = 3;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (75 * metrics.density * rows + 10 * metrics.density * rows - 1));
        layoutParams.setMargins((int) (15 * metrics.density), (int) (10 * metrics.density), (int) (15 * metrics.density), 0);
        gridView.setLayoutParams(layoutParams);
    }

    public void init() {
        refreshGridLength();
        gridView.setAdapter(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == selectPicCode) {
            try {
                ArrayList<String> selectNewPics = data.getStringArrayListExtra(EXTRA_RESULT);
                addPicsToView(selectNewPics);
//                Toast.makeText(this, "addPicsToView:" + selectNewPics.size(), Toast.LENGTH_SHORT).show();
            } catch (NullPointerException npe) {
            }
        }
    }

    private void addPicsToView(ArrayList<String> selectNewPics) {
        refreshData(selectNewPics);
        notifyDataSetChanged();
    }

    class ClickListener implements View.OnClickListener {

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            TextView iv_select_pic_id = (TextView) v.findViewById(R.id.iv_select_pic_id);
            int position = Integer.parseInt(iv_select_pic_id.getText().toString());
            if (position < paths.size() - 1 || (position == selectedPicsNum-1 && !paths.get(selectedPicsNum-1).equals("+"))) {
                paths.remove(position);
//                Toast.makeText(activity, "position:" + position + ",onClick:" + paths.size(), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            } else {
                showAddPicsView();
            }
        }
    }

    private class SelectView {
        ImageView iv_select_pic;
        ImageView iv_select_pic_del;
        TextView iv_select_pic_id;
    }
}
