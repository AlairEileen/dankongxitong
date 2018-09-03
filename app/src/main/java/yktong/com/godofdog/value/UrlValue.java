package yktong.com.godofdog.value;

/**
 * Created by vampire on 2017/7/3.
 */

public interface UrlValue {
    int SUCCESS = 1;
    int NO_DATE = 2;
    int PARAM_MISS = 3;
    int NO_PERMISSION = 4;
    int ERROR = 5;
    int ERROR_VERIFY_CODE = 6;
    // 线上
    String LOCAL_URL = "http://47.94.42.159:8080/";
    // 本地
//    String LOCAL_URL = "http://192.168.1.6:8080/";
//    String LOCAL_URL = "http://192.168.1.9:8080/";
//    String LOCAL_URL = "http://192.168.1.5:8080/";
//    String LOCAL_URL = BASE_URL;
    //本地陈琳Url
//    String LOCAL_URL_CL = "http://192.168.1.5:8080/";
//    String LOCAL_URL = "http://192.168.1.6:8080/";
    //    String LOCAL_URL_CL = BASE_URL;
    // 项目地址
    String PROJECT_URL = LOCAL_URL + "ManualControl/httpService/";
    //项目地址陈琳Url
//    String PROJECT_URL_CL = LOCAL_URL_CL + "ManualControl/httpService/";

    //登录注册开始
    String REQUEST_VERIFY_CODE = PROJECT_URL + "sendCode.action?cLoginname=";
    String REQUEST_USER_LOGIN = PROJECT_URL + "startLogin.action";
    String IMEI= "&imei=";
    String REQUEST_SIGN_ON = PROJECT_URL + "register.action";
    String REQUEST_FIND_PWD = PROJECT_URL + "editPassword.action";

    //登录注册结束

    //txt文件上传开始
    String UPLOAD_FILE_TXT = PROJECT_URL + "uploadfield.action";
    String UPLOAD_FILE_AVATAR = PROJECT_URL + "uploadPortrait.action";
    String REQUEST_USER_AVATAR = PROJECT_URL + "checkPortrait.action?userid=";
    String DATA_MANAGE_FIND_ALL = PROJECT_URL + "selectFile.action?cCompanyid=";
    String REQUEST_FILE_STATUS_CHANGE = PROJECT_URL + "updateType.action?cCompanyid=";
    String REQUEST_PARAM_FILE_ID = "&cFileid=";
    //    String UPLOAD_FILE_TXT = PROJECT_URL + "register.action?";
    //txt文件上传结束

    //managementfragment请求
    String REQUEST_MANAGEMENT_FRAGMENT = PROJECT_URL + "countOrder.action?companyid=";


    /**
     * 接口参数：
     * cOuserid 用户id
     * cCustomername 客户姓名
     * cCompanyname 公司名称
     * cLinktel 联系电话
     * cWechat 微信
     * cAddress 详细地址
     * cProduct 产品
     * cNum 数量
     * cAmount 金额
     * cPayment 收款方式
     */
    String ADD_ORDER = PROJECT_URL + "addOrder.action";

    // 营销页
    String PAY_INFO = PROJECT_URL + "countWeChatData.action?cWduserid=";

    //获取订单 get
    String GET_ORDER = PROJECT_URL + "selectOrderByUser.action?userid=";

    //订单详情
    String ORDER_INFO = PROJECT_URL + "selectOneOrder.action?orderid=";

    //粉丝管理
    String FANS_MANGER = PROJECT_URL + "countFans.action?companyid=";
    String FANS_MANAGER_PARAM_DEPT = "&deptid=";

    //获取任务列表
    String GET_TASK_LIST = PROJECT_URL + "selectTaskByUser.action?userid=";

    //微信好友自增
    String ADD_WE_FRIEND = PROJECT_URL + "addWeChatFriend.action?userid=";

    //添加群组列表 (post)
    String REFRSH_GROUP = PROJECT_URL + "addWeChatQun.action";

    // 查询群组 userid get
    String QUERY_GROUP = PROJECT_URL + "selectWeChatQun.action?userid=";

    //首页信息 (get)
    String MAIN_INFO = PROJECT_URL + "indexWechatData.action?userid=";


    String ORER_INFO = PROJECT_URL + "selectOneOrder.action?Orderid=";

    //业绩管理(get)
    String PERFORMANCE_MANGER = PROJECT_URL + "orderManagerIndex.action?cCompanyid=";
    String DEPT_ID = "&cUdeptid=";

