package yktong.com.godofdog.tool;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vampire on 2017/8/3.
 */

public class SavePic {
    private static SavePic ourInstance;
    private Context mContext;

    public static SavePic getInstance() {
        if (ourInstance == null) {
            synchronized (SavePic.class) {
                if (ourInstance == null) {
                    ourInstance = new SavePic();
                }
            }
        }
        return ourInstance;

    }

    private SavePic() {
    }

    public void savePic(Context context , ArrayList<String> urls){
        FileUtility.deleteFolder("/storage/emulated/0/Download/DXYH/PIC/");
        ImgDonwload.donwloadImg(mContext,urls);
        mContext =context;
    }

    public ArrayList<File> getPic(){
        File file = new File("/storage/emulated/0/Download/DXYH/PIC/");
        if (file.isDirectory()){
            File[] files = file.listFiles();
            List<File> fileArrayList = Arrays.asList(files);
            return new ArrayList<>(fileArrayList);
        }else {
            return null;
        }

    }
}