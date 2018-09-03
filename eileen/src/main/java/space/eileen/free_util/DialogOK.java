package space.eileen.free_util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import space.eileen.R;

/**
 * Created by Eileen on 2017/7/27.
 */

public class DialogOK extends Dialog implements View.OnClickListener {
    public OkDialogListener okDialogListener;
    private String title;

    public DialogOK(@NonNull Context context, String title, OkDialogListener okDialogListener) {
        super(context,R.style.dialog_ok);
        this.title = title;
        this.okDialogListener = okDialogListener;
        setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
    }

    @Override
    public void onClick(View v) {
        if (okDialogListener != null) {
            okDialogListener.onOkClick();
            dismiss();
        }
    }

    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ok);
        LinearLayout ll_ok = (LinearLayout) findViewById(R.id.ll_ok);
        tv_title = (TextView) findViewById(R.id.tv_title);
        if (title != null) tv_title.setText(title);
        ll_ok.setOnClickListener(this);
    }

    public interface OkDialogListener {
        void onOkClick();
    }
}
