package yktong.com.godofdog.activity.manage;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import space.eileen.free_util.ProgressDialog;
import space.eileen.tools.PutImageGridAdapter;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.bean.OrderRequestBean;
import yktong.com.godofdog.popup.PikerPopup;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/7/3.
 */

public class PayManageActivity extends BaseActivity {

    private WindowManager.LayoutParams params;
    private PutImageGridAdapter putImageGridAdapter;
    private ArrayList<String> piList;
    private Button addPayBtn;
    private EditText clientEt;
    private EditText clientCompanytEt;
    private EditText siteEt;
    private EditText productNumEt;
    private EditText moneyEt;
    private EditText weChatEt;
    private EditText productNameTv;
    private EditText phoneNumberEt;
    private TextView wayTv;
    private ProgressDialog progressDialog;
    private int selectedPicsNum = 4;
    private OptionsPickerView pvOptions;

    @Override
    protected int setLayout() {
        return R.layout.activity_manger_pay;
    }

    @Override
    protected void initView() {
        clientEt = bindView(R.id.client_pay_et);
        clientCompanytEt = bindView(R.id.client_company_pay_et);
        phoneNumberEt = bindView(R.id.client_phone_pay_et);

        weChatEt = bindView(R.id.client_wechat_pay_et);
        Button equalBtn = bindView(R.id.equal_phone_pay_btn);
        siteEt = bindView(R.id.site_pay_et);
        RelativeLayout productNameRl = bindView(R.id.product_info_pay_rl);
        productNameTv = bindView(R.id.product_name_tv);
        productNumEt = bindView(R.id.product_num_pay_et);
        moneyEt = bindView(R.id.money_pay_et);
        RelativeLayout wayPayRl = bindView(R.id.way_pay_rl);
        wayTv = bindView(R.id.pay_way_tv);
        GridView updateGv = bindView(R.id.gv_pics);
        // TODO: 2017/7/4  need adapter
        RelativeLayout salesmanRl = bindView(R.id.salesman_pay_rl);
        TextView salesmanNameTv = bindView(R.id.salesman_pay_name_tv);
        salesmanNameTv.setText(MyApp.userName);
        addPayBtn = bindView(R.id.add_pay_btn);

        phoneNumberEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = s.length();
                if (len == 11) {
                    equalBtn.setVisibility(View.VISIBLE);
                } else {
                    equalBtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        equalBtn.setOnClickListener(v -> {
            String phone = phoneNumberEt.getText().toString();
            weChatEt.setText(phone);
        });

        // TODO: 2017/7/4  产品列表 待获取
        ArrayList<String> list = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            list.add("产品" + i);
//        }
        //产品名称
//        chooseProduct(productNameRl, productNameTv, list);
        chooseWayToPay(wayPayRl, wayTv);
        chooseSalesName(salesmanRl, salesmanNameTv);
        putImageGridAdapter = new PutImageGridAdapter(this, selectedPicsNum, updateGv);
    }

    private void chooseSalesName(RelativeLayout salesmanRl, TextView salesmanNeamTv) {
        ArrayList<String> nameList = new ArrayList<>();
//        nameList.add("关羽");
//        nameList.add("张飞");
//        nameList.add("赵云");
//        nameList.add("马超");
//        nameList.add("黄忠");
        salesmanRl.setOnClickListener(v -> {
            PikerPopup popup = new PikerPopup(mContext, position -> salesmanNeamTv.setText(MyApp.userName), nameList, new CommonAdapter<String>(nameList, mContext, R.layout.piker_item) {
                @Override
                public void setData(String s, int position, CommonViewHolder viewHolder) {
                    viewHolder.setText(R.id.picker_item_tv, s);
                }
            });
        });
    }

    private void chooseWayToPay(RelativeLayout wayPayRl, TextView wayTv) {
        ArrayList<String> wayList = new ArrayList<>();
        wayList.add("支付宝");
        wayList.add("微信");
        wayList.add("银行转账");
        wayList.add("现金支付");


        pvOptions = new OptionsPickerView.Builder(this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx = wayList.get(options1);
            wayTv.setText(tx);
        })
                .setTitleSize(8)
                .setSubCalSize(15)
                .setSubmitColor(getResources().getColor(R.color.text_md_gray))
                .setCancelColor(getResources().getColor(R.color.text_md_gray))
                .setTitleBgColor(getResources().getColor(R.color.theme_white))
                .setBgColor(getResources().getColor(R.color.content_split_gray))
                .setContentTextSize(18)
                .setTextColorCenter(getResources().getColor(R.color.text_black))
                .setTextColorCenter(getResources().getColor(R.color.text_md_gray))
                .build();
        pvOptions.setPicker(wayList);

        wayPayRl.setOnClickListener(v -> {
            pvOptions.show();
//            PikerPopup popup = new PikerPopup(mContext, position -> {
//                wayTv.setText(wayList.get(position));
//            }, wayList, new CommonAdapter<String>(wayList, mContext, R.layout.piker_item) {
//                @Override
//                public void setData(String s, int position, CommonViewHolder viewHolder) {
//                    viewHolder.setText(R.id.picker_item_tv, s);
//                }
//            });
//            popup.showAtLocation(findViewById(R.id.product_info_pay_rl), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//            params = getWindow().getAttributes();
//            //当弹出Popupwindow时，背景变半透明
//            params.alpha = 0.7f;
//            getWindow().setAttributes(params);
//            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
//            popup.setOnDismissListener(() -> {
//                params = getWindow().getAttributes();
//                params.alpha = 1f;
//                getWindow().setAttributes(params);
//            });
        });
    }

