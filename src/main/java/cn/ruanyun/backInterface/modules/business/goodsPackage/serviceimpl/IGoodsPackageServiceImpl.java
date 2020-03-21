package cn.ruanyun.backInterface.modules.business.goodsPackage.serviceimpl;

import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis.IUserServiceImpl;
import cn.ruanyun.backInterface.modules.base.vo.AppUserVO;
import cn.ruanyun.backInterface.modules.base.vo.BackUserVO;
import cn.ruanyun.backInterface.modules.business.area.mapper.AreaMapper;
import cn.ruanyun.backInterface.modules.business.area.pojo.Area;
import cn.ruanyun.backInterface.modules.business.area.service.IAreaService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.DTO.ShopParticularsDTO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.VO.AppGoodsPackageListVO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.VO.GoodsPackageListVO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.VO.GoodsPackageParticularsVO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.VO.ShopParticularsVO;
import cn.ruanyun.backInterface.modules.business.goodsPackage.mapper.GoodsPackageMapper;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.goodsPackage.service.IGoodsPackageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcloud.cos.transfer.Copy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;
import javax.persistence.Convert;

import static jdk.nashorn.api.scripting.ScriptUtils.convert;


/**
 * 商品套餐接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IGoodsPackageServiceImpl extends ServiceImpl<GoodsPackageMapper, GoodsPackage> implements IGoodsPackageService {


       @Autowired
       private SecurityUtil securityUtil;

        @Resource
        private IUserServiceImpl iUserService;

        @Resource
        private GoodsPackageMapper igoodsPackageMapper;

       @Override
       public void insertOrderUpdateGoodsPackage(GoodsPackage goodsPackage) {

           if (ToolUtil.isEmpty(goodsPackage.getCreateBy())) {

                       goodsPackage.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       goodsPackage.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(goodsPackage)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeGoodsPackage(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * 后端查询商品全部数据
     */
    public List<GoodsPackage> BackGoodsPackageList(){

        return this.list();
    }


    /**
     * App查询商家商品详情
     * @param ids
     * @return
     */
      public Result<Object> GetGoodsPackage(String ids){

          GoodsPackage goodsPackage = this.getById(ids);
          GoodsPackageParticularsVO goodsPackageParticularsVO = new GoodsPackageParticularsVO();
          BeanUtils.copyProperties(goodsPackage,goodsPackageParticularsVO);
          return new ResultUtil<>().setData(goodsPackageParticularsVO);
     }


    /**
     * App分类商家商品筛选
     */
    public List<GoodsPackageListVO> GetGoodsPackageList(String classId,String areaId,Integer newPrice){


        List<GoodsPackage> list= this.list(new QueryWrapper<GoodsPackage>().lambda()
                .eq(EmptyUtil.isNotEmpty(classId),GoodsPackage::getClassId,classId)
                .eq(EmptyUtil.isNotEmpty(areaId),GoodsPackage::getAreaId,areaId)
                .orderByDesc(EmptyUtil.isNotEmpty(newPrice)&&newPrice.equals(1),GoodsPackage::getNewPrice)
                .orderByAsc(EmptyUtil.isNotEmpty(newPrice)&&newPrice.equals(0),GoodsPackage::getNewPrice)
        );

        List<GoodsPackageListVO> goodsPackageListVOS = list.parallelStream().map(goodsPackage -> {

            GoodsPackageListVO goodsPackageListVOList =new GoodsPackageListVO();

            BackUserVO backUserVO = iUserService.getBackUserVO(goodsPackage.getCreateBy());//查询用户信息
            goodsPackageListVOList.setUserid(goodsPackage.getCreateBy())
                    .setUserName(backUserVO.getUsername())
                    .setUserPic(backUserVO.getAvatar());

            ToolUtil.copyProperties(goodsPackage, goodsPackageListVOList);
            return goodsPackageListVOList;
        }).collect(Collectors.toList());

        return goodsPackageListVOS;
    }


    /**
     * 获取App店铺详情数据成功
     */
    public ShopParticularsVO getShopParticulars(String ids){

        return  igoodsPackageMapper.getShopParticulars(ids);
    }

    /**
     * 查询商家精选套餐
     */
    public List<AppGoodsPackageListVO> AppGoodsPackageList(String ids){

        return  igoodsPackageMapper.AppGoodsPackageList(ids);
    }

    /**
     * 修改店铺详情
     */
    public void UpdateShopParticulars(ShopParticularsDTO shopParticularsDTO){

        igoodsPackageMapper.UpdateShopParticulars(shopParticularsDTO);
    }



}