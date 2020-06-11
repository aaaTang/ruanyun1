package cn.ruanyun.backInterface.modules.business.recommendedPackage.service;

import cn.ruanyun.backInterface.common.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.recommendedPackage.pojo.RecommendedPackage;

import java.util.List;

/**
 * 推荐商品和套餐接口
 * @author z
 */
public interface IRecommendedPackageService extends IService<RecommendedPackage> {


      /**
        * 插入或者更新recommendedPackage
        * @param recommendedPackage
       */
      Result<Object> insertOrderUpdateRecommendedPackage(RecommendedPackage recommendedPackage);



      /**
       * 移除recommendedPackage
       * @param ids
       */
     void removeRecommendedPackage(String ids);
}