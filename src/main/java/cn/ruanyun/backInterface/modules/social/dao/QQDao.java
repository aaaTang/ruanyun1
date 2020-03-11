package cn.ruanyun.backInterface.modules.social.dao;

import cn.ruanyun.backInterface.base.RuanyunBaseDao;
import cn.ruanyun.backInterface.modules.social.entity.QQ;

/**
 * qq登录数据处理层
 * @author fei
 */
public interface QQDao extends RuanyunBaseDao<QQ,String> {

    /**
     * 通过openId获取
     * @param openId
     * @return
     */
    QQ findByOpenId(String openId);

    /**
     * 通过username获取
     * @param username
     * @return
     */
    QQ findByRelateUsername(String username);

    /**
     * 通过username删除
     * @param username
     */
    void deleteByUsername(String username);
}