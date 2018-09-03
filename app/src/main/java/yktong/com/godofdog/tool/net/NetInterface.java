package yktong.com.godofdog.tool.net;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface NetInterface {

    <T> void startRequest(String type, String url, Class<T> tClass, OnHttpCallBack<T> callBack);

    <T> void postRequest(String url, String json, Class<T> tClass, OnHttpCallBack<T> callBack);


    <T> void upLoadFile(String actionUrl, HashMap<String, Object> paramsMap, final Class<T> tClass, OkHttpUtil.ReqProgressCallBack<T> callBack);

    <T> void upLoadFile(String actionUrl, ArrayList<String> files, final Class<T> tClass, OkHttpUtil.ReqProgressCallBack<T> callBack);

    <T> void downLoadFile(String fileUrl, String destFileDir, OkHttpUtil.ReqProgressCallBack<T> callBack, String[] fileName);
    <T> void upLoadMultiFile(String actionUrl, Map<String, String> paramsMap, Map<String, List<File>> paramFilesMap,  Class<T> tClass,  OkHttpUtil.ReqProgressCallBack<T> callBack);
    <T> void upLoadJson(String actionUrl, Map<String, String> paramsMap, Class<T> tClass,  OnHttpCallBack<T> callBack);
}
