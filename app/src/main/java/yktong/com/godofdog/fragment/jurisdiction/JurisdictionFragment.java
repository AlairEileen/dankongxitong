package yktong.com.godofdog.fragment.jurisdiction;

import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import space.eileen.free_util.ProgressDialog;
import yktong.com.godofdog.R;
import yktong.com.godofdog.base.BaseFragment;
import yktong.com.godofdog.base.MyApp;
import yktong.com.godofdog.bean.jurisdiction_beans.JurisdictionBean;
import yktong.com.godofdog.bean.jurisdiction_beans.JurisdictionResponseBean;
import yktong.com.godofdog.bean.jurisdiction_beans.ResponseUserRoleBean;
import yktong.com.godofdog.bean.jurisdiction_beans.RoleBean;
import yktong.com.godofdog.bean.jurisdiction_beans.RoleResponseBase;
import yktong.com.godofdog.tool.net.NetTool;
import yktong.com.godofdog.tool.net.OkHttpUtil;
import yktong.com.godofdog.tool.net.OnHttpCallBack;
import yktong.com.godofdog.tool.view.CommonAdapter;
import yktong.com.godofdog.tool.view.CommonViewHolder;
import yktong.com.godofdog.value.UrlValue;


/**
 * Created by eileen on 2017/9/5.
 */

public class JurisdictionFragment extends BaseFragment {


    private LeftListViewManager leftListViewManager;
    private RightListViewManager rightListViewManager;
    private TopGridViewManager topGridViewManager;
    private ProgressDialog progressDialog;

    @Override
    protected int setLayout() {
        return R.layout.fragment_jurisdiction;
    }

    @Override
    protected void initView() {
        initTopGridView();
        initRightListView();
        initLeftListView();
    }

    private void initRightListView() {
        if (rightListViewManager == null)
            rightListViewManager = new RightListViewManager();
        rightListViewManager.init();

    }


    private void initTopGridView() {
        if (topGridViewManager == null)
            topGridViewManager = new TopGridViewManager();
        topGridViewManager.init();
    }


    private void initLeftListView() {
        if (leftListViewManager == null)
            leftListViewManager = new LeftListViewManager();
        leftListViewManager.init();
    }


    @Override
    protected void initData() {
        progressDialog = new ProgressDialog(getActivity(), R.mipmap.loading);
        initRoleData();
    }

