package cn.ruanyun.backInterface.modules.business.storeActivity.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.OrderStatusEnum;
import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.storeActivity.DTO.StoreActivityDTO;
import cn.ruanyun.backInterface.modules.business.storeActivity.VO.StoreActivityVO;
import cn.ruanyun.backInterface.modules.business.storeActivity.mapper.StoreActivityMapper;
import cn.ruanyun.backInterface.modules.business.storeActivity.pojo.StoreActivity;
import cn.ruanyun.backInterface.modules.business.storeActivity.service.IStoreActivityService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 店铺活动接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IStoreActivityServiceImpl extends ServiceImpl<StoreActivityMapper, StoreActivity> implements IStoreActivityService {


       @Autowired
       private SecurityUtil securityUtil;
       @Autowired
       private IGoodService iGoodService;
       @Resource
       private UserMapper userMapper;

       @Override
       public void insertOrderUpdateStoreActivity(StoreActivity storeActivity) {

           if (ToolUtil.isEmpty(storeActivity.getCreateBy())) {

                       storeActivity.setCreateBy(securityUtil.getCurrUser().getId());

                        String userRole = iGoodService.getRoleUserList(securityUtil.getCurrUser().getId());

                        if(userRole.equals(CommonConstant.PER_STORE)){
                            storeActivity.setUserTypeEnum(UserTypeEnum.PER_STORE);

                        }else if(userRole.equals(CommonConstant.STORE)){
                            storeActivity.setUserTypeEnum(UserTypeEnum.STORE);

                        }else if(userRole.equals(CommonConstant.ADMIN)){
                            storeActivity.setUserTypeEnum(UserTypeEnum.ADMIN);
                        }

                   }else {

                       storeActivity.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(storeActivity)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeStoreActivity(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }


    /**
     * 获取商家活动数据
     * @param createBy
     * @return
     */
      @Override
      public  List<StoreActivityVO> getStoreActivity(String createBy,UserTypeEnum userTypeEnum){

           return Optional.ofNullable(ToolUtil.setListToNul(this.list(new QueryWrapper<StoreActivity>().lambda()

            .eq(ToolUtil.isNotEmpty(createBy),StoreActivity::getCreateBy,createBy)

            .eq(ToolUtil.isNotEmpty(userTypeEnum),StoreActivity::getUserTypeEnum,userTypeEnum)

            .orderByDesc(StoreActivity::getCreateTime)

           ))).map(storeActivities -> storeActivities.parallelStream().flatMap(storeActivity -> {

               StoreActivityVO storeActivityVO = new StoreActivityVO();

               storeActivityVO.setShopName(Optional.ofNullable(userMapper.selectById(storeActivity.getCreateBy())).map(User::getShopName).orElse("暂无设置店铺名称！"));

               ToolUtil.copyProperties(storeActivity,storeActivityVO);
               return  Stream.of(storeActivityVO);
           }).collect(Collectors.toList()))
                   .orElse(null);

      }


    /**
     * 获取商家活动列表
     * @param
     * @return
     */
    @Override
    public  List<StoreActivityVO> getStoreActivityList(StoreActivityDTO storeActivityDTO){

        String userRole = iGoodService.getRoleUserList(securityUtil.getCurrUser().getId());

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(new QueryWrapper<StoreActivity>().lambda()

                .eq(userRole.equals(CommonConstant.PER_STORE)||userRole.equals(CommonConstant.STORE),StoreActivity::getCreateBy,securityUtil.getCurrUser().getId())

                .eq(userRole.equals(CommonConstant.PER_STORE),StoreActivity::getUserTypeEnum,UserTypeEnum.PER_STORE)

                .eq(userRole.equals(CommonConstant.STORE),StoreActivity::getUserTypeEnum,UserTypeEnum.STORE)

                .orderByDesc(StoreActivity::getCreateTime)

        ))).map(storeActivities -> storeActivities.parallelStream().flatMap(storeActivity -> {

            StoreActivityVO storeActivityVO = new StoreActivityVO();
            storeActivityVO.setShopName(Optional.ofNullable(userMapper.selectById(storeActivity.getCreateBy())).map(User::getShopName).orElse("暂无设置店铺名称！"));

            ToolUtil.copyProperties(storeActivity,storeActivityVO);
            return  Stream.of(storeActivityVO);
        }).collect(Collectors.toList()))
                .orElse(null);

    }

    @Override
    public Result<Object> getActivity(String id) {
        return Optional.ofNullable(this.getById(id))
                .map(order -> {

                    return new ResultUtil<>().setData(order,"查询成功！");

                }).orElse(new ResultUtil<>().setErrorMsg(201, "该活动不存在！"));
    }


}