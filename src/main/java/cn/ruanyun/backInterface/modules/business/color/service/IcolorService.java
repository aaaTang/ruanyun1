package cn.ruanyun.backInterface.modules.business.color.service;

import cn.ruanyun.backInterface.modules.business.color.VO.ColorInfoVO;
import cn.ruanyun.backInterface.modules.business.color.VO.ColorVO;
import cn.ruanyun.backInterface.modules.business.color.entity.Color;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 商品颜色接口
 * @author fei
 */
public interface IcolorService extends IService<Color> {


    /**
     * 插入颜色分类
     * @param color
     */
    void  insertColorCategory(Color color);

    /**
     * 删除颜色分类
     * @param id
     */
    void  deleteColorCategory(String id);

    /**
     * 修改颜色分类
     * @param color
     */
    void  updateColorCategory(Color color);


    /**
     * 获取颜色名字
     * @param id
     * @return
     */
    String getColorName(String id);

    /**
     * 获取颜色列表
     * @param pid
     * @param userId
     * @return
     */
    List<ColorVO> getColorList(String pid, String userId);

    /**
     * 通过id获取尺寸详情
     * @param ids
     * @return
     */
    List<ColorInfoVO> getColorInfoVO(String ids);


}