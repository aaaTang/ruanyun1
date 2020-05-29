package cn.ruanyun.backInterface.modules.business.studio.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.business.studio.dto.StudioDto;
import cn.ruanyun.backInterface.modules.business.studio.vo.StudioListVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.studio.pojo.Studio;

/**
 * 工作室接口
 * @author fei
 */
public interface IstudioService extends IService<Studio> {


    /**
     * 插入或者更新studio
     * @param studio studio
     */
    void insertOrderUpdateStudio(Studio studio);


    /**
     * 移除studio
     * @param ids ids
     */
    void removeStudio(String ids);


    /**
     * 处理邀请信息
     * @param studio studio
     * @return Object
     */
    Result<Object> resolveInvite(Studio studio);

    /**
     * 获取成员列表
     * @param pageVo 分页
     * @param studioDto 筛选条件
     * @return StudioListVo
     */
    Result<DataVo<StudioListVo>> getStudioList(PageVo pageVo, StudioDto studioDto);

}