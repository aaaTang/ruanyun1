package cn.ruanyun.backInterface.modules.business.goodCategory.service;


import cn.ruanyun.backInterface.common.enums.RentTypeEnum;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.business.goodCategory.VO.CategoryVo;
import cn.ruanyun.backInterface.modules.business.goodCategory.VO.FourDevarajasCategoryVo;
import cn.ruanyun.backInterface.modules.business.goodCategory.VO.GoodCategoryListVO;
import cn.ruanyun.backInterface.modules.business.goodCategory.VO.GoodCategoryVO;
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import com.alipay.api.domain.CategoryVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * 商品分类接口
 * @author fei
 */
public interface IGoodCategoryService extends IService<GoodCategory> {

    /**
     * 插入分类
     * @param goodCategory
     */
    void  insertGoodCategory(GoodCategory goodCategory);

    /**
     * 删除分类
     * @param id
     */
    void  deleteGoodCategory(String id);

    /**
     * 修改
     * @param goodCategory
     */
    void  updateGoodCategory(GoodCategory goodCategory);

    /**
     * 获取分类列表
     * @param pid
     * @return
     */
    List<GoodCategoryVO> getGoodCategoryList(String pid);

    /**
     * 获取app分类列表
     * @return
     */
    List<GoodCategoryListVO> getAppGoodCategoryList();


    /**
     * 通过父类id获取其子类id集合
     * @param pid
     * @return
     */
    Set<String> getIdByParentId(String pid);

    /**
     * 获取分类名称
     * @param id
     * @return
     */
    String getGoodCategoryName(String id);

    /**
     * 判断分类是否有父类
     * @param id
     * @return
     */
    Boolean judgeGoodCategoryHaveParent(String id);

    /**
     * 按分类获取商家列表
     * @return
     */
    List getCategoryShop(String classId,String areaId);


    /**
     * 获取四大金刚分类数据
     * @return FourDevarajasCategoryVo
     */
    Result<List<FourDevarajasCategoryVo>> getFourDearestsCategory();


    /**
     * 获取后台管理系统的分类列表
     * @return CategoryVO
     */
    Result<List<CategoryVo>> getBackStoreCategoryList();


    /**
     * 获取优质服务的分类数据
     * @return List<CategoryVo>
     */
    Result<List<CategoryVo>> getGoodServiceGoodCategory();


    /**
     * 获取分类的购买状态
     * @param goodCategoryId goodCategoryId
     * @return Object
     */
    Result<Object> getCategoryState(String goodCategoryId);


    /**
     * 根据分类id获取尾款支付类型
     * @param goodCategoryId  分类id
     * @return Integer
     */
    RentTypeEnum getLeaseState(String goodCategoryId);

    /**
     * 判断商家类型
     * @param user user
     * @return user
     */
    Integer judgeStoreType(User user);
}