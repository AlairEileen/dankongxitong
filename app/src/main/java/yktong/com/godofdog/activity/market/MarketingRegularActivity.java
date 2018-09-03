package yktong.com.godofdog.activity.market;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import space.eileen.free_util.DialogOK;
import space.eileen.free_util.ProgressDialog;
import space.eileen.photo_viewer.PicViewerActivity;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.ManageMatterActivity;
import yktong.com.godofdog.activity.WebActivity;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.MatterBean;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.bean.TaskBean;
import yktong.com.godofdog.bean.UserListBean;
import yktong.com.godofdog.bean.WeFriendBean;
import yktong.com.godofdog.manager.DeviceManager;
import yktong.com.godofdog.popup.SecondPickerPopup;
import yktong.com.godofdog.popup.TimePickerPopup;
import yktong.com.godofdog.popup.TokerDeviceSelectPopup;
import yktong.com.godofdog.popup.TokerNumAddPopup;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

import static space.eileen.tools.XString.convertText;
import static space.eileen.tools.XString.isFutureTime;

public class MarketingRegularActivity extends BaseActivity {
    public final static int REQUEST_MATTER_DATA_CODE = 10010;
    public final static String MATTER_JSON = "MATTER_JSON";
    LinearLayout ll_sjjg;
    LinearLayout ll_add_num;
    LinearLayout ll_yxdx;
    LinearLayout ll_sbxz;
    LinearLayout ll_xzsj;
    LinearLayout ll_qdsj;
    LinearLayout ll_matter_data;
    List<WeFriendBean.CWechatFriendBean> selectedFriend = new ArrayList<>();
    private TextView tv_add_num;
    private TextView tv_yxdx;
    private TextView tv_sjjg;
    private TextView tv_sbxz;
    private TextView tv_qdsj;
    private WindowManager.LayoutParams params;
    private ArrayList<UserListBean.UserBean> userBeanList;
    private List<WeFriendBean.CWechatFriendBean> cWechatFriend;
    private MatterBean matterBean;
    private MatterList matterList;
    private TextView sendTaskBtn;
    private int selectedDeviceId = -1;
    private int seconds;
    private int addNum;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogLoading;
    private boolean loadedUsers, loadedFriendList;
    private OptionsPickerView pvOptions;

