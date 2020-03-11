package cn.ruanyun.backInterface.modules.base.service;


import cn.ruanyun.backInterface.base.RuanyunBaseService;
import cn.ruanyun.backInterface.modules.base.entity.MessageSend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 消息发送接口
 * @author fei
 */
public interface MessageSendService extends RuanyunBaseService<MessageSend,String> {

    /**
     * 发送消息 带websock推送
     * @param messageSend
     * @return
     */
    MessageSend send(MessageSend messageSend);

    /**
     * 通过消息id删除
     * @param messageId
     */
    void deleteByMessageId(String messageId);

    /**
     * 多条件分页获取
     * @param messageSend
     * @param pageable
     * @return
     */
    Page<MessageSend> findByCondition(MessageSend messageSend, Pageable pageable);


}