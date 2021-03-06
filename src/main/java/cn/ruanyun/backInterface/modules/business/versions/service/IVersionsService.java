package cn.ruanyun.backInterface.modules.business.versions.service;

import cn.ruanyun.backInterface.common.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.versions.pojo.Versions;

import java.util.List;

/**
 * 设备版本接口
 * @author z
 */
public interface IVersionsService extends IService<Versions> {


      /**
        * 插入或者更新versions
        * @param versions
       */
     void insertOrderUpdateVersions(Versions versions);



      /**
       * 移除versions
       * @param ids
       */
     void removeVersions(String ids);

    /**
     * 查询当前系统版本
     * @param currentVersion  当前版本号
     * @return
     */
    Result<Object> getVersions(String currentVersion);
}