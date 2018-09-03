package yktong.com.godofdog.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import space.eileen.photo_viewer.PicViewerActivity;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.ManageMatterActivity;
import yktong.com.godofdog.activity.MatterSearchActivity;
import yktong.com.godofdog.activity.WebActivity;
import yktong.com.godofdog.activity.market.MarketingRegularActivity;
import yktong.com.godofdog.bean.MatterBean;

/**
 * Created by Eileen on 2017/7/5.
 */

public class MatterListAdapter extends BaseAdapter {

    private final Activity matterActivity;
    List<View> views = new ArrayList<>();
    private List<MatterBean> matterBeanList;
    public MatterListAdapter(Activity matterActivity, List<MatterBean> matterBeenList) {
        this.matterActivity = matterActivity;
        this.matterBeanList = matterBeenList;
    }

    public void setData(List<MatterBean> matterBeanList) {
        this.matterBeanList = matterBeanList;
        notifyDataSetChanged();
    }

    public void addData(List<MatterBean> matterBeanList) {
        this.matterBeanList.addAll(matterBeanList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return matterBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return matterBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
        views.clear();
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        MatterList matterList = null;
        view = LayoutInflater.from(matterActivity).inflate(R.layout.item_matter_list, null);
        matterList = new MatterList();
        bindView(matterList, view);
        view.setTag(matterList);
        bindData(matterList, position, view);
        return view;
    }

    private void bindData(MatterList matterList, int position, View view) {
        bindCommon(matterList, position);
        if (matterBeanList.get(position).getMatter_url_title() == null) {
            if (matterBeanList.get(position).getImgList().size() > 1) {
                bindMultiImgTextData(matterList, position);
            } else if (matterBeanList.get(position).getImgList().size() == 1) {
                bindSingleImgTextData(matterList, position);
            }
        } else {
            bindUrlData(matterList, position, view);
        }
        int code = -1;
        if (matterActivity instanceof ManageMatterActivity) {
            code = ((ManageMatterActivity) matterActivity).code;
        } else if (matterActivity instanceof MatterSearchActivity) {
            code = ((MatterSearchActivity) matterActivity).code;

        }
        if (code == MarketingRegularActivity.REQUEST_MATTER_DATA_CODE) {
            view.setOnClickListener((v) -> {
                String matterJson = new Gson().toJson(matterBeanList.get(position));
                matterActivity.getIntent().putExtra(MarketingRegularActivity.MATTER_JSON, matterJson);
                matterActivity.setResult(MarketingRegularActivity.REQUEST_MATTER_DATA_CODE, matterActivity.getIntent());
                matterActivity.finish();
            });
        }

    }


    private void bindMultiImgTextData(MatterList matterList, int position) {
        matterList.gl_type_multi_img.setVisibility(View.VISIBLE);

        if (matterBeanList.get(position).getImgList().size() >= 1) {
            matterList.iv_single_multi_img_0.setVisibility(View.VISIBLE);
//                    matterList.iv_single_multi_img_0.setImageURI(Uri.parse(matterBeanList.get(position).getImgList().get(0)));
            Glide.with(matterActivity).load(matterBeanList.get(position).getImgList().get(0)).asBitmap().into(matterList.iv_single_multi_img_0);
            matterList.iv_single_multi_img_0.setOnClickListener((v) -> {
                goPhotoViewer(matterBeanList.get(position), 0);
            });
        }
        if (matterBeanList.get(position).getImgList().size() >= 2) {
            matterList.iv_single_multi_img_1.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_1.setImageResource(matterBeanList.get(position).getImgList().get(1));
            Glide.with(matterActivity).load(matterBeanList.get(position).getImgList().get(1)).asBitmap().into(matterList.iv_single_multi_img_1);
            matterList.iv_single_multi_img_1.setOnClickListener((v) -> {
                goPhotoViewer(matterBeanList.get(position), 1);
            });
        }
        if (matterBeanList.get(position).getImgList().size() >= 3) {
            matterList.iv_single_multi_img_2.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_2.setImageResource(matterBeanList.get(position).getImgList().get(2));
            Glide.with(matterActivity).load(matterBeanList.get(position).getImgList().get(2)).asBitmap().into(matterList.iv_single_multi_img_2);
            matterList.iv_single_multi_img_2.setOnClickListener((v) -> {
                goPhotoViewer(matterBeanList.get(position), 2);
            });
        }
        if (matterBeanList.get(position).getImgList().size() >= 4) {
            matterList.iv_single_multi_img_3.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_3.setImageResource(matterBeanList.get(position).getImgList().get(3));
            Glide.with(matterActivity).load(matterBeanList.get(position).getImgList().get(3)).asBitmap().into(matterList.iv_single_multi_img_3);
            matterList.iv_single_multi_img_3.setOnClickListener((v) -> {
                goPhotoViewer(matterBeanList.get(position), 3);
            });
        }
        if (matterBeanList.get(position).getImgList().size() >= 5) {
            matterList.iv_single_multi_img_4.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_4.setImageResource(matterBeanList.get(position).getImgList().get(4));
            Glide.with(matterActivity).load(matterBeanList.get(position).getImgList().get(4)).asBitmap().into(matterList.iv_single_multi_img_4);
            matterList.iv_single_multi_img_4.setOnClickListener((v) -> {
                goPhotoViewer(matterBeanList.get(position), 4);
            });
        }
        if (matterBeanList.get(position).getImgList().size() >= 6) {
            matterList.iv_single_multi_img_5.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_5.setImageResource(matterBeanList.get(position).getImgList().get(5));
            Glide.with(matterActivity).load(matterBeanList.get(position).getImgList().get(5)).asBitmap().into(matterList.iv_single_multi_img_5);
            matterList.iv_single_multi_img_5.setOnClickListener((v) -> {
                goPhotoViewer(matterBeanList.get(position), 5);
            });
        }
        if (matterBeanList.get(position).getImgList().size() >= 7) {
            matterList.iv_single_multi_img_6.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_6.setImageResource(matterBeanList.get(position).getImgList().get(6));
            Glide.with(matterActivity).load(matterBeanList.get(position).getImgList().get(6)).asBitmap().into(matterList.iv_single_multi_img_6);
            matterList.iv_single_multi_img_6.setOnClickListener((v) -> {
                goPhotoViewer(matterBeanList.get(position), 6);
            });
        }
        if (matterBeanList.get(position).getImgList().size() >= 8) {
            matterList.iv_single_multi_img_7.setVisibility(View.VISIBLE);
            //        matterList.iv_single_multi_img_7.setImageResource(matterBeanList.get(position).getImgList().get(7));
            Glide.with(matterActivity).load(matterBeanList.get(position).getImgList().get(7)).asBitmap().into(matterList.iv_single_multi_img_7);
            matterList.iv_single_multi_img_7.setOnClickListener((v) -> {
                goPhotoViewer(matterBeanList.get(position), 7);
            });
        }
        if (matterBeanList.get(position).getImgList().size() >= 9) {
            matterList.iv_single_multi_img_8.setVisibility(View.VISIBLE);
//        matterList.iv_single_multi_img_8.setImageResource(matterBeanList.get(position).getImgList().get(8));
            Glide.with(matterActivity).load(matterBeanList.get(position).getImgList().get(8)).asBitmap().into(matterList.iv_single_multi_img_8);
            matterList.iv_single_multi_img_8.setOnClickListener((v) -> {
                goPhotoViewer(matterBeanList.get(position), 8);
            });
        }


    }

    private void goPhotoViewer(MatterBean imgList, int position) {
        Intent intent = new Intent(matterActivity, PicViewerActivity.class);
        intent.putStringArrayListExtra(PicViewerActivity.MIDDLE_URL, (ArrayList<String>) imgList.getImgMiddleList());
        intent.putStringArrayListExtra(PicViewerActivity.LARGE_URL, (ArrayList<String>) imgList.getImgLargeList());
        intent.putExtra(PicViewerActivity.POSITION, position);
        matterActivity.startActivity(intent);
    }

    private void bindCommon(MatterList matterList, int position) {
        matterList.tv_matter_title.setText(matterBeanList.get(position).getTitle());
        matterList.tv_matter_time.setText(matterBeanList.get(position).getMatter_date_time());
        matterList.tv_matter_use_count.setText("使用" + matterBeanList.get(position).getMatter_use_count() + "次");
        matterList.tv_matter_type.setText(matterBeanList.get(position).getMatterType());
        matterList.tv_matter_another.setText(matterBeanList.get(position).getMatter_another());

        matterList.tv_matter_title.setVisibility(View.VISIBLE);
        matterList.tv_matter_time.setVisibility(View.VISIBLE);
        matterList.tv_matter_use_count.setVisibility(View.VISIBLE);
        matterList.tv_matter_type.setVisibility(View.VISIBLE);
        matterList.tv_matter_another.setVisibility(View.VISIBLE);
    }

    private void bindUrlData(MatterList matterList, int position, View view) {
        matterList.ll_type_url.setVisibility(View.VISIBLE);

        matterList.tv_title_url.setText(matterBeanList.get(position).getMatter_url_title());
        matterList.tv_title_url.setVisibility(View.VISIBLE);

        Glide.with(matterActivity).load(matterBeanList.get(position).getMatter_url_icon()).asBitmap().into(matterList.iv_icon_url);
//        matterList.iv_icon_url.setImageResource();

        matterList.iv_icon_url.setVisibility(View.VISIBLE);
        view.setOnClickListener(v -> {
            Intent intent = new Intent(matterActivity, WebActivity.class);
            intent.putExtra(WebActivity.WEB_TITLE, matterBeanList.get(position).getMatter_url_title());
            intent.putExtra(WebActivity.LOAD_URL, matterBeanList.get(position).getMatter_url_link());
            matterActivity.startActivity(intent);
        });

    }

    private void bindSingleImgTextData(MatterList matterList, int position) {
        Glide.with(matterActivity).load(matterBeanList.get(position).getImgList().get(0)).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource.getHeight() > resource.getWidth()) {
                    matterList.ll_type_single_height_img.setVisibility(View.VISIBLE);

                    matterList.iv_single_height_img.setVisibility(View.VISIBLE);
                    matterList.iv_single_height_img.setImageBitmap(resource);
                    matterList.iv_single_height_img.setOnClickListener((v) -> {
                        goPhotoViewer(matterBeanList.get(position), 0);
                    });
                } else {
                    //对比图片长宽
                    matterList.ll_type_single_width_img.setVisibility(View.VISIBLE);

                    matterList.iv_single_width_img.setVisibility(View.VISIBLE);
//        matterList.iv_single_width_img.setImageResource(/);
                    matterList.iv_single_width_img.setImageBitmap(resource);
                    matterList.iv_single_width_img.setOnClickListener((v) -> {
                        goPhotoViewer(matterBeanList.get(position), 0);
                    });
                }
            }
        });


    }

    private void bindView(MatterList matterList, View view) {

        matterList.ll_type_url = (LinearLayout) view.findViewById(R.id.ll_type_url);
        matterList.ll_type_single_width_img = (LinearLayout) view.findViewById(R.id.ll_type_single_width_img);
        matterList.ll_type_single_height_img = (LinearLayout) view.findViewById(R.id.ll_type_single_height_img);
        matterList.gl_type_multi_img = (GridLayout) view.findViewById(R.id.gl_type_multi_img);

        matterList.tv_matter_title = (TextView) view.findViewById(R.id.tv_matter_title);
        matterList.tv_title_url = (TextView) view.findViewById(R.id.tv_title_url);
        matterList.tv_matter_time = (TextView) view.findViewById(R.id.tv_matter_time);
        matterList.tv_matter_use_count = (TextView) view.findViewById(R.id.tv_matter_use_count);
        matterList.tv_matter_type = (TextView) view.findViewById(R.id.tv_matter_type);
        matterList.tv_matter_another = (TextView) view.findViewById(R.id.tv_matter_another);

        matterList.iv_icon_url = (ImageView) view.findViewById(R.id.iv_icon_url);
        matterList.iv_single_width_img = (ImageView) view.findViewById(R.id.iv_single_width_img);
        matterList.iv_single_height_img = (ImageView) view.findViewById(R.id.iv_single_height_img);
        matterList.iv_single_multi_img_0 = (ImageView) view.findViewById(R.id.iv_single_multi_img_0);
        matterList.iv_single_multi_img_1 = (ImageView) view.findViewById(R.id.iv_single_multi_img_1);
        matterList.iv_single_multi_img_2 = (ImageView) view.findViewById(R.id.iv_single_multi_img_2);
        matterList.iv_single_multi_img_3 = (ImageView) view.findViewById(R.id.iv_single_multi_img_3);
        matterList.iv_single_multi_img_4 = (ImageView) view.findViewById(R.id.iv_single_multi_img_4);
        matterList.iv_single_multi_img_5 = (ImageView) view.findViewById(R.id.iv_single_multi_img_5);
        matterList.iv_single_multi_img_6 = (ImageView) view.findViewById(R.id.iv_single_multi_img_6);
        matterList.iv_single_multi_img_7 = (ImageView) view.findViewById(R.id.iv_single_multi_img_7);
        matterList.iv_single_multi_img_8 = (ImageView) view.findViewById(R.id.iv_single_multi_img_8);
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