    private void resetJurisdictionData() {
        if (topGridViewManager.getSelectedBean() == null || leftListViewManager.getSelectedBean() == null)
            return;
        NetTool.getInstance().startRequest(OkHttpUtil.GET,
                UrlValue.FIND_JURISDICTION_BY_FATHER + leftListViewManager.getSelectedBean().getId() +
                        UrlValue.FIND_JURISDICTION_BY_FATHER_ROLE_ID + topGridViewManager.getSelectedBean().getId()
                        + UrlValue.FANS_MANAGER_PARAM_USER_ID + MyApp.userId,
                JurisdictionResponseBean.class, new OnHttpCallBack<JurisdictionResponseBean>() {
                    @Override
                    public void onSuccess(JurisdictionResponseBean response) {
                        response.doResponse(new JurisdictionResponseBean.JurisdictionResponseStatus() {
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
                                    if (response.getJurisdictionBeanList() == null) return;
                                    rightListViewManager.jurisdictionBeanList.clear();
                                    rightListViewManager.jurisdictionBeanList.addAll(response.getJurisdictionBeanList());
                                    rightListViewManager.jurisdictionBeanCommonAdapter.notifyDataSetChanged();
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void resetJurisdictionTitleData() {
        if (progressDialog != null) progressDialog.show();

        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.FIND_ALL_JURISDICTION + UrlValue.FANS_MANAGER_PARAM_USER_ID_FIRST + MyApp.userId,
                JurisdictionResponseBean.class, new OnHttpCallBack<JurisdictionResponseBean>() {
                    @Override
                    public void onSuccess(JurisdictionResponseBean response) {
                        response.doResponse(new JurisdictionResponseBean.JurisdictionResponseStatus() {
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
                                    if (response.getJurisdictionBeanList() == null) return;
                                    leftListViewManager.jurisdictionTitleBeanList.clear();
                                    leftListViewManager.jurisdictionTitleBeanList.addAll(response.getJurisdictionBeanList());
                                    leftListViewManager.jurisdictionTitleBeanCommonAdapter.setList(leftListViewManager.jurisdictionTitleBeanList);
                                    leftListViewManager.jurisdictionTitleBeanCommonAdapter.notifyDataSetChanged();
                                    if (leftListViewManager.jurisdictionTitleBeanList.size() == 0)
                                        return;
                                    resetJurisdictionData();
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    private void initRoleData() {
        if (progressDialog != null) progressDialog.show();
        NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.FIND_ALL_ROLES + MyApp.companyDd + UrlValue.FANS_MANAGER_PARAM_USER_ID + MyApp.userId, RoleResponseBase.class, new OnHttpCallBack<RoleResponseBase>() {
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
                            topGridViewManager.roleBeanList.clear();
                            topGridViewManager.roleBeanList.addAll(response.getRoleBeanList());
                            topGridViewManager.groupBeanCommonAdapter.setList(topGridViewManager.roleBeanList);
                            topGridViewManager.groupBeanCommonAdapter.notifyDataSetChanged();
                            if (topGridViewManager.roleBeanList.size() == 0) return;
                            resetJurisdictionTitleData();
                        });
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }


    private class LeftListViewManager {
        boolean[] checkeds;
        private CommonAdapter<JurisdictionBean> jurisdictionTitleBeanCommonAdapter;
        private List<JurisdictionBean> jurisdictionTitleBeanList = new ArrayList<>();
        private ListView lv_jurisdiction_title;
        private int selectedIndex = 0;


        void init() {
            lv_jurisdiction_title = bindView(R.id.lv_group);
            final List<View> convertViewList = new ArrayList<>();

            jurisdictionTitleBeanCommonAdapter = new CommonAdapter<JurisdictionBean>(jurisdictionTitleBeanList, getActivity(), R.layout.item_lv_jurisdiction_left) {

                @Override
                public void setData(JurisdictionBean deptBean, int position, CommonViewHolder viewHolder) {
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
                public void setList(List<JurisdictionBean> beenList) {
                    checkeds = new boolean[beenList.size()];
                    for (int i = 0; i < checkeds.length; i++) {
                        checkeds[i] = selectedIndex == i ? true : false;
                    }
                    super.setList(beenList);
                }
            };
            lv_jurisdiction_title.setOnItemClickListener((parent, view, position, id) -> {
                for (View view1 : convertViewList) {
                    view1.setBackgroundResource(R.color.content_split_gray);
                    ((TextView) view1.findViewById(R.id.tv_lv_jurisdiction_text))
                            .setTextColor(getActivity().getResources().getColor(R.color.text_md_gray));
                }
                ((TextView) view.findViewById(R.id.tv_lv_jurisdiction_text))
                        .setTextColor(getActivity().getResources().getColor(R.color.text_black));
                view.setBackgroundResource(R.color.theme_white);
                checkeds[selectedIndex] = false;
                selectedIndex = position;
                checkeds[selectedIndex] = true;
                resetJurisdictionTitleData();
            });
            lv_jurisdiction_title.setAdapter(jurisdictionTitleBeanCommonAdapter);
        }

        JurisdictionBean getSelectedBean() {
            return jurisdictionTitleBeanList.get(selectedIndex);
        }

    }


    private class RightListViewManager {

        private CommonAdapter<JurisdictionBean> jurisdictionBeanCommonAdapter;
        private List<JurisdictionBean> jurisdictionBeanList = new ArrayList<>();
        private ListView lv_jurisdiction;


        void init() {
            lv_jurisdiction = bindView(R.id.lv_jurisdiction);
            jurisdictionBeanCommonAdapter = new CommonAdapter<JurisdictionBean>(jurisdictionBeanList, getActivity(), R.layout.item_gv_jurisdiction_right) {
                @Override
                public void setData(JurisdictionBean jurisdictionBean, int position, CommonViewHolder viewHolder) {
                    viewHolder.setText(R.id.tv_gv_item_jurisdiction_right, jurisdictionBean.getName());
                    View view = viewHolder.getConvertView();
                    changeJurisdiction(view, jurisdictionBean.getSelectedStatusBool());
                }
            };
            lv_jurisdiction.setOnItemClickListener((parent, view, position, id) -> {
                if (jurisdictionBeanList == null || topGridViewManager.getSelectedBean() == null || jurisdictionBeanList.size() <= position)
                    return;
                NetTool.getInstance().startRequest(OkHttpUtil.GET, UrlValue.CHANGE_JURISDICTION + jurisdictionBeanList.get(position).getId() +
                                UrlValue.FIND_JURISDICTION_BY_FATHER_ROLE_ID + topGridViewManager.getSelectedBean().getId()
                                + UrlValue.FANS_MANAGER_PARAM_USER_ID + MyApp.userId,

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
                                        resetJurisdictionTitleData();
                                    }
                                });

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
            });
            lv_jurisdiction.setAdapter(jurisdictionBeanCommonAdapter);
        }

        private void changeJurisdiction(View view, boolean isSelected) {
            view.findViewById(R.id.iv_gv_item_jurisdiction_right).setVisibility(isSelected ? View.VISIBLE : View.GONE);
            ((TextView) view.findViewById(R.id.tv_gv_item_jurisdiction_right)).
                    setTextColor(getActivity().getResources().getColor(isSelected ? R.color.text_blue_light : R.color.text_black));
        }
    }


    private class TopGridViewManager {
        boolean[] checkeds;
        private CommonAdapter<RoleBean> groupBeanCommonAdapter;
        private List<RoleBean> roleBeanList = new ArrayList<>();
        private GridView gv_user;
        private int selectedIndex = 0;


        void init() {
            gv_user = bindView(R.id.gv_user);
            List<View> convertViewList = new ArrayList<>();
            groupBeanCommonAdapter = new CommonAdapter<RoleBean>(roleBeanList, getActivity(), R.layout.item_gv_group) {
                @Override
                public void setData(RoleBean roleBean, int position, CommonViewHolder viewHolder) {
                    viewHolder.setText(R.id.lv_text, roleBean.getName());
                    initSelectedTopGridView(position, viewHolder.getConvertView(), checkeds[position]);
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
                        checkeds[i] = selectedIndex == i ? true : false;
                    }
                    super.setList(beenList);
                }
            };
            gv_user.setOnItemClickListener((parent, view, position, id) -> {
                for (View view1 : convertViewList) {
                    initSelectedTopGridView(position, view1, false);
                }
                checkeds[selectedIndex] = false;
                selectedIndex = position;
                checkeds[selectedIndex] = true;
                initSelectedTopGridView(position, view, true);
                resetJurisdictionTitleData();
            });
            gv_user.setAdapter(groupBeanCommonAdapter);
        }

        private void initSelectedTopGridView(int position, View view, boolean status) {
            TextView textView = (TextView) view.findViewById(R.id.lv_text);
            textView.setBackgroundResource(status ? R.drawable.selected_item_gv_group : R.drawable.unselected_item_gv_group);
            textView.setTextColor(getActivity().getResources().getColor(status ? R.color.text_blue_light : R.color.text_black));
        }

        public RoleBean getSelectedBean() {
            if (roleBeanList.size() == 0) return null;
            return roleBeanList.get(selectedIndex);
        }
    }
}
