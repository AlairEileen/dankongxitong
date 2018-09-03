package yktong.com.godofdog.activity.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler2;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.bean.xpbean.WxHistoryBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.util.PlayerUtil;
import yktong.com.godofdog.util.time.DateUtils;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/8/25.
 */

public class ChatHistoryActivity extends BaseActivity {

    private List<WxHistoryBean.CWechatChatsBean> cWechatChats = new ArrayList<>();
    private PlayerUtil playerUtil;
    private MultiItemTypeAdapter recyAdapter;
    private PtrClassicFrameLayout mPtrFrame;
    private int pageIndex = 1;
    private String username;
    private int userId;
    private boolean refresh;
    private boolean load;

    @Override
    protected int setLayout() {
        return R.layout.activity_chat_history;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        RecyclerView recyclerView = bindView(R.id.chat_history_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int itemCount = layoutManager.getItemCount();
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    Log.d("ChatHistoryActivity", "第一个:" + firstVisibleItemPosition);
                    Log.d("ChatHistoryActivity", "总数:" + itemCount);
                    Log.d("ChatHistoryActivity", "位置" + lastPosition);
                    if (lastPosition == layoutManager.getItemCount() - 1) {
                        refresh = true;
                        load = false;
                    } else if (firstVisibleItemPosition == 0) {
                        refresh = false;
                        load = true;
                    }else {
                        refresh = false;
                        load = false;
                    }
                }

            }
        });

        mPtrFrame = bindView(R.id.list_view_with_empty_view_fragment_ptr_frame);
        mPtrFrame.autoRefresh();
        mPtrFrame.setPtrHandler(new PtrHandler2() {
            @Override
            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
                return refresh;
            }

            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                refreshDate(recyclerView);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return load;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(() -> {
                    pageIndex++;
                    loadDate(recyclerView, username, userId, pageIndex);
                }, 1500);

            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(true);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);


        Intent intent = getIntent();
        String nickName = intent.getStringExtra(ChatInfoActivity.NICK_NAME);
        TextView nickTv = bindView(R.id.nick_tv);
        nickTv.setText(nickName);

        username = intent.getStringExtra(ChatInfoActivity.USER_NAME);
        userId = intent.getIntExtra(ChatInfoActivity.USER_ID, -1);
        pageIndex = 1;
        loadDate(recyclerView, username, userId, pageIndex);

        bindView(R.id.btn_ref).setOnClickListener(v -> {
            refreshDate(recyclerView);
        });


    }

    private void refreshDate(final RecyclerView recyclerView) {
        NetTool.getInstance().startRequest("get", UrlValue.QUERY_CHAT_INFO + userId + UrlValue.USERNAME + username + UrlValue.PAGE + pageIndex
                , WxHistoryBean.class, new OnHttpCallBack<WxHistoryBean>() {
                    @Override
                    public void onSuccess(WxHistoryBean response) {
                        Log.d("ChatHistoryActivity", UrlValue.QUERY_CHAT_INFO + userId + UrlValue.USERNAME + username);
                        if (response.getCode().equals("1")) {
                            cWechatChats = new ArrayList<WxHistoryBean.CWechatChatsBean>();
                            for (int i = 0; i < response.getCWechatChats().size(); i++) {
                                cWechatChats.add(0,response.getCWechatChats().get(i));
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                            recyAdapter = new MultiItemTypeAdapter(mContext, cWechatChats);
                            recyAdapter.addItemViewDelegate(new SendItemDelegate());
                            recyAdapter.addItemViewDelegate(new GetMsgItemDelegate());
                            recyclerView.setAdapter(recyAdapter);
                            recyclerView.scrollToPosition(cWechatChats.size() - 1);

                        }
                        Log.d("ChatHistoryActivity", UrlValue.QUERY_CHAT_INFO + userId + UrlValue.USERNAME + username + UrlValue.PAGE + pageIndex);
                        Log.d("ChatHistoryActivity", response.getCode());
                        mPtrFrame.refreshComplete();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void loadDate(final RecyclerView recyclerView, String username, int userId, int pageIndex) {
        NetTool.getInstance().startRequest("get", UrlValue.QUERY_CHAT_INFO + userId + UrlValue.USERNAME + username + UrlValue.PAGE + pageIndex
                , WxHistoryBean.class, new OnHttpCallBack<WxHistoryBean>() {
                    @Override
                    public void onSuccess(WxHistoryBean response) {
                        if (response.getCode().equals("1")) {
                            int temp = response.getCWechatChats().size();
                            for (int i = 0; i < temp; i++) {
                                cWechatChats.add(0,response.getCWechatChats().get(i));
                            }
//                            cWechatChats.addAll(0, response.getCWechatChats());
                            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                            recyAdapter = new MultiItemTypeAdapter(mContext, cWechatChats);
                            recyclerView.scrollToPosition(temp - 1);

                            recyAdapter.addItemViewDelegate(new SendItemDelegate());
                            recyAdapter.addItemViewDelegate(new GetMsgItemDelegate());
                            recyclerView.setAdapter(recyAdapter);
                        } else if (response.getCode().equals("2")) {
                            Toast.makeText(ChatHistoryActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("ChatHistoryActivity", UrlValue.QUERY_CHAT_INFO + userId + UrlValue.USERNAME + username + UrlValue.PAGE + pageIndex);
                        Log.d("ChatHistoryActivity", response.getCode());
                        mPtrFrame.refreshComplete();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    protected void initData() {

    }

    private class SendItemDelegate implements com.zhy.adapter.recyclerview.base.ItemViewDelegate<WxHistoryBean.CWechatChatsBean> {

        private String time;
        private TextView msgTime;

        @Override
        public int getItemViewLayoutId() {
            return R.layout.send_msg_item;
        }

        @Override
        public boolean isForViewType(WxHistoryBean.CWechatChatsBean item, int position) {
            return item.getIssend() == 1;
        }

        @Override
        public void convert(ViewHolder holder, WxHistoryBean.CWechatChatsBean item, int position) {
            Log.d("SendItemDelegate", "item.getIssend():" + item.getIssend());
            msgTime = holder.getView(R.id.msg_time_tv);
            long itemCtime = Long.parseLong(item.getConversationtime());
            if (DateUtils.isToday(item.getConversationtime())) {
                msgTime.setText(DateUtils.timeMinute(String.valueOf(itemCtime / 1000)));
            } else {
                msgTime.setText(DateUtils.times(itemCtime / 1000 + ""));
            }
            ImageView photoIv = holder.getView(R.id.user_photo_iv);
//            Glide.with(mContext).load(item.getcUiname()).into(photoIv);
//
//            ImageView avatarView = viewHolder.getView(R.id.avatar_iv);
            Glide.with(ChatHistoryActivity.this).load(item.getcUiname()).asBitmap().into(new BitmapImageViewTarget(photoIv) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ChatHistoryActivity.this.getResources(), resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                        circularBitmapDrawable.setCircular(true);
                    }
                    photoIv.setImageDrawable(circularBitmapDrawable);
                }
            });


            if (position != 0) {
                itemCtime = Long.parseLong(item.getConversationtime());
                long lastCtime = Long.parseLong(cWechatChats.get(position - 1).getConversationtime());
                if (itemCtime - lastCtime > 30000) {
                    // TODO: 2017/9/11   操作时间显示
                    msgTime.setText(DateUtils.timeMinute(itemCtime / 1000 + ""));
                    msgTime.setVisibility(View.VISIBLE);
                    Log.d("GetMsgItemDelegate", DateUtils.tim(itemCtime / 1000 + ""));
                } else {
                    msgTime.setVisibility(View.GONE);
                }
            }
            if (item.getMsgtype() == 34) {
                String content = item.getContent();
                time = getTime(content);
                String length = "    ";
                holder.getView(R.id.send_msg_tv).setVisibility(View.GONE);
                holder.getView(R.id.send_voice_tv).setVisibility(View.VISIBLE);
                holder.getView(R.id.msg_time).setVisibility(View.VISIBLE);
                TextView voiceLength = holder.getView(R.id.send_voice_tv);
                AnimationDrawable animationDrawable = (AnimationDrawable) voiceLength.getBackground();

                voiceLength.setOnClickListener(v -> {
                    MediaPlayer mediaPlayer = PlayerUtil.getInstense().playVoice(item.getVoicename());
                    mediaPlayer.setOnCompletionListener(mp -> {
                        animationDrawable.stop();
                    });
                    animationDrawable.start();
                    Log.d("MultipleLayoutAdapter", item.getVoicename());
                });
                TextView timeTv = holder.getView(R.id.msg_time);
                timeTv.setVisibility(View.VISIBLE);
                int sec = Integer.parseInt(time);
                sec = sec / 1000;
                timeTv.setText(sec + "''");
                voiceLength(length, voiceLength, sec);
            } else {
                holder.getView(R.id.send_msg_tv).setVisibility(View.VISIBLE);
                holder.getView(R.id.send_voice_tv).setVisibility(View.GONE);
                holder.getView(R.id.msg_time).setVisibility(View.GONE);
            }
            if (item.getMsgtype() == 1) {
                holder.setText(R.id.send_msg_tv, item.getContent());
            }
        }

        private void voiceLength(String length, TextView voiceTv, int sec) {
            if (sec > 30) {
                sec = 30;
            }
            for (int i = 0; i < sec; i++) {
                length += " ";
            }
            voiceTv.setText(length);
        }

        private String getTime(String content) {
            int i = content.indexOf(":");
            String substring = content.substring(i + 1);
            int index = substring.indexOf(":");
            String time = substring.substring(0, index);
            return time;
        }
    }

    private class GetMsgItemDelegate implements com.zhy.adapter.recyclerview.base.ItemViewDelegate<WxHistoryBean.CWechatChatsBean> {
        private String time;
        private TextView msgTime;

        @Override
        public int getItemViewLayoutId() {
            return R.layout.get_msg_item;
        }

        @Override
        public boolean isForViewType(WxHistoryBean.CWechatChatsBean item, int position) {
            return item.getIssend() == 0;
        }

        @Override
        public void convert(ViewHolder holder, WxHistoryBean.CWechatChatsBean item, int position) {
            msgTime = holder.getView(R.id.msg_time_tv);
            long itemCtime = Long.parseLong(item.getConversationtime());
            if (position != 0) {
                itemCtime = Long.parseLong(item.getConversationtime());
                long lastCtime = Long.parseLong(cWechatChats.get(position - 1).getConversationtime());
                Log.d("GetMsgItemDelegate", "lastCtime-itemCtime:" + (itemCtime - lastCtime));
                if (itemCtime - lastCtime > 30000) {
                    // TODO: 2017/9/11   操作时间显示
                    msgTime.setText(DateUtils.timeMinute(itemCtime / 1000 + ""));
                    msgTime.setVisibility(View.VISIBLE);
                    Log.d("GetMsgItemDelegate", DateUtils.tim(itemCtime / 1000 + ""));
                } else {
                    msgTime.setVisibility(View.GONE);
                }
            }
            if (DateUtils.isToday(item.getConversationtime())) {
                msgTime.setText(DateUtils.timeMinute(String.valueOf(itemCtime / 1000)));
            } else {
                msgTime.setText(DateUtils.times(itemCtime / 1000 + ""));
            }

            Log.d("GetMsgItemDelegate", "item.getIssend():" + item.getIssend());
            String content = item.getContent();
            String length = "    ";
            ImageView imageView = holder.getView(R.id.getter_iv);
//            Glide.with(mContext).load(item.getCwcuImage()).into(imageView);
//
//            ImageView avatarView = viewHolder.getView(R.id.avatar_iv);
            Glide.with(ChatHistoryActivity.this).load(item.getCwcuImage()).asBitmap().into(new BitmapImageViewTarget(imageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ChatHistoryActivity.this.getResources(), resource);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                        circularBitmapDrawable.setCircular(true);
                    }
                    imageView.setImageDrawable(circularBitmapDrawable);
                }
            });
            if (content != null) {
                holder.setText(R.id.msg_tv, content);
            }
            if (item.getMsgtype() == 34) {
                time = getTime(content);
                holder.getView(R.id.msg_tv).setVisibility(View.GONE);
                TextView voiceTv = holder.getView(R.id.get_voice_tv);
                AnimationDrawable animationDrawable = (AnimationDrawable) voiceTv.getBackground();
                voiceTv.setOnClickListener(v -> {
//                    playerUtil.initMediaPlayer();
                    animationDrawable.start();
                    MediaPlayer mediaPlayer = PlayerUtil.getInstense().playVoice(item.getVoicename());
                    mediaPlayer.setOnCompletionListener(mp -> animationDrawable.stop());
                    Log.d("MultipleLayoutAdapter", item.getVoicename());
                });
                voiceTv.setVisibility(View.VISIBLE);
                TextView timeTv = holder.getView(R.id.msg_time);
                timeTv.setVisibility(View.VISIBLE);
                int sec = Integer.parseInt(time);
                sec = sec / 1000;
                timeTv.setText(sec + "''");
                voiceLength(length, voiceTv, sec);
            } else {
                holder.getView(R.id.msg_tv).setVisibility(View.VISIBLE);
                holder.getView(R.id.get_voice_tv).setVisibility(View.GONE);
                holder.getView(R.id.msg_time).setVisibility(View.GONE);
            }
        }

        private void voiceLength(String length, TextView voiceTv, int sec) {
            if (sec > 30) {
                sec = 30;
            }
            for (int i = 0; i < sec; i++) {
                length += " ";
            }
            voiceTv.setText(length);
        }

        private String getTime(String content) {
            int i = content.indexOf(":");
            String substring = content.substring(i + 1);
            int index = substring.indexOf(":");
            String time = substring.substring(0, index);
            return time;
        }
    }
}
