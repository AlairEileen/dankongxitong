package yktong.com.godofdog.activity.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.wechat.UserChatBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.util.SortList;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by vampire on 2017/9/12.
 */

public class UserChatActivity extends BaseActivity {

    private List<UserChatBean.DeptchatBean.UserCWechatChatBean> beanList;
    private CommonAdapter<UserChatBean.DeptchatBean.UserCWechatChatBean> adapter;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.activity_user_chat_list;
    }

    @Override
    protected void initView() {
        RelativeLayout chooseDeptRl = bindView(R.id.choose_dept_rl);
        ListView userLv = bindView(R.id.user_chat_lv);
        TextView deptTv = bindView(R.id.dept_name_tv);
        adapter = new CommonAdapter<UserChatBean.DeptchatBean.UserCWechatChatBean>(beanList, mContext, R.layout.user_chat_item) {
            @Override
            public void setData(UserChatBean.DeptchatBean.UserCWechatChatBean userBean, int position, CommonViewHolder viewHolder) {
                viewHolder.setText(R.id.user_name_tv, userBean.getCName());
//                viewHolder.setImage(R.id.avatar_iv, userBean.getCUiname());
                ImageView avatarView = viewHolder.getView(R.id.avatar_iv);
                Glide.with(UserChatActivity.this).load(userBean.getCUiname()).asBitmap().into(new BitmapImageViewTarget(avatarView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(UserChatActivity.this.getResources(), resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                            circularBitmapDrawable.setCircular(true);
                        }
                        avatarView.setImageDrawable(circularBitmapDrawable);
                    }
                });

                viewHolder.setText(R.id.daily_num_tv, userBean.getCountNum() + "个");
                ImageView honorIv = viewHolder.getView(R.id.honor_iv);
                if (honorIv.getTag() == null) {
                    honorIv.setTag(position);
                }
//                int pos = (int) honorIv.getTag();
//                Log.d("UserChatActivity", "position:" + position);
                honorIv.setVisibility(View.VISIBLE);
                if (position == 0) {
                    viewHolder.setImage(R.id.honor_iv, R.mipmap.jp);
                } else if (position == 1) {
                    viewHolder.setImage(R.id.honor_iv, R.mipmap.yp);
                } else if (position == 2) {
                    viewHolder.setImage(R.id.honor_iv, R.mipmap.tp);
                } else {
                    viewHolder.getView(R.id.honor_iv).setVisibility(View.INVISIBLE);
                }
            }
        };
        userLv.setAdapter(adapter);
        userLv.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(mContext, ChatInfoActivity.class);
            int userid = beanList.get(position).getCId();
            intent.putExtra("userId", userid);
            startActivity(intent);
        });

    }

    @Override
    protected void initData() {
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        NetTool.getInstance().startRequest("get", UrlValue.USER_LIST + MyApp.companyDd + "&userid=" + MyApp.userId, UserChatBean.class, new OnHttpCallBack<UserChatBean>() {
            @Override
            public void onSuccess(UserChatBean response) {
                if (progressDialogLoading!=null)progressDialogLoading.dismiss();
                Log.d("UserChatActivity", response.getCode());
                // TODO: 2017/9/14
                if (response.getCode().equals("1")) {
                    Log.d("UserChatActivity", UrlValue.USER_LIST + MyApp.companyDd);
                    beanList = response.getDeptchat().getUserCWechatChat();
                    SortList<UserChatBean.DeptchatBean.UserCWechatChatBean> sortList = new SortList<>();
                    sortList.Sort(beanList, "getCountNum", "desc");
                    adapter.setList(beanList);
                } else {
                    Toast.makeText(mContext, response.getCode(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });


    }
}
