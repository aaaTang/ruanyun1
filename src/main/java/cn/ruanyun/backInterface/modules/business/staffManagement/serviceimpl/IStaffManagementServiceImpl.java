package cn.ruanyun.backInterface.modules.business.staffManagement.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.staffManagement.dto.StaffDto;
import cn.ruanyun.backInterface.modules.business.staffManagement.mapper.StaffManagementMapper;
import cn.ruanyun.backInterface.modules.business.staffManagement.pojo.StaffManagement;
import cn.ruanyun.backInterface.modules.business.staffManagement.service.IStaffManagementService;
import cn.ruanyun.backInterface.modules.business.staffManagement.vo.StaffListVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


/**
 * 员工管理接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IStaffManagementServiceImpl extends ServiceImpl<StaffManagementMapper, StaffManagement> implements IStaffManagementService {


    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IOrderService orderService;


    /**
     * 添加员工
     *
     * @param staffDto 入参
     */
    @Override
    public Result<Object> addStaff(StaffDto staffDto) {


        String currentUserId = securityUtil.getCurrUser().getId();

        if (ToolUtil.isNotEmpty(staffDto.getCode())) {

            if (ToolUtil.isNotEmpty(CommonConstant.PRE_SMS.concat(staffDto.getMobile()))) {

                if (ObjectUtil.equal(staffDto.getCode(), RedisUtil.getStr(CommonConstant.PRE_SMS.concat(staffDto.getMobile())))) {

                    //1.判断是否有当前手机号用户
                    return Optional.ofNullable(userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getMobile, staffDto.getMobile())))
                            .map(user -> {

                                //2.如果有直接绑定

                                //2.1 判断是否已经绑定过
                                return  Optional.ofNullable(this.getOne(Wrappers.<StaffManagement>lambdaQuery()
                                        .eq(StaffManagement::getStaffId, user.getId())))
                                        .map(staffManagement -> new ResultUtil<>().setErrorMsg(201, "已经绑定过该员工！"))
                                        .orElseGet(() -> {

                                            //2.1 更改角色信息
                                            ThreadPoolUtil.getPool().execute(() -> {

                                                userRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, user.getId()));

                                                UserRole userRole = new UserRole();
                                                userRole.setUserId(user.getId()).setRoleId(roleService.getIdByRoleName(CommonConstant.STAFF))
                                                        .setCreateBy(currentUserId);
                                                userRoleService.save(userRole);

                                            });


                                            //2.2 绑定员工

                                            ThreadPoolUtil.getPool().execute(() -> {

                                                StaffManagement staffManagement = new StaffManagement();
                                                staffManagement.setStaffId(user.getId())
                                                        .setCreateBy(currentUserId);
                                                this.save(staffManagement);

                                            });

                                            return new ResultUtil<>().setSuccessMsg("绑定员工成功！");
                                        });

                            }).orElseGet(() -> {

                                //如果没有生成用户信息，绑定到一起
                                User user = new User();
                                ToolUtil.copyProperties(staffDto, user);
                                user.setPayPassword(new BCryptPasswordEncoder().encode("111111"));
                                if (userService.save(user)) {

                                    StaffManagement staffManagement = new StaffManagement();
                                    staffManagement.setStaffId(user.getId())
                                            .setCreateBy(currentUserId);
                                    this.save(staffManagement);

                                }

                                return new ResultUtil<>().setSuccessMsg("绑定员工成功！");
                            });
                }else {

                    return new ResultUtil<>().setErrorMsg(203, "验证码不一致");
                }

            }else {

                return new ResultUtil<>().setErrorMsg(204, "验证码失效");
            }

        }else {

            return new ResultUtil<>().setErrorMsg(205, "验证码为空！");
        }

    }

    /**
     * 编辑员工
     *
     * @param staffDto 入参
     */
    @Override
    public void updateStaff(StaffDto staffDto) {

       Optional.ofNullable(this.getById(staffDto.getId())).flatMap(staffManagement ->
               Optional.ofNullable(userService.getById(staffManagement.getStaffId())))
               .ifPresent(user -> {

                   ToolUtil.copyProperties(staffDto, user);
                   userService.updateById(user);
               });
    }

    /**
     * 移除员工
     *
     * @param ids 入参
     */
    @Override
    public Result<Object> removeStaff(String ids) {

        Optional.ofNullable(ToolUtil.setListToNul(ToolUtil.splitterStr(ids)))
                .ifPresent(staffIds -> staffIds.parallelStream().forEach(staffId -> {

                    //1. 判空
                    Optional.ofNullable(this.getById(staffId)).ifPresent(staffManagement -> {

                        //1. 更改当前用户的角色信息
                        userRoleService.remove(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, staffManagement.getStaffId()));

                        UserRole userRole = new UserRole();
                        userRole.setUserId(staffManagement.getStaffId())
                                .setRoleId(roleService.getIdByRoleName(CommonConstant.DEFAULT_ROLE))
                                .setCreateBy(staffManagement.getStaffId());
                        userRoleService.save(userRole);

                        //2. 移除绑定关系

                        this.removeById(staffId);

                    });
                }));

        return new ResultUtil<>().setSuccessMsg("移除员工成功！");
    }

    /**
     * 获取员工列表
     *
     * @return StaffListVo
     */
    @Override
    public List<StaffListVo> getStaffList() {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<StaffManagement>lambdaQuery()
                .eq(StaffManagement::getCreateBy, securityUtil.getCurrUser().getId())
                .orderByDesc(StaffManagement::getCreateTime))))
        .map(staffManagements -> staffManagements.parallelStream().flatMap(staffManagement -> {

            StaffListVo staffListVo = new StaffListVo();
            Optional.ofNullable(userService.getById(staffManagement.getStaffId())).ifPresent(user ->
                    ToolUtil.copyProperties(user, staffListVo));
            staffListVo.setId(staffManagement.getId());

            //当前员工销售额
            staffListVo.setSaleAmount(orderService.getStaffSaleAmount(staffManagement.getStaffId()));
            return Stream.of(staffListVo);

        }).sorted(Comparator.comparing(StaffListVo::getSaleAmount)).collect(Collectors.toList()))
        .orElse(null);
    }
}