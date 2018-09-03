package space.eileen.tools;

import android.app.Activity;
import android.content.Intent;

import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;

import java.util.ArrayList;

import static com.foamtrace.photopicker.PhotoPickerActivity.EXTRA_RESULT;

/**
 * Created by Eileen on 2017/7/7.
 */

public class SelectImagesTool {


    private static int selectPicCode = 8000;

    public static void showAddPicView(Activity activity, String picPath) {
        ArrayList<String> paths = new ArrayList<>();
        if (picPath != null)
            paths.add(picPath);
        showAddPicsView(activity, paths, 1);
    }
    public static void showAddPicView(Activity activity, String picPath,int requestCode) {
        selectPicCode=requestCode;
        ArrayList<String> paths = new ArrayList<>();
        if (picPath != null)
            paths.add(picPath);
        showAddPicsView(activity, paths, 1);
    }
    public static void showAddPicsView(Activity activity, ArrayList<String> selectedPicsPath, int maxTotal) {
        PhotoPickerIntent intent = new PhotoPickerIntent(activity);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照， 默认false
        intent.setMaxTotal(maxTotal); // 最多选择照片数量，默认为9
        intent.setSelectedPaths(selectedPicsPath); // 已选中的照片，ArrayList集合
        activity.startActivityForResult(intent, selectPicCode);
    }

    public static ArrayList<String> onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == selectPicCode) {
            try {
                ArrayList<String> selectNewPics = data.getStringArrayListExtra(EXTRA_RESULT);
                return selectNewPics;
//                Toast.makeText(this, "addPicsToView:" + selectNewPics.size(), Toast.LENGTH_SHORT).show();
            } catch (NullPointerException npe) {
            }
        }
        return null;
    }
}
