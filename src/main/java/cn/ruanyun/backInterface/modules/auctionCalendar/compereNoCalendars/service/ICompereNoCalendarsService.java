package cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.service;

import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.DTO.CompereNoCalendarsDTO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.VO.AppGetCompereNoCalendarsVO;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.VO.CompereNoCalendarsVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.auctionCalendar.compereNoCalendars.pojo.CompereNoCalendars;

import java.util.List;

/**
 * 设置主持人没有档期的时间接口
 * @author z
 */
public interface ICompereNoCalendarsService extends IService<CompereNoCalendars> {


      /**
        * 插入或者更新compereNoCalendars
        * @param compereNoCalendars
       */
     void insertOrderUpdateCompereNoCalendars(CompereNoCalendars compereNoCalendars);



      /**
       * 移除compereNoCalendars
       */
     void removeCompereNoCalendars(CompereNoCalendars compereNoCalendars);


    /**
     * 后端获取主持人没有档期的列表
     * @param compereNoCalendarsDTO
     * @return
     */
    List<CompereNoCalendarsVO> PcGetCompereNoCalendars(CompereNoCalendarsDTO compereNoCalendarsDTO);

    /**
     * 获取主持人商品或者套餐已经被购买的档期列表
     * @return
     */
    List<AppGetCompereNoCalendarsVO> AppGetCompereNoCalendars(CompereNoCalendarsDTO compereNoCalendarsDTO);
}