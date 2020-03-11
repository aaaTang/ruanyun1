package cn.ruanyun.backInterface.modules.base.controller.manage;


import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.entity.Role;
import cn.ruanyun.backInterface.modules.base.entity.RoleDepartment;
import cn.ruanyun.backInterface.modules.base.entity.RolePermission;
import cn.ruanyun.backInterface.modules.base.entity.UserRole;
import cn.ruanyun.backInterface.modules.base.service.RoleDepartmentService;
import cn.ruanyun.backInterface.modules.base.service.RolePermissionService;
import cn.ruanyun.backInterface.modules.base.service.RoleService;
import cn.ruanyun.backInterface.modules.base.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


/**
 * @author fei
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/role")
@Transactional
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;



    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/getAllList", method = RequestMethod.GET)
    public Result<Object> roleGetAll(){

        List<Role> list = roleService.getAll();
        return new ResultUtil<>().setData(list);
    }

    @RequestMapping(value = "/getAllByPage", method = RequestMethod.GET)
    public Result<Page<Role>> getRoleByPage(@ModelAttribute PageVo page){

        Page<Role> list = roleService.findAll(PageUtil.initPage(page));
        for(Role role : list.getContent()){
            // 角色拥有权限
            List<RolePermission> permissions = rolePermissionService.findByRoleId(role.getId());
            role.setPermissions(permissions);
            // 角色拥有数据权限
            List<RoleDepartment> departments = roleDepartmentService.findByRoleId(role.getId());
            role.setDepartments(departments);
        }
        return new ResultUtil<Page<Role>>().setData(list);
    }

    @RequestMapping(value = "/setDefault", method = RequestMethod.POST)
    public Result<Object> setDefault(@RequestParam String id,
                                     @RequestParam Boolean isDefault){

        Role role = roleService.get(id);
        if(role==null){
            return new ResultUtil<>().setErrorMsg("角色不存在");
        }
        role.setDefaultRole(isDefault);
        roleService.update(role);
        return new ResultUtil<>().setSuccessMsg("设置成功");
    }

    @RequestMapping(value = "/editRolePerm", method = RequestMethod.POST)
    public Result<Object> editRolePerm(@RequestParam String roleId,
                                       @RequestParam(required = false) String[] permIds){

        //删除其关联权限
        rolePermissionService.deleteByRoleId(roleId);
        //分配新权限
        for(String permId : permIds){
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permId);
            rolePermissionService.save(rolePermission);
        }
        //手动批量删除缓存
        Set<String> keysUser = redisTemplate.keys("user:" + "*");
        assert keysUser != null;
        redisTemplate.delete(keysUser);
        Set<String> keysUserRole = redisTemplate.keys("userRole:" + "*");
        assert keysUserRole != null;
        redisTemplate.delete(keysUserRole);
        Set<String> keysUserPerm = redisTemplate.keys("userPermission:" + "*");
        assert keysUserPerm != null;
        redisTemplate.delete(keysUserPerm);
        Set<String> keysUserMenu = redisTemplate.keys("permission::userMenuList:*");
        assert keysUserMenu != null;
        redisTemplate.delete(keysUserMenu);
        return new ResultUtil<>().setData(null);
    }

    @RequestMapping(value = "/editRoleDep", method = RequestMethod.POST)
    public Result<Object> editRoleDep(@RequestParam String roleId,
                                      @RequestParam Integer dataType,
                                      @RequestParam(required = false) String[] depIds){

        Role r = roleService.get(roleId);
        r.setDataType(dataType);
        roleService.update(r);
        // 删除其关联数据权限
        roleDepartmentService.deleteByRoleId(roleId);
        // 分配新数据权限
        for(String depId : depIds){
            RoleDepartment roleDepartment = new RoleDepartment();
            roleDepartment.setRoleId(roleId);
            roleDepartment.setDepartmentId(depId);
            roleDepartmentService.save(roleDepartment);
        }
        // 手动删除相关缓存
        Set<String> keys = redisTemplate.keys("department:" + "*");
        assert keys != null;
        redisTemplate.delete(keys);
        Set<String> keysUserRole = redisTemplate.keys("userRole:" + "*");
        assert keysUserRole != null;
        redisTemplate.delete(keysUserRole);

        return new ResultUtil<>().setData(null);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Result<Role> save(@ModelAttribute Role role){

        Role r = roleService.save(role);
        return new ResultUtil<Role>().setData(r);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result<Role> edit(@ModelAttribute Role entity){

        Role r = roleService.update(entity);
        //手动批量删除缓存
        Set<String> keysUser = redisTemplate.keys("user:" + "*");
        assert keysUser != null;
        redisTemplate.delete(keysUser);
        Set<String> keysUserRole = redisTemplate.keys("userRole:" + "*");
        assert keysUserRole != null;
        redisTemplate.delete(keysUserRole);
        return new ResultUtil<Role>().setData(r);
    }

    @RequestMapping(value = "/delAllByIds/{ids}", method = RequestMethod.DELETE)
    public Result<Object> delByIds(@PathVariable String[] ids){

        for(String id:ids){
            List<UserRole> list = userRoleService.findByRoleId(id);
            if(list!=null&&list.size()>0){
                return new ResultUtil<>().setErrorMsg("删除失败，包含正被用户使用关联的角色");
            }
        }
        for(String id:ids){
            roleService.delete(id);
            //删除关联菜单权限
            rolePermissionService.deleteByRoleId(id);
            //删除关联数据权限
            roleDepartmentService.deleteByRoleId(id);
        }
        return new ResultUtil<>().setSuccessMsg("批量通过id删除数据成功");
    }

}
