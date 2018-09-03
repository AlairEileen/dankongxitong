package yktong.com.godofdog.tool.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bumptech.glide.util.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import space.eileen.BuildConfig;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.tool.BitmapUtil;

/**
 * Created by vampire on 2017/7/27.
 */

public class ShareByApi {
    private static ShareByApi ourInstance;
    public static final int SNS = 1;
    public static final int WX = 2;
    private final IWXAPI api;

    public static ShareByApi getInstance() {
        if (ourInstance == null) {
            synchronized (ShareByApi.class) {
                if (ourInstance == null) {
                    ourInstance = new ShareByApi();
                }
            }
        }
        return ourInstance;

    }

    private ShareByApi() {
        api = WXAPIFactory.createWXAPI(MyApp.getmContext(), MyApp.getmContext().getString(R.string.we_chat_app_id),true);
        api.registerApp(MyApp.getmContext().getString(R.string.we_chat_app_id));
    }

    public void sharePic (Bitmap bmp , int where){
        WXImageObject imageObject = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imageObject;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp,100,100,true);
        msg.thumbData = BitmapUtil.Bitmap2Bytes(thumbBmp);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = where;
        api.sendReq(req);
        bmp.recycle();

    }

    public void shareVideo (String url , String title , String description ,int where){
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = url;
        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = title;
        msg.description = description;
        Bitmap thumb = BitmapFactory.decodeResource(MyApp.getmContext().getResources(),R.mipmap.logo);
        msg.thumbData = BitmapUtil.Bitmap2Bytes(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = where;
        api.sendReq(req);
        thumb.recycle();
    }

    public void shareWeb(String url , String title ,Bitmap bmp ,String description,int where){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        Log.d("ShareByApi", "bmp == null:" + (bmp == null));
        Log.d("ShareByApi", "bmp.getWidth():" + bmp.getWidth());
        Log.d("ShareByApi", "bmp.getHeight():" + bmp.getHeight());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
        Log.d("ShareByApi", "bitmap == null:" + (thumbBmp == null));
        msg.thumbData = BitmapUtil.Bitmap2Bytes(bmp);

        SendMessageToWX.Req req= new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = where;
        api.sendReq(req);
//        bmp.recycle();
    }

    public void shareminiPriject(){
        WXMiniProgramObject miniProgram = new WXMiniProgramObject();
        miniProgram.webpageUrl = "http://www.qq.com";
        miniProgram.userName = "gh_d43f693ca31f";
        miniProgram.path = "pages/play/index?cid=fvue88y1fsnk4w2&ptag=vicyao&seek=3219";
        WXMediaMessage msg = new WXMediaMessage(miniProgram);
        msg.title = "分享小程序Title";
        msg.description = "分享小程序描述信息";
        Bitmap bmp = BitmapFactory.decodeResource(MyApp.getmContext().getResources(), R.mipmap.ic_launcher);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
        bmp.recycle();
        msg.thumbData = BitmapUtil.Bitmap2Bytes(thumbBmp);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    private static String buildTransaction(String wxWebpageObject) {
        return (wxWebpageObject == null) ? String.valueOf(System.currentTimeMillis())
                : wxWebpageObject + System.currentTimeMillis();
    }


}