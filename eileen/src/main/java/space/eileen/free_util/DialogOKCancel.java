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

public class DialogOKCancel extends Dialog {
    public OkCancelDialogListener okCancelDialogListener;
    TextView tv_title;
    private String title;

    public DialogOKCancel(@NonNull Context context, String title, OkCancelDialogListener okCancelDialogListener) {
        super(context, R.style.dialog_ok);
        this.title = title;
        this.okCancelDialogListener = okCancelDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ok_cancel);
        TextView ll_ok = (TextView) findViewById(R.id.tv_ok);
        TextView tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_title = (TextView) findViewById(R.id.tv_title);
        if (title != null) tv_title.setText(title);
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
