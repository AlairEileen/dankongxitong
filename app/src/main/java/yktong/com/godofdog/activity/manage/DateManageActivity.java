package yktong.com.godofdog.activity.manage;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import space.eileen.free_util.ProgressDialog;
import space.eileen.tools.XString;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.market.TokerAccurateActivity;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.DataManagerBean;
import yktong.com.godofdog.bean.PhoneNumberFileBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.value.UrlValue;
import yktong.com.godofdog.view.SingleCheckView;

import static yktong.com.godofdog.activity.market.TokerAccurateActivity.REQUEST_DATA_CODE;

/**
 * Created by vampire on 2017/7/5.
 */

public class DateManageActivity extends BaseActivity {
    List<PhoneNumberFileBean> phoneNumberFileBeen = new ArrayList<>();
    CommonAdapter<PhoneNumberFileBean> adapter;
    private AlertDialog alertDialog;
    private int code;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.activity_manage_date;
    }

    @Override
    protected void initView() {
        ListView listView = bindView(R.id.date_lv);
        Button updateBtn = bindView(R.id.update_date_btn);
        TextView addDate = bindView(R.id.add_date_btn);
        addDate.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            //intent.setCommunicatetype(“image/*”);//选择图片
            //intent.setCommunicatetype(“audio/*”); //选择音频
            //intent.setCommunicatetype(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
            //intent.setCommunicatetype(“video/*;image/*”);//同时选择视频和图片
            intent.setType("*/*");//无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);
        });


