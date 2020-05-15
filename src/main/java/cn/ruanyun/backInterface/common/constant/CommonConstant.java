package cn.ruanyun.backInterface.common.constant;

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


    Integer YES = 0;

    Integer NO = 1;

    Integer AWAIT = -1;
}


