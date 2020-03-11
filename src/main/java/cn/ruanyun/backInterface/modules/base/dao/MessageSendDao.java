package cn.ruanyun.backInterface.modules.base.dao;


import cn.ruanyun.backInterface.base.RuanyunBaseDao;
import cn.ruanyun.backInterface.modules.base.entity.MessageSend;

/**
 * 消息发送数据处理层
 * @author fei
 */
public interface MessageSendDao extends RuanyunBaseDao<MessageSend,String> {

    /**
     * 通过消息id删除
     * @param messageId
     */
    void deleteByMessageId(String messageId);
}