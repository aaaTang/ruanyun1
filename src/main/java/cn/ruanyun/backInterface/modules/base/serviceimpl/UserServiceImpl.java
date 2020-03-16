package cn.ruanyun.backInterface.modules.base.serviceimpl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.vo.SearchVo;
import cn.ruanyun.backInterface.modules.base.mapper.UserDao;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.PermissionMapper;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserRoleMapper;
import cn.ruanyun.backInterface.modules.base.pojo.Permission;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.UserService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IPermissionService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private PermissionMapper permissionMapper;


    @Override
    public UserDao getRepository() {
        return userDao;
    }

    @Override
    public User findByUsername(String username) {

        /* // 关联角色
        List<Role> roleList = userRoleMapper.findByUserId(user.getId());
        user.setRoles(roleList);

        // 关联权限菜单
        List<Permission> permissionList = permissionMapper.findByUserId(user.getId());
        user.setPermissions(permissionList);*/
        return userDao.findByUsername(username);
    }

    @Override
    public User findByMobile(String mobile) {

        return userDao.findByMobile(mobile);
    }


    @Override
    public Page<User> findByCondition(User user, SearchVo searchVo, Pageable pageable) {

        return userDao.findAll(new Specification<User>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {

                Path<String> usernameField = root.get("username");
                Path<String> mobileField = root.get("mobile");
                Path<String> emailField = root.get("email");
                Path<String> departmentIdField = root.get("departmentId");
                Path<String> sexField = root.get("sex");
                Path<Integer> typeField = root.get("type");
                Path<Integer> statusField = root.get("status");
                Path<Date> createTimeField = root.get("createTime");

                List<Predicate> list = new ArrayList<Predicate>();

                //模糊搜素
                if(StrUtil.isNotBlank(user.getUsername())){
                    list.add(cb.like(usernameField,'%'+user.getUsername()+'%'));
                }
                if(StrUtil.isNotBlank(user.getMobile())){
                    list.add(cb.like(mobileField,'%'+user.getMobile()+'%'));
                }

                //性别
                if(StrUtil.isNotBlank(user.getSex())){
                    list.add(cb.equal(sexField, user.getSex()));
                }
                //类型
                if(user.getType()!=null){
                    list.add(cb.equal(typeField, user.getType()));
                }
                //状态
                if(user.getStatus()!=null){
                    list.add(cb.equal(statusField, user.getStatus()));
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
    public List<User> findByUsernameLikeAndStatus(String username, Integer status) {

        return userDao.findByUsernameLikeAndStatus(username, status);
    }
}
