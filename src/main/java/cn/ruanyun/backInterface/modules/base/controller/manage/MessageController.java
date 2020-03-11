package cn.ruanyun.backInterface.modules.base.controller.manage;

import cn.ruanyun.backInterface.common.constant.ActivitiConstant;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.common.vo.SearchVo;
import cn.ruanyun.backInterface.modules.base.pojo.Message;
import cn.ruanyun.backInterface.modules.base.service.MessageSendService;
import cn.ruanyun.backInterface.modules.base.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Exrick
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/message")
@Transactional
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageSendService sendService;

    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    public Result<Page<Message>> getByCondition(@ModelAttribute Message message,
                                                @ModelAttribute SearchVo searchVo,
                                                @ModelAttribute PageVo pageVo){

        Page<Message> page = messageService.findByCondition(message, searchVo, PageUtil.initPage(pageVo));
        return new ResultUtil<Page<Message>>().setData(page);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Result<Message> get(@PathVariable String id){

        Message message = messageService.get(id);
        return new ResultUtil<Message>().setData(message);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<Object> addMessage(@ModelAttribute Message message){

        messageService.sendMessage(message);
        return new ResultUtil<>().setSuccessMsg("添加成功");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result<Object> editMessage(@ModelAttribute Message message){

        Message m = messageService.update(message);
        return new ResultUtil<>().setSuccessMsg("编辑成功");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    public Result<Object> delMessage(@PathVariable String[] ids){

        for(String id:ids){
            if(ActivitiConstant.MESSAGE_PASS_ID.equals(id)||ActivitiConstant.MESSAGE_BACK_ID.equals(id)||ActivitiConstant.MESSAGE_DELEGATE_ID.equals(id)
                    ||ActivitiConstant.MESSAGE_TODO_ID.equals(id)){
                return new ResultUtil<>().setErrorMsg("抱歉，无法删除工作流相关系统消息");
            }
            messageService.delete(id);
            // 删除发送表
            sendService.deleteByMessageId(id);
        }
        return new ResultUtil<>().setSuccessMsg("编辑成功");
    }
}
