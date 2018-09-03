package yktong.com.godofdog.util.crash;
import java.io.File;

import android.content.Context;
/**
 * Created by vampire on 2017/7/31.
 */

public class CrashEmailReport extends AbstractCrashReportHandler {
    private String mReceiveEmail;

    public CrashEmailReport(Context context) {
        super(context);
    }

    public void setReceiver(String receiveEmail) {
        mReceiveEmail = receiveEmail;
    }

    @Override
    protected void sendReport(String title, String body, File file) {
        LogMail sender = new LogMail().setUser("ykt_crash@163.com").setPass("aishiyu1234")
                .setFrom("ykt_crash@163.com").setTo(mReceiveEmail).setHost("smtp.163.com")
                .setPort("465").setSubject(title).setBody(body);
        sender.init();
        try {
            sender.addAttachment(file.getPath(), file.getName());
            sender.send();
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}