    private void chooseProduct(RelativeLayout productNameRl, TextView productNameTv, final ArrayList<String> list) {
        productNameRl.setOnClickListener(v -> {
            PikerPopup popup = new PikerPopup(mContext, position -> {
//                productNameTv.setText(list.get(position));
//                productNameTv.setTextColor(0X333);
            }, list, new CommonAdapter<String>(list, mContext, R.layout.piker_item) {
                @Override
                public void setData(String s, int position, CommonViewHolder viewHolder) {
                    viewHolder.setText(R.id.picker_item_tv, s);
                }
            });
            popup.showAtLocation(findViewById(R.id.product_info_pay_rl), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            params = getWindow().getAttributes();
            //当弹出Popupwindow时，背景变半透明
            params.alpha = 0.7f;
            getWindow().setAttributes(params);
            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
            popup.setOnDismissListener(() -> {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            });
        });
    }

    @Override
    protected void initData() {
        addPayBtn.setOnClickListener(v -> {
            Log.d("PayManageActivity", "piList.isEmpty():" + piList.isEmpty());
            Log.d("PayManageActivity", "piList==null:" + (piList == null));
            if (null != piList) {
                for (String s : piList) {
                    Log.d("PayManageActivity", s);
                }
            }
            if (null == piList || piList.isEmpty()) {
                Toast.makeText(mContext, "请先选取订单图片", Toast.LENGTH_SHORT).show();
            } else {
                if (progressDialog == null)
                    progressDialog = new ProgressDialog(this, R.mipmap.loading);
                progressDialog.show();
                String client = clientEt.getText().toString();
                String clientCompany = clientCompanytEt.getText().toString();
                String site = siteEt.getText().toString();
                String productNum = productNumEt.getText().toString();
                String money = moneyEt.getText().toString();
                String weChat = weChatEt.getText().toString();
                String productName = productNameTv.getText().toString();
                String phone = phoneNumberEt.getText().toString();
                String payment = wayTv.getText().toString();
                OrderRequestBean orderRequestBean = new OrderRequestBean();
                orderRequestBean.setAddress(site);
                orderRequestBean.setAmount(Double.valueOf(money));
                orderRequestBean.setCompanyName(clientCompany);
                orderRequestBean.setCustomerName(client);
                orderRequestBean.setLinkTel(phone);
                orderRequestBean.setNum(Integer.valueOf(productNum));
                orderRequestBean.setPayment(payment);
                orderRequestBean.setProduct(productName);
                orderRequestBean.setUserId(MyApp.userId);
                orderRequestBean.setWeChat(weChat);
                String json = new Gson().toJson(orderRequestBean);
                Map<String, String> paramsMap = new HashMap<String, String>();
                paramsMap.put("cOrders", json);
                Log.d("#####OrderAdd",json);
                Map<String, List<File>> paramFilesMap = new HashMap<String, List<File>>();
                List<File> files = new ArrayList<File>();
                for (String path : piList) {
                    files.add(new File(path));
                }
                paramFilesMap.put("files", files);

                NetTool.getInstance().upLoadMultiFile(UrlValue.ADD_ORDER, paramsMap, paramFilesMap, NormalResultBean.class, new OkHttpUtil.ReqProgressCallBack<NormalResultBean>() {
                    @Override
                    public void onProgress(long total, long current) {
                        long progress = current / total;
                        Log.d("PayManageActivity", "progress:" + progress);
                    }

                    @Override
                    public void onSuccess(NormalResultBean response) {
                        Log.d("PayManageActivity", response.toString());
                        if (response.getCode().equals("1")) {
                            runOnUiThread(() -> {
                                Toast.makeText(mContext, "提交成功", Toast.LENGTH_SHORT).show();
                            });
                            finish();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailed(String msg) {
                        Log.d("PayManageActivity", msg);
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        putImageGridAdapter.onActivityResult(requestCode, resultCode, data);
        piList = putImageGridAdapter.getSelectedPicsList();
        Log.d("PayManageActivity", "piList.isEmpty():" + piList.isEmpty());
        Log.d("PayManageActivity", "piList==null:" + (piList == null));
        if (null != piList) {
            for (String s : piList) {
                Log.d("PayManageActivity", s);
            }
        }

    }
}
