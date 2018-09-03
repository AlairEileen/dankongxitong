package yktong.com.godofdog.tool.net;


import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.robv.android.xposed.XposedBridge;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.tool.thread.ThreadPool;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/*
 * Created by vampire on 2016年11月07日16:50:59.
 */

public class OkHttpUtil implements NetInterface {
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final String GET = "get";
    public static final String POST = "post";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient mClient;
    //这样定义的handler对象无论在哪里创建的 都是属于主线程的
    private Handler mHandler =
            new Handler(Looper.getMainLooper());
    private Gson mGson;

    public OkHttpUtil() {
        super();
        mGson = new Gson();
        //获取系统的sd卡
        File path =
                Environment.getExternalStorageDirectory();
        //初始化okhttp
        mClient = new OkHttpClient.Builder()
                //设置缓存位置 以及缓存大小
                .cache(new Cache(path, 10 * 1024 * 1024))
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public <T> void startRequest(final String type, final String url, final Class<T> tClass, final OnHttpCallBack<T> callBack) {
        Log.d(TAG, "Thread.currentThread():" + Thread.currentThread());
        Log.d("OkHttpUtil", url);
        ThreadPool.thredP.execute(() -> {
            Log.d("OkHttpUtil", Thread.currentThread().getName());
            if (type.equals(POST)) {
                Request request;
                //有的请求需要加body
                FormBody.Builder builder = new FormBody.Builder();
                RequestBody requestBody = builder.build();
                request = new Request
                        .Builder().url(url)
                        .post(requestBody)
                        .build();
                Log.d("OkHttpUtil", url);
                mClient.newCall(request).enqueue(new Callback() {

                    private Object result;

                    @Override
                    public void onFailure(Call call, final IOException e) {
                        mHandler.post(() -> {
                            callBack.onError(e);
                            Log.d("OkHttpUtil", "请求失败" + e.toString());
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String str = response.body().string().trim();
                        Log.e("OkHttpUtil", str);

                        try {
                            result = mGson.fromJson(str, tClass);
                        } catch (Exception e) {
                            Log.d("OkHttpUtil", e.toString());
                        }
                        mHandler.post(() -> callBack.onSuccess((T) result));

                    }

                });
            } else {
                Request request;
                request = new Request
                        .Builder().url(url)
                        .build();

                mClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        mHandler.post(() -> {
                            Log.d("OkHttpUtil", "请求失败" + e.toString() + "\n" + url);
                            callBack.onError(e);
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("OkHttpUtil", response.toString());
                        String str = response.body().string().trim();
//                        try {
//                            NormalResultBean bean = mGson.fromJson(str,NormalResultBean.class);
//                            Log.e(TAG,"code : " + bean.getCode());
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
                        try {
                            final T result = mGson.fromJson(str, tClass);
                            Log.d("OkHttpUtil", tClass.getName());
                            mHandler.post(() -> callBack.onSuccess(result));
                        } catch (JsonSyntaxException e) {
                            Log.d("OkHttpUtil", "e:" + e);
                            e.printStackTrace();
                        }

                    }

                });
            }

        });
    }

    @Override
    public <T> void postRequest(String url, String json, Class<T> tClass, OnHttpCallBack<T> callBack) {
        Log.d("OkHttpUtil", url);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url).post(body).build();
        mClient.newCall(request).enqueue(new Callback() {
            private Object result;

            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(() -> {
                    callBack.onError(e);
                    Log.d("OkHttpUtil", "请求失败" + e.toString());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string().trim();
                Log.e("OkHttpUtil", str);

                try {
                    result = mGson.fromJson(str, tClass);
                } catch (Exception e) {
                    Log.d("OkHttpUtil", e.toString());
                }
                mHandler.post(() -> callBack.onSuccess((T) result));
            }
        });

    }

    /**
     * 上传文件
     *
     * @param actionUrl 接口地址
     * @param paramsMap 参数
     * @param callBack  回调
     * @param <T>
     */
    @Override
    public <T> void upLoadFile(String actionUrl, HashMap<String, Object> paramsMap, final Class<T> tClass, final ReqProgressCallBack<T> callBack) {
        try {
            //补全请求地址
//            String requestUrl = String.format("%s/%s", mClient, actionUrl);
            String requestUrl = actionUrl;
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            for (String key : paramsMap.keySet()) {
                Object object = paramsMap.get(key);
                if (!(object instanceof File)) {
                    builder.addFormDataPart(key, object.toString());
                } else {
                    File file = (File) object;
                    builder.addFormDataPart(key, file.getName(), createProgressRequestBody(MEDIA_TYPE_MARKDOWN, file, callBack));
                }
            }
            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            final Call call = mClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
            call.enqueue(new Callback() {
                public Object result;

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, e.toString());
                    failedCallBack("上传失败", callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG, "response ----->" + string);
                        successCallBack(string, callBack);
                    } else {
                        failedCallBack("上传失败", callBack);
                    }
                }


                private void failedCallBack(String str, ReqProgressCallBack<T> callBack) {
                    callBack.onFailed(str);
                }

                private void successCallBack(String objT, ReqProgressCallBack<T> callBack) {
                    result = mGson.fromJson(objT, tClass);
                    callBack.onSuccess((T) result);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 上传多文件
     * @param actionUrl 上传地址
     * @param paramsMap 参数列表
     * @param paramFilesMap 文件列表
     * @param tClass 响应bean class
     * @param callBack 响应监听
     * @param <T> 响应bean
     */
    @Override
    public <T> void upLoadMultiFile(String actionUrl, Map<String, String> paramsMap,
                                    Map<String, List<File>> paramFilesMap, final Class<T> tClass, final ReqProgressCallBack<T> callBack) {
        try {
            //补全请求地址
//            String requestUrl = String.format("%s/%s", mClient, actionUrl);
            String requestUrl = actionUrl;

            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            if (paramsMap != null) {
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    builder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }
            //追加参数文件
            if (paramFilesMap != null) {
                int i=0;
                for (Map.Entry<String, List<File>> entry : paramFilesMap.entrySet()) {
                    for (File file : entry.getValue()) {
                        builder.addFormDataPart(entry.getKey()+i++, file.getName(), createProgressRequestBody(MEDIA_TYPE_MARKDOWN, file, callBack));
                    }
                }
            }
            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            final Call call = mClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
            call.enqueue(new Callback() {
                public Object result;

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, e.toString());
                    failedCallBack("上传失败", callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG, "response ----->" + string);
                        successCallBack(string, callBack);
                    } else {
                        failedCallBack("上传失败", callBack);
                    }
                }


                private void failedCallBack(String str, ReqProgressCallBack<T> callBack) {
                    callBack.onFailed(str);
                }

                private void successCallBack(String objT, ReqProgressCallBack<T> callBack) {
                    result = mGson.fromJson(objT, tClass);
                    callBack.onSuccess((T) result);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public <T> void upLoadJson(String actionUrl, Map<String, String> paramsMap, Class<T> tClass, OnHttpCallBack<T> callBack) {
        try {
            //补全请求地址
//            String requestUrl = String.format("%s/%s", mClient, actionUrl);
            String requestUrl = actionUrl;

            MultipartBody.Builder builder = new MultipartBody.Builder();
            //设置类型
            builder.setType(MultipartBody.FORM);
            //追加参数
            if (paramsMap != null) {
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    builder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }

            //创建RequestBody
            RequestBody body = builder.build();
            //创建Request
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            final Call call = mClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            XposedBridge.log(e.getMessage());
        }
    }

    @Override
    public <T> void upLoadFile(String actionUrl, ArrayList<String> files, Class<T> tClass, ReqProgressCallBack<T> callBack) {
        try {
            Log.d(TAG, actionUrl);
            String requestUrl = actionUrl;
            MultipartBody.Builder mbody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (int i = 0; i < files.size(); i++) {
                File picFile = new File(files.get(i));
                Log.d(TAG, files.get(i));
                if (picFile.exists()) {
                    mbody.addFormDataPart("image" + i, picFile.getName(), createProgressRequestBody(MediaType.parse("image/png"), picFile, callBack));
                }
            }
            //创建RequestBody
            RequestBody body = mbody.build();
            //创建Request
            final Request request = new Request.Builder().url(requestUrl).post(body).build();
            final Call call = mClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
            call.enqueue(new Callback() {
                public Object result;

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, e.toString());
                    failedCallBack("上传失败", callBack);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String string = response.body().string();
                        Log.e(TAG, "response ----->" + string);
                        successCallBack(string, callBack);
                    } else {
                        failedCallBack("上传失败", callBack);
                    }
                }

                private void failedCallBack(String str, ReqProgressCallBack<T> callBack) {
                    callBack.onFailed(str);
                }

                private void successCallBack(String objT, ReqProgressCallBack<T> callBack) {
                    Looper.prepare();
                    result = mGson.fromJson(objT, tClass);
                    callBack.onSuccess((T) result);
                    Looper.loop();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    /**
     * 创建带进度的RequestBody
     *
     * @param contentType MediaType
     * @param file        准备上传的文件
     * @param callBack    回调
     * @param <T>
     * @return
     */
    public <T> RequestBody createProgressRequestBody(final MediaType contentType, final File file, final ReqProgressCallBack<T> callBack) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        Log.e(TAG, "current------>" + current);
                        Log.e(TAG, "remaining------>" + remaining);
                        progressCallBack(remaining, current, callBack);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    /**
     * 下载文件
     *
     * @param fileUrl     文件url
     * @param destFileDir 存储目标目录
     * @param fileName
     */
    @Override
    public <T> void downLoadFile(String fileUrl, final String destFileDir, final ReqProgressCallBack<T> callBack, String[] fileName) {
//        final String fileName = MD5.encode(fileUrl);
        File folder = new File(destFileDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String fileNi = null;
        if (fileName != null && fileName.length > 0) {
            fileNi = fileName[0];
        } else {
            fileNi = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        }
        final File file = new File(destFileDir, fileNi);
        if (file.exists()) {
            callBack.onSuccess((T) file);
            return;
        }
        final Request request = new Request.Builder().url(fileUrl).build();
        final Call call = mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.toString());
                callBack.onFailed("下载失败:"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[1024*1024*5];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
//                    Log.e(TAG, "total------>" + total);
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
//                        Log.e(TAG, "current------>" + current);
                        callBack.onProgress(total,current);
                    }
                    fos.flush();
                    callBack.onSuccess((T) file);
                } catch (IOException e) {
                    Log.e(TAG, e.toString());
                    callBack.onFailed("下载失败");
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        });
    }


    /**
     * 统一处理进度信息
     *
     * @param total    总计大小
     * @param current  当前进度
     * @param callBack
     * @param <T>
     */
    private <T> void progressCallBack(final long total, final long current, final ReqProgressCallBack<T> callBack) {
        mHandler.post(() -> {
            if (callBack != null) {
                callBack.onProgress(total, current);
            }
        });
    }

    public interface ReqProgressCallBack<T> {
        /**
         * 响应进度更新
         */
        void onProgress(long total, long current);

        void onSuccess(T response);

        void onFailed(String msg);
    }
}


