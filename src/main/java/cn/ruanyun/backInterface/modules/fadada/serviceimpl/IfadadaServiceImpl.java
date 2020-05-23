package cn.ruanyun.backInterface.modules.fadada.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.fadada.dto.UploaddocsDto;
import cn.ruanyun.backInterface.modules.fadada.mapper.fadadaMapper;
import cn.ruanyun.backInterface.modules.fadada.pojo.fadada;
import cn.ruanyun.backInterface.modules.fadada.service.IfadadaService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fadada.sdk.client.FddClientBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 法大大接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IfadadaServiceImpl extends ServiceImpl<fadadaMapper, fadada> implements IfadadaService {


    /**
     * 法大大AppId
     */
    public static final String APP_ID = "403272";

    /**
     * 法大大AppSecret
     */
    public static final String APP_SECRET = "mVsjGqtP4HR4hFisqN68zP8o";

    /**
     * 主机
     */
    public static final String HOST = "http://test.api.fabigbig.com:8888/api/";

    /**
     * 版本号
     */
    public static final String VERSION= "2.0";


    @Override
    public Result<Object> uploaddocs(UploaddocsDto uploaddocsDto) {

        FddClientBase base = new FddClientBase(APP_ID, APP_SECRET, VERSION, HOST );

        String result = base.invokeUploadDocs(uploaddocsDto.getContract_id(), uploaddocsDto.getDoc_title(),
                uploaddocsDto.getFile(), uploaddocsDto.getDoc_url(), uploaddocsDto.getDoc_type());

        JSONObject jsonObject = JSONObject.parseObject(result);

        if (ObjectUtil.equal(jsonObject.getString("code"), "2002")) {

            return new ResultUtil<>().setSuccessMsg("上传合同成功！");
        }else {

            return new ResultUtil<>().setErrorMsg(Integer.parseInt(jsonObject.getString("code")), jsonObject.getString("msg"));
        }
    }
}