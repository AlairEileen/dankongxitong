package yktong.com.godofdog.bean;

import java.util.List;

/**
 * Created by Eileen on 2017/7/17.
 */

public class DataManagerBean extends ResponseBaseBean {
    private List<PhoneNumberFileBean> file;


    public List<PhoneNumberFileBean> getFile() {
        return file;

    }

    public void setFile(List<PhoneNumberFileBean> file) {
        this.file = file;
    }

    public void doResponse(DataManageResponseStatus responseStatus) {
        super.doResponse(responseStatus);

    }

    public interface DataManageResponseStatus extends ResponseBaseBean.ResponseStatus{

    }
}
