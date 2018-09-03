package yktong.com.godofdog.activity.manage.tel;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.bean.tel.TelTotalBean;
import yktong.com.godofdog.bean.tel.TelUserCallLogBean;
import yktong.com.godofdog.popup.PikerPopup;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.util.time.DateUtils;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/8/18.
 */

public class TelUserActivity extends BaseActivity {

    private List<TelUserCallLogBean.CommunicateDataBean> communicateData;
    private List<TelUserCallLogBean.CommunicateTypeBean> communicateType;
    private ListView dateLv;
    private CommonAdapter<TelUserCallLogBean.CommunicateDataBean> adapter;
    private RelativeLayout chooseStateRl;
    private TextView stateName;
    private WindowManager.LayoutParams params;
    private String[] typeName = new String[]{"全部状态", "呼入", "呼出", "未接"};
    private ArrayList<TelUserCallLogBean.CommunicateDataBean> dataBeen;

    @Override
    protected int setLayout() {
        return R.layout.activity_tel_user;
    }

    @Override
    protected void initView() {
        chooseStateRl = bindView(R.id.choose_state_rl);
        stateName = bindView(R.id.state_name_tv);
        dateLv = bindView(R.id.user_call_log_lv);

    }

    @Override
    protected void initData() {
        int userId = getIntent().getIntExtra("USER_ID", -1);
        if (userId == -1) return;
        NetTool.getInstance().startRequest("get", UrlValue.SELECT_USER_CALL_LOG + userId, TelUserCallLogBean.class, new OnHttpCallBack<TelUserCallLogBean>() {
            @Override
            public void onSuccess(TelUserCallLogBean response) {
                Log.d("TelUserActivity", UrlValue.SELECT_USER_CALL_LOG + userId);
                if (response.getCode().equals("1")) {
                    communicateData = response.getCommunicateData();
                    communicateType = response.getCommunicateType();
                    TelUserCallLogBean.CommunicateTypeBean bean = new TelUserCallLogBean.CommunicateTypeBean();
                    bean.setCommunicatetype(0);
                    communicateType.add(0, bean);
                    setDate(0);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
        chooseStateRl.setOnClickListener(v -> {
            PikerPopup popup = new PikerPopup(mContext, position -> {
                int type = communicateType.get(position).getCommunicatetype();
                setDate(type);
                stateName.setText(typeName[type]);
            }, communicateType, new CommonAdapter<TelUserCallLogBean.CommunicateTypeBean>(communicateType, mContext, R.layout.piker_item) {
                @Override
                public void setData(TelUserCallLogBean.CommunicateTypeBean cDeptBean, int position, CommonViewHolder viewHolder) {
                    int type = cDeptBean.getCommunicatetype();
                    viewHolder.setText(R.id.picker_item_tv, typeName[type]);
                }
            });
            popup.showAtLocation(findViewById(R.id.choose_state_rl), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

        setDate(0);

        dateLv.setOnItemClickListener((parent, view, position, id) -> {
            String communicateuserid = dataBeen.get(position).getCommunicatenumber();
            Intent intent = new Intent(mContext, TelInfoActivity.class);
            intent.putExtra("communicatenumber", communicateuserid);
            intent.putExtra("userid",dataBeen.get(position).getCommunicateuserid());
            startActivity(intent);
        });

    }

    private void setDate(int type) {
        dataBeen = new ArrayList<>();
        if (communicateData == null) return;
        for (TelUserCallLogBean.CommunicateDataBean communicateDataBean : communicateData) {
            if (type == 0) {
                dataBeen.add(communicateDataBean);
            } else {
                if (type == communicateDataBean.getCommunicatetype()) {
                    dataBeen.add(communicateDataBean);
                }
            }
        }


        adapter = new CommonAdapter<TelUserCallLogBean.CommunicateDataBean>(dataBeen, mContext, R.layout.call_log_item) {
            @Override
            public void setData(TelUserCallLogBean.CommunicateDataBean communicateDataBean, int position, CommonViewHolder viewHolder) {
                int callType = communicateDataBean.getCommunicatetype();
                setInfo(communicateDataBean, viewHolder, callType);
            }

            private void setInfo(TelUserCallLogBean.CommunicateDataBean communicateDataBean, CommonViewHolder viewHolder, int callType) {
                viewHolder.setText(R.id.call_name_tv, communicateDataBean.getCommunicatename());
                String timedate = DateUtils.timedate(communicateDataBean.getCommunicatetime() / 1000 + "");
                viewHolder.setText(R.id.call_time_tv, timedate);
                long currentTimeMillis = System.currentTimeMillis();
                String timeslashData = DateUtils.timeslashData(currentTimeMillis / 1000 + "");
                String time = DateUtils.timeslashData(communicateDataBean.getCommunicatetime() / 1000 + "");
                if (time.equals(timeslashData))
                    viewHolder.setText(R.id.call_time_tv, DateUtils.timeMinute(communicateDataBean.getCommunicatetime() / 1000 + ""));

                Integer type = (Integer) viewHolder.getView(R.id.call_time_tv).getTag();
                if (type==null){
                    viewHolder.getView(R.id.call_time_tv).setTag(callType);
                    type = callType;
                }
                switch (type) {
                    case 1:
                        viewHolder.getView(R.id.call_state_iv).setVisibility(View.GONE);
                        break;
                    case 2:
                        //呼出
                        break;
                    case 3:
                        TextView tv = viewHolder.getView(R.id.call_name_tv);
                        tv.setTextColor(Color.RED);
                        viewHolder.getView(R.id.call_state_iv).setVisibility(View.GONE);
                        break;
                }
//                if (callType != 2)
//
//                if (callType == 3) {
//
//                }
                viewHolder.setText(R.id.call_total_time_tv, DateUtils.changeSec(communicateDataBean.getCommunicateduration()));
            }
        };
        dateLv.setAdapter(adapter);
    }
}
