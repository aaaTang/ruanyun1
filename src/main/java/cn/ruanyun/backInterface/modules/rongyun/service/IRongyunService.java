package cn.ruanyun.backInterface.modules.rongyun.service;

import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.modules.rongyun.pojo.MediaMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.rongyun.pojo.Rongyun;
import io.rong.messages.BaseMessage;
import io.rong.models.CheckOnlineResult;

/**
 * 融云接口
 * @author fei
 */
public interface IRongyunService extends IService<Rongyun> {


    /**
     * 注册IM用户
     * @param id
     * @param name
     * @param portrait
     * @throws RuanyunException
     */
    void addUser(String id, String name, String portrait) throws RuanyunException;

    /**
     * 修改IM用户信息
     * @param id
     * @param name
     * @param portrait
     * @return
     * @throws RuanyunException
     */
    boolean updateUser(String id,String name,String portrait)throws RuanyunException;



    /**
     * 获取融云token
     * @param userId
     * @param name
     * @param portraitUri
     * @return
     * @throws RuanyunException
     */
    String getToken(String userId, String name, String portraitUri) throws RuanyunException;


    /**
     * 检查用户是否在线
     * @param userId
     * @return
     */
    String checkOnlineResult(String userId) throws RuanyunException;




}

