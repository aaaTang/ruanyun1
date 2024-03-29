package cn.ruanyun.backInterface.common.constant;

import org.springframework.context.annotation.Primary;

/**
 * 常量
 * @author fei
 */
public interface CommonConstant {

    /**
     * 用户默认头像
     */
    String USER_DEFAULT_AVATAR = "https://i.loli.net/2019/04/28/5cc5a71a6e3b6.png";

    /**
     * 用户正常状态
     */
    Integer USER_STATUS_NORMAL = 0;

    /**
     * 用户禁用状态
     */
    Integer USER_STATUS_LOCK = -1;

    /**
     * 普通用户
     */
    Integer USER_TYPE_NORMAL = 0;

    /**
     * 管理员
     */
    Integer USER_TYPE_ADMIN = 1;

    /**
     * 全部数据权限
     */
    Integer DATA_TYPE_ALL = 0;

    /**
     * 自定义数据权限
     */
    Integer DATA_TYPE_CUSTOM = 1;

    /**
     * 正常状态
     */
    Integer STATUS_NORMAL = 0;

    /**
     * 禁用状态
     */
    Integer STATUS_DISABLE = -1;

    /**
     * 删除标志
     */
    Integer DEL_FLAG = 1;

    /**
     * 限流标识
     */
    String LIMIT_ALL = "RUANYUN_LIMIT_ALL";

    /**
     * 顶部菜单类型权限
     */
    Integer PERMISSION_NAV = -1;

    /**
     * 页面类型权限
     */
    Integer PERMISSION_PAGE = 0;

    /**
     * 操作类型权限
     */
    Integer PERMISSION_OPERATION = 1;

    /**
     * 1级菜单父id
     */
    String PARENT_ID = "0";

    /**
     * 0级菜单
     */
    Integer LEVEL_ZERO = 0;

    /**
     * 1级菜单
     */
    Integer LEVEL_ONE = 1;

    /**
     * 2级菜单
     */
    Integer LEVEL_TWO = 2;

    /**
     * 3级菜单
     */
    Integer LEVEL_THREE = 3;

    /**
     * 消息发送范围
     */
    Integer MESSAGE_RANGE_ALL = 0;

    /**
     * 未读
     */
    Integer MESSAGE_STATUS_UNREAD = 0;

    /**
     * 已读
     */
    Integer MESSAGE_STATUS_READ = 1;

    /**
     * github登录
     */
    Integer SOCIAL_TYPE_GITHUB = 0;

    /**
     * qq登录
     */
    Integer SOCIAL_TYPE_QQ = 1;

    /**
     * 微博登录
     */
    Integer SOCIAL_TYPE_WEIBO = 2;

    /**
     * 短信验证码key前缀
     */
    String PRE_SMS = "RUANYUN_PRE_SMS:";

    /**
     * 邮件验证码key前缀
     */
    String PRE_EMAIL = "RUANYUN_PRE_EMAIL:";

    /**
     * 本地文件存储
     */
    Integer OSS_LOCAL = 0;

    /**
     * 七牛云OSS存储
     */
    Integer OSS_QINIU = 1;

    /**
     * 阿里云OSS存储
     */
    Integer OSS_ALI = 2;

    /**
     * 腾讯云COS存储
     */
    Integer OSS_TENCENT = 3;

    /**
     * MINIO存储
     */
    Integer OSS_MINIO = 4;

    /**
     * 部门负责人类型 主负责人
     */
    Integer HEADER_TYPE_MAIN = 0;

    /**
     * 部门负责人类型 副负责人
     */
    Integer HEADER_TYPE_VICE = 1;



    /*---------------用户角色-----------------------*/

    String DEFAULT_ROLE = "普通用户";

    String STORE = "商家";

    String ADMIN = "ROLE_ADMIN";

    String PER_STORE = "个人商家";

    String STAFF = "员工";


    /*-------------------筛选类型-----------------------------*/

    /*
    销量升序
     */
    Integer SALES_VOLUME_ASC =  1;

    /*
    销量降序
     */
    Integer SALES_VOLUME_DESC = 2;

    /*
    价格升序
     */
    Integer PRICE_ASC = 3;

    /*
    价格降序
     */
    Integer PRICE_DESC = 4;

    /*
    评论数升序
     */
    Integer COMMENTS_NUM_ASC = 5;

    /*
    评论数降序
     */
    Integer COMMENTS_NUM_DESC = 6;

    /**
     * 门店等级升序 7
     */
    Integer STORE_LEVEL_ASC = 7;

    /**
     * 门店等级降序8
     */
    Integer STORE_LEVEL_DESC = 8;

    /**
     * 门店星级升序 9
     */
    Integer STORE_STAT_LEVEL_ASC = 9;

    /**
     * 门店星级降序10
     */
    Integer STORE_STAT_LEVEL_DESC = 10;

    /**
     * 距离升序 11
     */
    Integer DISTANCE_ASC = 11;

    /**
     * 距离降序12
     */
    Integer DISTANCE_DESC = 12;


    Integer YES = 0;

    Integer NO = 1;

    Integer AWAIT = -1;


    /**
     * 个人工作室
     */
    String PERSON_STUDIO = "个人工作室";


    /*-------------------微信授权参数------------------------*/

    String APP_ID = "wxfb1c7c1fc40e00e7";

    String APP_SECRET = "ad006bc22cf37b9ba7ef357347affbca";


    /*-------------------法大大---------------------------*/

    String F_APP_ID = "500355";


    String F_APP_SECRET = "KQ2AKUzQWYeYD17hLxFAp6jp";



    String F_HOST = "http://textapi.fadada.com:9999/api2/";


    String F_VERSION= "2.0";


}


