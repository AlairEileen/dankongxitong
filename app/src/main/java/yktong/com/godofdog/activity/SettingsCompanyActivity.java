package yktong.com.godofdog.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;
import java.util.List;

import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;

public class SettingsCompanyActivity extends BaseActivity {


    private EditText tv_tjscbq;
    private TextView tv_xzry;
    private TextView tv_szbm;
    private TextView tv_szqx;

    @Override
    protected int setLayout() {
        return R.layout.activity_settings_company;
    }

    @Override
    protected void initView() {
        LinearLayout ll_xzry = bindView(R.id.ll_xzry);
        LinearLayout ll_szbm = bindView(R.id.ll_szbm);
        LinearLayout ll_szqx = bindView(R.id.ll_szqx);
        tv_xzry = bindView(R.id.tv_xzry);
        tv_szbm = bindView(R.id.tv_szbm);
        tv_szqx = bindView(R.id.tv_szqx);
        tv_tjscbq = bindView(R.id.tv_tjscbq);

        List<String> options1Items = new ArrayList<String>();
        options1Items.add("张三");
        options1Items.add("李四");
        options1Items.add("王五");
        options1Items.add("老刘");
        OptionsPickerView opv_xzry = create_xzry_Pickers(options1Items, tv_xzry);

        ll_xzry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opv_xzry.show();
            }
        });
        List<String> options2Items = new ArrayList<String>();
        options2Items.add("技术部");
        options2Items.add("营销部");
        options2Items.add("人事部");
        options2Items.add("业务部");
        OptionsPickerView opv_szbm = create_xzry_Pickers(options2Items, tv_szbm);

        ll_szbm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opv_szbm.show();
            }
        });
        List<String> options3Items = new ArrayList<String>();
        options3Items.add("读取");
        options3Items.add("写入");
        options3Items.add("都可以");
        options3Items.add("不知道");
        OptionsPickerView opv_szqx = create_xzry_Pickers(options3Items, tv_szqx);

        ll_szqx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opv_szqx.show();
            }
        });

    }

    private OptionsPickerView create_xzry_Pickers(List<String> optionsItems, TextView textView) {

        OptionsPickerView xzryOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = optionsItems.get(options1);
                textView.setText(tx);
            }
        })
                .setTitleSize(8)
                .setSubCalSize(15)
                .setSubmitColor(getResources().getColor(R.color.text_md_gray))
                .setCancelColor(getResources().getColor(R.color.text_md_gray))
                .setTitleBgColor(getResources().getColor(R.color.theme_white))
                .setBgColor(getResources().getColor(R.color.content_split_gray))
                .setContentTextSize(14)
                .setTextColorCenter(getResources().getColor(R.color.text_black))
                .setTextColorCenter(getResources().getColor(R.color.text_md_gray))
                .build();
        xzryOptions.setPicker(optionsItems);
        return xzryOptions;
    }

    @Override
    protected void initData() {

    }
}
