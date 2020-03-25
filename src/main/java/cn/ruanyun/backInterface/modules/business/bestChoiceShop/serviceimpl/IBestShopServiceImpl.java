package cn.ruanyun.backInterface.modules.business.bestChoiceShop.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.VO.BackBestShopListVO;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.VO.BestShopListVO;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.mapper.BestShopMapper;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.pojo.BestShop;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.service.IBestShopService;
import cn.ruanyun.backInterface.modules.business.goodsPackage.mapper.GoodsPackageMapper;
import cn.ruanyun.backInterface.modules.business.goodsPackage.pojo.GoodsPackage;
import cn.ruanyun.backInterface.modules.business.storeAudit.mapper.StoreAuditMapper;
import cn.ruanyun.backInterface.modules.business.storeAudit.pojo.StoreAudit;
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

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Resource;


/**
 * 严选商家接口实现
 * @author zhu
 */
@Slf4j
@Service
@Transactional
public class IBestShopServiceImpl extends ServiceImpl<BestShopMapper, BestShop> implements IBestShopService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private StoreAuditMapper storeAuditMapper;

       @Resource
       private UserMapper userMapper;

        @Resource
        private GoodsPackageMapper goodsPackageMapper;

        @Resource
        private BestShopMapper bestShopMapper;


    @Override
       public void insertOrderUpdateBestShop(BestShop bestShop) {

           if (ToolUtil.isEmpty(bestShop.getCreateBy())) {

                       bestShop.setCreateBy(securityUtil.getCurrUser().getId());
                   }else {

                       bestShop.setUpdateBy(securityUtil.getCurrUser().getId());
                   }


                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(bestShop)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeBestShop(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     *  APP严选商家
     * @param strict 是否是严选商家，0否，1是
     * @return
     */
    @Override
    public List<BestShopListVO> getBestChoiceShopList(String strict){

        List<StoreAudit> storeAudits = storeAuditMapper.selectList(new QueryWrapper<StoreAudit>().lambda()
                .eq(StoreAudit::getCheckEnum, 2));

        List<BestShopListVO> bestShopListVOS =storeAudits.parallelStream().map(st->{

            BestShopListVO bestShopListVO = bestShopMapper.getBestChoiceShopList(st.getCreateBy(),strict);
            if(ToolUtil.isNotEmpty(bestShopListVO)) {
                List<GoodsPackage> goodsPackages =
                        goodsPackageMapper.selectList(new QueryWrapper<GoodsPackage>().lambda()
                                .eq(GoodsPackage::getCreateBy, st.getCreateBy())
                                .orderByAsc(GoodsPackage::getNewPrice));


                String goodPrice = ToolUtil.isEmpty(goodsPackages) ? null : goodsPackages.get(0).getNewPrice();
                bestShopListVO.setPrice(goodPrice);
                ToolUtil.copyProperties(st, bestShopListVO);
            }
                return bestShopListVO;
        }).filter(store->ToolUtil.isNotEmpty(store)).collect(Collectors.toList());

        return bestShopListVOS;
    }


    /**
     * 后端修改严选商家
     */
    @Override
    public Result<Object> updateStrictShop(String StrictId) {

        return Optional.ofNullable(super.getById(StrictId))
                .map(shop -> {
                    String result = "";
                    if(shop.getStrict().equals(1)){
                        shop.setStrict(0);
                        result="取消严选商家成功！";
                    }else if (shop.getStrict().equals(0)){
                        shop.setStrict(1);
                        result="设为严选商家成功！";
                    }
                    super.updateById(shop);

                    return new ResultUtil<>().setSuccessMsg(result);
                }).orElse(new ResultUtil<>().setErrorMsg(201, "在此商家信息有误"));
    }


}