package cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.DayTimeTypeEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.DTO.SitePriceDTO;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.VO.SitePriceVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.mapper.SitePriceMapper;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.pojo.SitePrice;
import cn.ruanyun.backInterface.modules.auctionCalendar.sitePrice.service.ISitePriceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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


/**
 * 设置场地档期价格接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class ISitePriceServiceImpl extends ServiceImpl<SitePriceMapper, SitePrice> implements ISitePriceService {


       @Autowired
       private SecurityUtil securityUtil;

       @Override
       public void insertOrderUpdateSitePrice(SitePrice sitePrice) {
           sitePrice.setId(null);

           List<SitePrice> sitePrice1 = this.list(Wrappers.<SitePrice>lambdaQuery()
                   .eq(SitePrice::getScheduleTime,sitePrice.getScheduleTime())
                   .eq(SitePrice::getSiteId,sitePrice.getSiteId())
                   .eq(SitePrice::getCreateBy,securityUtil.getCurrUser().getId())
                   .eq(SitePrice::getDelFlag,CommonConstant.STATUS_NORMAL));

            if(ToolUtil.isNotEmpty(sitePrice1)){

                for (SitePrice o : sitePrice1) {
                    ToolUtil.copyProperties(sitePrice,o);
                    sitePrice.setUpdateBy(securityUtil.getCurrUser().getId());
                    this.updateById(o);
                }

            }else {
                sitePrice.setCreateBy(securityUtil.getCurrUser().getId());
                sitePrice.setDayTimeType(DayTimeTypeEnum.A_M);
                this.save(sitePrice);

                sitePrice.setId(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()));
                sitePrice.setCreateBy(securityUtil.getCurrUser().getId());
                sitePrice.setDayTimeType(DayTimeTypeEnum.P_M);
                this.save(sitePrice);
            }

       }

      @Override
      public void removeSitePrice(String siteId,String scheduleTime) {


          List<SitePrice> sitePrice1 = this.list(Wrappers.<SitePrice>lambdaQuery()
                  .eq(SitePrice::getScheduleTime,scheduleTime)
                  .eq(SitePrice::getSiteId,siteId)
                  .eq(SitePrice::getDelFlag,CommonConstant.STATUS_NORMAL));

          for (SitePrice o : sitePrice1) {
              o.setUpdateBy(securityUtil.getCurrUser().getId());
              o.setDelFlag(CommonConstant.DEL_FLAG);
              this.updateById(o);
          }
      }


    @Override
    public List<SitePriceVO> getSitePrice(SitePriceDTO sitePriceDTO) {


        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<SitePrice>lambdaQuery()

                .eq(SitePrice::getScheduleTime,sitePriceDTO.getScheduleTime())

                .eq(SitePrice::getSiteId,sitePriceDTO.getSiteId())

                .eq(SitePrice::getDelFlag, CommonConstant.STATUS_NORMAL))))

                .map(sitePrices -> sitePrices.parallelStream().flatMap(sitePrice -> {

                    SitePriceVO sitePriceVO = new SitePriceVO();
                    ToolUtil.copyProperties(sitePrice,sitePriceVO);

                    return Stream.of(sitePriceVO);

                }).collect(Collectors.toList()))

                .orElse(null);

    }





}