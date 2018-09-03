package yktong.com.godofdog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import space.eileen.photo_viewer.PicViewerActivity;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.MyOrderBean;
import yktong.com.godofdog.bean.OrderInfoBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.time.DateUtils;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/7/7.
 */

public class OrderInfoActivity extends BaseActivity {

    private TextView productName;
    private TextView time;
    private TextView name;
    private TextView money;
    private TextView way;
    private TextView company;
    private TextView tel;
    private TextView weChat;
    private TextView location;
    private ImageView[] imageViews;
    private TextView salesman;

    @Override
    protected int setLayout() {
        return R.layout.activity_order_info;
    }

    @Override
    protected void initView() {
        productName = bindView(R.id.item_product_name_tv);
        time = bindView(R.id.order_info_time_tv);
        name = bindView(R.id.item_order_name_tv);
        money = bindView(R.id.item_order_money_tv);
        way = bindView(R.id.item_order_pay_way_tv);
        company = bindView(R.id.order_info_company_tv);
        tel = bindView(R.id.order_info_tel_tv);
        weChat = bindView(R.id.order_info_wx_tv);
        location = bindView(R.id.order_info_location_tv);
        salesman = bindView(R.id.order_info_saleman_tv);


        // TODO: 2017/7/7  pic
        ImageView pic1 = bindView(R.id.order_info_pic1);
        ImageView pic2 = bindView(R.id.order_info_pic2);
        ImageView pic3 = bindView(R.id.order_info_pic3);
        ImageView pic4 = bindView(R.id.order_info_pic4);
        imageViews = new ImageView[]{pic1, pic2, pic3, pic4};
        String userName = getIntent().getStringExtra(MyOrderActivity.USER_NAME);
        salesman.setText(userName == null ? MyApp.userName : userName);


    }

    @Override
    protected void initData() {
        Bundle bundle = this.getIntent().getBundleExtra("argument");

        MyOrderBean.OrdersBean argment = (MyOrderBean.OrdersBean) bundle.getSerializable("bean");
        productName.setText(argment.getCProduct());
        String times = DateUtils.times(argment.getCOrdertime());
        time.setText(times);
        name.setText(argment.getCCustomername());
        money.setText(argment.getCAmount() + "");
        way.setText(argment.getCPayment());
        company.setText(argment.getCCompanyname());
        tel.setText(argment.getCLinktel());
        weChat.setText(argment.getCWechat());
        location.setText(argment.getCAddress());
        NetTool.getInstance().startRequest("get", UrlValue.ORDER_INFO + argment.getCOrderid(), OrderInfoBean.class, new OnHttpCallBack<OrderInfoBean>() {
            @Override
            public void onSuccess(OrderInfoBean response) {
                if (!response.getCode().equals("1")) return;
                List<OrderInfoBean.OrderBean.COrderImageUrlsBean> cOrderImageUrls = response.getOrder().getCOrderImageUrls();
                ArrayList<String> middleList=new ArrayList<String>();
                ArrayList<String> largeList=new ArrayList<String>();
                for (int i = 0; i < cOrderImageUrls.size(); i++) {
                    Glide.with(mContext).load(cOrderImageUrls.get(i).getImageUrlthree()).into(imageViews[i]);
                    middleList.add(cOrderImageUrls.get(i).getImageUrltwo());
                    largeList.add(cOrderImageUrls.get(i).getImageUrlone());
                    int finalI = i;
                    imageViews[i].setOnClickListener(v -> goPhotoViewer(middleList,largeList, finalI));
                }

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void goPhotoViewer(ArrayList<String> middleList, ArrayList<String> largeList, int position) {
        Intent intent = new Intent(this, PicViewerActivity.class);
        intent.putStringArrayListExtra(PicViewerActivity.MIDDLE_URL, middleList);
        intent.putStringArrayListExtra(PicViewerActivity.LARGE_URL, largeList);
        intent.putExtra(PicViewerActivity.POSITION, position);
        startActivity(intent);
    }
}
