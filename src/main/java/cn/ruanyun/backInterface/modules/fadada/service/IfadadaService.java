package cn.ruanyun.backInterface.modules.fadada.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.fadada.dto.UploaddocsDto;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.fadada.pojo.fadada;

import java.util.List;

/**
 * 法大大接口
 * @author z
 */
public interface IfadadaService extends IService<fadada> {


    /**
     * 合同上传
     * @param uploaddocsDto 参数
     * @return 执行结果
     */
    Result<Object> uploaddocs(UploaddocsDto uploaddocsDto);


    Result<Object> extsign();

}