package yktong.com.godofdog.manager;

import java.util.ArrayList;
import java.util.List;

import yktong.com.godofdog.bean.DeviceBean;
import yktong.com.godofdog.bean.UserListBean;
import yktong.com.godofdog.tool.datebase.DBTool;

/**
 * Created by Eileen on 2017/8/21.
 */

public class DeviceManager {

    private static DeviceManager deviceManager;

    private DeviceManager() {

    }

    public static DeviceManager getInstance() {
        if (deviceManager == null) deviceManager = new DeviceManager();
        return deviceManager;
    }


    public ArrayList<UserListBean.UserBean> getDevices() {
        List<DeviceBean> deviceBeanList = DBTool.getInstance().queryDeviceBeanList();
        ArrayList<UserListBean.UserBean> userBeanList = new ArrayList<>();
        if (deviceBeanList == null) return userBeanList;

        for (DeviceBean deviceBean : deviceBeanList) {
            UserListBean.UserBean userBean = new UserListBean.UserBean();
            userBean.setCId(deviceBean.getId());
            userBean.setCName(deviceBean.getName());
            userBeanList.add(userBean);
        }
        return userBeanList;
    }

    public void saveDeviceBeanList(ArrayList<UserListBean.UserBean> userBeanList) {
        List<DeviceBean> deviceBeanList = new ArrayList<>();
        if (userBeanList == null) return;
        for (UserListBean.UserBean userBean : userBeanList) {
            DeviceBean deviceBean = new DeviceBean();
            deviceBean.setId(userBean.getCId());
            deviceBean.setName(userBean.getCName());
            deviceBeanList.add(deviceBean);
        }
        DBTool.getInstance().saveDeviceBeanList(deviceBeanList);
    }
}
