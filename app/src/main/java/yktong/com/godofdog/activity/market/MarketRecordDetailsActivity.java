package yktong.com.godofdog.activity.market;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import space.eileen.free_util.ProgressDialog;
import space.eileen.photo_viewer.PicViewerActivity;
import yktong.com.godofdog.R;
import yktong.com.godofdog.activity.WebActivity;
import yktong.com.godofdog.base.BaseActivity;
import yktong.com.godofdog.bean.FriendBean;
import yktong.com.godofdog.bean.MatterBean;
import yktong.com.godofdog.bean.market_beans.MarketRecord;
import yktong.com.godofdog.bean.market_beans.MarketRecordDetailsResponseBean;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.value.UrlValue;

public class MarketRecordDetailsActivity extends BaseActivity {
    public final static String MARKET_RECORD_ID = "marketRecordId";
    private int marketRecordId;
    private MarketRecord marketRecord;
    private MatterList matterList;
    private MatterBean matterBean;
    private ProgressDialog progressDialogLoading;

    @Override
    protected int setLayout() {
        return R.layout.activity_market_record_details;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

        marketRecordId = getIntent().getIntExtra(MARKET_RECORD_ID, -1);
        if (progressDialogLoading == null)
            progressDialogLoading = new ProgressDialog(this, R.mipmap.loading);
        progressDialogLoading.show();
        NetTool.getInstance().startRequest("get", UrlValue.FIND_ONE_TASK_BY_TASK_ID + marketRecordId, MarketRecordDetailsResponseBean.class, new OnHttpCallBack<MarketRecordDetailsResponseBean>() {
            @Override
            public void onSuccess(MarketRecordDetailsResponseBean response) {
                if (progressDialogLoading!=null)progressDialogLoading.dismiss();

                response.doResponse(() -> {
                    bindCommon(response);
                    switch (response.getMarketRecordDetail().getMarketRecord().getTypeText().trim()) {
                        case "精准拓客":
                            bindTokerAccurate(response);
                            break;
                        case "社群拓客":
                            bindTokerCommunity(response);
                            break;
                        case "定时互动":
                            bindInteractionRegular(response);
                            break;
                        case "朋友圈":
                            bindCircleFriends(response);
                            break;
                        case "定时营销":
                            bindMarketingRegular(response);
                            break;
                        case "小程序":
                            bindXCX(response);
                            break;
                    }
                });

            }


            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void bindXCX(MarketRecordDetailsResponseBean response) {
        LinearLayout ll_xcx = bindView(R.id.ll_xcx);
        ll_xcx.setVisibility(View.VISIBLE);

        TextView tv_yxdx_xcx = bindView(R.id.tv_yxdx_xcx);
        TextView tv_yxjg_yx = bindView(R.id.tv_yxjg_xcx);
        TextView tv_xcx = bindView(R.id.tv_xcx);
        if (response.getMarketRecordDetail().getcWechatFriendList() != null && response.getMarketRecordDetail().getcWechatFriendList().size() > 0) {
            String names = "";
            for (FriendBean.CWechatFriend wf : response.getMarketRecordDetail().getcWechatFriendList()) {
                names += wf.getNickName() + "、";
            }
            names = names.substring(0, names.length() - 1);
            tv_yxdx_xcx.setText(names);
        }
        tv_yxjg_yx.setText(response.getMarketRecordDetail().getMarketRecord().getcIntervaltime() + "");
        tv_xcx.setText(response.getMarketRecordDetail().getMarketRecord().getcCxcname());
        //// TODO: 2017/8/1 绑定定时营销数据
    }

    private void bindCommon(MarketRecordDetailsResponseBean response) {
        TextView tv_rwlx = bindView(R.id.tv_rwlx);
        TextView tv_zxsj = bindView(R.id.tv_zxsj);
        TextView tv_cjsj = bindView(R.id.tv_cjsj);
        TextView tv_tjr = bindView(R.id.tv_tjr);

        tv_rwlx.setText(response.getMarketRecordDetail().getMarketRecord().getTypeText());
        tv_zxsj.setText(response.getMarketRecordDetail().getMarketRecord().getTimeText());
        tv_cjsj.setText(response.getMarketRecordDetail().getMarketRecord().getcSubmittimeText());
        tv_tjr.setText(response.getMarketRecordDetail().getMarketRecord().getcName());
        //// TODO: 2017/8/1 绑定通用数据

        LinearLayout ll_zxzt = bindView(R.id.ll_zxzt);
        TextView tv_zxzt = bindView(R.id.tv_zxzt);
        ll_zxzt.setVisibility(View.VISIBLE);
        //// TODO: 2017/8/1 绑定执行状态
        tv_zxzt.setText(response.getMarketRecordDetail().getMarketRecord().getStatusText());
    }

    /**
     * 定时营销
     *
     * @param response
     */
    private void bindMarketingRegular(MarketRecordDetailsResponseBean response) {
        bindMarketCommon(response);
        LinearLayout ll_yx = bindView(R.id.ll_yx);
        ll_yx.setVisibility(View.VISIBLE);

        TextView tv_yxdx_yx = bindView(R.id.tv_yxdx_yx);
        TextView tv_yxsl_yx = bindView(R.id.tv_yxsl_yx);
        TextView tv_yxjg_yx = bindView(R.id.tv_yxjg_yx);
        if (response.getMarketRecordDetail().getcWechatFriendList() != null && response.getMarketRecordDetail().getcWechatFriendList().size() > 0) {
            tv_yxdx_yx.setText(response.getMarketRecordDetail().getcWechatFriendList().get(0).getNickName());
        }
//        tv_yxsl_yx.setText(response.getMarketRecordDetail().getMarketRecord().getcNum() + "");
//        tv_yxjg_yx.setText(response.getMarketRecordDetail().getMarketRecord().getcIntervaltime() + "");
        //// TODO: 2017/8/1 绑定定时营销数据
    }

    private void bindMarketCommon(MarketRecordDetailsResponseBean response) {
        LinearLayout l_market = bindView(R.id.l_market);
        l_market.setVisibility(View.VISIBLE);
        LinearLayout ll_matter = bindView(R.id.ll_matter);
        ll_matter.setVisibility(View.VISIBLE);
        convertToMatterBean(response);
        bindMatterData();
    }

    private void convertToMatterBean(MarketRecordDetailsResponseBean response) {
        //// TODO: 2017/8/1 将返回来的数据转换成MatterBean
        matterBean = response.getMarketRecordDetail().getMatterContentBean().convertToMatter(0);
    }

    /**
     * 朋友圈
     *
     * @param response
     */
    private void bindCircleFriends(MarketRecordDetailsResponseBean response) {
        LinearLayout ll_circle_split = bindView(R.id.ll_circle_split);
        ll_circle_split.setVisibility(View.GONE);
        bindMarketCommon(response);
    }

    /**
     * 定时互动
     *
     * @param response
     */
    private void bindInteractionRegular(MarketRecordDetailsResponseBean response) {
        LinearLayout l_interaction = bindView(R.id.l_interaction);
        l_interaction.setVisibility(View.VISIBLE);

        TextView tv_hdsl = bindView(R.id.tv_hdsl);
        TextView tv_xwjg = bindView(R.id.tv_xwjg);
        //// TODO: 2017/8/1 绑定定时互动数据
        tv_hdsl.setText(response.getMarketRecordDetail().getMarketRecord().getcNum() + "");
        tv_xwjg.setText(response.getMarketRecordDetail().getMarketRecord().getcIntervaltime() + "");
    }

    /**
     * 社群拓客
     *
     * @param response
     */
    private void bindTokerCommunity(MarketRecordDetailsResponseBean response) {
        LinearLayout ll_qz_sq = bindView(R.id.ll_qz_sq);
        ll_qz_sq.setVisibility(View.VISIBLE);

        TextView tv_qz_sq = bindView(R.id.tv_qz_sq);
        //// TODO: 2017/8/1 绑定群组
        if (response.getMarketRecordDetail().getMarketRecord().getcWechatqun() != null && response.getMarketRecordDetail().getMarketRecord().getcWechatqun().trim().length() > 0) {
            tv_qz_sq.setText(response.getMarketRecordDetail().getMarketRecord().getcWechatqun());
        }
        bindTokerCommon(response);

    }

    /**
     * 精准拓客
     *
     * @param response
     */
    private void bindTokerAccurate(MarketRecordDetailsResponseBean response) {
        LinearLayout ll_sj_jz = bindView(R.id.ll_sj_jz);
        ll_sj_jz.setVisibility(View.VISIBLE);

        TextView tv_sj_jz = bindView(R.id.tv_sj_jz);
        if (response.getMarketRecordDetail().getTxtDataFileBean() == null || response.getMarketRecordDetail().getTxtDataFileBean() == null)
            return;
        tv_sj_jz.setText(response.getMarketRecordDetail().getTxtDataFileBean().getName());
        //// TODO: 2017/8/1 绑定数据
        bindTokerCommon(response);


    }

    private void bindTokerCommon(MarketRecordDetailsResponseBean response) {
        LinearLayout l_toker = bindView(R.id.l_toker);
        l_toker.setVisibility(View.VISIBLE);

        TextView tv_xb = bindView(R.id.tv_xb);
        TextView tv_tjsl = bindView(R.id.tv_tjsl);
        TextView tv_sjjg = bindView(R.id.tv_sjjg);
        TextView tv_yznr = bindView(R.id.tv_yznr);
        //// TODO: 2017/8/1 绑定拓客内容

        tv_xb.setText(response.getMarketRecordDetail().getMarketRecord().getcSexText());
        tv_tjsl.setText(response.getMarketRecordDetail().getMarketRecord().getcNum() + "");
        tv_sjjg.setText(response.getMarketRecordDetail().getMarketRecord().getcIntervaltime() + "");
        tv_yznr.setText(response.getMarketRecordDetail().getMarketRecord().getcCheckmage());
    }


    private void bindMatterData() {
        if (matterBean == null) return;
        if (matterList == null) {
            matterList = new MatterList();
            bindView(matterList);
        }
        clearViews(matterList);
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
