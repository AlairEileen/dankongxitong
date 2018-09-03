package space.eileen.free_util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import space.eileen.R;

/**
 * Created by Eileen on 2017/7/27.
 */

public class DialogPluginDownload extends Dialog {
    public OkCancelDialogListener okCancelDialogListener;
    TextView tv_content;
    private String content;

    public DialogPluginDownload(@NonNull Context context, String content, OkCancelDialogListener okCancelDialogListener) {
        super(context, R.style.dialog_ok);
        this.content =content;
        this.okCancelDialogListener = okCancelDialogListener;
        setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plugin_download);
        TextView ll_ok = (TextView) findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_content = (TextView) findViewById(R.id.tv_content);
        if (content != null) tv_content.setText(content);
        ll_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                okCancelDialogListener.onOkClick();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                okCancelDialogListener.onCancelClick();
            }
        });
    }


    public interface OkCancelDialogListener {
        void onOkClick();

        void onCancelClick();
    }
}
