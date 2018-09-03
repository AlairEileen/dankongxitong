package yktong.com.godofdog.service.task;

import android.accessibilityservice.AccessibilityService;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.tool.OverTask;
import yktong.com.godofdog.util.PerformClickUtils;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.SupportUtil;
import yktong.com.godofdog.value.SpValue;

import static yktong.com.godofdog.service.task.PraiseInSns.sleep;

/**
 * Created by vampire on 2017/7/31.
 */

public class ShareToSns {
    private static ShareToSns ourInstance;

    public static ShareToSns getInstance() {
        if (ourInstance == null) {
            synchronized (ShareToSns.class) {
                if (ourInstance == null) {
                    ourInstance = new ShareToSns();
                }
            }
        }
        return ourInstance;

    }

    private ShareToSns() {
    }

    public void share(AccessibilityService mService , SupportUtil supportUtil , String atyName){
            if (atyName.equals(supportUtil.getSendSnsUi())){
                String content  = (String) SPUtil.get(MyApp.getmContext(), SpValue.SNS_CONTENT,"哈哈哈哈哈啊哈哈哈哈哈哈啊哈哈哈");

                PerformClickUtils.setText(mService,PerformClickUtils.getNode(mService,"com.tencent.mm:id/cst"),content);
                sleep(200);
                PerformClickUtils.findViewIdAndClick(mService,supportUtil.getSendRequestBtnId());
                WorkManger.getInstence().doTask(SpValue.STATE_NORMAL);
                OverTask.getInstance().finishTask();
//                Intent intent = new Intent();
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setClass(MyApp.getmContext(), MainActivity.class);
//                MyApp.getmContext().startActivity(intent);
            }
    }

}