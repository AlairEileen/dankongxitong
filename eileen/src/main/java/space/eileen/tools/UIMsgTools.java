package space.eileen.tools;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Eileen on 2017/7/12.
 */

public class UIMsgTools {
    public static void showToastNetError(final Activity activity){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,"网络请求失败！",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
