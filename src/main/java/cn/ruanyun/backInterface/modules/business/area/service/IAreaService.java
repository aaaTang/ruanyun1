package cn.ruanyun.backInterface.modules.business.area.service;

import cn.ruanyun.backInterface.modules.business.area.VO.AppAreaListVO;
import cn.ruanyun.backInterface.modules.business.area.VO.AppAreaVO;
import cn.ruanyun.backInterface.modules.business.area.VO.BackAreaVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.area.pojo.Area;

import java.util.List;


/**
 * 城市管理接口
 * @author fei
 */
public interface IAreaService extends IService<Area> {


    /**
     * 插入或者更新area
     * @param area area
     */
    void insertOrderUpdateArea(Area area);


    /**
     * 移除area
     * @param ids ids
     */
    void removeArea(String ids);


    /**
     * 获取后台管理系统城市列表
     * @param pid pid
     * @return BackAreaVO
     */
    List<BackAreaVO> getBackAreaList(String pid);


    /**
     * 获取app区域列表
     * @return AppAreaListVO
     */
    List<AppAreaListVO> getAppAreaList();


    /**
     * 获取app热门区域列表
     * @return AppAreaVO
     */
    List<AppAreaVO> getAppHotAreaList();


    /**
     * 向上查询详细地址
     * @param id id
     * @return String
     */
    String getAddress(String id);


    /**
     * 查询地区名称
     * @param id id
     * @return return
     */
    String getAddressName(String id);


    /**
     * 根据名称获取id
     * @param name id
     * @return String
     */
    String getIdByAreaName(String name);


}