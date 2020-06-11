package cn.ruanyun.backInterface.modules.fadada.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.fadada.dto.CompanyVerifyDto;
import cn.ruanyun.backInterface.modules.fadada.mapper.EnterpriseCertificationMapper;
import cn.ruanyun.backInterface.modules.fadada.pojo.EnterpriseCertification;
import cn.ruanyun.backInterface.modules.fadada.pojo.PersonalCertificate;
import cn.ruanyun.backInterface.modules.fadada.service.IEnterpriseCertificationService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 企业认证接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IEnterpriseCertificationServiceImpl extends ServiceImpl<EnterpriseCertificationMapper, EnterpriseCertification> implements IEnterpriseCertificationService {


    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserService userService;

    /**
     * 提交企业认证
     *
     * @param companyVerifyDto enterpriseCertification
     * @return Object
     */
    @Override
    public Result<Object> commitOrUpdateEnterpriseCertification(CompanyVerifyDto companyVerifyDto) {

        User user = userService.getById(securityUtil.getCurrUser().getId());

        GetCompanyVerifyUrl companyVerifyUrl = new GetCompanyVerifyUrl(CommonConstant.F_APP_ID, CommonConstant.F_APP_SECRET, CommonConstant.F_VERSION, CommonConstant.F_HOST);

        //首次提交
        if (ToolUtil.isEmpty(companyVerifyDto.getId())) {

            //1.1 提交个人认证api
            EnterpriseCertification enterpriseCertificationNew = new EnterpriseCertification();
            ToolUtil.copyProperties(companyVerifyDto, enterpriseCertificationNew);
            //企业信息
            CompanyInfoINO companyInfo = new CompanyInfoINO();
            companyInfo.setCompany_name(enterpriseCertificationNew.getCompanyName());
            companyInfo.setCredit_image_path(enterpriseCertificationNew.getCreditImagePath());
            companyInfo.setCredit_no(enterpriseCertificationNew.getCreditNo());


            //对公账号信息
            BankInfoINO bankInfoIno = new BankInfoINO();
            bankInfoIno.setBank_name(enterpriseCertificationNew.getBankName());
            bankInfoIno.setBank_id(enterpriseCertificationNew.getBankId());
            bankInfoIno.setSubbranch_name(enterpriseCertificationNew.getSubbranchName());

            //法人信息
            LegalInfoINO legalInfo = new LegalInfoINO();
            legalInfo.setLegal_name(enterpriseCertificationNew.getLegalName());
            legalInfo.setLegal_id(enterpriseCertificationNew.getLegalId());
            legalInfo.setLegal_mobile(enterpriseCertificationNew.getLegalMobile());
            legalInfo.setLegal_id_front_path(enterpriseCertificationNew.getLegalIdFrontPath());


            //代理人信息
            AgentInfoINO agentInfo = new AgentInfoINO();
            agentInfo.setAgent_name(enterpriseCertificationNew.getAgentName());
            agentInfo.setAgent_id(enterpriseCertificationNew.getAgentId());
            agentInfo.setAgent_mobile(enterpriseCertificationNew.getAgentMobile());
            agentInfo.setAgent_id_front_path(enterpriseCertificationNew.getAgentIdFrontPath());

            String result = companyVerifyUrl.invokeCompanyVerifyUrl(companyInfo, bankInfoIno, legalInfo, agentInfo, securityUtil.getCurrUser().getCustomerId(),
                    enterpriseCertificationNew.getVerifiedWay(), enterpriseCertificationNew.getMVerifiedWay(), enterpriseCertificationNew.getPageModify(), enterpriseCertificationNew.getCompanyPrincipalType(), enterpriseCertificationNew.getReturnUrl(),
                    enterpriseCertificationNew.getNotifyUrl(), enterpriseCertificationNew.getResultType(), enterpriseCertificationNew.getCertFlag());


            //2. 保存数据
            if (JSONObject.parseObject(result).getIntValue("code") == 1) {

                String data = JSONObject.parseObject(result).getString("data");
                enterpriseCertificationNew.setTransactionNo(JSONObject.parseObject(data).getString("transactionNo"))
                        .setVerifyUrl(JSONObject.parseObject(data).getString("url"))
                        .setAuthenticationType(2)
                        .setCreateBy(user.getId());
                this.save(enterpriseCertificationNew);

                //2.1 更新用户
                user.setAuthenticationType(2);
                userService.updateById(user);

                return new ResultUtil<>().setSuccessMsg("申请企业实名认证成功！");
            } else {

                return new ResultUtil<>().setErrorMsg(JSONObject.parseObject(result).getIntValue("code"), JSONObject.parseObject(result).getString("msg"));
            }
        }else {

            //2.更新
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


            //2. 保存数据
            if (JSONObject.parseObject(result).getIntValue("code") == 1) {

                EnterpriseCertification enterpriseCertification = this.getById(companyVerifyDto.getId());
                ToolUtil.copyProperties(companyVerifyDto, enterpriseCertification);

                String data = JSONObject.parseObject(result).getString("data");
                enterpriseCertification.setTransactionNo(JSONObject.parseObject(data).getString("transactionNo"))
                        .setVerifyUrl(JSONObject.parseObject(data).getString("url"))
                        .setUpdateBy(user.getId());
                this.updateById(enterpriseCertification);
                return new ResultUtil<>().setSuccessMsg("修改企业实名认证成功！");
            } else {

                return new ResultUtil<>().setErrorMsg(JSONObject.parseObject(result).getIntValue("code"), JSONObject.parseObject(result).getString("msg"));
            }
        }


    }
}