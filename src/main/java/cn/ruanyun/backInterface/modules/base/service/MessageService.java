package cn.ruanyun.backInterface.modules.base.service;


import cn.ruanyun.backInterface.base.RuanyunBaseService;
import cn.ruanyun.backInterface.common.vo.SearchVo;
import cn.ruanyun.backInterface.modules.base.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 消息内容接口
 * @author fei
 */
public interface MessageService extends RuanyunBaseService<Message,String> {

    /**
     * 多条件分页获取
     * @param message
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<Message> findByCondition(Message message, SearchVo searchVo, Pageable pageable);

    /**
     * 通过创建发送标识获取
     * @param createSend
     * @return
     */
    List<Message> findByCreateSend(Boolean createSend);

    /**
     * 发送消息
     * @param message
     */
    void sendMessage(Message message);
}