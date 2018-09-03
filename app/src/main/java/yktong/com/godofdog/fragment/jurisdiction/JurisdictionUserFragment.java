package yktong.com.godofdog.fragment.jurisdiction;

import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.utils.Log;

import java.util.ArrayList;
import java.util.List;

import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.adapter.GridSelectAdapter;
import yktong.com.godofdog.base.BaseFragment;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.jurisdiction_beans.ResponseUserRoleBean;
import yktong.com.godofdog.bean.jurisdiction_beans.RoleBean;
import yktong.com.godofdog.bean.jurisdiction_beans.RoleResponseBase;
import yktong.com.godofdog.bean.jurisdiction_beans.UserListResponseBean;
import yktong.com.godofdog.bean.jurisdiction_beans.UserMoveToRoleBean;
import yktong.com.godofdog.bean.user_beans.UserRequestBean;
import yktong.com.godofdog.popup.PutTextPopup;
import yktong.com.godofdog.popup.SelectListPopup;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.value.UrlValue;


/**
 * Created by eileen on 2017/9/5.
 */

public class JurisdictionUserFragment extends BaseFragment {


    private GridView gv_users;
    private List<UserRequestBean> userRequestBeanArrayList = new ArrayList<>();
    private GridSelectAdapter<UserRequestBean> userRequestBeanGridSelectAdapter;

    private LeftListViewManager leftListViewManager;
    private ProgressDialog progressDialog;

    @Override
    protected int setLayout() {
        return R.layout.fragment_jurisdiction_user;

    }

    @Override
    protected void initView() {
        bindView(R.id.tv_add).setOnClickListener(v -> goAddPopup());
        bindView(R.id.tv_move).setOnClickListener(v -> goMovePopup());
        initLeftListView();

        gv_users = bindView(R.id.gv_users);
        userRequestBeanGridSelectAdapter = new GridSelectAdapter<>(userRequestBeanArrayList, getActivity(), R.layout.item_gv_jurisdiction_right, gv_users, R.id.tv_gv_item_jurisdiction_right, R.id.iv_gv_item_jurisdiction_right);
        gv_users.setAdapter(userRequestBeanGridSelectAdapter);
        bindView(R.id.tv_selectAll).setOnClickListener(v -> userRequestBeanGridSelectAdapter.selectAllOrNone());
    }

