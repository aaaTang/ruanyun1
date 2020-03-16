package cn.ruanyun.backInterface.modules.base.controller.manage;


import cn.ruanyun.backInterface.base.RuanyunBaseController;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.Message;
import cn.ruanyun.backInterface.modules.base.pojo.MessageSend;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.MessageSendService;
import cn.ruanyun.backInterface.modules.base.service.MessageService;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author fei
 */
@Slf4j
@RestController

@RequestMapping("/ruanyun/messageSend")
@Transactional
public class MessageSendController extends RuanyunBaseController<MessageSend, String> {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public MessageSendService getService() {
        return messageSendService;
    }

    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    public Result<Page<MessageSend>> getByCondition(@ModelAttribute MessageSend ms,
                                                    @ModelAttribute PageVo pv){

        ms.setUserId(securityUtil.getCurrUser().getId());
        Page<MessageSend> page = messageSendService.findByCondition(ms, PageUtil.initPage(pv));
        // lambda
        page.getContent().forEach(item->{
            User u = userService.get(item.getUserId());
            item.setUsername(u.getUsername());
            Message m = messageService.get(item.getMessageId());
            item.setTitle(m.getTitle());
            item.setContent(m.getContent());
            item.setType(m.getType());
        });
        return new ResultUtil<Page<MessageSend>>().setData(page);
    }


}
