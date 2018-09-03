package yktong.com.godofdog.tool.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;

import yktong.com.godofdog.R;
import yktong.com.godofdog.base.MyApp;


/**
 * Created by Vampire on 16/9/13.
 * 一个通用的viewHolder;适用于所有的adapter
 */
public class CommonViewHolder {
    private static final String TAG = "Vampire_CommonViewHolder";
    //SparseArray可以看成Key值是int类型的HasMap;
    // 是android特有的 , 效率比HasMap高
    private SparseArray<View> views;
    private View convertView; // 行布局

    public static CommonViewHolder getHolder(View convertView, LayoutInflater inflater, int id, ViewGroup parent) {
        CommonViewHolder viewHolder;
        if (convertView == null) {
            View view = inflater.inflate(id, parent, false);
            viewHolder = new CommonViewHolder(view);
        } else {
            viewHolder = (CommonViewHolder) convertView.getTag();
        }
        return viewHolder;
    }

    // 返回行布局
    public View getConvertView() {
        return convertView;
    }

    public CommonViewHolder(View convertView) {
        views = new SparseArray<>();
        this.convertView = convertView;
        this.convertView.setTag(this);
    }

    /**
     * 通过id来获得行布局里指定的view的方法
     *
     * @param id view的id
     * @return 该id所对应的view
     */
    public <T extends View> T getView(int id) {
        View view = views.get(id);
        if (view == null) {
            //执行findViewById 找到这个组件 然后放到views里
            view = convertView.findViewById(id);
            // 放到views里
            views.put(id, view);
        }
        return (T) view;
    }

    // 设置文字
    public void setText(int id, String text) {
        TextView textView = getView(id);
        textView.setText(text);
    }

    public void setText(int id, String text, int color) {
        TextView textView = getView(id);
        textView.setText(text);
        textView.setTextColor(color);
    }

    public void setBac(int layoutId, int color) {
        View view = getView(layoutId);
        view.setBackgroundColor(color);
    }

    // 设置图片
    public Bitmap setImage(int id, String url) {
        Uri uri = Uri.parse(url);
        ImageView imageView = getView(id);
        Glide.with(MyApp.getmContext()).load(uri).into(imageView);
        Bitmap scale = BitmapFactory.decodeResource(imageView.getResources(), id);
        return scale;
    }
    // 设置图片
    public void setImage(int id, int pic) {
        ImageView view = getView(id);
        view.setBackgroundResource(pic);
    }
    // 设置图片
    public Bitmap setImage(int id, Bitmap bitmap) {
        ImageView imageView = getView(id);
        imageView.setImageBitmap(bitmap);
//        Bitmap scale = BitmapFactory.decodeResource(imageView.getResources(), id);
        return null;
    }

    public void setImage(int id, Drawable drawable) {
        ImageView imageView = getView(id);
        imageView.setImageDrawable(drawable);
    }


    public void setImageForFerco(int id, String uri) {
        SimpleDraweeView simpleDraweeView = getView(id);
        if (uri.equals("vampire")) {
            simpleDraweeView.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            simpleDraweeView.setImageURI(Uri.parse(uri));
        }
    }


}
