package cn.ruanyun.backInterface.modules.business.selectStore.service;

import cn.ruanyun.backInterface.modules.business.selectStore.vo.SelectStoreListVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.selectStore.pojo.SelectStore;

import java.util.List;

/**
 * 商品接口
 * @author fei
 */
public interface ISelectStoreService extends IService<SelectStore> {

    /**
     * 插入或者更新selectStore
     * @param selectStore selectStore
     */
    void insertOrderUpdateSelectStore(SelectStore selectStore);

    /**
     * 移除selectStore
     * @param ids ids
     */
    void removeSelectStore(String ids);


    /**
     * 获取严选商家分类列表
     * @return SelectStoreListVO
     */
    List<SelectStoreListVO> getSelectStoreList();


    /**
     * 查询商家是否是严选商家
     */
    Integer getSelectStore(String userId);

}