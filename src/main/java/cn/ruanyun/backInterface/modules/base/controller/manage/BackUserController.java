package cn.ruanyun.backInterface.modules.base.controller.manage;


import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * 后台管理用户接口
 * @author fei
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/user")
@Transactional
public class BackUserController {


    private SecurityUtil securityUtil;

    private IUserService iUserService;

    @Autowired
    public BackUserController(SecurityUtil securityUtil, IUserService iUserService) {
        this.securityUtil = securityUtil;
        this.iUserService = iUserService;
    }

    /**
     * 获取用户个人信息详情
     * @return
     */
    @GetMapping("/info")
    public Result<Object> getUserInfo(){

        return new ResultUtil<>().setData(securityUtil.getCurrUser(), "获取个人信息成功！");
    }


    // TODO: 2020/3/16 分条件获取用户列表信息 

    /**
     * 重置密码
     * @param userIds
     * @return
     */
    @PostMapping("/resetPass")
    public Result<Object> resetPass(String userIds){

        return iUserService.resetPass(userIds);
    }


    /**
     * 修改用户资料
     * @param u
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result<Object> editOwn(@ModelAttribute User u){

        return iUserService.editOwn(u);
    }


    /**
     * 修改密码
     * @param password
     * @param newPass
     * @return
     */
    @PostMapping("/modifyPass")
    public Result<Object> modifyPass( String password, String newPass){

        return iUserService.modifyPass(password, newPass);
    }


    /**
     * 后台管理添加用户
     * @param user
     * @param roleIds
     * @return
     */
    @PostMapping(value = "/addUser")
    public Result<Object> addUser(User user, String roleIds){

        return iUserService.addUser(user, roleIds);
    }

}
