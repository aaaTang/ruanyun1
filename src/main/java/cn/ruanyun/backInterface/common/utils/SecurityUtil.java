package cn.ruanyun.backInterface.common.utils;


import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.constant.SecurityConstant;
import cn.ruanyun.backInterface.common.vo.TokenUser;
import cn.ruanyun.backInterface.config.properties.RuanyunTokenProperties;
import cn.ruanyun.backInterface.modules.base.pojo.Permission;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.RoleService;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRolePermissionService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.base.vo.BackUserInfo;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author fei
 */
@Component
public class SecurityUtil {

    @Autowired
    private RuanyunTokenProperties tokenProperties;

    @Autowired
    private IUserService iuserService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRolePermissionService rolePermissionService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private IUserService iUserService;

    public String getToken(String username, Boolean saveLogin){

        boolean saved = false;

        User user = iuserService.getById(iuserService.getUserIdByName(username));

        //token是否缓存在redis中
        if(saveLogin==null||saveLogin){
            saved = true;
            if(tokenProperties.getRedis()){
                tokenProperties.setTokenExpireTime(tokenProperties.getSaveLoginTime() * 60 * 24);
            }
        }
        // 生成token
        List<String> permissionList = new ArrayList<>();
        // 缓存权限
        if(tokenProperties.getStorePerms()){

            //添加权限
            ThreadPoolUtil.getPool().execute(() ->

                    Optional.ofNullable(rolePermissionService.getPermissionByRoles(userRoleService
                    .getRoleIdsByUserId(iuserService.getUserIdByName(username))))
                    .ifPresent(permissions -> permissions.parallelStream().forEach(permission -> {


                        if(CommonConstant.PERMISSION_OPERATION.equals(permission.getType())
                                && StrUtil.isNotBlank(permission.getTitle())
                                && StrUtil.isNotBlank(permission.getPath())) {
                            permissionList.add(permission.getTitle());
                        }
                    })));

            //添加角色
            ThreadPoolUtil.getPool().execute(() ->

                    Optional.ofNullable(roleService.getRolesByRoleIds(userRoleService.getRoleIdsByUserId(iuserService
                    .getUserIdByName(username))))
                    .ifPresent(roles -> roles.parallelStream().forEach(role ->
                            permissionList.add(role.getName()))));


        }
        // 登陆成功生成token
        String token;
        if(tokenProperties.getRedis()){
            // redis
            token = UUID.randomUUID().toString().replace("-", "");
            TokenUser tokenUser = new TokenUser(user.getUsername(), permissionList, saved);

            // 单设备登录 之前的token失效
            if(tokenProperties.getSdl()) {
                String oldToken = redisTemplate.opsForValue().get(SecurityConstant.USER_TOKEN + user.getUsername());
                if (StrUtil.isNotBlank(oldToken)) {
                    redisTemplate.delete(SecurityConstant.TOKEN_PRE + oldToken);
                }
            }
            if(saved){
                redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + user.getUsername(), token, tokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
                redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(tokenUser), tokenProperties.getSaveLoginTime(), TimeUnit.DAYS);
            }else{
                redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + user.getUsername(), token, tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
                redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(tokenUser), tokenProperties.getTokenExpireTime(), TimeUnit.MINUTES);
            }
        }else{
            // jwt
            token = SecurityConstant.TOKEN_SPLIT + Jwts.builder()
                    //主题 放入用户名
                    .setSubject(user.getUsername())
                    //自定义属性 放入用户拥有请求权限
                    .claim(SecurityConstant.AUTHORITIES, new Gson().toJson(permissionList))
                    //失效时间
                    .setExpiration(new Date(System.currentTimeMillis() + tokenProperties.getTokenExpireTime() * 60 * 1000))
                    //签名算法和密钥
                    .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                    .compact();
        }
        return token;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public BackUserInfo getCurrUser(){

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return iuserService.getBackUserInfo(user.getUsername());
    }


    /**
     * 通过用户名获取用户拥有权限
     * @param username
     */
    public List<GrantedAuthority> getCurrUserPerms(String username){

        List<GrantedAuthority> authorities = new ArrayList<>();

        Optional.ofNullable(rolePermissionService.getPermissionByRoles(userRoleService.getRoleIdsByUserId(iuserService
                .getUserIdByName(username))))
                .ifPresent(permissions -> permissions.parallelStream().forEach(permission -> {

                    if (ToolUtil.isNotEmpty(permission.getTitle())) {
                        authorities.add(new SimpleGrantedAuthority(permission.getTitle()));
                    }
                }));

        return authorities;
    }
}
