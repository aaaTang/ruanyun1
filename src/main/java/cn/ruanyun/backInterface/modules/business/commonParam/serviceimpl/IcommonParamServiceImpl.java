package cn.ruanyun.backInterface.modules.business.commonParam.serviceimpl;

import cn.ruanyun.backInterface.modules.business.commonParam.mapper.commonParamMapper;
import cn.ruanyun.backInterface.modules.business.commonParam.pojo.commonParam;
import cn.ruanyun.backInterface.modules.business.commonParam.service.IcommonParamService;
import cn.ruanyun.backInterface.modules.business.commonParam.vo.CommonParamVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 公众参数接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IcommonParamServiceImpl extends ServiceImpl<commonParamMapper, commonParam> implements IcommonParamService {

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public void insertOrUpdateCommonParam(commonParam commonParam) {

        if (ToolUtil.isEmpty(commonParam.getId())) {

            commonParam.setCreateBy(securityUtil.getCurrUser().getId());
            this.save(commonParam);

        } else {

            commonParam.setUpdateBy(securityUtil.getCurrUser().getId());
            this.updateById(commonParam);
        }
    }

    @Override
    public CommonParamVo getCommonParamVo() {

        return Optional.ofNullable(this.getOne(Wrappers.<commonParam>lambdaQuery()
                .orderByDesc(commonParam::getCreateTime)
                .last("limit 1")))
                .map(commonParam -> {

                    CommonParamVo commonParamVo = new CommonParamVo();
                    ToolUtil.copyProperties(commonParam, commonParamVo);
                    return commonParamVo;
                }).orElse(null);
    }
}