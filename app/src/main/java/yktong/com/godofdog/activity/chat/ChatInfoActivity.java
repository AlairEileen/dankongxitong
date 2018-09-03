package yktong.com.godofdog.activity.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.wechat.ChatDateBean;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.util.ShellUtils;
import yktong.com.godofdog.util.time.DateUtils;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/9/1.
 */

public class ChatInfoActivity extends BaseActivity {
    protected final static String USER_NAME = "username";
    protected final static String USER_ID = "userId";
    protected final static String NICK_NAME = "nickName";

    private List<ChatDateBean.CWechatChatsBean> cWechatChats = new ArrayList<>();
    private CommonAdapter<ChatDateBean.CWechatChatsBean> adapter;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.activity_chat_info;
    }

    @Override
    protected void initView() {
        Log.d("ChatInfoActivity", Build.SERIAL);
        Log.d("ChatInfoActivity", "MyApp.userId:" + MyApp.userId);
        int userId = getIntent().getIntExtra("userId", 0);
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        NetTool.getInstance().startRequest("get", UrlValue.QUERY_USER_CHAT_DATE + userId, ChatDateBean.class, new OnHttpCallBack<ChatDateBean>() {
            @Override
            public void onSuccess(ChatDateBean response) {
                if (progressDialogLoading!=null)progressDialogLoading.dismiss();
                Log.d("ChatInfoActivity", UrlValue.QUERY_USER_CHAT_DATE + userId);
                Log.d("ChatInfoActivity", response.getCode());
                if (response.getCode().equals("1")){
                    cWechatChats = response.getCWechatChats();
                    adapter.setList(cWechatChats);
                }else {
                    Toast.makeText(mContext, "code : " +response.getCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
        ListView chatInfoList = bindView(R.id.chat_info_lv);
        adapter = new CommonAdapter<ChatDateBean.CWechatChatsBean>(cWechatChats,mContext, R.layout.item_chat_date) {
            @Override
            public void setData(ChatDateBean.CWechatChatsBean cWechatChatsBean, int position, CommonViewHolder viewHolder) {
                Log.d("ChatInfoActivity", cWechatChatsBean.toString());
                if (cWechatChatsBean.getNickname()!=null){
                    viewHolder.setText(R.id.friend_nick_tv,cWechatChatsBean.getNickname());
                }else {
                    viewHolder.setText(R.id.friend_nick_tv,"佚名");
                }
                if (!cWechatChatsBean.getCwcuImage().isEmpty()){
//                    viewHolder.setImage(R.id.avatar_iv,cWechatChatsBean.getCwcuImage());
                    ImageView avatarView = viewHolder.getView(R.id.avatar_iv);
                    Glide.with(ChatInfoActivity.this).load(cWechatChatsBean.getCwcuImage()).asBitmap().into(new BitmapImageViewTarget(avatarView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(ChatInfoActivity.this.getResources(), resource);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                                circularBitmapDrawable.setCircular(true);
                            }
                            avatarView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
                if (cWechatChatsBean.getMsgtype() == 34){
                    viewHolder.setText(R.id.last_date_tv,cWechatChatsBean.getDigest());
                }else {
                    viewHolder.setText(R.id.last_date_tv,cWechatChatsBean.getContent());
                }
                Log.d("ChatInfoActivity", cWechatChatsBean.getConversationtime());
                long lcc = Long.valueOf(cWechatChatsBean.getConversationtime());
                int time = (int) (lcc/1000);
                String data = DateUtils.timeslashData(time+"");
                viewHolder.setText(R.id.last_time_tv,data);
            }
        };
        chatInfoList.setAdapter(adapter);
        chatInfoList.setOnItemClickListener((parent, view, position, id) -> {
            String username = cWechatChats.get(position).getUsername();
            int userid = cWechatChats.get(position).getcChatUserid();
            String nickname = cWechatChats.get(position).getNickname();
            Intent intent = new Intent(mContext,ChatHistoryActivity.class);
            intent.putExtra(USER_NAME,username);
            intent.putExtra(USER_ID,userid);
            intent.putExtra(NICK_NAME,nickname);
            startActivity(intent);

        });

        chatInfoList.setOnItemLongClickListener((parent, view, position, id) -> {
            String username = cWechatChats.get(position).getUsername();
            SPUtil.putAndApply(mContext, SpValue.USER_ID,username);

            String[] cmd = new String[]{"am start -n com.tencent.mm/com.tencent.mm.ui.chatting.En_5b8fbb1e -e Chat_User " + username};
            ShellUtils.execCommand(cmd, true);
            SPUtil.putAndApply(MyApp.getmContext(),SpValue.USER_AVATAR + username,"");
            WorkManger.getInstence().doTask(SpValue.STATE_GET_NICK);
            return true;
        });


    }

    @Override
    protected void initData() {

    }
}
