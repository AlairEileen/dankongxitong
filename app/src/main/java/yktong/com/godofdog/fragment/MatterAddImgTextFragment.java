package yktong.com.godofdog.fragment;

import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.GridView;
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
import space.eileen.tools.PutImageGridAdapter;
import space.eileen.tools.SelectImagesTool;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.MatterAddActivity;
import yktong.com.godofdog.base.BaseFragment;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.NormalResultBean;
import yktong.com.godofdog.bean.matter_beans.MatterRequestBean;
import yktong.com.godofdog.bean.sns.ClassifyBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.SpValue;
import yktong.com.godofdog.value.UrlValue;

/**
 * Created by Eileen on 2017/7/4.
 */

public class MatterAddImgTextFragment extends BaseFragment implements SpValue {
    public static String imgTextBean = "img_text_bean";
    private GridView gv_pics;
    private PutImageGridAdapter putImageGridAdapter;
    private OptionsPickerView pvOptions;
    private TextView tv_sclx;
    private TextView tv_another;
    private EditText et_content;
    private int typeId = -1;
    private ArrayList<String> urlIconList;
    private ProgressDialog progressDialog;
    private int selectedPicsNum = 9;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.fragment_matter_add_img_text;
    }

    @Override
    protected void initView() {
        gv_pics = bindView(R.id.gv_pics);
        LinearLayout ll_sclx = bindView(R.id.ll_sclx);
        tv_sclx = bindView(R.id.tv_sclx);
        tv_another = bindView(R.id.tv_another);
        et_content = bindView(R.id.et_content);
        TextView btn_submit = bindView(R.id.btn_submit);


        putImageGridAdapter = new PutImageGridAdapter(getActivity(), selectedPicsNum, gv_pics);
        ll_sclx.setOnClickListener(v -> {
         if (pvOptions!=null)  pvOptions.show();
        });
        btn_submit.setOnClickListener(v ->
        {
            goSubmit(btn_submit);
        });

        createMatterTypePicker();
    }

    /**
     * 提交数据
     *
     * @param btn_submit
     */
    private void goSubmit(TextView btn_submit) {
        if (typeId==-1){
            Toast.makeText(getActivity(),"请选择类型",Toast.LENGTH_SHORT).show();
            return;
        }
        btn_submit.setEnabled(false);
        if (progressDialog == null)
            progressDialog = new ProgressDialog(getActivity(), R.mipmap.loading);
        progressDialog.show();
        String content = et_content.getText().toString();
        MatterRequestBean matterRequestBean = new MatterRequestBean();
        matterRequestBean.setUserId(MyApp.userId);
        matterRequestBean.setCompanyId(MyApp.companyDd);
        matterRequestBean.setContent(content);
        matterRequestBean.setLibrariesTortId(typeId);
        matterRequestBean.setLibrariesStage(1);
        String json = new Gson().toJson(matterRequestBean);
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("cLibrarys", json);
        Log.d("#####ImgTextAdd", json);
        Map<String, List<File>> paramFilesMap = new HashMap<>();
        List<File> files = new ArrayList<>();
        for (int i=0;i<(urlIconList.size()==9?9:urlIconList.size()-1);i++) {
            files.add(new File(urlIconList.get(i)));
        }
        paramFilesMap.put("files", files);
        NetTool.getInstance().upLoadMultiFile(UrlValue.ADD_LIBRARY, paramsMap, paramFilesMap, NormalResultBean.class, new OkHttpUtil.ReqProgressCallBack<NormalResultBean>() {
                    @Override
                    public void onProgress(long total, long current) {
                        if (current != 0) {
                            long num = current * 100 / total;
                            Log.d("MatterAddImgTextFragmen", "content/total:" + num);
                            System.out.println("@@@@@@@@@@progress:total:" + total + ";current:" + current);

                        }
                    }

                    @Override
                    public void onSuccess(NormalResultBean response) {
                        if (response.getCode().equals("1")) {
                            Log.d("MatterAddImgTextFragmen", response.toString());
                            getActivity().runOnUiThread(() -> {
                                progressDialog.dismiss();
                                Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
                            });
                            getActivity().finish();
                        } else {
                            Log.d("MatterAddImgTextFragmen", response.getCode());
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(mContext, "添加失败", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            });
                        }
                    }

                    @Override
                    public void onFailed(String msg) {
                        try {
                            btn_submit.setEnabled(true);
                            progressDialog.dismiss();
                            Log.e("MatterAddImgTextFragmen", msg);
                        } catch (Exception e) {
                            Log.e("MatterAddImgTextFragmen", e.getMessage());
                        }
                    }
                }
        );

    }

    private void createMatterTypePicker() {
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
                        typeId = options1 + 1;
                        tv_sclx.setText(tx);
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
                    pvOptions.setPicker(response.getOptions());
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });


    }

    @Override
    protected void initData() {
        initAnother();


    }

    /**
     * 初始化用户名称
     */
    private void initAnother() {
        tv_another.setText(MyApp.userName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        putImageGridAdapter.onActivityResult(requestCode, resultCode, data);
        urlIconList = SelectImagesTool.onActivityResult(requestCode, resultCode, data);

    }

}