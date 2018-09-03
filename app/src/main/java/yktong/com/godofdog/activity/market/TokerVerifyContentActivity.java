package yktong.com.godofdog.activity.market;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;

public class TokerVerifyContentActivity extends BaseActivity {

    private EditText et_content;
    private TextView tv_finish;
    private TextView tv_content_length;
    private int maxContentSize = 30;
    public static String  tokerVerifyContentId = "content";

    @Override
    protected int setLayout() {
        return R.layout.activity_toker_verify_content;
    }

    @Override
    protected void initView() {
        et_content = bindView(R.id.et_content);
        tv_finish = bindView(R.id.tv_finish);
        tv_content_length = bindView(R.id.tv_content_length);

        et_content.addTextChangedListener(new TextWatcher() {
            String text = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text = et_content.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cText = et_content.getText().toString();
                int textCount = cText.length();
                if (textCount > maxContentSize) {
                    et_content.setText(text);
                    tv_content_length.setText(maxContentSize + "/" + maxContentSize);
                } else {
                    tv_content_length.setText(textCount + "/" + maxContentSize);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TokerVerifyContentActivity.this.setResult(1, getIntent().putExtra("content", et_content.getText().toString()));
                TokerVerifyContentActivity.this.finish();
            }
        });
    }

    @Override
    protected void initData() {
        try {
            et_content.setText(getIntent().getStringExtra(tokerVerifyContentId));
        } catch (NullPointerException e) {

        }
    }


    public void finishEdit(View view) {

    }
}
