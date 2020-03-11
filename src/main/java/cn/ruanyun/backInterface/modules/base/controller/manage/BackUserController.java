package cn.ruanyun.backInterface.modules.base.controller.manage;


import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.annotation.SystemLog;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.LogType;
import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.common.vo.SearchVo;
import cn.ruanyun.backInterface.modules.base.aync.AddMessage;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.base.service.*;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * @author fei
 */
@Slf4j
@RestController
@Api(description = "用户接口")
@RequestMapping("/ruanyun/user")
@CacheConfig(cacheNames = "user")
@Transactional
public class BackUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private IRoleService iRoleService;

    @Autowired
    private IUserRoleService iUserRoleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private AddMessage addMessage;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping(value = "/smsLogin", method = RequestMethod.POST)
    @SystemLog(description = "短信登录", type = LogType.LOGIN)
    public Result<Object> smsLogin(@RequestParam String mobile,
                                   @RequestParam(required = false) Boolean saveLogin){

        User u = userService.findByMobile(mobile);
        if(u==null){
            throw new RuanyunException("手机号不存在");
        }
        String accessToken = securityUtil.getToken(u.getUsername(), saveLogin);
        return new ResultUtil<>().setData(accessToken);
    }

    @RequestMapping(value = "/resetByMobile", method = RequestMethod.POST)
    public Result<Object> resetByMobile(@RequestParam String mobile,
                                        @RequestParam String password,
                                        @RequestParam String passStrength){

        User u = userService.findByMobile(mobile);
        String encryptPass = new BCryptPasswordEncoder().encode(password);
        u.setPassword(encryptPass);
        u.setPassStrength(passStrength);
        userService.update(u);
        // 删除缓存
        redisTemplate.delete("user::"+u.getUsername());
        return new ResultUtil<>().setSuccessMsg("重置密码成功");
    }

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    public Result<Object> regist(@ModelAttribute User u){

        if(StrUtil.isBlank(u.getUsername()) || StrUtil.isBlank(u.getPassword())){
            return new ResultUtil<>().setErrorMsg("缺少必需表单字段");
        }

        if(userService.findByUsername(u.getUsername())!=null){
            return new ResultUtil<>().setErrorMsg("该用户名已被注册");
        }

        if(userService.findByMobile(u.getMobile())!=null){
            return new ResultUtil<>().setErrorMsg("该手机号已被注册");
        }

        String encryptPass = new BCryptPasswordEncoder().encode(u.getPassword());
        u.setPassword(encryptPass);
        u.setType(CommonConstant.USER_TYPE_NORMAL);
        User user = userService.save(u);
        if(user==null){
            return new ResultUtil<>().setErrorMsg("注册失败");
        }
        // 默认角色
        List<Role> roleList = roleService.findByDefaultRole(true);
        if(roleList!=null&&roleList.size()>0){
            for(Role role : roleList){
                UserRole ur = new UserRole();
                ur.setUserId(user.getId());
                ur.setRoleId(role.getId());
                iUserRoleService.save(ur);
            }
        }
        // 异步发送创建账号消息
        addMessage.addSendMessage(user.getId());

        return new ResultUtil<>().setData(user);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Result<User> getUserInfo(){

        User u = securityUtil.getCurrUser();
        // 清除持久上下文环境 避免后面语句导致持久化
        entityManager.clear();
        u.setPassword(null);
        return new ResultUtil<User>().setData(u);
    }

    @RequestMapping(value = "/changeMobile", method = RequestMethod.POST)
    public Result<Object> changeMobile(@RequestParam String mobile){

        if(userService.findByMobile(mobile)!=null){
            return new ResultUtil<>().setErrorMsg("该手机号已绑定其他账户");
        }
        User u = securityUtil.getCurrUser();
        u.setMobile(mobile);
        userService.update(u);
        // 删除缓存
        redisTemplate.delete("user::"+u.getUsername());
        return new ResultUtil<>().setSuccessMsg("修改手机号成功");
    }

    @RequestMapping(value = "/unlock", method = RequestMethod.POST)
    public Result<Object> unLock(@RequestParam String password){

        User u = securityUtil.getCurrUser();
        if(!new BCryptPasswordEncoder().matches(password, u.getPassword())){
            return new ResultUtil<>().setErrorMsg("密码不正确");
        }
        return new ResultUtil<>().setData(null);
    }

    @RequestMapping(value = "/resetPass", method = RequestMethod.POST)
    public Result<Object> resetPass(@RequestParam String[] ids){

        for(String id:ids){
            User u = userService.get(id);
            u.setPassword(new BCryptPasswordEncoder().encode("123456"));
            userService.update(u);
            redisTemplate.delete("user::"+u.getUsername());
        }
        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @CacheEvict(key = "#u.username")
    public Result<Object> editOwn(@ModelAttribute User u){

        User old = securityUtil.getCurrUser();
        u.setPassword(old.getPassword());
        User user = userService.update(u);
        if(user==null){
            return new ResultUtil<>().setErrorMsg("修改失败");
        }
        return new ResultUtil<>().setSuccessMsg("修改成功");
    }

    @RequestMapping(value = "/admin/edit", method = RequestMethod.POST)
    @CacheEvict(key = "#u.username")
    public Result<Object> edit(@ModelAttribute User u,
                               @RequestParam(required = false) String[] roles){

        User old = userService.get(u.getId());
        // 若修改了用户名
        if(!old.getUsername().equals(u.getUsername())){
            // 若修改用户名删除原用户名缓存
            redisTemplate.delete("user::"+old.getUsername());
            // 判断新用户名是否存在
            if(userService.findByUsername(u.getUsername())!=null){
                return new ResultUtil<>().setErrorMsg("该用户名已存在");
            }
        }

        // 若修改了手机和邮箱判断是否唯一
        if(!old.getMobile().equals(u.getMobile())&&userService.findByMobile(u.getMobile())!=null){
            return new ResultUtil<>().setErrorMsg("该手机号已绑定其他账户");
        }

        u.setPassword(old.getPassword());
        User user = userService.update(u);
        if(user==null){
            return new ResultUtil<>().setErrorMsg("修改失败");
        }
        //删除该用户角色
        userRoleService.deleteByUserId(u.getId());
        if(roles!=null&&roles.length>0){
            //新角色
            for(String roleId : roles){
                UserRole ur = new UserRole();
                ur.setRoleId(roleId);
                ur.setUserId(u.getId());
                userRoleService.save(ur);
            }
        }
        //手动删除缓存
        redisTemplate.delete("userRole::"+u.getId());
        redisTemplate.delete("userRole::depIds:"+u.getId());
        redisTemplate.delete("permission::userMenuList:"+u.getId());
        return new ResultUtil<>().setSuccessMsg("修改成功");
    }


    @RequestMapping(value = "/modifyPass", method = RequestMethod.POST)
    @ApiOperation(value = "修改密码")
    public Result<Object> modifyPass(@ApiParam("旧密码") @RequestParam String password,
                                     @ApiParam("新密码") @RequestParam String newPass,
                                     @ApiParam("密码强度") @RequestParam String passStrength){

        User user = securityUtil.getCurrUser();
        if(!new BCryptPasswordEncoder().matches(password, user.getPassword())){
            return new ResultUtil<>().setErrorMsg("旧密码不正确");
        }

        String newEncryptPass= new BCryptPasswordEncoder().encode(newPass);
        user.setPassword(newEncryptPass);
        user.setPassStrength(passStrength);
        userService.update(user);

        //手动更新缓存
        redisTemplate.delete("user::"+user.getUsername());

        return new ResultUtil<>().setSuccessMsg("修改密码成功");
    }

    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    public Result<Page<User>> getByCondition(@ModelAttribute User user,
                                             @ModelAttribute SearchVo searchVo,
                                             @ModelAttribute PageVo pageVo){

        Page<User> page = userService.findByCondition(user, searchVo, PageUtil.initPage(pageVo));
        for(User u: page.getContent()){

            // 关联角色
            List<Role> list = iRoleService.getRolesByRoleIds(iUserRoleService.getRoleIdsByUserId(u.getId()));
            // 清除持久上下文环境 避免后面语句导致持久化
            entityManager.clear();
            u.setPassword(null);
        }
        return new ResultUtil<Page<User>>().setData(page);
    }


    @RequestMapping(value = "/searchByName/{username}", method = RequestMethod.GET)
    public Result<List<User>> searchByName(@PathVariable String username) throws UnsupportedEncodingException {

        List<User> list = userService.findByUsernameLikeAndStatus("%"+ URLDecoder.decode(username, "utf-8")+"%", CommonConstant.STATUS_NORMAL);
        return new ResultUtil<List<User>>().setData(list);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public Result<List<User>> getByCondition(){

        List<User> list = userService.getAll();
        for(User u: list){
            // 清除持久上下文环境 避免后面语句导致持久化
            entityManager.clear();
            u.setPassword(null);
        }
        return new ResultUtil<List<User>>().setData(list);
    }

    @RequestMapping(value = "/admin/add", method = RequestMethod.POST)
    public Result<Object> regist(@ModelAttribute User u,
                                 @RequestParam(required = false) String[] roles){

        if(StrUtil.isBlank(u.getUsername()) || StrUtil.isBlank(u.getPassword())){
            return new ResultUtil<>().setErrorMsg("缺少必需表单字段");
        }

        if(userService.findByUsername(u.getUsername())!=null){
            return new ResultUtil<>().setErrorMsg("该用户名已被注册");
        }
        //删除缓存
        redisTemplate.delete("user::"+u.getUsername());

        String encryptPass = new BCryptPasswordEncoder().encode(u.getPassword());
        u.setPassword(encryptPass);
        User user = userService.save(u);
        if(user==null){
            return new ResultUtil<>().setErrorMsg("添加失败");
        }
        if(roles!=null&&roles.length>0){
            //添加角色
            for(String roleId : roles){
                UserRole ur = new UserRole();
                ur.setUserId(u.getId());
                ur.setRoleId(roleId);
                userRoleService.save(ur);
            }
        }
        // 发送创建账号消息
        addMessage.addSendMessage(user.getId());

        return new ResultUtil<>().setSuccessMsg("添加成功");
    }

    @RequestMapping(value = "/admin/disable/{userId}", method = RequestMethod.POST)
    public Result<Object> disable( @PathVariable String userId){

        User user = userService.get(userId);
        if(user==null){
            return new ResultUtil<>().setErrorMsg("通过userId获取用户失败");
        }
        user.setStatus(CommonConstant.USER_STATUS_LOCK);
        userService.update(user);
        //手动更新缓存
        redisTemplate.delete("user::"+user.getUsername());
        return new ResultUtil<>().setSuccessMsg("操作成功");
    }

    @RequestMapping(value = "/admin/enable/{userId}", method = RequestMethod.POST)
    public Result<Object> enable(@PathVariable String userId){

        User user = userService.get(userId);
        if(user==null){
            return new ResultUtil<>().setErrorMsg("通过userId获取用户失败");
        }
        user.setStatus(CommonConstant.USER_STATUS_NORMAL);
        userService.update(user);
        //手动更新缓存
        redisTemplate.delete("user::"+user.getUsername());
        return new ResultUtil<>().setSuccessMsg("操作成功");
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    public Result<Object> delAllByIds(@PathVariable String[] ids){

        for(String id:ids){
            User u = userService.get(id);
            //删除相关缓存
            redisTemplate.delete("user::" + u.getUsername());
            redisTemplate.delete("userRole::" + u.getId());
            redisTemplate.delete("userRole::depIds:" + u.getId());
            redisTemplate.delete("permission::userMenuList:" + u.getId());
            Set<String> keys = redisTemplate.keys("department::*");
            assert keys != null;
            redisTemplate.delete(keys);

            userService.delete(id);

            //删除关联角色
            userRoleService.deleteByUserId(id);
        }
        return new ResultUtil<>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/importData", method = RequestMethod.POST)
    public Result<Object> importData(@RequestBody List<User> users){

        List<Integer> errors = new ArrayList<>();
        List<String> reasons = new ArrayList<>();
        int count = 0;
        for(User u: users){
            count++;
            // 验证用户名密码不为空
            if(StrUtil.isBlank(u.getUsername())||StrUtil.isBlank(u.getPassword())){
                errors.add(count);
                reasons.add("用户名或密码为空");
                continue;
            }
            // 验证用户名唯一
            if(userService.findByUsername(u.getUsername())!=null){
                errors.add(count);
                reasons.add("用户名已存在");
                continue;
            }
            // 加密密码
            u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));

            if(u.getStatus()==null){
                u.setStatus(CommonConstant.USER_STATUS_NORMAL);
            }
            // 保存数据
            userService.save(u);
        }
        int successCount = users.size() - errors.size();
        String successMessage = "全部导入成功，共计 " + successCount + " 条数据";
        String failMessage = "导入成功 " + successCount + " 条，失败 " + errors.size() + " 条数据。<br>" +
                "第 " + errors.toString() + " 行数据导入出错，错误原因分别为：<br>" + reasons.toString();
        String message;
        if(errors.size()==0){
            message = successMessage;
        }else{
            message = failMessage;
        }
        return new ResultUtil<>().setSuccessMsg(message);
    }
}