    @Override
    protected int setLayout() {
        return R.layout.activity_marketing_regular;
    }
    private void hiddenLoading() {
        if (loadedFriendList &&loadedUsers&&progressDialogLoading != null)
            progressDialogLoading.dismiss();
    }
    @Override
    protected void initView() {
        tv_add_num = bindView(R.id.tv_yxsl);
        tv_yxdx = bindView(R.id.tv_yxdx);
        tv_sjjg = bindView(R.id.tv_yxjg);
        tv_sbxz = bindView(R.id.tv_sbxz);
        ll_sjjg = bindView(R.id.ll_sjjg);
        ll_add_num = bindView(R.id.ll_add_num);
        ll_sbxz = bindView(R.id.ll_sbxz);
        ll_yxdx = bindView(R.id.ll_yxdx);
        ll_xzsj = bindView(R.id.ll_xzsj);
        ll_qdsj = bindView(R.id.ll_qdsj);
        ll_matter_data = bindView(R.id.ll_matter_data);
        tv_qdsj = bindView(R.id.tv_qdsj);
        sendTaskBtn = bindView(R.id.send_task_btn);

        ll_qdsj.setOnClickListener(v -> {
            TimePickerPopup timePickerPopup = new TimePickerPopup(MarketingRegularActivity.this, true, (year, month, dayOfMonth, hour, minute) -> {
                String startTime = year + "-" + convertText(month) + "-" + convertText(dayOfMonth) + " " + convertText(hour) + ":" + convertText(minute) + ":00";
                try {
                    if (isFutureTime(startTime)) {
                        tv_qdsj.setText(startTime);
                    } else {
                        startTime = "";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            );
            timePickerPopup.showAtLocation(findViewById(R.id.ll_qdsj), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            params = getWindow().getAttributes();
            //当弹出Popupwindow时，背景变半透明
            params.alpha = 0.7f;
            getWindow().setAttributes(params);
            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
            timePickerPopup.setOnDismissListener(() -> {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            });

        });

        ll_sjjg.setOnClickListener(v -> {
            SecondPickerPopup timePickerPopup = new SecondPickerPopup(MarketingRegularActivity.this, (second) -> {
                seconds = (second);
                tv_sjjg.setText(seconds + "秒");
            });
            timePickerPopup.showAtLocation(findViewById(R.id.ll_sjjg), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            params = getWindow().getAttributes();
            //当弹出Popupwindow时，背景变半透明
            params.alpha = 0.7f;
            getWindow().setAttributes(params);
            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
            timePickerPopup.setOnDismissListener(() -> {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            });
        });
        ll_add_num.setOnClickListener(v -> {
            TokerNumAddPopup numAddPopup = new TokerNumAddPopup(MarketingRegularActivity.this, num -> {
                addNum = num;
                tv_add_num.setText(num + "");
            });
            numAddPopup.showAtLocation(findViewById(R.id.ll_add_num), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            params = getWindow().getAttributes();
            //当弹出Popupwindow时，背景变半透明
            params.alpha = 0.7f;
            getWindow().setAttributes(params);
            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
            numAddPopup.setOnDismissListener(() -> {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            });
        });

        ll_sbxz.setOnClickListener(v -> {
            TokerDeviceSelectPopup numAddPopup = new TokerDeviceSelectPopup(MarketingRegularActivity.this, userBeanList, false, arg -> {
                if (arg == null || arg.size() == 0) {
                    selectedDeviceId = -1;
                    tv_sbxz.setText("");
                    return;
                }
                tv_sbxz.setText(arg.get(0).getCName());
                getFriendList(arg.get(0).getCId());
                selectedDeviceId = arg.get(0).getCId();
            });
            numAddPopup.showAtLocation(findViewById(R.id.ll_sbxz), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            params = getWindow().getAttributes();
            //当弹出Popupwindow时，背景变半透明
            params.alpha = 0.7f;
            getWindow().setAttributes(params);
            //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
            numAddPopup.setOnDismissListener(() -> {
                params = getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().setAttributes(params);
            });
        });

        ll_yxdx.setOnClickListener(v -> {
            if (pvOptions==null){
                Toast.makeText(this,"营销对象为空！",Toast.LENGTH_SHORT).show();
                return;
            }
            pvOptions.show();
//            PikerPopup popup = new PikerPopup(mContext, position -> {
////                if (selectedFriend==null||selectedFriend.size()==0)return;
//                selectedFriend.clear();
//                tv_yxdx.setText(cWechatFriend.get(position).getCWffriendname());
//                selectedFriend.add(cWechatFriend.get(position));
//
//            }, cWechatFriend, new CommonAdapter<WeFriendBean.CWechatFriendBean>(cWechatFriend, mContext, R.layout.piker_item) {
//                @Override
//                public void setData(WeFriendBean.CWechatFriendBean cWechatFriendBean, int position, CommonViewHolder viewHolder) {
//                    viewHolder.setText(R.id.picker_item_tv, cWechatFriendBean.getCWffriendname());
//                }
//            });
//            popup.showAtLocation(findViewById(R.id.ll_yxdx), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

        ll_xzsj.setOnClickListener(v -> {
            Intent intent = new Intent(MarketingRegularActivity.this, ManageMatterActivity.class);
            intent.putExtra("code", REQUEST_MATTER_DATA_CODE);
            MarketingRegularActivity.this.startActivityForResult(intent, REQUEST_MATTER_DATA_CODE);
        });

        sendTaskBtn.setOnClickListener(v -> {
            if (hasNo()) return;
            if (progressDialog == null)
                progressDialog = new ProgressDialog(this, R.mipmap.loading);
            progressDialog.show();
            TaskBean bean = new TaskBean();
            TaskBean.CtaskBean ctaskBean = new TaskBean.CtaskBean();
            ctaskBean.setCTasktype(3);
            ctaskBean.setcIntervaltime(seconds);
            List<TaskBean.CWechatFriendBean> cWechatFriendBeenList = new ArrayList<TaskBean.CWechatFriendBean>();
            for (WeFriendBean.CWechatFriendBean cWechatFriendBean : selectedFriend) {
                TaskBean.CWechatFriendBean wechatFriendBean = new TaskBean.CWechatFriendBean();
                wechatFriendBean.setcWfid(cWechatFriendBean.getCWfid());
                cWechatFriendBeenList.add(wechatFriendBean);
            }
            bean.setCWechatFriend(cWechatFriendBeenList);
            ctaskBean.setCStarttime(tv_qdsj.getText().toString());
            ctaskBean.setcLibraryid(Integer.valueOf(matterBean.getMatter_id()));
            List<TaskBean.CuserBean> been = new ArrayList<>();
            TaskBean.CuserBean cuserBean = new TaskBean.CuserBean();
            cuserBean.setCId(selectedDeviceId);
            been.add(cuserBean);
            ctaskBean.setCNum(addNum);
            bean.setCuser(been);
            bean.setCtask(ctaskBean);
            Gson gson = new Gson();
            String json = gson.toJson(bean);
            Log.d("InteractionRegularActiv", json);
            NetTool.getInstance().postRequest(UrlValue.ADD_TAKS + MyApp.userId, json, NormalResultBean.class, new OnHttpCallBack<NormalResultBean>() {
                @Override
                public void onSuccess(NormalResultBean response) {
                    progressDialog.dismiss();
                    boolean equals = response.getCode().equals("1");
                    if (equals) {
//                        Log.d("InteractionRegularActiv", json);
//                        Toast.makeText(mContext, "任务添加成功", Toast.LENGTH_SHORT).show();
//                        finish();
                        new DialogOK(MarketingRegularActivity.this, "任务添加成功", () -> {
                            finish();
                        }).show();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    progressDialog.dismiss();
                }
            });
        });
    }
    private void resetWFs() {
        if (cWechatFriend == null) {
            pvOptions = null;
            return;
        }
        List<String> groups = new ArrayList<>();
        for (int i = 0; i < cWechatFriend.size(); i++) {
            groups.add(cWechatFriend.get(i).getCWffriendname());
        }

        pvOptions = new OptionsPickerView.Builder(this, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String tx = cWechatFriend.get(options1).getCWffriendname();
            selectedFriend.clear();
            selectedFriend.add(cWechatFriend.get(options1));
            tv_yxdx.setText(tx);
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
        pvOptions.setPicker(groups);
    }
    private boolean hasNo() {
        if (tv_sbxz.getText().toString().trim().length() == 0 ||
                tv_qdsj.getText().toString().trim().length() == 0 ||
                matterBean == null || selectedDeviceId == -1||
        selectedFriend==null||selectedFriend.size()==0        /*||
                tv_add_num.getText().toString().trim().length() == 0 */ ||
                tv_sjjg.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "不能有空", Toast.LENGTH_SHORT).show();
            return true;
        }


        return false;
    }

    @Override
    protected void initData() {
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        userBeanList = DeviceManager.getInstance().getDevices();
        NetTool.getInstance().startRequest("get", UrlValue.SELECT_USER + MyApp.companyDd+UrlValue.FANS_MANAGER_PARAM_USER_ID+MyApp.userId, UserListBean.class, new OnHttpCallBack<UserListBean>() {
            @Override
            public void onSuccess(UserListBean response) {
                loadedUsers=true;
                hiddenLoading();
                if (response.getCode().equals("1")) {
                    userBeanList = (ArrayList<UserListBean.UserBean>) response.getUser();
                    DeviceManager.getInstance().saveDeviceBeanList(userBeanList);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
        getFriendList(MyApp.userId);
    }

    private void getFriendList(int userId) {
        selectedFriend.clear();
        tv_yxdx.setText("");
        if (progressDialogLoading!=null)   progressDialogLoading.show();
        if (userId == -1) userId = MyApp.userId;
        NetTool.getInstance().startRequest("get", UrlValue.SELECT_FRIEND + userId, WeFriendBean.class, new OnHttpCallBack<WeFriendBean>() {
            @Override
            public void onSuccess(WeFriendBean response) {
                loadedFriendList=true;
                hiddenLoading();
                if (response.getCode().equals("1")) {
                    cWechatFriend = response.getCWechatFriend();
                    resetWFs();
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MATTER_DATA_CODE) {
            try {
                String matterJson = data.getStringExtra(MATTER_JSON);
                matterBean = new Gson().fromJson(matterJson, MatterBean.class);
                bindMatterData();
            } catch (Exception e) {

            }
        }
    }

    private void bindMatterData() {
        if (matterBean == null) return;
        if (matterList == null) {
            matterList = new MatterList();
            bindView(matterList);
        }
        clearViews(matterList);
        ll_matter_data.setVisibility(View.VISIBLE);
        bindCommon(matterList);
        if (matterBean.getMatter_url_title() == null) {
            if (matterBean.getImgList().size() > 1) {
                bindMultiImgTextData(matterList);
            } else if (matterBean.getImgList().size() == 1) {
                bindSingleImgTextData(matterList);
            }
        } else {
            bindUrlData(matterList);
        }
    }

    private void clearViews(MatterList matterList) {
        matterList.ll_type_url.setVisibility(View.GONE);
        matterList.ll_type_single_height_img.setVisibility(View.GONE);
        matterList.ll_type_single_width_img.setVisibility(View.GONE);

        matterList.tv_matter_another.setVisibility(View.GONE);
        matterList.tv_matter_time.setVisibility(View.GONE);
        matterList.tv_matter_title.setVisibility(View.GONE);
        matterList.tv_matter_type.setVisibility(View.GONE);
        matterList.tv_matter_use_count.setVisibility(View.GONE);
        matterList.tv_title_url.setVisibility(View.GONE);

        matterList.iv_icon_url.setVisibility(View.GONE);
        matterList.iv_single_height_img.setVisibility(View.GONE);
        matterList.iv_single_multi_img_0.setVisibility(View.GONE);
        matterList.iv_single_multi_img_1.setVisibility(View.GONE);
        matterList.iv_single_multi_img_2.setVisibility(View.GONE);
        matterList.iv_single_multi_img_3.setVisibility(View.GONE);
        matterList.iv_single_multi_img_4.setVisibility(View.GONE);
        matterList.iv_single_multi_img_5.setVisibility(View.GONE);
        matterList.iv_single_multi_img_6.setVisibility(View.GONE);
        matterList.iv_single_multi_img_7.setVisibility(View.GONE);
        matterList.iv_single_multi_img_8.setVisibility(View.GONE);
        matterList.iv_single_width_img.setVisibility(View.GONE);
    }


    private void bindMultiImgTextData(MatterList matterList) {
        matterList.gl_type_multi_img.setVisibility(View.VISIBLE);
        if (matterBean.getImgList().size() >= 1) {
            matterList.iv_single_multi_img_0.setVisibility(View.VISIBLE);
//                    matterList.iv_single_multi_img_0.setImageURI(Uri.parse(matterBean.getImgList().get(0)));
            Glide.with(this).load(matterBean.getImgList().get(0)).asBitmap().into(matterList.iv_single_multi_img_0);
            matterList.iv_single_multi_img_0.setOnClickListener((v) -> {
                goPhotoViewer(matterBean, 0);
            });
        }
        if (matterBean.getImgList().size() >= 2) {
            matterList.iv_single_multi_img_1.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_1.setImageResource(matterBean.getImgList().get(1));
            Glide.with(this).load(matterBean.getImgList().get(1)).asBitmap().into(matterList.iv_single_multi_img_1);
            matterList.iv_single_multi_img_1.setOnClickListener((v) -> {
                goPhotoViewer(matterBean, 1);
            });
        }
        if (matterBean.getImgList().size() >= 3) {
            matterList.iv_single_multi_img_2.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_2.setImageResource(matterBean.getImgList().get(2));
            Glide.with(this).load(matterBean.getImgList().get(2)).asBitmap().into(matterList.iv_single_multi_img_2);
            matterList.iv_single_multi_img_2.setOnClickListener((v) -> {
                goPhotoViewer(matterBean, 2);
            });
        }
        if (matterBean.getImgList().size() >= 4) {
            matterList.iv_single_multi_img_3.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_3.setImageResource(matterBean.getImgList().get(3));
            Glide.with(this).load(matterBean.getImgList().get(3)).asBitmap().into(matterList.iv_single_multi_img_3);
            matterList.iv_single_multi_img_3.setOnClickListener((v) -> {
                goPhotoViewer(matterBean, 3);
            });
        }
        if (matterBean.getImgList().size() >= 5) {
            matterList.iv_single_multi_img_4.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_4.setImageResource(matterBean.getImgList().get(4));
            Glide.with(this).load(matterBean.getImgList().get(4)).asBitmap().into(matterList.iv_single_multi_img_4);
            matterList.iv_single_multi_img_4.setOnClickListener((v) -> {
                goPhotoViewer(matterBean, 4);
            });
        }
        if (matterBean.getImgList().size() >= 6) {
            matterList.iv_single_multi_img_5.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_5.setImageResource(matterBean.getImgList().get(5));
            Glide.with(this).load(matterBean.getImgList().get(5)).asBitmap().into(matterList.iv_single_multi_img_5);
            matterList.iv_single_multi_img_5.setOnClickListener((v) -> {
                goPhotoViewer(matterBean, 5);
            });
        }
        if (matterBean.getImgList().size() >= 7) {
            matterList.iv_single_multi_img_6.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_6.setImageResource(matterBean.getImgList().get(6));
            Glide.with(this).load(matterBean.getImgList().get(6)).asBitmap().into(matterList.iv_single_multi_img_6);
            matterList.iv_single_multi_img_6.setOnClickListener((v) -> {
                goPhotoViewer(matterBean, 6);
            });
        }
        if (matterBean.getImgList().size() >= 8) {
            matterList.iv_single_multi_img_7.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_7.setImageResource(matterBean.getImgList().get(7));
            Glide.with(this).load(matterBean.getImgList().get(7)).asBitmap().into(matterList.iv_single_multi_img_7);
            matterList.iv_single_multi_img_7.setOnClickListener((v) -> {
                goPhotoViewer(matterBean, 7);
            });
        }
        if (matterBean.getImgList().size() >= 9) {
            matterList.iv_single_multi_img_8.setVisibility(View.VISIBLE);
//        matterList.iv_single_multi_img_8.setImageResource(matterBean.getImgList().get(8));
            Glide.with(this).load(matterBean.getImgList().get(8)).asBitmap().into(matterList.iv_single_multi_img_8);
            matterList.iv_single_multi_img_8.setOnClickListener((v) -> {
                goPhotoViewer(matterBean, 8);
            });
        }


    }

    private void goPhotoViewer(MatterBean imgList, int position) {
        Intent intent = new Intent(this, PicViewerActivity.class);
        intent.putStringArrayListExtra(PicViewerActivity.MIDDLE_URL, (ArrayList<String>) imgList.getImgMiddleList());
        intent.putStringArrayListExtra(PicViewerActivity.LARGE_URL, (ArrayList<String>) imgList.getImgLargeList());
        intent.putExtra(PicViewerActivity.POSITION, position);
        this.startActivity(intent);
    }

    private void bindCommon(MatterList matterList) {
        matterList.tv_matter_title.setText(matterBean.getTitle());
        matterList.tv_matter_time.setText(matterBean.getMatter_date_time());
        matterList.tv_matter_use_count.setText("使用" + matterBean.getMatter_use_count() + "次");
        matterList.tv_matter_type.setText(matterBean.getMatterType());
        matterList.tv_matter_another.setText(matterBean.getMatter_another());

        matterList.tv_matter_title.setVisibility(View.VISIBLE);
        matterList.tv_matter_time.setVisibility(View.VISIBLE);
        matterList.tv_matter_use_count.setVisibility(View.VISIBLE);
        matterList.tv_matter_type.setVisibility(View.VISIBLE);
        matterList.tv_matter_another.setVisibility(View.VISIBLE);
    }

    private void bindUrlData(MatterList matterList) {
        matterList.ll_type_url.setVisibility(View.VISIBLE);

        matterList.tv_title_url.setText(matterBean.getMatter_url_title());
        matterList.tv_title_url.setVisibility(View.VISIBLE);

        Glide.with(this).load(matterBean.getMatter_url_icon()).asBitmap().into(matterList.iv_icon_url);
//        matterList.iv_icon_url.setImageResource();

        matterList.iv_icon_url.setVisibility(View.VISIBLE);
        matterList.ll_type_url.setOnClickListener(v -> {
            Intent intent = new Intent(this, WebActivity.class);
            intent.putExtra(WebActivity.WEB_TITLE, matterBean.getMatter_url_title());
            intent.putExtra(WebActivity.LOAD_URL, matterBean.getMatter_url_link());
            this.startActivity(intent);
        });

    }

    private void bindSingleImgTextData(MatterList matterList) {
        Glide.with(this).load(matterBean.getImgList().get(0)).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource.getHeight() > resource.getWidth()) {
                    matterList.ll_type_single_height_img.setVisibility(View.VISIBLE);

                    matterList.iv_single_height_img.setVisibility(View.VISIBLE);
                    matterList.iv_single_height_img.setImageBitmap(resource);
                    matterList.iv_single_height_img.setOnClickListener((v) -> {
                        goPhotoViewer(matterBean, 0);
                    });
                } else {
                    //对比图片长宽
                    matterList.ll_type_single_width_img.setVisibility(View.VISIBLE);

                    matterList.iv_single_width_img.setVisibility(View.VISIBLE);
//        matterList.iv_single_width_img.setImageResource(/);
                    matterList.iv_single_width_img.setImageBitmap(resource);
                    matterList.iv_single_width_img.setOnClickListener((v) -> {
                        goPhotoViewer(matterBean, 0);
                    });
                }
            }
        });


    }

    private void bindView(MatterList matterList) {

        matterList.ll_type_url = (LinearLayout) findViewById(R.id.ll_type_url);
        matterList.ll_type_single_width_img = (LinearLayout) findViewById(R.id.ll_type_single_width_img);
        matterList.ll_type_single_height_img = (LinearLayout) findViewById(R.id.ll_type_single_height_img);
        matterList.gl_type_multi_img = (GridLayout) findViewById(R.id.gl_type_multi_img);

        matterList.tv_matter_title = (TextView) findViewById(R.id.tv_matter_title);
        matterList.tv_title_url = (TextView) findViewById(R.id.tv_title_url);
        matterList.tv_matter_time = (TextView) findViewById(R.id.tv_matter_time);
        matterList.tv_matter_use_count = (TextView) findViewById(R.id.tv_matter_use_count);
        matterList.tv_matter_type = (TextView) findViewById(R.id.tv_matter_type);
        matterList.tv_matter_another = (TextView) findViewById(R.id.tv_matter_another);

        matterList.iv_icon_url = (ImageView) findViewById(R.id.iv_icon_url);
        matterList.iv_single_width_img = (ImageView) findViewById(R.id.iv_single_width_img);
        matterList.iv_single_height_img = (ImageView) findViewById(R.id.iv_single_height_img);
        matterList.iv_single_multi_img_0 = (ImageView) findViewById(R.id.iv_single_multi_img_0);
        matterList.iv_single_multi_img_1 = (ImageView) findViewById(R.id.iv_single_multi_img_1);
        matterList.iv_single_multi_img_2 = (ImageView) findViewById(R.id.iv_single_multi_img_2);
        matterList.iv_single_multi_img_3 = (ImageView) findViewById(R.id.iv_single_multi_img_3);
        matterList.iv_single_multi_img_4 = (ImageView) findViewById(R.id.iv_single_multi_img_4);
        matterList.iv_single_multi_img_5 = (ImageView) findViewById(R.id.iv_single_multi_img_5);
        matterList.iv_single_multi_img_6 = (ImageView) findViewById(R.id.iv_single_multi_img_6);
        matterList.iv_single_multi_img_7 = (ImageView) findViewById(R.id.iv_single_multi_img_7);
        matterList.iv_single_multi_img_8 = (ImageView) findViewById(R.id.iv_single_multi_img_8);
    }

    private class MatterList {
        LinearLayout ll_type_url;
        LinearLayout ll_type_single_width_img;
        LinearLayout ll_type_single_height_img;
        GridLayout gl_type_multi_img;

        TextView tv_matter_title;
        TextView tv_title_url;
        TextView tv_matter_time;
        TextView tv_matter_use_count;
        TextView tv_matter_type;
        TextView tv_matter_another;

        ImageView iv_icon_url;
        ImageView iv_single_width_img;
        ImageView iv_single_height_img;
        ImageView iv_single_multi_img_0;
        ImageView iv_single_multi_img_1;
        ImageView iv_single_multi_img_2;
        ImageView iv_single_multi_img_3;
        ImageView iv_single_multi_img_4;
        ImageView iv_single_multi_img_5;
        ImageView iv_single_multi_img_6;
        ImageView iv_single_multi_img_7;
        ImageView iv_single_multi_img_8;
    }


}
