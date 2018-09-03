package yktong.com.godofdog.activity.manage.tel;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.tel.CallInfoBean;
import yktong.com.godofdog.bean.tel.PlayerProgressBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.util.PlayerUtil;
import yktong.com.godofdog.util.Utility;
import yktong.com.godofdog.util.time.DateUtils;
import yktong.com.godofdog.value.UrlValue;
import yktong.com.godofdog.view.MyListView;

/**
 * Created by vampire on 2017/8/21.
 */

public class TelInfoActivity extends BaseActivity {

    private TextView nickTv;
    private TextView numberTv;
    private CallInfoBean.OneCommunicateDataBean.CountOneCommunicateBean countOneCommunicate;
    private TextView durationTv;
    private List<CallInfoBean.OneCommunicateDataBean.CommunicateListBean> communicateList;
    private MyListView infoLv;
    private CommonAdapter<CallInfoBean.OneCommunicateDataBean.CommunicateListBean> adapter;
    private SeekBar seekBar;
    private TextView callTypeTv;
    private TextView callTimeTv;
    private ImageView playStateIv;
    private List<CallInfoBean.OneCommunicateDataBean.CommunicateListBean.UserCommunicateDataBean> userCommunicateData;
    private MediaPlayer mediaPlayer;
    private PlayerUtil playerUtil;
    private String recordname;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setProgress(PlayerProgressBean bean){
        Log.d("TelInfoActivity", "bean.getProgress():" + bean.getProgress());
        seekBar.setProgress(bean.getProgress());
    }
    @Override
    protected int setLayout() {
        return R.layout.activity_call_info;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        RelativeLayout headView = bindView(R.id.call_info_head_rl);
        infoLv = bindView(R.id.call_info_lv);
        durationTv = bindView(R.id.info_total_call_tv);
        nickTv = bindView(R.id.info_name_tv);
        numberTv = bindView(R.id.info_number_tv);

        seekBar = bindView(R.id.seek_bar);
        callTypeTv = bindView(R.id.call_type_tv);
        callTimeTv = bindView(R.id.call_time_tv);
        playStateIv = bindView(R.id.play_state_iv);
        playStateIv.setOnClickListener(v -> {
            if (recordname!=null){
                playerUtil.playVoice(recordname);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    seekBar.setTag(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                playerUtil.pausePalyer();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int pro = (int) seekBar.getTag();
                playerUtil.seekTo(pro);
            }
        });

    }

    @Override
    protected void initData() {
        String communicateid = getIntent().getStringExtra("communicatenumber");
        int id = getIntent().getIntExtra("userid",-1);
        String url = UrlValue.SELECT_CALL_INFO + MyApp.userId + UrlValue.COMMUNICATE_NUMBER + communicateid;
        Log.d("TelInfoActivity", url);
        NetTool.getInstance().startRequest("get", UrlValue.SELECT_CALL_INFO + id + UrlValue.COMMUNICATE_NUMBER + communicateid, CallInfoBean.class, new OnHttpCallBack<CallInfoBean>() {
            @Override
            public void onSuccess(CallInfoBean response) {
                if (response.getCode().equals("1")) {
                    countOneCommunicate = response.getOneCommunicateData().getCountOneCommunicate();
                    String duration = DateUtils.changeSec(countOneCommunicate.getCommunicateduration());
                    durationTv.setText(duration);
                    nickTv.setText(countOneCommunicate.getCommunicatename());
                    numberTv.setText(countOneCommunicate.getCommunicatenumber());
                    communicateList = response.getOneCommunicateData().getCommunicateList();
                    adapter.setList(communicateList);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });

        adapter = new CommonAdapter<CallInfoBean.OneCommunicateDataBean.CommunicateListBean>(communicateList, mContext, R.layout.call_log_info_item) {
            @Override
            public void setData(CallInfoBean.OneCommunicateDataBean.CommunicateListBean communicateDataBean, int position, CommonViewHolder viewHolder) {
                long currentTimeMillis = System.currentTimeMillis();
                String timeslashData = DateUtils.timeslashData(currentTimeMillis / 1000 + "");
                userCommunicateData = communicateDataBean.getUserCommunicateData();
                Log.d("TelInfoActivity", "userCommunicateData.size():" + userCommunicateData.size());
                String time = DateUtils.timeslashData(userCommunicateData.get(0).getCommunicatetime() / 1000 + "");
                viewHolder.setText(R.id.call_day_tv, DateUtils.timeslashData(userCommunicateData.get(0).getCommunicatetime() / 1000 + ""));
                if (time.equals(timeslashData))
                    viewHolder.setText(R.id.call_day_tv, "今天");
                MyListView lv = viewHolder.getView(R.id.call_info_item_lv);
                CommonAdapter<CallInfoBean.OneCommunicateDataBean.CommunicateListBean.UserCommunicateDataBean> miniAdapter =
                        new CommonAdapter<CallInfoBean.OneCommunicateDataBean.CommunicateListBean.UserCommunicateDataBean>(userCommunicateData, mContext, R.layout.call_info_item) {
                            @Override
                            public void setData(CallInfoBean.OneCommunicateDataBean.CommunicateListBean.UserCommunicateDataBean userCommunicateDataBean, int position, CommonViewHolder viewHolder) {
                                String timeMinute = DateUtils.timeMinute(userCommunicateDataBean.getCommunicatetime() / 1000 + "");
                                viewHolder.setText(R.id.info_item_time, timeMinute);
                                String changeSec = DateUtils.changeSec(userCommunicateDataBean.getCommunicateduration());
                                viewHolder.setText(R.id.info_item_duration, changeSec);
                                ImageView playerIv = viewHolder.getView(R.id.player_iv);
                                if (userCommunicateDataBean.getCommunicateduration()>0&&!userCommunicateDataBean.getRecordname().isEmpty()){
                                    playerIv.setVisibility(View.VISIBLE);
//                                    playerIv.setOnClickListener(v -> {
//                                        playerUtil.initMediaPlayer();
//                                        recordname = userCommunicateDataBean.getRecordname();
//                                        playerUtil.playVoice(userCommunicateDataBean.getRecordname());
//                                    });
                                }else {
                                    playerIv.setVisibility(View.GONE);
                                }
                                viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        playCallLog();
                                    }

                                    private void playCallLog() {
                                        callTimeTv.setText("缓冲中...");
                                        Log.d("TelInfoActivity", userCommunicateDataBean.getRecordname());
                                        mediaPlayer = PlayerUtil.getInstense().playVoice(userCommunicateDataBean.getRecordname());
                                        if (mediaPlayer.isPlaying()) {
                                            viewHolder.getView(R.id.player_iv).setBackgroundResource(R.mipmap.stoop_s);
                                            playStateIv.setBackgroundResource(R.mipmap.stop_b);
                                        } else {
                                            viewHolder.getView(R.id.player_iv).setBackgroundResource(R.mipmap.play_s);
                                            playStateIv.setBackgroundResource(R.mipmap.play_b);
                                        }
                                    }
                                });
                                TextView ct = viewHolder.getView(R.id.info_item_type);
                                callTypeTv.setText(ct.getText().toString());

                                int callType = userCommunicateDataBean.getCommunicatetype();
                                switch (callType) {
                                    case 1:
                                        viewHolder.setText(R.id.info_item_type, "呼入电话");
                                        break;
                                    case 2:
                                        viewHolder.setText(R.id.info_item_type, "呼出电话");
                                        break;
                                    case 3:
                                        viewHolder.setText(R.id.info_item_type, "未接电话");
                                        break;
                                }
                                if (userCommunicateDataBean.getCommunicateduration() > 0 && !userCommunicateDataBean.getRecordname().isEmpty()) {
                                    playerIv.setVisibility(View.VISIBLE);
                                    playerIv.setOnClickListener(v -> {
                                    });
                                } else {
                                    playerIv.setVisibility(View.GONE);
                                }
                            }
                        };
                lv.setAdapter(miniAdapter);
                lv.setDividerHeight(0);
                lv.setOnItemClickListener((parent, view, position1, id) -> {

                });
                Utility.setListViewHeightBasedOnChildren(lv);

            }
        };
        infoLv.setAdapter(adapter);
    }
}
