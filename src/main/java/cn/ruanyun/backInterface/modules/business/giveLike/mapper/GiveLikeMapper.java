package cn.ruanyun.backInterface.modules.business.giveLike.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.ruanyun.backInterface.modules.business.giveLike.pojo.GiveLike;

import java.util.List;

/**
 * 用户点赞数据处理层
 * @author z
 */
public interface GiveLikeMapper extends BaseMapper<GiveLike> {

    void removeGiveLike(String dynamicVideoId, String id);
}