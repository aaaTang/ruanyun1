package cn.ruanyun.backInterface.modules.social.service;


import cn.ruanyun.backInterface.base.RuanyunBaseService;
import cn.ruanyun.backInterface.common.vo.SearchVo;
import cn.ruanyun.backInterface.modules.social.entity.Weibo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 微博登录接口
 * @author fei
 */
public interface WeiboService extends RuanyunBaseService<Weibo,String> {

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
     * 分页多条件获取
     * @param username
     * @param relateUsername
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<Weibo> findByCondition(String username, String relateUsername, SearchVo searchVo, Pageable pageable);

    /**
     * 通过username删除
     * @param username
     */
    void deleteByUsername(String username);
}