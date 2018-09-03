package yktong.com.godofdog.activity.market;

import android.widget.EditText;
import android.widget.TextView;

import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;

public class CommentSendActivity extends BaseActivity {

    public static String commentSendContentId = "content";
    private EditText et_content;
    private TextView tv_finish;

    @Override
    protected int setLayout() {
        return R.layout.activity_comment_send;
    }

    @Override
    protected void initView() {
        et_content = bindView(R.id.et_content);
        tv_finish = bindView(R.id.tv_finish);
        tv_finish.setOnClickListener(v -> {
            CommentSendActivity.this.setResult(1, getIntent().putExtra("content", et_content.getText().toString()));
            CommentSendActivity.this.finish();
        });
    }

    @Override
    protected void initData() {
        try {
            et_content.setText(getIntent().getStringExtra(commentSendContentId));

        } catch (NullPointerException e) {

        }
    }


}
