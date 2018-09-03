package yktong.com.godofdog.value;

/**
 * Created by vampire on 2017/6/13.
 */

public interface SpValue {
    String WORK_STATE = "work_state";
    String GROUP_NAME = "GROUP_NAME"; // 要添加好友的群组
    int STATE_NORMAL = 0;
    int STATE_ADD_LINK = 1; //通讯录添加
    int STATE_PRAISE = 2; // 点赞
    int STATE_QUERY_FRIENDS_NUM = 3; // 统计好友数
    int STATE_CHECK_GROUP = 4; // 检查群组列表
    int STATE_ADD_GROUP_FRIEND = 5; // 社群拓客
    int STATE_INTERACT = 6;//统计今日聊天
    int STATE_COMMENT = 7; // 今日点赞评论
    int STATE_SEND_SNS = 8; // 发送朋友圈
    int STATE_SEND_PERSON = 9; //发送到个人
    int STATE_SHARE_MINI_PROJECT = 10; //发送
    int STATE_GET_NICK = 11;   // 获取用户昵称
    String IS_SCROLL = "isScroll"; // 统计当日聊天数时 确定是否需要滚动
    String SNS_DAILY_SCROLL = "snsDailyScroll";  // 当日互动统计是否需要滑动
    String SCREEN_WIDE = "screenWide"; //屏幕宽
    String SCREEN_HEIGHT = "screenHeight"; // 屏幕高
    String IS_FIRST_TIME = "isFirstTime"; // 是否是首次登陆

    String ADD_NUM = "addNum"; // 好友添加数量
    String CONTENT = "content"; // 验证内容
    String INTERACT_NUM = "interactNum"; // 互动次数

    String SNS_UI = "sns_ui";
    String SNS_NUM = "sns_sum";
    String MARKET_UI = "SelectConversationUI";
    String MARKETING_NUM = "marketing_sum";

    String SNS_CONTENT = "snsContent"; //朋友圈文字
    String SHARE_NAME = "shareName"; // 分享对象

    String TASK_ID = "taskId"; // 任务id

    String START_TIME = "startTime"; //校验数据启动时间
    String MINI_PROJCET_NAME = "miniProjectName"; // 小程序名字
    String PROJECT_INDEX = "projectIndex";

    String USER_ID = "username"; // 微信id
    String IS_CHECK_NAME = "isCheckName"; // 校验名称

    //微信头像
    String USER_AVATAR = "userAvatar";


}
