package cn.ruanyun.backInterface.modules.business.size.service;


import cn.ruanyun.backInterface.modules.business.size.VO.SizeInfoVO;
import cn.ruanyun.backInterface.modules.business.size.VO.SizeVO;
import cn.ruanyun.backInterface.modules.business.size.entity.Size;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 商品尺寸接口
 * @author fei
 */
public interface IsizeService extends IService<Size> {


    /**
     * 插入尺寸
     * @param size
     */
    void insertSize(Size size);

    /**
     * 删除尺寸
     * @param id
     */
    void deleteSize(String id);

    /**
     * 修改尺寸
     * @param size
     */
    void updateSize(Size size);

    /**
     * 获取尺寸列表
     * @param goodCategoryId
     * @return
     */
    List<SizeVO> getSizeList(String goodCategoryId);

    /**
     * 获取尺寸名称
     * @param sizeId
     * @return
     */
    String getSizeName(String sizeId);

    /**
     * 通过id获取尺寸详情
     * @param ids
     * @return
     */
    List<SizeInfoVO> getSizeVoByIds(String ids);
}