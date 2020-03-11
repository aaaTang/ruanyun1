package cn.ruanyun.backInterface.modules.base.mapper;


import cn.ruanyun.backInterface.base.RuanyunBaseDao;
import cn.ruanyun.backInterface.modules.base.pojo.Message;

import java.util.List;

/**
 * 消息内容数据处理层
 * @author fei
 */
public interface MessageDao extends RuanyunBaseDao<Message,String> {

    /**
     * 通过创建发送标识获取
     * @param createSend
     * @return
     */
    List<Message> findByCreateSend(Boolean createSend);
}