    private void goMovePopup() {
        if (leftListViewManager.getSelectedBean() == null) return;
        if (userRequestBeanGridSelectAdapter.getSelectedBeans().size() == 0) {
            Toast.makeText(getActivity(), "请选择要移动的人员", Toast.LENGTH_SHORT).show();
            return;
        }
        new SelectListPopup<>(getActivity(), "选择移动到的角色", R.id.tv_move, RoleBean.class, leftListViewManager.roleBeanArrayList, leftListViewManager.getSelectedBean(), bean -> {
            if (userRequestBeanGridSelectAdapter.getSelectedBeans() == null || userRequestBeanGridSelectAdapter.getSelectedBeans().size() == 0)
                return;
            List<UserMoveToRoleBean> userMoveToRoleBeanList = new ArrayList<>();
            for (UserRequestBean userRequestBean : userRequestBeanGridSelectAdapter.getSelectedBeans()) {
                UserMoveToRoleBean userMoveToRoleBean = new UserMoveToRoleBean();
                userMoveToRoleBean.setUserId(userRequestBean.getId());
                userMoveToRoleBeanList.add(userMoveToRoleBean);
            }

            String json = new Gson().toJson(userMoveToRoleBeanList);
            Log.d("#####goMovePopup", json);
            if (progressDialog != null) progressDialog.show();
            NetTool.getInstance().postRequest(UrlValue.MOVE_USER_TO_ROLE + bean.getId() +
                            UrlValue.FANS_MANAGER_PARAM_USER_ID + MyApp.userId, json,

                    ResponseUserRoleBean.class, new OnHttpCallBack<ResponseUserRoleBean>() {
                        @Override
                        public void onSuccess(ResponseUserRoleBean response) {

                            response.doResponse(new ResponseUserRoleBean.ResponseUserRoleStatus() {
                                @Override
                                public void not_admin(String msg) {
                                    getActivity().runOnUiThread(() -> {
                                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
                                    });
                                }

                                @Override
                                public void cannot_do(String msg) {
                                    getActivity().runOnUiThread(()->{
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                                    });
                                }

                                @Override
                                public void doSuccess() {
                                    resetRightUserData();
                                }
                            });
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        });
    }

    private void goAddPopup() {
        PutTextPopup.build(getActivity(), "添加角色", R.id.lv_group, text -> {
            if (text == null || text.trim().length() == 0) return;
            RoleBean roleBean = new RoleBean();
            roleBean.setCompanyId(MyApp.companyDd);
            roleBean.setName(text);
            String json = new Gson().toJson(roleBean);
            if (progressDialog != null) progressDialog.show();
            NetTool.getInstance().postRequest(UrlValue.ROLE_ADD_REQUEST + UrlValue.FANS_MANAGER_PARAM_USER_ID_FIRST + MyApp.userId, json,

                    ResponseUserRoleBean.class, new OnHttpCallBack<ResponseUserRoleBean>() {
                        @Override
                        public void onSuccess(ResponseUserRoleBean response) {

                            response.doResponse(new ResponseUserRoleBean.ResponseUserRoleStatus() {
                                @Override
                                public void not_admin(String msg) {
                                    getActivity().runOnUiThread(() -> {
                                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
                                    });
                                }

                                @Override
                                public void cannot_do(String msg) {
                                    getActivity().runOnUiThread(()->{
                                        progressDialog.dismiss();
                                        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
                                    });
                                }

                                @Override
                                public void doSuccess() {
                                    resetRoleData();
                                }
                            });

                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        });
    }

    private void resetRoleData() {
        if (progressDialog != null) progressDialog.show();
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.FIND_ALL_ROLES + MyApp.companyDd + UrlValue.FANS_MANAGER_PARAM_USER_ID + MyApp.userId,
                RoleResponseBase.class, new OnHttpCallBack<RoleResponseBase>() {
                    @Override
                    public void onSuccess(RoleResponseBase response) {

                        response.doResponse(new RoleResponseBase.RoleResponseStatus() {
                            @Override
                            public void notAdmin(String msg) {
                                getActivity().runOnUiThread(() -> {
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                });
                            }

                            @Override
                            public void doSuccess() {
                                getActivity().runOnUiThread(() -> {
                                    if (response.getRoleBeanList() == null) return;
                                    leftListViewManager.roleBeanArrayList.clear();
                                    leftListViewManager.roleBeanArrayList.addAll(response.getRoleBeanList());
                                    leftListViewManager.roleBeanCommonAdapter.setList(leftListViewManager.roleBeanArrayList);
                                    leftListViewManager.roleBeanCommonAdapter.notifyDataSetChanged();
                                    if (response.getRoleBeanList().size() == 0) return;
                                    resetRightUserData();
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void resetRightUserData() {
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.FIND_USER_LIST_BY_ROLE + leftListViewManager.getSelectedBean().getId() + UrlValue.FANS_MANAGER_PARAM_USER_ID + MyApp.userId, UserListResponseBean.class, new OnHttpCallBack<UserListResponseBean>() {
            @Override
            public void onSuccess(UserListResponseBean response) {
                response.doResponse(new UserListResponseBean.UserListResponseStatus() {
                    @Override
                    public void notAdmin(String msg) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        });
                    }

                    @Override
                    public void doSuccess() {
                        getActivity().runOnUiThread(() -> {
                            if (progressDialog != null) progressDialog.dismiss();
                            if (response.getUserRequestBeanList() == null) return;
                            userRequestBeanArrayList.clear();
                            userRequestBeanArrayList.addAll(response.getUserRequestBeanList());
                            userRequestBeanGridSelectAdapter.setList(userRequestBeanArrayList);
                            userRequestBeanGridSelectAdapter.notifyDataSetChanged();
                        });
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }


    private void initLeftListView() {
        if (leftListViewManager == null)
            leftListViewManager = new LeftListViewManager();
        leftListViewManager.init();
    }


    @Override
    protected void initData() {
        progressDialog = new ProgressDialog(getActivity(), R.mipmap.loading);
        resetRoleData();
    }


    private class LeftListViewManager {
        boolean[] checkeds;
        private CommonAdapter<RoleBean> roleBeanCommonAdapter;
        private List<RoleBean> roleBeanArrayList = new ArrayList<>();
        private ListView lv_role_title;
        private int selectedDeptIndex = 0;


        void init() {
            lv_role_title = bindView(R.id.lv_group);
            final List<View> convertViewList = new ArrayList<>();

            roleBeanCommonAdapter = new CommonAdapter<RoleBean>(roleBeanArrayList, getActivity(), R.layout.item_lv_jurisdiction_left) {

                @Override
                public void setData(RoleBean deptBean, int position, CommonViewHolder viewHolder) {
                    viewHolder.setText(R.id.tv_lv_jurisdiction_text, deptBean.getName());
                    View view = viewHolder.getConvertView();
                    view.setBackgroundResource(checkeds[position] ? R.color.theme_white : R.color.content_split_gray);
                    convertViewList.add(viewHolder.getConvertView());
                }

                @Override
                public void notifyDataSetChanged() {
                    convertViewList.clear();
                    super.notifyDataSetChanged();
                }

                @Override
                public void setList(List<RoleBean> beenList) {
                    checkeds = new boolean[beenList.size()];
                    for (int i = 0; i < checkeds.length; i++) {
                        checkeds[i] = selectedDeptIndex == i ? true : false;
                    }
                    super.setList(beenList);
                }
            };
            lv_role_title.setOnItemClickListener((parent, view, position, id) -> {
                for (View view1 : convertViewList) {
                    view1.setBackgroundResource(R.color.content_split_gray);
                    ((TextView) view1.findViewById(R.id.tv_lv_jurisdiction_text))
                            .setTextColor(getActivity().getResources().getColor(R.color.text_md_gray));
                }
                ((TextView) view.findViewById(R.id.tv_lv_jurisdiction_text))
                        .setTextColor(getActivity().getResources().getColor(R.color.text_black));
                view.setBackgroundResource(R.color.theme_white);
                checkeds[selectedDeptIndex] = false;
                selectedDeptIndex = position;
                checkeds[selectedDeptIndex] = true;
                resetRightUserData();
            });
            lv_role_title.setAdapter(roleBeanCommonAdapter);
        }

        RoleBean getSelectedBean() {
            return roleBeanArrayList.get(selectedDeptIndex);
        }

    }
}
