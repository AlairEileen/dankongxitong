package yktong.com.godofdog.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import yktong.com.godofdog.R;
import yktong.com.godofdog.adapter.MainAdapter;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.FriendNickListBean;
import yktong.com.godofdog.bean.MessageBean;
import yktong.com.godofdog.bean.ShareBean;
import yktong.com.godofdog.bean.TaskBean;
import yktong.com.godofdog.fragment.MainFragment;
import yktong.com.godofdog.fragment.ManagementFragment;
import yktong.com.godofdog.fragment.MarketingFragment;
import yktong.com.godofdog.fragment.UserFragment;
import yktong.com.godofdog.manager.AppUpdateManager;
import yktong.com.godofdog.manager.PluginsManager;
import yktong.com.godofdog.manager.WorkManger;
import yktong.com.godofdog.map.amap.LineService;
import yktong.com.godofdog.service.MyService;
import yktong.com.godofdog.tool.BitmapUtil;
import yktong.com.godofdog.tool.FileUtility;
import yktong.com.godofdog.tool.SavePic;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.share.ShareByApi;
import yktong.com.godofdog.tool.share.ShareUtil;
import yktong.com.godofdog.tool.thread.ThreadPool;
import yktong.com.godofdog.util.DownImg;
import yktong.com.godofdog.util.SPUtil;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.Strings;
import yktong.com.godofdog.value.UrlValue;

import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneSession;
import static com.tencent.mm.opensdk.modelmsg.SendMessageToWX.Req.WXSceneTimeline;

public class MainActivity extends BaseActivity implements Strings {

    private AlertDialog dialog;
    private UserFragment userFragment;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void shareInfo(TaskBean taskBean) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void beginShare(MessageBean bean) {
        if (bean.getMessage().equals("分享")) {
            ArrayList<File> pic = SavePic.getInstance().getPic();

            ShareUtil shareUtil = new ShareUtil(mContext);
            String content = (String) SPUtil.get(MyApp.getmContext(), SpValue.SNS_CONTENT, "");
//            shareUtil.shareImgToWXCircle(content,pic);
            shareUtil.shareMultiplePictureToTimeLine(pic, mContext, content);
        }
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        ViewPager viewPager = bindView(R.id.main_vp);
        ImageView iv_tab_home = bindView(R.id.iv_tab_home);
        ImageView iv_tab_yx = bindView(R.id.iv_tab_yx);
        ImageView iv_tab_gl = bindView(R.id.iv_tab_gl);
        ImageView iv_tab_w = bindView(R.id.iv_tab_w);
        LinearLayout ll_tab_home = bindView(R.id.ll_tab_home);
        LinearLayout ll_tab_yx = bindView(R.id.ll_tab_yx);
        LinearLayout ll_tab_gl = bindView(R.id.ll_tab_gl);
        LinearLayout ll_tab_w = bindView(R.id.ll_tab_w);
        ImageView[] imageViews = {iv_tab_home, iv_tab_yx, iv_tab_gl, iv_tab_w};
        LinearLayout[] linearLayouts = {ll_tab_home, ll_tab_yx, ll_tab_gl, ll_tab_w};
//        TabLayout tabLayout = bindView(R.id.main_table);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new MainFragment());
        fragments.add(new MarketingFragment());
        fragments.add(new ManagementFragment());
        userFragment = new UserFragment();
        fragments.add(userFragment);
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
//        initTableLayout(viewPager, tabLayout);
        initTableLayout(viewPager, imageViews, linearLayouts);

    }

