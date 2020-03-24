package cn.ruanyun.backInterface.modules.business.bestChoiceShop.mapper;

import cn.ruanyun.backInterface.modules.business.bestChoiceShop.VO.BackBestShopListVO;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.VO.BestShopListVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.ruanyun.backInterface.modules.business.bestChoiceShop.pojo.BestShop;


/**
 * 严选商家数据处理层
 * @author zhu
 */
public interface BestShopMapper extends BaseMapper<BestShop> {

    /**
     * APP严选商家
     * @param strict 是否严选1是  0否
     * @return
     */
    BestShopListVO getBestChoiceShopList(String ids,String strict);

    /**
     * 后端严选商家
     * @param strict 是否严选1是  0否
     * @return
     */
    BackBestShopListVO BackBestChoiceShopList(String ids, String strict);

}