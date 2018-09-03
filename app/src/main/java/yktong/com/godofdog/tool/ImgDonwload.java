package yktong.com.godofdog.tool;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.tool.share.ShareUtil;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.ShellUtils;
import yktong.com.godofdog.value.SpValue;

/**
 * Created by vampire on 2017/8/3.
 */

public class ImgDonwload {
    private static String filePath;
    private static ArrayList<String> filePaths;
    private static Bitmap mBitmap;
    private static String mFileName="为米老乡";
    private static String mSaveMessage;
    private final static String TAG = "ImageActivity";
    private static Context context;

    private static ProgressDialog mSaveDialog = null;

//    public static void donwloadImg(Context contexts,String filePaths){
//        activity = contexts;
//        filePath = filePaths;
////        mSaveDialog = ProgressDialog.show(activity, "保存图片", "图片正在保存中，请稍等...", true);
//
//        new Thread(saveFileRunnable).start();
//    }

    public static void donwloadImg(Context contexts,ArrayList<String> fileUrl){
        filePaths = fileUrl;
//        mSaveDialog = ProgressDialog.show(activity, "保存图片", "图片正在保存中，请稍等...", true);

        new Thread(saveFileRunnable).start();
    }

    private static Runnable saveFileRunnable = new Runnable(){
        @Override
        public void run() {
            for (String path : filePaths) {
                try {
                    byte[] data = getImage(path);
                    if(data!=null){
                        mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// bitmap
                    }else{
                        Toast.makeText(context, "Image error!", Toast.LENGTH_LONG).show();
                    }



                    saveFile(mBitmap, path + ".jpg");
                    mSaveMessage = "图片保存成功！";
                } catch (IOException e) {
                    mSaveMessage = "图片保存失败！";
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            messageHandler.sendMessage(messageHandler.obtainMessage());
        }

    };

    private static Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            mSaveDialog.dismiss();
            Log.d(TAG, mSaveMessage);
            Toast.makeText(context, mSaveMessage, Toast.LENGTH_SHORT).show();
            ArrayList<File> pic = SavePic.getInstance().getPic();
            ShareUtil shareUtil = new ShareUtil(MyApp.getmContext());
            String content = (String) SPUtil.get(MyApp.getmContext(),SpValue.SNS_CONTENT,"");
            shareUtil.shareImgToWXCircle(content,pic);
        }
    };


    /**
     * Get image from newwork
     * @param path The path of image
     * @return byte[]
     * @throws Exception
     */
    public static byte[] getImage(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        InputStream inStream = conn.getInputStream();
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            return readStream(inStream);
        }
        return null;
    }

    /**
     * Get data from stream
     * @param inStream
     * @return byte[]
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }


    /**
     * 保存文件
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(Environment.getExternalStorageDirectory().getPath());
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
//        fileName = UUID.randomUUID().toString()+".jpg";
        File jia=new File("/storage/emulated/0/Download/DXYH/PIC/");
        if(!jia.exists()){   //判断文件夹是否存在，不存在则创建
            jia.mkdirs();
        }
        File myCaptureFile = new File(jia +"/"+ fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }
}