    private void initTableLayout(ViewPager viewPager, ImageView[] imageViews, LinearLayout[] linearLayouts) {
        for (int i = 0; i < imageViews.length; i++) {
            final int index = i;
            linearLayouts[index].setOnClickListener(v -> {
                viewPager.setCurrentItem(index);
            });
        }
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < SELECTED.length; i++) {
                    imageViews[i].setImageResource(UNSELECTED[i]);
                }
                imageViews[position].setImageResource(SELECTED[position]);
                if (position == 3) {
                    userFragment.onShow();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        shareInfo();
        Log.d("MainActivity", JPushInterface.getRegistrationID(MyApp.getmContext()));
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission(new String[]{Manifest.permission.WRITE_CONTACTS});
            }
        }
        AppUpdateManager.doCheck(this);
        checkPlugins();
    }

    private void checkPlugins() {
        //检测语音插件
        if (PluginsManager.checkAddInstallVoicePlugin(this)) {
        } else {
//            Toast.makeText(this, "noVoice", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareInfo() {
        int isShare = getIntent().getIntExtra(Strings.IS_SHARE, 0);
        int taskId = getIntent().getIntExtra(Strings.TASK_ID, 0);
        if (0 != isShare) {
            int where = 0;
            if (isShare == 3) {
                where = WXSceneSession;
                WorkManger.getInstence().doTask(SpValue.STATE_SEND_PERSON);
            } else if (isShare == 5) {
                where = WXSceneTimeline;
                WorkManger.getInstence().doTask(SpValue.STATE_SEND_SNS);
            }
            int libraryId = getIntent().getIntExtra(Strings.PASSAGE_ID, 0);
            NetTool.getInstance().startRequest("get", UrlValue.GET_TASK_NICK_NAME + taskId, FriendNickListBean.class, new OnHttpCallBack<FriendNickListBean>() {
                @Override
                public void onSuccess(FriendNickListBean response) {
                    if (response.getCode().equals("1")) {
                        Log.d("MainActivity", response.toString());
                        List<String> friendnames = response.getFriendname();
                        if (friendnames != null && !friendnames.isEmpty()) {
                            String nick = friendnames.get(0);
                            SPUtil.putAndApply(MyApp.getmContext(), SpValue.SHARE_NAME, nick);
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {

                }
            });
            int finalWhere = where;
            NetTool.getInstance().startRequest("get", UrlValue.SELECT_LIBRARY_BY_ID + libraryId, ShareBean.class, new OnHttpCallBack<ShareBean>() {
                @Override
                public void onSuccess(ShareBean response) {
                    if (response.getCode().equals("1")) {
                        ShareBean.LibraryImageBean libraryImageBean = response.getLibrary_Image();
                        ShareBean.LibraryImageBean.ClibraryBean clibrary = libraryImageBean.getClibrary();
                        ThreadPool.thredP.execute(() -> {
                            SPUtil.putAndApply(MyApp.getmContext(), SpValue.SNS_CONTENT, clibrary.getCContent());
                            String url = clibrary.getcInterlinkage();
                            if (url == null) {
                                // 图文
                                if (libraryImageBean.getClibraryImageUrl().size() > 1) {
                                    ArrayList<String> pics = new ArrayList<String>();
                                    for (ShareBean.LibraryImageBean.ClibraryImageUrlBean clibraryImageUrlBean : libraryImageBean.getClibraryImageUrl()) {
                                        pics.add(clibraryImageUrlBean.getImageUrltwo());
                                    }
                                    FileUtility.deleteFolder("/storage/emulated/0/Download/DXYH/PIC/");
                                    DownImg downImg = new DownImg();
                                    downImg.downImg(pics);
                                } else {
                                    Bitmap bitmap = BitmapUtil.returnBitMap(libraryImageBean.getClibraryImageUrl().get(0).getImageUrltwo());
                                    ShareByApi.getInstance().sharePic(bitmap, finalWhere);
                                }
                            } else {
                                //web
                                Bitmap bitmap = BitmapUtil.returnBitMap(libraryImageBean.getClibraryImageUrl().get(0).getImageUrlone());
                                ShareByApi.getInstance().shareWeb(clibrary.getcInterlinkage(), clibrary.getcHeadline(), bitmap, clibrary.getcHeadline(), finalWhere);
                            }
                        });

                    }
                }

                @Override
                public void onError(Throwable e) {

                }
            });
        }
    }




    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission(String[] permissions) {
        new AlertDialog.Builder(this)
                .setTitle("联系人修改权限不可用")
                .setMessage("由于需要导入联系人；\n否则，您将无法正常使用微信好友添加")
                .setPositiveButton("立即开启", (dialog1, which) -> startRequestPermission(permissions))
                .setNegativeButton("取消", (dialog12, which) -> finish()).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else {
                        finish();
                    }
                } else {
                    goToAppSetting();

                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting() {

        dialog = new AlertDialog.Builder(this)
                .setTitle("联系人权限不可用")
                .setMessage("请在-应用设置-权限-中，允许获取联系人权限")
                .setPositiveButton("立即开启", (dialog12, which) -> {
                    // 跳转到应用设置界面
                    goToAppSetting();
                })
                .setNegativeButton("取消", (dialog1, which) -> finish()).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 321);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (userFragment != null) {
            userFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 适配android M，检查权限
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isNeedRequestPermissions(permissions)) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
        }
        Intent intent = new Intent(this, LineService.class);
        intent.putExtra("id", MyApp.userId);
        startService(intent);
    }

    private boolean isNeedRequestPermissions(List<String> permissions) {
        // 定位精确位置
        addPermission(permissions, Manifest.permission.ACCESS_FINE_LOCATION);
        // 存储权限
        addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // 读取手机状态
        addPermission(permissions, Manifest.permission.READ_PHONE_STATE);
        return permissions.size() > 0;
    }

    private void addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }
}
