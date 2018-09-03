package yktong.com.godofdog.activity.market;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;

public class InteractionCommentActivity extends BaseActivity {

    private EditText et_content;
    private TextView tv_finish;
    private TextView tv_content_length;
    private int maxContentSize = 30;
    public static String InteractionCommentContentId = "content";

    @Override
    protected int setLayout() {
        return R.layout.activity_interaction_comment;
    }

    @Override
    protected void initView() {
        et_content = bindView(R.id.et_content);
        tv_finish = bindView(R.id.tv_finish);
        tv_content_length = bindView(R.id.tv_content_length);
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InteractionCommentActivity.this.setResult(1, getIntent().putExtra("content", et_content.getText().toString()));
                InteractionCommentActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        try {
            et_content.setText(getIntent().getStringExtra(InteractionCommentContentId));
        } catch (NullPointerException e) {

        }
    }


    public void finishEdit(View view) {

    }
}
