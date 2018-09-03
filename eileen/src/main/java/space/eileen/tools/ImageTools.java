package space.eileen.tools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.Date;

/**
 * Created by Eileen on 2017/6/28.
 */

public class ImageTools {

    /**
     * 创建缩略图
     *
     * @param src
     * @param dstWidth
     * @param dstHeight
     * @param inSampleSize
     * @return
     */
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight, int inSampleSize) {
        //如果inSampleSize是2的倍数，也就说这个src已经是我们想要的缩略图了，直接返回即可。
        if (inSampleSize % 2 == 0) {
            return src;
        }
        // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    /**
     * 从文件中获取bitmap图片
     *
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static Bitmap decodeSampleBitmapFromFile(String path, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(path, options);
        return createScaleBitmap(src, width, height, options.inSampleSize);
    }

    /**
     * 计算图片大小
     *
     * @param options
     * @param reqHeight
     * @param reqWidth
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqHeight, int reqWidth) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     *      * 裁剪原始的图片
     *      
     */
    public static Uri cropImage(Activity activity, int CODE_RESULT_REQUEST, Uri uri, int output_X, int output_Y, String tempFilePath) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");//把裁剪的数据填入里面
        // 设置裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("circleCrop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
//        intent.putExtra("return-data", true);
        File file =new File(tempFilePath);
        if (!file.exists()){
            file.mkdirs();
        }
        Uri uriTempFile= Uri.parse("file://" + "/" + tempFilePath + "temp_" + new Date().getTime() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        activity.startActivityForResult(intent, CODE_RESULT_REQUEST);
        return uriTempFile;
    }
}
