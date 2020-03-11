package cn.ruanyun.backInterface.modules.base.mapper;


import cn.ruanyun.backInterface.base.RuanyunBaseDao;
import cn.ruanyun.backInterface.modules.base.pojo.MessageSend;

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