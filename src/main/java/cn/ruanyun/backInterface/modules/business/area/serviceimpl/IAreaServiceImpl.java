package cn.ruanyun.backInterface.modules.business.area.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.Role;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.pojo.UserRole;
import cn.ruanyun.backInterface.modules.business.area.VO.AppAreaListVO;
import cn.ruanyun.backInterface.modules.business.area.VO.AppAreaVO;
import cn.ruanyun.backInterface.modules.business.area.VO.BackAreaVO;
import cn.ruanyun.backInterface.modules.business.area.mapper.AreaMapper;
import cn.ruanyun.backInterface.modules.business.area.pojo.Area;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
 * 城市管理接口实现
 *
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IAreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements IAreaService {


    @Autowired
    private SecurityUtil securityUtil;
    @Resource
    private UserMapper userMapper;

    @Override
    public void insertOrderUpdateArea(Area area) {

        if (ToolUtil.isEmpty(area.getCreateBy())) {
            area.setCreateBy(securityUtil.getCurrUser().getId());
        } else {
            area.setUpdateBy(securityUtil.getCurrUser().getId());
        }
        Mono.fromCompletionStage(CompletableFuture.runAsync(() -> {

            if (ToolUtil.isEmpty(area.getParentId())) {

                area.setParentId(CommonConstant.PARENT_ID);
            }

            if (ToolUtil.isNotEmpty(area.getAreaIndex())) {

                area.setSortOrder(area.getAreaIndex().getCode());
            }
            super.saveOrUpdate(area);

        })).publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool())).toFuture().join();
    }

    @Override
    public void removeArea(String ids) {

        CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
    }

    @Override
    public List<BackAreaVO> getBackAreaList(String pid) {

        //1.获取一级分类
        CompletableFuture<Optional<String>> parentId = CompletableFuture.supplyAsync(() -> Optional.ofNullable(pid)
                , ThreadPoolUtil.getPool());

        //2.获取封装之前的数据
        CompletableFuture<Optional<List<Area>>> getAreas = parentId.thenApplyAsync(optionalS -> optionalS
                .map(optional -> Optional.ofNullable(getAreaList(optional))).orElseGet(() ->
                        Optional.ofNullable(getAreaList(CommonConstant.PARENT_ID))), ThreadPoolUtil.getPool());

        //3.获取封装之后的数据
        CompletableFuture<List<BackAreaVO>> backAreaList = getAreas.thenApplyAsync(areas ->
                areas.map(areaList -> areaList.parallelStream().flatMap(area -> {

                    BackAreaVO backAreaVO = new BackAreaVO();
                    ToolUtil.copyProperties(area, backAreaVO);

                    if (ToolUtil.isNotEmpty(super.list(Wrappers.<Area>lambdaQuery()
                            .eq(Area::getParentId, area.getId())))) {

                        backAreaVO.setIsParent(true);
                    }
                    return Stream.of(backAreaVO);
                }).collect(Collectors.toList())).orElse(null), ThreadPoolUtil.getPool());

        return backAreaList.join();
    }


    /**
     * 获取基础分类列表
     *
     * @param pid
     * @return
     */
    public List<Area> getAreaList(String pid) {

        return ToolUtil.setListToNul(this.list(Wrappers.<Area>lambdaQuery()
                .eq(Area::getParentId, pid)
                .orderByDesc(Area::getCreateTime)));
    }


    @Override
    public List<AppAreaListVO> getAppAreaList() {

        return Mono.fromCompletionStage(CompletableFuture.supplyAsync(() -> Optional.ofNullable(ToolUtil.setListToNul(super
                .list(Wrappers.<Area>lambdaQuery()
                        .isNotNull(Area::getAreaIndex)
                        .eq(Area::getStatus, CommonConstant.STATUS_NORMAL)
                        .orderByAsc(Area::getSortOrder)))))

                .thenApplyAsync(areas -> areas.map(areaList -> areaList.parallelStream().collect(Collectors.groupingBy(Area::getAreaIndex))))
                .thenApplyAsync(areaIndexEnumListMap -> areaIndexEnumListMap.map(areaIndexEnumList -> {

                    List<AppAreaListVO> appAreaListVos = Lists.newArrayList();

                    areaIndexEnumList.forEach((k, v) -> {

                        AppAreaListVO appAreaListVO = new AppAreaListVO();
                        appAreaListVO.setAreaIndex(k)
                                .setAreaListVOS(v.parallelStream().flatMap(area -> {

                                    AppAreaVO appAreaVO = new AppAreaVO();
                                    ToolUtil.copyProperties(area, appAreaVO);
                                    return Stream.of(appAreaVO);
                                }).collect(Collectors.toList()));

                        appAreaListVos.add(appAreaListVO);
                    });

                    return appAreaListVos;
                }).orElse(null))).toFuture().join();
    }

    @Override
    public List<AppAreaVO> getAppHotAreaList() {

        List<Area> areaList = this.list();

        List<AppAreaVO> appAreaVO = new ArrayList<>();
        for (Area area : areaList) {
            AppAreaVO areaVO = new AppAreaVO();
            areaVO.setId(area.getId());
            areaVO.setTitle(area.getTitle());
            areaVO.setCount(Optional.ofNullable(userMapper.selectList(
                    new QueryWrapper<User>().lambda().eq(User::getAreaId,area.getId())).size()).orElse(0));
            appAreaVO.add(areaVO);
        }
        AppAreaVO areaVO = appAreaVO.get(0);
         for (int i= 0;i<appAreaVO.size();i++){
                if(areaVO.getCount()<appAreaVO.get(i).getCount()){
                        areaVO = appAreaVO.get(i);
                }
         }
         List<AppAreaVO> appArea = new ArrayList<>();
        appArea.add(areaVO);
        return appArea;
    }

    @Override
    public String getAddress(String id) {
        String address = "";
        Area area = super.getById(id);
        if (ToolUtil.isNotEmpty(area)) {
            if (ToolUtil.isNotEmpty(area.getParentId()) && !"0".equals(area.getParentId()) && !"1".equals(area.getParentId())) {
                address = spliceAddress(address, id);
            } else {
                address = address.concat(area.getTitle());
            }
        }
        return address;
    }

    //拼接详细地址
    public String spliceAddress(String address, String id) {
        Area area = super.getById(id);
        if (ToolUtil.isNotEmpty(area.getParentId()) && !"0".equals(area.getParentId()) && !"1".equals(area.getParentId())) {
            address = area.getTitle() + address;
            address = spliceAddress(address, area.getParentId());
        } else {
            address = (area.getTitle()).concat(address);
        }
        return address;
    }

}