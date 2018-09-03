package yktong.com.godofdog.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import yktong.com.godofdog.bean.MessageBean;
import yktong.com.godofdog.tool.FileUtility;


/**
 * Created by vampire on 2017/8/3.
 */

public class DownImg {

    private Bitmap bitmap;

    public void downImg(ArrayList<String> urls){
        for (String url : urls) {
            try {
                byte[] data = getImage(url);
                if (data!=null){
                    bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                }
                saveFile(bitmap,System.currentTimeMillis()+".jpg");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventBus.getDefault().post(new MessageBean("分享"));
    }
    /**
     * Get image from newwork
     * @param path The path of image
     * @return byte[]
     * @throws Exception
     */
    private  byte[] getImage(String path) throws Exception{
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
    private byte[] readStream(InputStream inStream) throws Exception{
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
    private  void saveFile(Bitmap bm, String fileName) throws IOException {
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
