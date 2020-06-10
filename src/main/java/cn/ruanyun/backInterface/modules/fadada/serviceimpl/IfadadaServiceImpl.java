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

    @Autowired
    private IUserService userService;


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

    /**
     * 获取企业实名认证地址
     *
     * @param companyVerifyDto 参数
     * @return Object
     */
    @Override
    public Result<Object> getCompanyVerifyUrl(CompanyVerifyDto companyVerifyDto) {

        GetCompanyVerifyUrl companyVerifyUrl = new GetCompanyVerifyUrl(APP_ID, APP_SECRET, VERSION, HOST);


        User user = userService.getById(securityUtil.getCurrUser().getId());

        //企业信息
        CompanyInfoINO companyInfo = new CompanyInfoINO();
        companyInfo.setCompany_name(companyVerifyDto.getCompanyName());
        companyInfo.setCredit_image_path(companyVerifyDto.getCreditImagePath());
        companyInfo.setCredit_no(companyVerifyDto.getCreditNo());


        //对公账号信息
        BankInfoINO bankInfoIno = new BankInfoINO();
        bankInfoIno.setBank_name(companyVerifyDto.getBankName());
        bankInfoIno.setBank_id(companyVerifyDto.getBankId());
        bankInfoIno.setSubbranch_name(companyVerifyDto.getSubbranchName());

        //法人信息
        LegalInfoINO legalInfo = new LegalInfoINO();
        legalInfo.setLegal_name(companyVerifyDto.getLegalName());
        legalInfo.setLegal_id(companyVerifyDto.getLegalId());
        legalInfo.setLegal_mobile(companyVerifyDto.getLegalMobile());
        legalInfo.setLegal_id_front_path(companyVerifyDto.getLegalIdFrontPath());


        //代理人信息
        AgentInfoINO agentInfo = new AgentInfoINO();
        agentInfo.setAgent_name(companyVerifyDto.getAgentName());
        agentInfo.setAgent_id(companyVerifyDto.getAgentId());
        agentInfo.setAgent_mobile(companyVerifyDto.getAgentMobile());
        agentInfo.setAgent_id_front_path(companyVerifyDto.getAgentIdFrontPath());


        String result = companyVerifyUrl.invokeCompanyVerifyUrl(companyInfo, bankInfoIno, legalInfo, agentInfo, securityUtil.getCurrUser().getCustomerId(),
                companyVerifyDto.getVerifiedWay(), companyVerifyDto.getMVerifiedWay(), companyVerifyDto.getPageModify(), companyVerifyDto.getCompanyPrincipalType(), companyVerifyDto.getReturnUrl(),
                companyVerifyDto.getNotifyUrl(), companyVerifyDto.getResultType(), companyVerifyDto.getCertFlag());

        if (JSONObject.parseObject(result).getIntValue("code") == 1) {

            String data = JSONObject.parseObject(result).getString("data");
            user.setTransactionNo(JSONObject.parseObject(data).getString("transactionNo"))
                    .setVerifyUrl(JSONObject.parseObject(data).getString("url"));
            userService.updateById(user);

            return new ResultUtil<>().setData(user.getVerifyUrl(), "申请成功！");
        }else {

            return new ResultUtil<>().setErrorMsg(204, JSONObject.parseObject(result).getString("msg"));
        }

    }

    /**
     * 获取个人实名认证地址
     *
     * @param personVerifyDto 参数
     * @return Object
     */
    @Override
    public Result<Object> getPersonVerifyUrl(PersonVerifyDto personVerifyDto) {

        User user = userService.getById(securityUtil.getCurrUser().getId());

        GetPersonVerifyUrl personVerify = new GetPersonVerifyUrl(APP_ID, APP_SECRET, VERSION, HOST);

        String result = personVerify.invokePersonVerifyUrl(securityUtil.getCurrUser().getCustomerId(), personVerifyDto.getVerifiedWay(), personVerifyDto.getPageModify(),
                personVerifyDto.getNotifyUrl(), personVerifyDto.getReturnUrl(), personVerifyDto.getCustomerName(), personVerifyDto.getCustomerIdentType(),personVerifyDto.getCustomerIdentNo()
        ,personVerifyDto.getMobile(), personVerifyDto.getIdentFrontPath(), personVerifyDto.getResultType(), personVerifyDto.getCertFlag());

        log.info(result);

        if (JSONObject.parseObject(result).getIntValue("code") == 1) {

            String data = JSONObject.parseObject(result).getString("data");
            user.setTransactionNo(JSONObject.parseObject(data).getString("transactionNo"))
                    .setVerifyUrl(JSONObject.parseObject(data).getString("url"));
            userService.updateById(user);

            return new ResultUtil<>().setData(user.getVerifyUrl(), "申请个人实名认证成功！");
        }else {

            return new ResultUtil<>().setErrorMsg(204, JSONObject.parseObject(result).getString("msg"));
        }
    }


    /**
     * 实名认证证书申请
     * @return Object
     */
    @Override
    public Result<Object> applyCert() {

        ApplyCert applyCert = new ApplyCert(APP_ID, APP_SECRET, VERSION, HOST);

        User user = userService.getById(securityUtil.getCurrUser().getId());

        String result = applyCert.invokeApplyCert(user.getCustomerId(), user.getTransactionNo());

        return new ResultUtil<>().setData(JSONObject.parseObject(result), "申请实名认证证书成功！");
    }


    @Override
    public Result<Object> addSignature(SignatureDto signatureDto) throws Exception {

        FddClientBase base = new FddClientBase(APP_ID, APP_SECRET, VERSION, HOST );

        String result = base.invokeaddSignature(securityUtil.getCurrUser().getCustomerId(), MultipartFileToFile.multipartFileToFile(signatureDto.getImageFile()), signatureDto.getImgUrl());

        return new ResultUtil<>().setData(JSONObject.parseObject(result), "上传签章成功！");
    }
}
