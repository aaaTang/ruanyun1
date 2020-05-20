package cn.ruanyun.backInterface.modules.jpush.service;

import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.jpush.dto.JpushDto;
import cn.ruanyun.backInterface.modules.jpush.vo.JpushVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.jpush.pojo.Jpush;

import java.util.List;

/**
 * 虚拟号接口
 * @author z
 */
public interface IJpushService extends IService<Jpush> {


    /**
     * 添加或者修改内容创作
     * @param jpush 推送实体
     */
    void insertOrUpdateJpush(Jpush jpush);


    /**
     * 移除内容创作中心
     * @param ids ids
     */
    void removeJpush(String ids);


    /**
     * 获取推送列表
     * @param jpushDto 分页
     * @return JpushVo
     */
    DataVo<JpushVo> getJpushList(JpushDto jpushDto);


    /**
     * 管理员审核帖子
     * @param jpushDto 参数
     */
    void checkJpush(JpushDto jpushDto);


    /**
     * 推送之后更新表信息
     * @param jpushDto 参数
     */
    void updateJpushByAfterPush(JpushDto jpushDto);

    /**
     * 推送公告给客户
     * @param jpushDto 推送参数
     * @return object
     */
    Result<Object> pushArticleToUser(JpushDto jpushDto);
}