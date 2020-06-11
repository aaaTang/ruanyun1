package cn.ruanyun.backInterface.modules.fadada.serviceimpl;

import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.fadada.dto.*;
import cn.ruanyun.backInterface.modules.fadada.service.IfadadaService;
import com.alibaba.fastjson.JSONObject;
import com.fadada.sdk.client.FddClientBase;
import com.fadada.sdk.client.authForfadada.ApplyCert;
import com.fadada.sdk.client.authForfadada.GetCompanyVerifyUrl;
import com.fadada.sdk.client.authForfadada.GetPersonVerifyUrl;
import com.fadada.sdk.client.authForfadada.model.AgentInfoINO;
import com.fadada.sdk.client.authForfadada.model.BankInfoINO;
import com.fadada.sdk.client.authForfadada.model.CompanyInfoINO;
import com.fadada.sdk.client.authForfadada.model.LegalInfoINO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author Administrator
 */
@Slf4j
@Service
@Transactional
public class IfadadaServiceImpl implements IfadadaService {


    /**
     * 业务需求：1. 商家实名认证 -> 上传印章 -> 手动签署 -> 根据合同编号生成待审核合同列表 -> 审核同意商家会受到推送 -> 在我的电子合同可以看到
     * 合同内容 -> 审核通过会出现平台手动签署合同，并且默认归档合同 -> 审核失败：也会有推送点击跳转到 手动签署页面 重新签署 提交 -> 闭环 -> 平台可以查看合同 以及下载合同
     */

    @Autowired
    private SecurityUtil securityUtil;


    public static final String APP_ID = "500355";


    public static final String APP_SECRET = "KQ2AKUzQWYeYD17hLxFAp6jp";



    public static final String HOST = "http://textapi.fadada.com:9999/api2/";


    public static final String VERSION= "2.0";


    @Override
    public String accountRegister(String openId, String accountType) {

        FddClientBase base = new FddClientBase(APP_ID, APP_SECRET, VERSION, HOST );

        String result = base.invokeregisterAccount(openId, accountType);

        return JSONObject.parseObject(result).getString("data");
    }


    @Override
    public Result<Object> addSignature(SignatureDto signatureDto) throws Exception {

        FddClientBase base = new FddClientBase(APP_ID, APP_SECRET, VERSION, HOST );

        String result = base.invokeaddSignature(securityUtil.getCurrUser().getCustomerId(), /*MultipartFileToFile.multipartFileToFile(signatureDto.getImageFile())*/null, signatureDto.getImgUrl());

        return new ResultUtil<>().setData(JSONObject.parseObject(result), "上传签章成功！");
    }
}
