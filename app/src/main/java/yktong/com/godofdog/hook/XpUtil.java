package yktong.com.godofdog.hook;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import de.robv.android.xposed.XposedBridge;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.EventbusValueBean;
import yktong.com.godofdog.bean.xpbean.CWecatChatJson;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.ShellUtils;
import yktong.com.godofdog.value.SpValue;

/**
 * Created by vampire on 2017/8/24.
 */

public class XpUtil {

    private static String content;
    private static int index = 0;

    public static CWecatChatJson transformConten(ContentValues contentValues, Context dxContext) {
        SettingsHelper mSettings = new SettingsHelper("yktong.com.godofdog");
        CWecatChatJson cWecatChatJson = new CWecatChatJson();
        Long lastSeq = (Long) contentValues.get("lastSeq");
        cWecatChatJson.setLastSeq(String.valueOf(lastSeq));
        String digest = (String) contentValues.get("digest");
        cWecatChatJson.setDigest(digest);
        String content = (String) contentValues.get("content");
        cWecatChatJson.setContent(content);
        String digestUser = (String) contentValues.get("digestUser");
        if (null != digestUser && !digestUser.isEmpty())
            cWecatChatJson.setDigestUser(digestUser);
        Integer status = (Integer) contentValues.get("status");
        if (status!=null)
        cWecatChatJson.setStatus(status);
        String username = (String) contentValues.get("username");
        cWecatChatJson.setUsername(username);
        String msgType = (String) contentValues.get("msgType");
        int i;
        try {
             i = Integer.parseInt(msgType);
        }catch (NumberFormatException e){
            i =0;
        }
        cWecatChatJson.setMsgType(i);
        Long conversationTime = (Long) contentValues.get("conversationTime");
        cWecatChatJson.setConversationTime(String.valueOf(conversationTime));

        Integer msgCount = (Integer) contentValues.get("msgCount");
        if (msgCount != null)
            cWecatChatJson.setMsgCount(msgCount);
        Integer hasTrunc = (Integer) contentValues.get("hasTrunc");
        try {
            cWecatChatJson.setHasTrunc(hasTrunc);
        }catch (NullPointerException e){
            cWecatChatJson.setHasTrunc(0);
        }
        Integer unReadCount = (Integer) contentValues.get("unReadCount");
        if (unReadCount != null)
            cWecatChatJson.setUnReadCount(unReadCount);
        cWecatChatJson.setContent(content);
        Integer isSend = (Integer) contentValues.get("isSend");
        try {
            cWecatChatJson.setIsSend(isSend);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        Long flag = (Long) contentValues.get("flag");
        cWecatChatJson.setFlag(String.valueOf(flag));
        String nick = mSettings.getString(username, "");
        XposedBridge.log("-nickName:---"+nick+"-----");
        if (nick.isEmpty()) {
            cWecatChatJson.setNickname(username);
//
//            Intent intent = new Intent();
//            ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.chatting.En_5b8fbb1e");
//            intent.setComponent(comp);
//            intent.putExtra("Chat_User", username);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            dxContext.startActivity(intent);
////            String[] shell = new String[]{"am start -n com.tencent.mm/com.tencent.mm.ui.chatting.En_5b8fbb1e -e Chat_User " + username};
////            int result = ShellUtils.execCommand(shell, true).result;
//
//            HashMap<String,String> map = new HashMap<>();
//            map.put(SpValue.USER_ID,username);
//            EventBus.getDefault().post(new EventbusValueBean(map));
//
//            Intent bro = new Intent();
//            bro.setAction("new_user");
//            bro.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//            bro.putExtra(SpValue.USER_ID,username);
//            dxContext.sendBroadcast(bro);

        } else {
            cWecatChatJson.setNickname(nick);
        }
        return cWecatChatJson;
    }
}