//        for (int i = 0; i < 10; i++) {
//            PhoneNumberFileBean bean = new DateListBean("file" + i, "2017年07月05日15:09:37", "未使用", "1000");
//            bean.setState(false);
//            phoneNumberFileBeen.add(bean);
//        }
        adapter = new CommonAdapter<PhoneNumberFileBean>(phoneNumberFileBeen, mContext, R.layout.date_list_item) {
            @Override
            public void setData(PhoneNumberFileBean dateListBean, int position, CommonViewHolder viewHolder) {
                SingleCheckView checkView = viewHolder.getView(R.id.check_view);
                checkView.setText(R.id.item_file_name, dateListBean.getcFilename());
                checkView.setText(R.id.item_update_time, dateListBean.getcFiletime());
                checkView.setText(R.id.date_num_tv, dateListBean.getCountPhone() + "条");
                checkView.setText(R.id.file_state_tv, dateListBean.getFileTypeName());
                ((ImageView) viewHolder.getView(R.id.file_state_icon)).setImageResource(dateListBean.isSelected() ? R.mipmap.icon_arr_zk : R.mipmap.icon_arr);
                checkView.setChecked(dateListBean.isSelected());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            for (int i = 0; i < phoneNumberFileBeen.size(); i++) {
                PhoneNumberFileBean bean = phoneNumberFileBeen.get(i);
                if (i == position) {
                    bean.setSelected(!bean.isSelected());
                } else {
                    bean.setSelected(false);
                }
                phoneNumberFileBeen.set(i, bean);
            }
            adapter.setList(phoneNumberFileBeen);
        });

        updateBtn.setOnClickListener(v -> {
            // TODO: 2017/7/5 确认选择

            if (phoneNumberFileBeen == null || phoneNumberFileBeen.size() == 0) {
                if (code == REQUEST_DATA_CODE) {
                    doBack(null);
                    return;

                }
//                changeFileStatus(fileBean.getcFileid());
                return;
            }

            for (PhoneNumberFileBean fileBean :
                    phoneNumberFileBeen) {
                if (fileBean.isSelected()) {
                    if (code == REQUEST_DATA_CODE) {
                        doBack(fileBean);
                        return;
                    }
                    changeFileStatus(fileBean.getcFileid());
                    break;
                }
            }
        });

    }

    private void doBack(PhoneNumberFileBean fileBean) {
        if (fileBean != null) {
            getIntent().putExtra(TokerAccurateActivity.DATA_ID, fileBean.getcFileid());
            getIntent().putExtra(TokerAccurateActivity.DATA_NAME, fileBean.getcFilename());
            setResult(TokerAccurateActivity.REQUEST_DATA_CODE, getIntent());
        }
        finish();
    }

    private void changeFileStatus(int fileId) {
        NetTool.getInstance().startRequest(OkHttpUtil.POST,
                UrlValue.REQUEST_FILE_STATUS_CHANGE
                        + MyApp.companyDd
                        + UrlValue.REQUEST_PARAM_FILE_ID
                        + fileId,
                DataManagerBean.class,
                new OnHttpCallBack<DataManagerBean>() {
                    @Override
                    public void onSuccess(DataManagerBean response) {
                        bindData(response);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    protected void initData() {
        initFiles();
    }

    private void initFiles() {
        try {
            code = getIntent().getIntExtra("code", 0);

        } catch (NullPointerException e) {

        }
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        NetTool.getInstance().startRequest(OkHttpUtil.POST, UrlValue.DATA_MANAGE_FIND_ALL + MyApp.companyDd, DataManagerBean.class, new OnHttpCallBack<DataManagerBean>() {
            @Override
            public void onSuccess(DataManagerBean response) {
                if (progressDialogLoading != null)
                    progressDialogLoading.dismiss();
                bindData(response);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = getPath(mContext, uri);
            Log.d("DateManageActivity", path);
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//            builder.setView(R.layout.dialog_view);
            View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_view, null);
            builder.setView(v);
            alertDialog = builder.show();
            v.findViewById(R.id.dialog_quite).setOnClickListener(v1 -> alertDialog.dismiss());
            v.findViewById(R.id.dialog_sure).setOnClickListener(v1 -> {
                // TODO: 2017/7/6   update
                doUpload(path);
            });

        }
    }


    /**
     * TODO:2017/7/11 uploadFile
     *
     * @param path
     */
    private void doUpload(String path) {
        File file = new File(path);
        if (!isFormatFile(file)) {
            alertDialog.dismiss();
            return;
        }
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this, R.mipmap.loading);
        progressDialog.show();
        HashMap<String, Object> params = new HashMap<>();
        params.put("file", file);
        params.put("cCompanyid", MyApp.companyDd);
        NetTool.getInstance().upLoadFile(UrlValue.UPLOAD_FILE_TXT, params, DataManagerBean.class, new OkHttpUtil.ReqProgressCallBack<DataManagerBean>() {
            @Override
            public void onProgress(long total, long current) {

            }

            @Override
            public void onSuccess(DataManagerBean response) {
                bindData(response);
                progressDialog.dismiss();
            }


            @Override
            public void onFailed(String msg) {
                progressDialog.dismiss();
            }
        });
    }

    private void bindData(DataManagerBean response) {

        response.doResponse(() -> {
            phoneNumberFileBeen.clear();
            phoneNumberFileBeen.addAll(response.getFile());
            this.runOnUiThread(() -> adapter.notifyDataSetChanged());
        });
    }

    private boolean isFormatFile(File file) {
        if (!file.exists()) {//不存在
            Toast.makeText(this, "文件不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!file.getName().substring(file.getName().indexOf(".") + 1).equals("txt")) {//不是txt文件格式
            Toast.makeText(this, "文件不合法！", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            //内容格式不正确
            FileReader fileReader = new FileReader(file);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
            try {
                String phoneNum = null;
                boolean isPhoneNo = true;

                while ((phoneNum = lineNumberReader.readLine()) != null) {
                    if (!XString.isMobileNO(phoneNum)) {
                        Toast.makeText(this, "文件内容非正确格式", Toast.LENGTH_SHORT).show();
                        isPhoneNo = false;
                        break;
                    }
                    if (lineNumberReader.getLineNumber() > 5000) {
                        Toast.makeText(this, "单次上传不能超过5000条", Toast.LENGTH_SHORT).show();
                        isPhoneNo = false;
                        break;
                    }
                }
                lineNumberReader.close();
                fileReader.close();
                return isPhoneNo;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The activity.
     * @param uri           The Uri to query.f
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */

    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
