package yktong.com.godofdog.tool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.bean.tel.CommunicateData;
import yktong.com.godofdog.bean.tel.CallReponse;
import yktong.com.godofdog.tool.datebase.DBTool;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.SortList;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/8/15.
 */

public class CallUtil {

    private static ArrayList<CommunicateData> jsonArray;
    private static CommunicateData data;

    public CallUtil() {
    }

    @SuppressLint("SimpleDateFormat")
    public static String StringgetCallHistoryList(Context context, ContentResolver cr, String path) {
        ArrayList<CommunicateData> beanArrayList = new ArrayList<>();
        Cursor cs;
        if (ActivityCompat.checkSelfPermission(MyApp.getmContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return null;
        }
        cs = cr.query(CallLog.Calls.CONTENT_URI,
                new String[]{
                        CallLog.Calls.CACHED_NAME,
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.TYPE,
                        CallLog.Calls.DATE,
                        CallLog.Calls.DURATION
                }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);

        String callHistoryListStr = "";
        int i = 0;
        if (cs != null && cs.getCount() > 0) {
            for (cs.moveToFirst(); !cs.isAfterLast() & i < 50; cs.moveToNext()) {
                String callName = cs.getString(0);
                String callNumber = cs.getString(1);
                //通话类型
                int callType = Integer.parseInt(cs.getString(2));
                String callTypeStr = "";
                switch (callType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr = "呼入";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr = "呼出";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr = "未接";
                        break;
                    default:
                        callType = CallLog.Calls.OUTGOING_TYPE;
                        break;
                }
                //拨打时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date callDate = new Date(Long.parseLong(cs.getString(3)));
                String callDateStr = sdf.format(callDate);
                //通话时长
                int callDuration = Integer.parseInt(cs.getString(4));
                int min = callDuration / 60;
                int sec = callDuration % 60;
                CommunicateData bean = new CommunicateData();
                String callDurationStr = min + "分" + sec + "秒";
                String callOne = "类型：" + callTypeStr + ", 称呼：" + callName + ", 号码："
                        + callNumber + ", 通话时长：" + callDurationStr + ", 时间:" + callDateStr
                        + "\n---------------------\n";
                bean.setCommunicatetype(callType);
                if (callName == null) {
                    callName = "";
                }
                bean.setCommunicatename(callName);
                bean.setCommunicatenumber(callNumber);
                bean.setCommunicateduration(callDuration);
                bean.setCommunicatetime(callDateStr);
                String substring = cs.getString(3);
                bean.setCallDate(Long.valueOf(substring));
                beanArrayList.add(bean);
                callHistoryListStr += callOne;
                i++;
            }
        }
        SortList<CommunicateData> sortList = new SortList<>();
        sortList.Sort(beanArrayList,"getCallDate","desc");
        if (!beanArrayList.isEmpty()){
            data = beanArrayList.get(0);
            jsonArray = new ArrayList<>();
            jsonArray.add(data);
        }else {
            return "false";
        }
        String  lastTime  = (String) SPUtil.get(MyApp.getmContext(),"callTime","");
        Log.d("CallUtil", lastTime);
        Log.d("CallUtil", data.getCommunicatetime());
        if (data.getCommunicatetime().equals(lastTime)){
            return "false";
        }
        Gson gson = new Gson();
        String toJson = gson.toJson(data);
        HashMap<String,String> param = new HashMap<>();
        param.put("cCommunicateDataJson",toJson);
        if (path !=null){
            HashMap<String,List<File>> fileHashMap = new HashMap<>();
            ArrayList<File> fileArrayList = new ArrayList<>();
            fileArrayList.add(new File(path));
            fileHashMap.put("file",fileArrayList);
            Log.d("CallUtil", UrlValue.ADD_LINK_DATE + MyApp.userId);
            NetTool.getInstance().upLoadMultiFile(UrlValue.ADD_LINK_DATE + MyApp.userId, param, fileHashMap, NormalResultBean.class, new OkHttpUtil.ReqProgressCallBack<NormalResultBean>() {
                @Override
                public void onProgress(long total, long current) {

                }

                @Override
                public void onSuccess(NormalResultBean response) {
                    Log.d("CallUtil", response.getCode());
                    if (response.getCode().equals("1")){
                        Log.d("CallUtil", "添加成功");
                        SPUtil.putAndApply(MyApp.getmContext(),"callTime",data.getCommunicatetime());
                    }
                }

                @Override
                public void onFailed(String msg) {
                    Log.d("CallUtil",msg);
                }
            });
        }else {
            NetTool.getInstance().upLoadJson(UrlValue.ADD_LINK_DATE + MyApp.userId, param, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                @Override
                public void onSuccess(NormalResultBean response) {
                    Log.d("CallUtil", response.getCode());
                    if (response.getCode().equals("1")){
                        Log.d("CallUtil", "success");
                        SPUtil.putAndApply(MyApp.getmContext(),"callTime",data.getCommunicatetime());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.d("CallUtil", e.getMessage());

                }
            });
        }
        Log.d("CallUtil", toJson);
        // TODO: 2017/8/18 表示

        return callHistoryListStr;
    }
}
