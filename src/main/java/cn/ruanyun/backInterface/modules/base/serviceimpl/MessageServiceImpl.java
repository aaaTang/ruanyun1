package cn.ruanyun.backInterface.modules.base.serviceimpl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.SearchVo;
import cn.ruanyun.backInterface.modules.base.mapper.MessageDao;
import cn.ruanyun.backInterface.modules.base.mapper.MessageSendDao;
import cn.ruanyun.backInterface.modules.base.pojo.Message;
import cn.ruanyun.backInterface.modules.base.pojo.MessageSend;
import cn.ruanyun.backInterface.modules.base.service.MessageService;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * 消息内容接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSendDao messageSendDao;


    @Override
    public MessageDao getRepository() {
        return messageDao;
    }

    @Override
    public Page<Message> findByCondition(Message message, SearchVo searchVo, Pageable pageable) {

        return messageDao.findAll(new Specification<Message>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                Path<String> titleField = root.get("title");
                Path<String> contentField = root.get("content");
                Path<Integer> typeField = root.get("type");
                Path<Date> createTimeField = root.get("createTime");

                List<Predicate> list = new ArrayList<Predicate>();

                //模糊搜素
                if(StrUtil.isNotBlank(message.getTitle())){
                    list.add(cb.like(titleField,'%'+message.getTitle()+'%'));
                }
                if(StrUtil.isNotBlank(message.getContent())){
                    list.add(cb.like(contentField,'%'+message.getContent()+'%'));
                }

                if(message.getType()!=null){
                    list.add(cb.equal(typeField, message.getType()));
                }

                //创建时间
                if(StrUtil.isNotBlank(searchVo.getStartDate())&&StrUtil.isNotBlank(searchVo.getEndDate())){
                    Date start = DateUtil.parse(searchVo.getStartDate());
                    Date end = DateUtil.parse(searchVo.getEndDate());
                    list.add(cb.between(createTimeField, start, DateUtil.endOfDay(end)));
                }

                Predicate[] arr = new Predicate[list.size()];
                cq.where(list.toArray(arr));
                return null;
            }
        }, pageable);
    }

    @Override
    public List<Message> findByCreateSend(Boolean createSend) {

        return messageDao.findByCreateSend(createSend);
    }

    @Override
    public void sendMessage(Message message) {

        //1.保存消息
        Message messageSave = messageDao.save(message);

        //2.判断类型
        if (CommonConstant.MESSAGE_RANGE_ALL.equals(message.getRange())){

            //2.1 遍历全体用户发送消息
            Optional.ofNullable(ToolUtil.setListToNul(userService.getAll()))
                    .ifPresent(users -> users.parallelStream().forEach(user -> {

                        messageSave(messageSave.getId(),user.getId());
                        //2.2 可以添加极光推送
                    }));
        }else {

            //3.发送个别人
            Optional.ofNullable(message.getUserIds()).flatMap(userIds -> Optional.ofNullable(ToolUtil
                    .setListToNul(ToolUtil.splitterStr(message.getUserIds()))))
                    .ifPresent(userIds -> userIds.parallelStream().forEach(userId -> {

                        messageSave(messageSave.getId(),userId);
                        //3.2 添加极光推送
                    }));

        }
    }


    /*
    发送消息封装类
     */
    public void  messageSave(String messageId,String userId) {

        MessageSend messageSend = new MessageSend();
        messageSend.setUserId(userId)
                .setMessageId(messageId);
        messageSendDao.save(messageSend);
    }

}