    /**
     * cWdtodayquan	今日发朋友圈数量	int
     * cWdtodaymarket	今日营销	int
     * cWdcountfriend	好友总数	int
     * cWdtodayinteract	今日互动	Int
     * cWdtime	上传数据时间	Dete
     */
    // 微信数据添加(post)
    String WECHAT_INFO = PROJECT_URL + "addWeChatData.action?cWduserid=";
    String cWdtodayquan = "&cWdtodayquan=";
    String cWdtodaymarket = "&cWdtodaymarket=";
    String cWdcountfriend = "&cWdcountfriend=";
    String cWdtodayinteract = "&cWdtodayinteract=";
    String cWdtime = "&cWdtime=";

    // 上传好友列表 (post)
    String UPDATE_FRIEND_LIST = PROJECT_URL + "addWechatFriend.action?userid=";
    // 查询好友列表(get)
    String SELECT_FRIEND = PROJECT_URL + "selectWechatFriend.action?userid=";
    String SELECT_USER = PROJECT_URL + "selectUserByCompany.action?companyid=";


    //查询素材分类列表 (get)
    String CLASSIFY_LIST = PROJECT_URL + "selectLibraryStort.action";
    //素材列表
    String REQUEST_MATTER_LIST = PROJECT_URL + "selectLirbrays.action?pageIndex=";
    String REQUEST_MATTER_LIST_PARAM_PAGE_INDEX = "&pageIndex=";
    String REQUEST_MATTER_LIST_PARAM_PAGE_SIZE = "&pageSize=";
    String REQUEST_MATTER_LIST_SEARCH_PARAM = "&nameContentHeadline=";

    //添加素材  (post)
    /**
     * cLuserid	用户id	Int	必填
     * cLcompanyid	公司id	Int	必填
     * cHeadline	标题	"Int"	选填
     * cContent	内容（此刻的想法）	String	选填
     * cInterlinkage	链接	String	选填
     * cLibrarystortid	类别id	Int	必填
     * cLibrarystage	素材类别(1：图文/2：链接)	"Int" 必填
     */
    String ADD_LIBRARY = PROJECT_URL + "addLibrary.action";
    String REQUEST_ADD_LIBRARY = PROJECT_URL + "addLibrary.action";

    String cLcompanyid = "&cLcompanyid=";
    String cHeadline = "&cHeadline=";
    String cContent = "&cContent=";
    String cInterlinkage = "&cInterlinkage=";
    String cLibrarystortid = "&cLibrarystortid=";
    String cLibrarystage = "&cLibrarystage=";


    // 微信精选 接口(get 键入页数)
    String WE_CHAT_DATE = "http://v.juhe.cn/weixin/query?key=ffcfe82ad84b635f223c6f1180fb84d8&pno=";

    // 添加任务  (post)
    String ADD_TAKS = PROJECT_URL + "addTask.action?userid=";
    //查询任务列表(get)
    String SELECT_TASK_LIST = PROJECT_URL + "selectTaskByUser.action?userid=";
    //根据taskId获取好友名称(get)
    String GET_TASK_NICK_NAME = PROJECT_URL + "selectFriendByTaskid.action?cTaskid=";
    //根据素材ID 查素材内容(get)
    String SELECT_LIBRARY_BY_ID = PROJECT_URL + "selectLibraryById.action?cLibraryid=";
    // 查询号码
    String QUERY_PHONE = PROJECT_URL + "queryPhone.action?cCtaskid=";

    //修改昵称
    String UPDATE_NICK = PROJECT_URL + "updateCname.action?cId=";
//    String UPDATE_NICK = "http://192.168.1.6:8080/updateCname.action?cId=";

    //任务列表查询
    String FIND_TASK_BY_STAGE = PROJECT_URL + "selectTaskByStage.action?userid=";
    String FIND_TASK_BY_STAGE_PARAM_STAGE = "&stage=";
    //任务详情查询
    String FIND_ONE_TASK_BY_TASK_ID = PROJECT_URL + "selectOneTask.action?cTaskid=";

    //修改任务状态 get
    String UPDATE_TASK = PROJECT_URL + "editTaskStage.action?cTaskid=";

    //程序更新检查
    String APP_UPDATE_CHECK = PROJECT_URL + "checkVersion.action?code=";

