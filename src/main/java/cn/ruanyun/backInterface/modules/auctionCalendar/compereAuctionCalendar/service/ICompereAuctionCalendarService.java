package cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.DTO.CompereAuctionCalendarDTO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.VO.CompereAuctionCalendarVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.VO.PcGetCompereAuctionCalendarVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereAuctionCalendar.pojo.CompereAuctionCalendar;

import java.util.List;

/**
 * 主持人档期的时间接口
 * @author z
 */
public interface ICompereAuctionCalendarService extends IService<CompereAuctionCalendar> {


      /**
        * 插入或者更新compereAuctionCalendar
        * @param compereAuctionCalendar
       */
      Result<Object> insertOrderUpdateCompereAuctionCalendar(CompereAuctionCalendar compereAuctionCalendar);



      /**
       * 移除compereAuctionCalendar
       */
     void removeCompereAuctionCalendar(String goodsId,String scheduleTime);


    /**
     * APP查询某天是否有档期
     * @param goodsId 商品id
     * @param scheduleTime  档期时间
     * @return
     */
    List<CompereAuctionCalendarVO> AppGetCompereAuctionCalendar(String goodsId,String scheduleTime);


    /**
     * 后台获取特殊档期价格列表
     * @return
     */
    List<PcGetCompereAuctionCalendarVO> PcGetCompereAuctionCalendar(CompereAuctionCalendarDTO compereAuctionCalendarDTO);

}