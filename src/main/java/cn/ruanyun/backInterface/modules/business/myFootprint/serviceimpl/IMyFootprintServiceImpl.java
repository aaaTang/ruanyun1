package cn.ruanyun.backInterface.modules.business.myFootprint.serviceimpl;

import cn.ruanyun.backInterface.common.utils.EmptyUtil;
import cn.ruanyun.backInterface.modules.business.good.mapper.GoodMapper;
import cn.ruanyun.backInterface.modules.business.good.pojo.Good;
import cn.ruanyun.backInterface.modules.business.good.service.IGoodService;
import cn.ruanyun.backInterface.modules.business.myFootprint.VO.MyFootprintVO;
import cn.ruanyun.backInterface.modules.business.myFootprint.mapper.MyFootprintMapper;
import cn.ruanyun.backInterface.modules.business.myFootprint.pojo.MyFootprint;
import cn.ruanyun.backInterface.modules.business.myFootprint.service.IMyFootprintService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 用户足迹接口实现
 * @author zhu
 */
@Slf4j
@Service
@Transactional
public class IMyFootprintServiceImpl extends ServiceImpl<MyFootprintMapper, MyFootprint> implements IMyFootprintService {


       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private GoodMapper iGoodMapper;

       @Autowired
       private IGoodService goodService;

       @Override
       public void insertOrderUpdateMyFootprint(MyFootprint myFootprint) {

           if (ToolUtil.isEmpty(this.getOne(Wrappers.<MyFootprint>lambdaQuery()
                   .eq(MyFootprint::getGoodsId, myFootprint.getGoodsId())
                   .eq(MyFootprint::getCreateBy, securityUtil.getCurrUser().getId())))) {

               myFootprint.setStoreId(Optional.ofNullable(goodService.getById(myFootprint.getGoodsId()))
                       .map(Good::getCreateBy).orElse(null));

               myFootprint.setCreateBy(securityUtil.getCurrUser().getId());
               this.save(myFootprint);

           }
       }

      @Override
      public void removeMyFootprint(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    @Override
    public List<MyFootprintVO> MyFootprintList() {
           //查詢我的足迹列表
        List<MyFootprint> myFootprints = this.list(new QueryWrapper<MyFootprint>().lambda()
                .eq(MyFootprint::getCreateBy,securityUtil.getCurrUser().getId()).orderByDesc(MyFootprint::getCreateTime));

        //按商品id查询商品详情
        List<MyFootprintVO> myFootprintVOList = new ArrayList<>();
        for (int i = 0; i < myFootprints.size(); i++) {
            Good good = iGoodMapper.selectById(myFootprints.get(i).getGoodsId());
            if (EmptyUtil.isNotEmpty(good)){
                MyFootprintVO myFootprintVO = new MyFootprintVO();
                myFootprintVO.setId(myFootprints.get(i).getId())
                        .setGoodsId(good.getId())
                        .setGoodName(good.getGoodName())
                        .setGoodPics(good.getGoodPics().split(",")[0])
                        .setGoodNewPrice(good.getGoodNewPrice());
                myFootprintVOList.add(myFootprintVO);
            }
        }
        return myFootprintVOList;
    }


    /**
     * 获取足迹数量
     * @return
     */
    @Override
    public Long getMyFootprintNum() {
        List<MyFootprint> list = this.list(new QueryWrapper<MyFootprint>().lambda().eq(MyFootprint::getCreateBy,securityUtil.getCurrUser().getId()));
        Long num = (long) list.size();
        return num;
    }

    @Override
    public List<MyFootprint> getMyFootPrintByStoreId(String storeId) {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<MyFootprint>lambdaQuery()
                .eq(MyFootprint::getStoreId, storeId)
                .orderByDesc(MyFootprint::getCreateTime))))
                .orElse(null);
    }

}