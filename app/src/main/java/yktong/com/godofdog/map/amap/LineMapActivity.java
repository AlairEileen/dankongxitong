package yktong.com.godofdog.map.amap;

import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.tencent.mm.opensdk.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import space.eileen.tools.XString;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.bean.map_beans.UserLocationBean;
import yktong.com.godofdog.bean.map_beans.UserLocationResponseBean;
import yktong.com.godofdog.popup.MarkerInfoPopup;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

import static yktong.com.godofdog.map.amap.LineMapActivity.MarkerType.STOPPING;

public class LineMapActivity extends BaseActivity {

    public final static String USER_ID = "userId";
    MapView mMapView = null;
    Map<LatLng, Boolean> markerList = new HashMap<>();
    List<MapMarker> mapMarkerList = new ArrayList<>();
    AMap aMap;
    private int userId = -1;
    private MapMarkerInfoView mapMarkerInfoView;
    private WindowManager.LayoutParams params;
    private MarkerInfoPopup markerInfoPopup;
    private MarkerInfoWindowAdapter markerInfoWindowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_map);


        //获取地图控件引用
        mMapView = bindView(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        Log.d("####LineMapActivity", "onCreate");
//        markerInfoWindowAdapter=new MarkerInfoWindowAdapter(this);
//mMapView.getMap().setInfoWindowAdapter(markerInfoWindowAdapter);
//        MyLocationStyle myLocationStyle;
//        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.interval(10000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        mMapView.getMap().setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        mMapView.getMap().getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
//        mMapView.getMap().setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_line_map;
    }

    @Override
    protected void initView() {
        if (mapMarkerInfoView == null) {
            mapMarkerInfoView = new MapMarkerInfoView();
            mapMarkerInfoView.tv_title = bindView(R.id.tv_title);
            mapMarkerInfoView.tv_time_text = bindView(R.id.tv_time_text);
            mapMarkerInfoView.tv_time = bindView(R.id.tv_time);
            mapMarkerInfoView.tv_address = bindView(R.id.tv_address);
        }
    }

    @Override
    protected void initData() {
        userId = getIntent().getIntExtra(USER_ID, -1);

        if (userId == -1) return;
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.FIND_USER_LINE + userId, UserLocationResponseBean.class, new OnHttpCallBack<UserLocationResponseBean>() {
            @Override
            public void onSuccess(UserLocationResponseBean response) {
                response.doResponse(() -> {
                    Log.d("####initData", response.toString() + "");
                    if (response.getUserLocationBeanList() != null && response.getUserLocationBeanList().size() != 0) {
                        List<LatLng> latLngList = convertToLatLngList(response.getUserLocationBeanList());
                        if (latLngList == null) return;
                        int color = getResources().getColor(R.color.theme_blue);

                        mMapView.getMap().addPolyline(new PolylineOptions().
                                addAll(latLngList).width(10).color(color));
                        setStopLocation(latLngList, response);
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void setStopLocation(List<LatLng> latLngList, UserLocationResponseBean response) {
        float round = 300f;
        int stopMinutes = 30;
        Map<LatLng, Integer> latLngIntegerMap = new HashMap<>();
        Map<LatLng,Map<String,String>> latLngMapMap=new HashMap<>();
        List<StopPoint> stopPoints=new ArrayList<>();
        for (int i = 0; i < latLngList.size(); i++) {
//            Map<String,String> map=new HashMap<>();
            StopPoint stopPoint=new StopPoint();
            for (int j = i + 1; j < latLngList.size(); j++) {
                float distance = AMapUtils.calculateLineDistance(latLngList.get(i), latLngList.get(j));
                Log.d("####distance:" + distance, latLngList.get(i).toString() + "\r\n" + latLngList.get(j).toString());
                if (distance <= round) {
                    int sumMinutes=XString.calcMines(XString.toMiniForLong(response.getUserLocationBeanList().get(i).getDateTime()),XString.toMiniForLong(response.getUserLocationBeanList().get(j).getDateTime()));
                    if (sumMinutes>= stopMinutes) {
                        stopPoint.endTime=XString.toMiniForLong(response.getUserLocationBeanList().get(j).getDateTime());
                        stopPoint.startTime=XString.toMiniForLong(response.getUserLocationBeanList().get(i).getDateTime());
                        stopPoint.timeSpan=sumMinutes;
                        stopPoint.latLng=latLngList.get((j+i)/2);
                    }
                    if (j == latLngList.size() - 1) {
                        i = j;
                    }
                } else {
                    i = j;
                    break;
                }
            }
            stopPoints.add(stopPoint);
        }
        putMarkerList(latLngList, stopPoints, response);
        setMarkerListener();
    }

    private void setMarkerListener() {
        mMapView.getMap().setOnMarkerClickListener(marker -> {
            Log.d("setOnMarkerClickListener", mapMarkerList.size() + "");
            for (MapMarker mapMarker : mapMarkerList) {
                if (marker.getPosition().equals(mapMarker.latLng)) {
                    if (!mapMarker.isShow) {
                        showMapMarkerInfo(mapMarker, true);
                        mapMarker.isShow = true;
                    } else {
                        showMapMarkerInfo(mapMarker, false);
                        mapMarker.isShow = false;
                    }
                }
            }
            return true;
        });
    }

    private void putMarkerList(List<LatLng> latLngList,  List<StopPoint> stopPoints, UserLocationResponseBean response) {
        putMarkerList(latLngList.get(0), response, MarkerType.START, null);
        mMapView.getMap().moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLngList.get(latLngList.size() - 1), 18, 0, 0)));
        putMarkerList(latLngList.get(latLngList.size() - 1), response, MarkerType.STOP, null);
        for (StopPoint stopPoint:stopPoints) {
            putMarkerList(stopPoint.latLng, response, STOPPING, stopPoint);
        }
//        mMapView.getMap().setInfoWindowAdapter();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
//                boolean canCancel = true;
                if (mMapView.getMap()==null||mMapView.getMap().getMapScreenMarkers()==null)return;
                for (Marker marker : mMapView.getMap().getMapScreenMarkers()) {
                    if (marker.isInfoWindowEnable() && !marker.isInfoWindowShown()) {
                        marker.showInfoWindow();
//                        canCancel = false;
                    }
                }
//                if (canCancel) {
//                    cancel();
//                }
            }
        }, 500, 500);
    }

    private void putMarkerList(LatLng latLng, UserLocationResponseBean response, MarkerType markerType, StopPoint stopPoint) {
        int icon = -1;
        boolean enableInfo = false;
        String timeText = "";
        String time = "";
        String title = "";
        switch (markerType) {
            case START:
                icon = R.mipmap.icon_q;
                timeText = "开始时间：";
                title = "开始位置";
                break;
            case STOP:
                icon = R.mipmap.icon_z;
                timeText = "上传时间：";
                title = "最后位置";
                break;
            case STOPPING:
                icon = R.mipmap.icon_t;
                timeText = "停留时间：";
                title = "停留位置";
                enableInfo = true;
                break;
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(icon));
        markerOptions.infoWindowEnable(enableInfo);
        String startTimeText="",stopTimeText="";
        if (stopPoint!=null){
            startTimeText=stopPoint.startTime;
            stopTimeText=stopPoint.endTime;
        }
        if (enableInfo) {
            time =XString.convertToTime(stopPoint.timeSpan);
            markerOptions.title(time);

        } else {
            time = XString.toMiniForLong(response.getUserLocationBeanList().get(response.getUserLocationBeanList().size() - 1).getDateTime());
        }
        mMapView.getMap().addMarker(markerOptions);
        MapMarker mapMarker = new MapMarker();
        mapMarker.latLng = latLng;
        mapMarker.isShow = false;
        mapMarker.time =markerType!=STOPPING?time:"时长："+time+"\n开始："+startTimeText+"\n结束："+stopTimeText;
       mapMarker.time_text=timeText;
        mapMarker.title = title;
        mapMarkerList.add(mapMarker);
    }


    private void searchInfo(LatLng latLng, SearchListener searchListener) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(LineMapActivity.this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {

            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                searchListener.success(regeocodeResult.getRegeocodeAddress().getFormatAddress());
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
            }
        });
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }


    private void showMapMarkerInfo(MapMarker mapMarker, boolean visible) {
        if (visible) {
            searchInfo(mapMarker.latLng, (address) -> {
                runOnUiThread(() -> {
                    mapMarker.address = address;
                    markerInfoPopup = new MarkerInfoPopup(this, mapMarker);
                    params = getWindow().getAttributes();
                    //当弹出Popupwindow时，背景变半透明
                    params.alpha = 0.7f;
                    getWindow().setAttributes(params);
                    //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
                    markerInfoPopup.setOnDismissListener(() -> {
                        params = getWindow().getAttributes();
                        params.alpha = 1f;
                        getWindow().setAttributes(params);
                    });
                    markerInfoPopup.showAtLocation(findViewById(R.id.map), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                });
            });


        } else {
            markerInfoPopup.dismiss();
        }

    }


    private List<LatLng> convertToLatLngList(List<UserLocationBean> userLocationBeanList) {
        List<LatLng> latLngList = new ArrayList<>();
        for (UserLocationBean userLocationBean : userLocationBeanList) {
            latLngList.add(new LatLng(userLocationBean.getLatitude(), userLocationBean.getLongitude()));
        }
        return latLngList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (markerInfoPopup != null) markerInfoPopup.dismiss();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    enum MarkerType {
        START, STOPPING, STOP;
    }

    interface SearchListener {
        void success(String address);
    }

    public class MapMarker {
        public String title;
        public String time;
        public String time_text;
        public String timeSpan;
        public String address;
        LatLng latLng;
        boolean isShow;
    }

    private class MapMarkerInfoView {
        public TextView tv_title;
        public TextView tv_time_text;
        public TextView tv_time;
        public TextView tv_address;
    }

    private class StopPoint{
        public LatLng latLng;
        public String startTime;
        public String endTime;
        public int timeSpan;
    }
}
