package cn.ruanyun.backInterface.modules.base.controller.manage;


import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.exception.RuanyunException;
import cn.ruanyun.backInterface.common.utils.CommonUtil;
import cn.ruanyun.backInterface.common.utils.HibernateProxyTypeAdapter;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.entity.Department;
import cn.ruanyun.backInterface.modules.base.entity.DepartmentHeader;
import cn.ruanyun.backInterface.modules.base.entity.User;
import cn.ruanyun.backInterface.modules.base.service.DepartmentHeaderService;
import cn.ruanyun.backInterface.modules.base.service.DepartmentService;
import cn.ruanyun.backInterface.modules.base.service.RoleDepartmentService;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
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
@RequestMapping("/ruanyun/department")
@CacheConfig(cacheNames = "department")
@Transactional
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private DepartmentHeaderService departmentHeaderService;


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 通过parentId获取
     * @param parentId
     * @param openDataFilter
     * @return
     */
    @RequestMapping(value = "/getByParentId/{parentId}", method = RequestMethod.GET)
    public Result<List<Department>> getByParentId(@PathVariable String parentId,
                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<Department> list;
        User u = securityUtil.getCurrUser();
        String key = "department::"+parentId+":"+u.getId()+"_"+openDataFilter;
        String v = redisTemplate.opsForValue().get(key);
        if(StrUtil.isNotBlank(v)){
            list = new Gson().fromJson(v, new TypeToken<List<Department>>(){}.getType());
            return new ResultUtil<List<Department>>().setData(list);
        }
        list = departmentService.findByParentIdOrderBySortOrder(parentId, openDataFilter);
        list = setInfo(list);
        redisTemplate.opsForValue().set(key,
                new GsonBuilder().registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).create().toJson(list));
        return new ResultUtil<List<Department>>().setData(list);
    }


    /**
     * 添加
     * @param department
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result<Object> add(@ModelAttribute Department department){

        Department d = departmentService.save(department);
        // 如果不是添加的一级 判断设置上级为父节点标识
        if(!CommonConstant.PARENT_ID.equals(department.getParentId())){
            Department parent = departmentService.get(department.getParentId());
            if(parent.getIsParent()==null||!parent.getIsParent()){
                parent.setIsParent(true);
                departmentService.update(parent);
            }
        }
        // 更新缓存
        Set<String> keys = redisTemplate.keys("department::*");
        assert keys != null;
        redisTemplate.delete(keys);
        return new ResultUtil<>().setSuccessMsg("添加成功");
    }

    /**
     * 编辑
     * @param department
     * @param mainHeader
     * @param viceHeader
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Result<Object> edit(@ModelAttribute Department department,
                               @RequestParam(required = false) String[] mainHeader,
                               @RequestParam(required = false) String[] viceHeader){

        Department d = departmentService.update(department);
        // 先删除原数据
        departmentHeaderService.deleteByDepartmentId(department.getId());
        for(String id:mainHeader){
            DepartmentHeader dh = new DepartmentHeader();
            dh.setUserId(id);
            dh.setDepartmentId(d.getId());
            dh.setType(CommonConstant.HEADER_TYPE_MAIN);
            departmentHeaderService.save(dh);
        }
        for(String id:viceHeader){
            DepartmentHeader dh = new DepartmentHeader();
            dh.setUserId(id);
            dh.setDepartmentId(d.getId());
            dh.setType(CommonConstant.HEADER_TYPE_VICE);
            departmentHeaderService.save(dh);
        }
        // 手动删除所有部门缓存
        Set<String> keys = redisTemplate.keys("department:" + "*");
        redisTemplate.delete(keys);
        // 删除所有用户缓存
        Set<String> keysUser = redisTemplate.keys("user:" + "*");
        redisTemplate.delete(keysUser);
        return new ResultUtil<>().setSuccessMsg("编辑成功");
    }

    /**
     * 批量通过id删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    public Result<Object> delByIds(@PathVariable String[] ids){

        for(String id : ids){
            deleteRecursion(id, ids);
        }
        // 手动删除所有部门缓存
        Set<String> keys = redisTemplate.keys("department:" + "*");
        redisTemplate.delete(keys);
        // 删除数据权限缓存
        Set<String> keysUserRoleData = redisTemplate.keys("userRole::depIds:" + "*");
        redisTemplate.delete(keysUserRoleData);
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    public void deleteRecursion(String id, String[] ids){

        List<User> list = userService.findByDepartmentId(id);
        if(list!=null&&list.size()>0){
            throw new RuanyunException("删除失败，包含正被用户使用关联的部门");
        }
        // 获得其父节点
        Department dep = departmentService.get(id);
        Department parent = null;
        if(dep!=null&&StrUtil.isNotBlank(dep.getParentId())){
            parent = departmentService.get(dep.getParentId());
        }
        departmentService.delete(id);
        // 删除关联数据权限
        roleDepartmentService.deleteByDepartmentId(id);
        // 删除关联部门负责人
        departmentHeaderService.deleteByDepartmentId(id);
        // 判断父节点是否还有子节点
        if(parent!=null){
            List<Department> childrenDeps = departmentService.findByParentIdOrderBySortOrder(parent.getId(), false);
            if(childrenDeps==null||childrenDeps.size()==0){
                parent.setIsParent(false);
                departmentService.update(parent);
            }
        }
        // 递归删除
        List<Department> departments = departmentService.findByParentIdOrderBySortOrder(id, false);
        for(Department d : departments){
            if(!CommonUtil.judgeIds(d.getId(), ids)){
                deleteRecursion(d.getId(), ids);
            }
        }
    }

    /**
     * 部门名模糊搜索
     * @param title
     * @param openDataFilter
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Result<List<Department>> searchByTitle(@RequestParam String title,
                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<Department> list = departmentService.findByTitleLikeOrderBySortOrder("%"+title+"%", openDataFilter);
        list = setInfo(list);
        return new ResultUtil<List<Department>>().setData(list);
    }

    public List<Department> setInfo(List<Department> list){

        // lambda表达式
        list.forEach(item -> {
            if(!CommonConstant.PARENT_ID.equals(item.getParentId())){
                Department parent = departmentService.get(item.getParentId());
                item.setParentTitle(parent.getTitle());
            }else{
                item.setParentTitle("一级部门");
            }
            // 设置负责人
            item.setMainHeader(departmentHeaderService.findHeaderByDepartmentId(item.getId(), CommonConstant.HEADER_TYPE_MAIN));
            item.setViceHeader(departmentHeaderService.findHeaderByDepartmentId(item.getId(), CommonConstant.HEADER_TYPE_VICE));
        });
        return list;
    }
}
