package yktong.com.godofdog.fragment;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import space.eileen.tools.ImageTools;
import space.eileen.tools.SelectImagesTool;
import space.eileen.tools.UriTools;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.MatterAddActivity;
import yktong.com.godofdog.base.BaseFragment;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.ResponseBaseBean;
import yktong.com.godofdog.bean.matter_beans.MatterRequestBean;
import yktong.com.godofdog.bean.sns.ClassifyBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by Eileen on 2017/7/4.
 */

public class MatterAddUrlFragment extends BaseFragment implements SpValue {
    private OptionsPickerView pvOptions;
    private TextView tv_sclx;
    private ImageView iv_url_icon;
    private ArrayList<String> urlIconList = new ArrayList<>();
    private TextView addBtn;
    private EditText titleEt;
    private EditText contetEt;
    private EditText addUrlInfoEt;
    private int typeId = -1;
    private int code_result_icon = 10023;
    private String picPath;
    private Uri picUri;
    private int selectRequestCode = 10022;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.fragment_matter_add_url;
    }

    @Override
    protected void initView() {
        LinearLayout ll_sclx = bindView(R.id.ll_sclx);
        tv_sclx = bindView(R.id.tv_sclx);
        TextView tv_add_url_icon = bindView(R.id.tv_add_url_icon);
        iv_url_icon = bindView(R.id.iv_url_icon);

        ll_sclx.setOnClickListener(v -> pvOptions.show());
        createMatterTypePicker();
        tv_add_url_icon.setOnClickListener(v -> SelectImagesTool.showAddPicView(getActivity(), picPath, selectRequestCode));
        addBtn = bindView(R.id.post_request_btn);

        titleEt = bindView(R.id.upload_url_title_et);
        TextView nickName = bindView(R.id.add_url_tv);
        nickName.setText(MyApp.userName);
        contetEt = bindView(R.id.content_et);
        addUrlInfoEt = bindView(R.id.add_url_info_et);
    }

    private void createMatterTypePicker() {
//        List<String> options1Items = new ArrayList<String>();
//        options1Items.add("产品");
//        options1Items.add("案例");
//        options1Items.add("活动");
//        options1Items.add("笑话");
//        options1Items.add("其他");
        if (progressDialogLoading == null)
            progressDialogLoading = ((MatterAddActivity)getActivity()).getProgressDialogLoading();
        progressDialogLoading.show();
        NetTool.getInstance().startRequest("get", UrlValue.CLASSIFY_LIST, ClassifyBean.class, new OnHttpCallBack<ClassifyBean>() {
            @Override
            public void onSuccess(ClassifyBean response) {
                if (progressDialogLoading!=null)progressDialogLoading.dismiss();
                if (response.getCode().equals("1")) {
                    pvOptions = new OptionsPickerView.Builder(getActivity(), (options1, option2, options3, v) -> {
                        //返回的分别是三个级别的选中位置
                        String tx = response.getCLibraryStort().get(options1).getCLsortname();
                        tv_sclx.setText(tx);
                        typeId = options1 + 1;

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
                    pvOptions.setPicker(response.getOptions());
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    ProgressDialog progressDialog;

    @Override
    protected void initData() {
        addBtn.setOnClickListener(v -> {
            if (typeId==-1){
                Toast.makeText(getActivity(),"请选择类型",Toast.LENGTH_SHORT).show();
                return;
            }
            if (progressDialog == null)
                progressDialog = new ProgressDialog(getActivity(), R.mipmap.loading);
            progressDialog.show();
            addBtn.setEnabled(false);
            String title = titleEt.getText().toString();
            String content = contetEt.getText().toString();
            String url = addUrlInfoEt.getText().toString();

            MatterRequestBean matterRequestBean=new MatterRequestBean();
            matterRequestBean.setUserId(MyApp.userId);
            matterRequestBean.setCompanyId(MyApp.companyDd);
            matterRequestBean.setContent(content);
            matterRequestBean.setLibrariesTortId(typeId);
            matterRequestBean.setLibrariesStage(2);
            matterRequestBean.setTitle(title);
            matterRequestBean.setUrl(url);
            String json = new Gson().toJson(matterRequestBean);
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("cLibrarys", json);
            Log.d("#####UrlAdd",json);
            Map<String, List<File>> paramFilesMap = new HashMap<String, List<File>>();
            List<File> files = new ArrayList<File>();
            for (int i=0;i<(urlIconList.size()==9?9:urlIconList.size()-1);i++) {
                files.add(new File(urlIconList.get(i)));
            }
            paramFilesMap.put("files", files);
            NetTool.getInstance().upLoadMultiFile(UrlValue.REQUEST_ADD_LIBRARY, paramsMap, paramFilesMap, ResponseBaseBean.class, new OkHttpUtil.ReqProgressCallBack<ResponseBaseBean>() {
                @Override
                public void onProgress(long total, long current) {

                    System.out.println("@@@@@@@@@@progress:total:" + total + ";current:" + current);


                }

                @Override
                public void onSuccess(ResponseBaseBean response) {
                    getActivity().runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
                    });
                    doSuccess(response);
                }


                @Override
                public void onFailed(String msg) {
                    addBtn.setEnabled(true);
                    getActivity().runOnUiThread(() -> {
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "添加失败", Toast.LENGTH_SHORT).show();
                    });

                }
            });


//            NetTool.getInstance().upLoadFile(UrlValue.ADD_LIBRARY + MyApp.userId +
//                            UrlValue.cLcompanyid + MyApp.companyDd + UrlValue.cHeadline + title + UrlValue.cContent + content + UrlValue.cInterlinkage + url +
//                            UrlValue.cLibrarystortid + typeId + UrlValue.cLibrarystage + 2
//                    , urlIconList, NormalResultBean.class, new OkHttpUtil.ReqProgressCallBack<NormalResultBean>() {
//                        @Override
//                        public void onProgress(long total, long current) {
//
//                        }
//
//                        @Override
//                        public void onSuccess(NormalResultBean response) {
//                            if (response.getCode().equals("1")) {
//                                Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
//                                getActivity().finish();
//                            }
//                        }
//
//                        @Override
//                        public void onFailed(String msg) {
//
//                        }
//                    });
        });
    }

    private void doSuccess(ResponseBaseBean response) {
        response.doResponse(() -> {
//                Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//                urlIconList = SelectImagesTool.onActivityResult(requestCode, resultCode, data);
        changeAvatar(requestCode, resultCode, data);
        if (requestCode == code_result_icon) {
            uploadIcon();
        }
    }

    private void changeAvatar(int requestCode, int resultCode, Intent data) {
        if (requestCode != selectRequestCode) return;
        ArrayList<String> paths = SelectImagesTool.onActivityResult(requestCode, resultCode, data);
        if (paths != null && paths.size() > 0) {
            picPath = paths.get(0);
            picUri = ImageTools.cropImage(getActivity(), code_result_icon, Uri.parse("file://" + "/" + picPath), 96, 96, getExternalStorageDirectory().getPath() + "/dx_temp/");
        }
    }

    private void uploadIcon() {
        urlIconList.clear();
        urlIconList.add(UriTools.getRealFilePath(getActivity(), picUri));
        iv_url_icon.setImageURI(picUri);
    }

    private void refreshIcon() {
        if (urlIconList != null && urlIconList.size() > 0) {
            iv_url_icon.setImageBitmap(ImageTools.decodeSampleBitmapFromFile(urlIconList.get(0), 48, 48));
        }
    }
}