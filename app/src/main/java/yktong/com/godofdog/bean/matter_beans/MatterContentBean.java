package yktong.com.godofdog.bean.matter_beans;

import java.util.ArrayList;
import java.util.List;

import space.eileen.tools.XString;
import yktong.com.godofdog.bean.MatterBean;

/**
 * Created by Eileen on 2017/7/19.
 */

public class MatterContentBean {
    private MatterTextBean clibrary;
    private List<MatterImageBean> clibraryImageUrl;

    public MatterTextBean getClibrary() {
        return clibrary;
    }

    public void setClibrary(MatterTextBean clibrary) {
        this.clibrary = clibrary;
    }

    public List<MatterImageBean> getClibraryImageUrl() {
        return clibraryImageUrl;
    }

    public void setClibraryImageUrl(List<MatterImageBean> clibraryImageUrl) {
        this.clibraryImageUrl = clibraryImageUrl;
    }

    public MatterBean convertToMatter(int... index) {

        MatterBean matterBean = new MatterBean();
        if (index != null) matterBean.setId(index[0] + "");
        matterBean.setMatter_use_count(getClibrary().getcUsetimes()+"");
        matterBean.setMatter_another(getClibrary().getcName());
        matterBean.setMatter_date_time(XString.toDateForLong(getClibrary().getcTime()));
        matterBean.setMatterType(matterBean.convertToMatterType(getClibrary().getcLibrarystortid()));
        matterBean.setTitle(getClibrary().getcContent());
        matterBean.setMatter_id(getClibrary().getcLibraryid() + "");
        if (matterBean.isLink(getClibrary().getcLibrarystage())) {
            if (getClibraryImageUrl().size() > 0) {
                matterBean.setMatter_url_icon(getClibraryImageUrl().get(0).getImageUrlone());
            }
            matterBean.setMatter_url_link(getClibrary().getcInterlinkage());
            matterBean.setMatter_url_title(getClibrary().getcHeadline());

        } else {
            List<String> largeImgList = new ArrayList<>();
            List<String> middleImgList = new ArrayList<>();
            List<String> imgList = new ArrayList<>();
            for (MatterImageBean matterImageBean : getClibraryImageUrl()) {
                largeImgList.add(matterImageBean.getImageUrlone());
                middleImgList.add(matterImageBean.getImageUrltwo());
                imgList.add(matterImageBean.getImageUrlthree());
            }
            matterBean.setImgLargeList(largeImgList);
            matterBean.setImgMiddleList(middleImgList);
            matterBean.setImgList(imgList);
        }
        return matterBean;
    }
}
