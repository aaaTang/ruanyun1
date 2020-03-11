package cn.ruanyun.backInterface.modules.social.dao;


import cn.ruanyun.backInterface.base.RuanyunBaseDao;
import cn.ruanyun.backInterface.modules.social.entity.Github;

/**
 * Github登录数据处理层
 * @author fei
 */
public interface GithubDao extends RuanyunBaseDao<Github,String> {

    /**
     * 通过openId获取
     * @param openId
     * @return
     */
    Github findByOpenId(String openId);

    /**
     * 通过username获取
     * @param username
     * @return
     */
    Github findByRelateUsername(String username);

    /**
     * 通过username删除
     * @param username
     */
    void deleteByUsername(String username);
}