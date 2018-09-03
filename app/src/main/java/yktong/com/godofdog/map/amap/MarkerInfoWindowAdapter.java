package yktong.com.godofdog.map.amap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

import yktong.com.godofdog.R;

/**
 * Created by Eileen on 2017/9/1.
 */

class MarkerInfoWindowAdapter implements AMap.InfoWindowAdapter {

    private final Context context;
    private View infoWindow;

    public MarkerInfoWindowAdapter(Context context) {
        this.context=context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (infoWindow == null) {
            infoWindow = LayoutInflater.from(context).inflate(
                    R.layout.window_marker_info, null);
        }
        render(marker, infoWindow);
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
//// TODO: 2017/9/1 如果想修改自定义Infow中内容，请通过view找到它并修改

    }
}
