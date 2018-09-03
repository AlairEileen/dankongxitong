package yktong.com.godofdog.tool.net;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;


public class NetTool extends OkHttpClient implements NetInterface {
    private static NetTool ourInstance;
    private NetInterface mNetInterface;

    private NetTool() {
        mNetInterface = new OkHttpUtil();
    }

    public static NetTool getInstance() {
        if (ourInstance == null) {
            synchronized (NetTool.class) {
                if (ourInstance == null) {
                    ourInstance = new NetTool();
                }
            }
        }
        return ourInstance;
    }

    @Override
    public <T> void startRequest(String type, String url, Class<T> bean, OnHttpCallBack<T> callBack) {
        mNetInterface.startRequest(type, url, bean, callBack);
    }

    @Override
    public <T> void postRequest(String url, String json, Class<T> tClass, OnHttpCallBack<T> callBack) {
        mNetInterface.postRequest(url, json, tClass, callBack);
    }

    public <T> void upLoadFile(String actionUrl, HashMap<String, Object> paramsMap, final Class<T> tClass, final OkHttpUtil.ReqProgressCallBack<T> callBack) {
        mNetInterface.upLoadFile(actionUrl, paramsMap, tClass, callBack);
    }

    @Override
    public <T> void upLoadFile(String actionUrl, ArrayList<String> files, Class<T> tClass, OkHttpUtil.ReqProgressCallBack<T> callBack) {
        mNetInterface.upLoadFile(actionUrl, files, tClass, callBack);
    }

    public <T> void downLoadFile(String fileUrl, String destFileDir, OkHttpUtil.ReqProgressCallBack<T> callBack, String... fileName) {
        mNetInterface.downLoadFile(fileUrl, destFileDir, callBack, fileName);
    }

    public <T> void upLoadMultiFile(String actionUrl, Map<String, String> paramsMap, Map<String, List<File>> paramFilesMap, Class<T> tClass, OkHttpUtil.ReqProgressCallBack<T> callBack) {
        mNetInterface.upLoadMultiFile(actionUrl, paramsMap, paramFilesMap, tClass, callBack);
    }

    @Override
    public <T> void upLoadJson(String actionUrl, Map<String, String> paramsMap, Class<T> tClass, OnHttpCallBack<T> callBack) {
        mNetInterface.upLoadJson(actionUrl,paramsMap,tClass,callBack);
    }
}
