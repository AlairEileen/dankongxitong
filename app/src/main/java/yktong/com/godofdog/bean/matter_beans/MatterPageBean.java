package yktong.com.godofdog.bean.matter_beans;

import java.util.ArrayList;
import java.util.List;

import yktong.com.godofdog.bean.MatterBean;

/**
 * Created by Eileen on 2017/7/19.
 */

public class MatterPageBean {

    private List<MatterContentBean> library_Image;
    private PageInfo pagination;

    public List<MatterContentBean> getLibrary_Image() {
        return library_Image;
    }

    public void setLibrary_Image(List<MatterContentBean> library_Image) {
        this.library_Image = library_Image;
    }

    public PageInfo getPagination() {
        return pagination;
    }

    public void setPagination(PageInfo pagination) {
        this.pagination = pagination;
    }

    public List<MatterBean> toMatterBeanList() {
        if (!(library_Image != null && library_Image.size() > 0)) return null;
        List<MatterBean> matterBeanList = new ArrayList<>();
        for (int i = 0; i < library_Image.size(); i++) {
//            MatterBean matterBean = new MatterBean();
//            matterBean.setId(i + "");
//            matterBean.setMatter_another(library_Image.get(i).getClibrary().getcName());
//            matterBean.setMatter_date_time(XString.toDateForLong(library_Image.get(i).getClibrary().getcTime()));
//            matterBean.setMatter_use_count(0 + "");
//            matterBean.setMatterType(matterBean.convertToMatterType(library_Image.get(i).getClibrary().getcLibrarystortid()));
//            matterBean.setTitle(library_Image.get(i).getClibrary().getcContent());
//            matterBean.setMatter_id(library_Image.get(i).getClibrary().getcLibraryid() + "");
//            if (matterBean.isLink(library_Image.get(i).getClibrary().getcLibrarystage())) {
//                if (library_Image.get(i).getClibraryImageUrl().size() > 0) {
//                    matterBean.setMatter_url_icon(library_Image.get(i).getClibraryImageUrl().get(0).getImageUrlone());
//                }
//                matterBean.setMatter_url_link(library_Image.get(i).getClibrary().getcInterlinkage());
//                matterBean.setMatter_url_title(library_Image.get(i).getClibrary().getcHeadline());
//
//            } else {
//                List<String> largeImgList = new ArrayList<>();
//                List<String> middleImgList = new ArrayList<>();
//                List<String> imgList = new ArrayList<>();
//                for (MatterImageBean matterImageBean : library_Image.get(i).getClibraryImageUrl()) {
//                    largeImgList.add(matterImageBean.getImageUrlone());
//                    middleImgList.add(matterImageBean.getImageUrltwo());
//                    imgList.add(matterImageBean.getImageUrlthree());
//                }
//                matterBean.setImgLargeList(largeImgList);
//                matterBean.setImgMiddleList(middleImgList);
//                matterBean.setImgList(imgList);
//            }
//            matterBeanList.add(matterBean);
            matterBeanList.add(library_Image.get(i).convertToMatter(i));
        }

        return matterBeanList;
    }
}