    //添加通讯录数据(post)
    String ADD_LINK_DATE = PROJECT_URL + "addCommunicateData.action?userid=";
    // 获取通讯录统计结果(get)
    String SELECT_LINK_DATE = PROJECT_URL + "countCommunicateData.action?cCompanyid=";
    String phonesymbol = "&phonesymbol=";
    //获取用户通讯数据(get)
    String SELECT_USER_CALL_LOG = PROJECT_URL + "selectCCommunicateDataByUser.action?userid=";
    //获取通话详情(get)
    String SELECT_CALL_INFO = PROJECT_URL + "selectOneCommunicateData.action?userid=";
    String COMMUNICATE_NUMBER="&communicatenumber=";
    String REQUEST_VERSION_LIST=PROJECT_URL+"selectVersion.action";
    String REQUEST_VERSION_INFO=PROJECT_URL+"selectOneVersion.action?cvid=";



    String REQUEST_COMPANY_NAME=PROJECT_URL+"selectCcompany.action?cCcompanyid=";
    String REQUEST_MANAGER_VERIFY_CODE=PROJECT_URL+"sendCodeA.action?cCompanyid=";

    String REQUEST_MANAGER_VERIFY_CODE_PHONE="&cLoginname=";

    String REQUEST_SIGN_ON_NEXT=PROJECT_URL+"registerCheck.action";

    String REQUEST_UPLOAD_USER_LOCATION=PROJECT_URL+"addLocusfree.action";

    String FIND_USERS_LINE=PROJECT_URL+"selectCountLocusfree.action?companyid=";
    String FIND_USERS_LINE_DEPTS="&deptid=";
    String FIND_USER_LINE=PROJECT_URL+"seletcOneUserLocusfree.action?userid=";

    //员工列表
    String USER_LIST = PROJECT_URL + "selectAllUser.action?cCompanyid=";
    //添加聊天记录(POST)
    String ADD_CHAT_DATE = PROJECT_URL + "addChat.action?imei=";
    //通过userId查询
    String QUERY_USER_CHAT_DATE = PROJECT_URL  +  "selectChatByUser.action?userid=";
    //查询用户详细聊天记录
    String QUERY_CHAT_INFO = PROJECT_URL + "selectHistoryChat.action?cChatUserid=";
    String USERNAME = "&username=";
    String PAGE = "&pageIndex=";

    // 添加微信好友头像
    String ADD_USER_PHOTO = PROJECT_URL + "addChatFriend.action?imei=";
    String DEPT_ADD_REQUEST=LOCAL_URL+"ManualControl/c_dept/addDept.action";
    String ROLE_ADD_REQUEST=LOCAL_URL+"ManualControl/role/addCRole.action";
    String FIND_ALL_ROLES=LOCAL_URL+"ManualControl/role/selectAllRole.action?companyid=";
    String FIND_ALL_DEPTS=LOCAL_URL+"ManualControl/c_dept/selectAllDept.action?companyid=";
    String FIND_ALL_USER_BY_DEPT=LOCAL_URL+"ManualControl/c_user/selectUserByDept.action?cCompanyid=";
    String FIND_ALL_USER_BY_DEPT_DEPT_ID="&cUdeptid=";
    String FIND_ALL_JURISDICTION=LOCAL_URL+"ManualControl/resource/selectOneResource.action";
    String FIND_JURISDICTION_BY_FATHER=LOCAL_URL+"ManualControl/resource/selectTwoResource.action?cResourceid=";
    String FIND_JURISDICTION_BY_FATHER_ROLE_ID="&cRoleid=";
    String FIND_USER_LIST_BY_ROLE=LOCAL_URL+"ManualControl/c_user/selectUserByRole.action?roleid=";
    String MOVE_USER_TO_ROLE=LOCAL_URL+"ManualControl/c_user/editUserRole.action?curRoleid=";
    String MOVE_USER_TO_DEPT=LOCAL_URL+"ManualControl/c_user/editUserDept.action?deptid=";
    String CHANGE_JURISDICTION=LOCAL_URL+"ManualControl/resource/addCRoleResource.action?cResourceid=";
    String FANS_MANAGER_PARAM_USER_ID="&userid=";
    String FANS_MANAGER_PARAM_USER_ID_FIRST ="?userid=";
    String IS_ADMIN_USER=LOCAL_URL+"ManualControl/role/selectRoleById.action?userid=";
    String DOWNLOAD_VOICE_PLUGIN=LOCAL_URL+"ManualControl/httpService/selectPlugUrl.action";
}
