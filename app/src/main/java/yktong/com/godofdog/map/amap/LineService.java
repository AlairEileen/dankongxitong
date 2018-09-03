package yktong.com.godofdog.map.amap;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceStatusListener;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import yktong.com.godofdog.bean.ResponseBaseBean;
import yktong.com.godofdog.bean.map_beans.UserLocationBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

public class LineService extends Service {
    private static boolean isRun = false;
    LineListener lineListener = new LineListener();
    Timer timer = new Timer();
    TimerTask saveLocationTask = new LocationSaveTask();
//    boolean isMove;
    private LBSTraceClient lbsTraceClient;
    private int id;
    private LatLng oldLatLng;
    private String locationJson;

    public LineService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!isRun) {
            //开始记录轨迹，每2s记录一次轨迹，每隔5个点合并请求一次纠偏并回调。
            lbsTraceClient = LBSTraceClient.getInstance(getApplicationContext());
            lbsTraceClient.startTrace(lineListener); //开始采集,需要传入一个状态回调监听。
            id = intent.getIntExtra("id", -1);
            timer.schedule(saveLocationTask, 6 * 1000, 3 * 60 * 1000);
            isRun = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lbsTraceClient.stopTrace();//在不需要轨迹纠偏时（如行程结束），可调用此接口结束纠偏
    }


    class LineListener implements TraceStatusListener {


        @Override
        public void onTraceStatus(List<TraceLocation> list, List<LatLng> list1, String s) {
//            List<LatLng> list2=new ArrayList<>();
//            for (TraceLocation traceLocation : list) {
//                list2.add(new LatLng(traceLocation.getLatitude(),traceLocation.getLongitude()));
////                Log.d("onTraceStatus-list", "latitude:" + traceLocation.getLatitude() + ",longitude" + traceLocation.getLongitude());
//            }
//            for (LatLng latLng : list1) {
////                Log.d("onTraceStatus-list1", "latitude:" + latLng.latitude + ",longitude" + latLng.longitude);
//            }
            if (list==null||list.size()==0)return;
            calcTraceLocation(list);
            Log.d("onTraceStatus", s);

        }

        private void calcTraceLocation(List<TraceLocation> list) {
            double latSum = 0, lngSum = 0, lat, lng;
            for (TraceLocation traceLocation : list) {
                latSum += traceLocation.getLatitude();
                lngSum += traceLocation.getLongitude();
//                Log.d("calcTraceLocation-old", "latitude:" + traceLocation.getLatitude() + ",longitude:" + traceLocation.getLongitude());
            }

            lat = convertToDouble(latSum / list.size(), 6);
            lng = convertToDouble(lngSum / list.size(), 6);
//            Log.d("calcTraceLocation", "lat:" + lat + ",lng:" + lng);
            if (oldLatLng == null || convertToDouble(oldLatLng.latitude, 4) != convertToDouble(lat, 4) || convertToDouble(oldLatLng.longitude, 4) != convertToDouble(lng, 4)) {
                oldLatLng = new LatLng(lat, lng);
                UserLocationBean userLocationBean = new UserLocationBean();
                userLocationBean.setUserId(id);
                userLocationBean.setLatitude(lat);
                userLocationBean.setLongitude(lng);
                locationJson = new Gson().toJson(userLocationBean);
//                isMove = true;
                Log.d("UserLocationBean-json", locationJson);
            }

        }

        private double convertToDouble(double dou, int length) {
            BigDecimal bigDecimal = new BigDecimal(dou);
            double num2 = bigDecimal.setScale(length, BigDecimal.ROUND_HALF_UP).doubleValue();
            return num2;
        }
    }

    class LocationSaveTask extends TimerTask {

        @Override
        public void run() {
//            if (isMove) {
//                isMove = false;
            if (locationJson==null)return;
                Log.d("UserLocationBean-upload", locationJson);
                if (isPersonTime())return;
                NetTool.getInstance().postRequest( UrlValue.REQUEST_UPLOAD_USER_LOCATION ,locationJson, ResponseBaseBean.class, new OnHttpCallBack<ResponseBaseBean>() {
                    @Override
                    public void onSuccess(ResponseBaseBean response) {
                        if (response==null)return;
                        response.doResponse(() -> {
                            Log.d("UserLocationBean", "onSuccess");
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
//            }
        }
    }

    private boolean isPersonTime() {
        Date date=new Date();
        boolean hours=date.getHours()>=8&&date.getHours()<=17;
        if (!hours){
            return true;
        }
        return false;
    }
}
