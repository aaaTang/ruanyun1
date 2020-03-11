package cn.ruanyun.backInterface.modules.social.controller;


import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.entity.User;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.social.entity.Github;
import cn.ruanyun.backInterface.modules.social.entity.QQ;
import cn.ruanyun.backInterface.modules.social.entity.Weibo;
import cn.ruanyun.backInterface.modules.social.service.GithubService;
import cn.ruanyun.backInterface.modules.social.service.QQService;
import cn.ruanyun.backInterface.modules.social.service.WeiboService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author fei
 */
@Slf4j
@Api(description = "绑定第三方账号接口")
@RequestMapping("/ruanyun/social")
@RestController
public class RelateController {

    @Autowired
    private UserService userService;

    @Autowired
    private GithubService githubService;

    @Autowired
    private QQService qqService;

    @Autowired
    private WeiboService weiboService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/relate", method = RequestMethod.POST)
    @ApiOperation(value = "绑定账号")
    public Result<Object> relate(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam Integer socialType,
                                 @RequestParam String id){

        User user = userService.findByUsername(username);
        if(user==null){
            return new ResultUtil<>().setErrorMsg("账号不存在");
        }
        if(!new BCryptPasswordEncoder().matches(password, user.getPassword())){
            return new ResultUtil<>().setErrorMsg("密码不正确");
        }
        String JWT = "";
        // 从redis中获取表id
        String ID = redisTemplate.opsForValue().get(id);
        if(StrUtil.isBlank(ID)){
            return new ResultUtil<>().setErrorMsg("无效的id");
        }
        // 绑定github
        if(CommonConstant.SOCIAL_TYPE_GITHUB.equals(socialType)){
            Github g = githubService.findByRelateUsername(username);
            if(g!=null){
                return new ResultUtil<>().setErrorMsg("该账户已绑定有Github账号，请先进行解绑操作");
            }
            Github github = githubService.get(ID);
            if(github==null){
                return new ResultUtil<>().setErrorMsg("绑定失败，请先进行第三方授权认证");
            }
            if(github.getIsRelated()&&StrUtil.isNotBlank(github.getRelateUsername())){
                return new ResultUtil<>().setErrorMsg("该Github账号已绑定有用户，请先进行解绑操作");
            }
            github.setIsRelated(true);
            github.setRelateUsername(username);
            githubService.update(github);
            JWT = securityUtil.getToken(username, true);
        }else if(CommonConstant.SOCIAL_TYPE_QQ.equals(socialType)){
            QQ q = qqService.findByRelateUsername(username);
            if(q!=null){
                return new ResultUtil<>().setErrorMsg("该账户已绑定有QQ账号，请先进行解绑操作");
            }
            QQ qq = qqService.get(ID);
            if(qq==null){
                return new ResultUtil<>().setErrorMsg("绑定失败，请先进行第三方授权认证");
            }
            if(qq.getIsRelated()&&StrUtil.isNotBlank(qq.getRelateUsername())){
                return new ResultUtil<>().setErrorMsg("该QQ账号已绑定有用户，请先进行解绑操作");
            }
            qq.setIsRelated(true);
            qq.setRelateUsername(username);
            qqService.update(qq);
            JWT = securityUtil.getToken(username, true);
        }else if(CommonConstant.SOCIAL_TYPE_WEIBO.equals(socialType)){
            Weibo w = weiboService.findByRelateUsername(username);
            if(w!=null){
                return new ResultUtil<>().setErrorMsg("该账户已绑定有微博账号，请先进行解绑操作");
            }
            Weibo weibo = weiboService.get(ID);
            if(weibo==null){
                return new ResultUtil<>().setErrorMsg("绑定失败，请先进行第三方授权认证");
            }
            if(weibo.getIsRelated()&&StrUtil.isNotBlank(weibo.getRelateUsername())){
                return new ResultUtil<>().setErrorMsg("该微博账号已绑定有用户，请先进行解绑操作");
            }
            weibo.setIsRelated(true);
            weibo.setRelateUsername(username);
            weiboService.update(weibo);
            JWT = securityUtil.getToken(username, true);
        }
        // 存入redis
        String JWTKey = UUID.randomUUID().toString().replace("-","");
        redisTemplate.opsForValue().set(JWTKey, JWT, 2L, TimeUnit.MINUTES);
        // 绑定成功删除缓存
        redisTemplate.delete("relate::relatedInfo:" + username);
        return new ResultUtil<>().setData(JWTKey);
    }

    @RequestMapping(value = "/getJWT", method = RequestMethod.GET)
    @ApiOperation(value = "获取JWT")
    public Result<Object> getJWT(@RequestParam String JWTKey){

        String JWT = redisTemplate.opsForValue().get(JWTKey);
        if(StrUtil.isBlank(JWT)){
            return new ResultUtil<>().setErrorMsg("获取JWT失败");
        }
        return new ResultUtil<>().setData(JWT);
    }
}
