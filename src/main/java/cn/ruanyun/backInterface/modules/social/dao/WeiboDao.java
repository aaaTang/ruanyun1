package cn.ruanyun.backInterface.modules.social.dao;


import cn.ruanyun.backInterface.base.RuanyunBaseDao;
import cn.ruanyun.backInterface.modules.social.entity.Weibo;

/**
 * 微博登录数据处理层
 * @author fei
 */
public interface WeiboDao extends RuanyunBaseDao<Weibo,String> {

    /**
     * 通过openId获取
     * @param openId
     * @return
     */
    Weibo findByOpenId(String openId);

    /**
     * 通过username获取
     * @param username
     * @return
     */
    Weibo findByRelateUsername(String username);

    /**
     * 通过username删除
     * @param username
     */
    void deleteByUsername(String username);
}