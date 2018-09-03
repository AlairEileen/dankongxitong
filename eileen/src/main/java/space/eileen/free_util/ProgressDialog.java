package space.eileen.free_util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import space.eileen.R;

/**
 * Created by Eileen on 2017/7/28.
 */

public class ProgressDialog extends Dialog {
    private final int gifResource;
    private final Context context;

    public ProgressDialog(@NonNull Context context, int gifResource) {
        super(context,R.style.dialog_ok);
        this.context=context;
        setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
        this.gifResource=gifResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        ImageView iv_progress=(ImageView)findViewById(R.id.iv_progress);
        Glide.with(context).load(gifResource).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv_progress);
    }
}
