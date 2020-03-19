package cn.ruanyun.backInterface.modules.business.goodsPackage.serviceimpl;

import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.goodsPackage.VO.GoodsPackageParticularsVO;
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
    public Result<Object> GetGoodsPackageList(String classId){

        return null;
    }

}