package cn.ruanyun.backInterface.modules.fadada.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.fadada.dto.PersonVerifyDto;
import cn.ruanyun.backInterface.modules.fadada.mapper.PersonalCertificateMapper;
import cn.ruanyun.backInterface.modules.fadada.pojo.EnterpriseCertification;
import cn.ruanyun.backInterface.modules.fadada.pojo.PersonalCertificate;
import cn.ruanyun.backInterface.modules.fadada.service.IEnterpriseCertificationService;
import cn.ruanyun.backInterface.modules.fadada.service.IPersonalCertificateService;
import cn.ruanyun.backInterface.modules.fadada.service.IfadadaService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fadada.sdk.client.authForfadada.FindCertInfo;
import com.fadada.sdk.client.authForfadada.GetPersonVerifyUrl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
 * 个人认证接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IPersonalCertificateServiceImpl extends ServiceImpl<PersonalCertificateMapper, PersonalCertificate> implements IPersonalCertificateService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IUserService userService;

    @Autowired
    private IEnterpriseCertificationService enterpriseCertificationService;

    @Autowired
    private IfadadaService ifadadaService;


    /**
     * 提交个人认证信息
     *
     * @param personVerifyDto personVerifyDto
     */
    @Override
    public Result<Object> commitOrUpdatePersonalCertificate(PersonVerifyDto personVerifyDto) {


        User user = userService.getById(securityUtil.getCurrUser().getId());

        if (StringUtils.isEmpty(user.getCustomerId())) {

            user.setCustomerId(ifadadaService.accountRegister(user.getId(), "1"));
        }

        GetPersonVerifyUrl personVerify = new GetPersonVerifyUrl(CommonConstant.F_APP_ID, CommonConstant.F_APP_SECRET, CommonConstant.F_VERSION, CommonConstant.F_HOST);

        //首次提交
        if (StringUtils.isEmpty(personVerifyDto.getId())) {

            //1.1 提交个人认证api
            PersonalCertificate personalCertificateNew = new PersonalCertificate();
            ToolUtil.copyProperties(personVerifyDto, personalCertificateNew);
            String result = personVerify.invokePersonVerifyUrl(user.getCustomerId(), personalCertificateNew.getVerifiedWay(), personalCertificateNew.getPageModify(),
                    personalCertificateNew.getNotifyUrl(), personalCertificateNew.getReturnUrl(), personalCertificateNew.getCustomerName(), personalCertificateNew.getCustomerIdentType(), personalCertificateNew.getCustomerIdentNo()
                    , personalCertificateNew.getMobile(), personalCertificateNew.getIdentFrontPath(), personalCertificateNew.getResultType(), personalCertificateNew.getCertFlag());


            //2. 保存数据
            if (JSONObject.parseObject(result).getIntValue("code") == 1) {

                String data = JSONObject.parseObject(result).getString("data");
                personalCertificateNew.setTransactionNo(JSONObject.parseObject(data).getString("transactionNo"))
                        .setVerifyUrl(JSONObject.parseObject(data).getString("url"))
                        .setAuthenticationType(1)
                        .setCreateBy(user.getId());
                this.save(personalCertificateNew);

                //2.1 更新用户
                user.setAuthenticationType(1);
                userService.updateById(user);

                return new ResultUtil<>().setSuccessMsg("申请个人实名认证成功！");
            } else {

                return new ResultUtil<>().setErrorMsg(JSONObject.parseObject(result).getIntValue("code"), JSONObject.parseObject(result).getString("msg"));
            }
        }else {

            //2.更新

            String result = personVerify.invokePersonVerifyUrl(user.getCustomerId(), personVerifyDto.getVerifiedWay(), personVerifyDto.getPageModify(),
                    personVerifyDto.getNotifyUrl(), personVerifyDto.getReturnUrl(), personVerifyDto.getCustomerName(), personVerifyDto.getCustomerIdentType(), personVerifyDto.getCustomerIdentNo()
                    , personVerifyDto.getMobile(), personVerifyDto.getIdentFrontPath(), personVerifyDto.getResultType(), personVerifyDto.getCertFlag());

            //2. 保存数据
            if (JSONObject.parseObject(result).getIntValue("code") == 1) {

                PersonalCertificate personalCertificate = this.getById(personVerifyDto.getId());
                ToolUtil.copyProperties(personVerifyDto, personalCertificate);

                String data = JSONObject.parseObject(result).getString("data");
                personalCertificate.setTransactionNo(JSONObject.parseObject(data).getString("transactionNo"))
                        .setVerifyUrl(JSONObject.parseObject(data).getString("url"));
                this.updateById(personalCertificate);
                return new ResultUtil<>().setSuccessMsg("更新个人实名认证成功！");
            } else {

                return new ResultUtil<>().setErrorMsg(JSONObject.parseObject(result).getIntValue("code"), JSONObject.parseObject(result).getString("msg"));
            }
        }
    }

    /**
     * 获取个人或者企业的认证信息
     *
     * @return PersonalCertificate
     */
    @Override
    public Result<Object> getCertificate() {

        User user = userService.getById(securityUtil.getCurrUser().getId());

        if (ToolUtil.isEmpty(user.getAuthenticationType())) {

            return new ResultUtil<>().setErrorMsg(203, "暂无认证！");
        }else if (user.getAuthenticationType() == 1) {

            return new ResultUtil<>().setData(this.getOne(Wrappers.<PersonalCertificate>lambdaQuery()
                    .eq(PersonalCertificate::getCreateBy, user.getId())), "获取个人实名认证信息成功！");

        }else {

            return new ResultUtil<>().setData(enterpriseCertificationService.getOne(Wrappers.<EnterpriseCertification>lambdaQuery()
            .eq(EnterpriseCertification::getCreateBy, user.getId())), "获取企业实名认证信息成功！");
        }

    }

    @Override
    public Result<Object> findCertInfo() {

        FindCertInfo personCertInfo = new FindCertInfo(CommonConstant.F_APP_ID, CommonConstant.F_APP_SECRET, CommonConstant.F_VERSION, CommonConstant.F_HOST);
        User user = userService.getById(securityUtil.getCurrUser().getId());

        if (ToolUtil.isEmpty(user.getAuthenticationType())) {

            return new ResultUtil<>().setErrorMsg(204, "暂时没有申请认证");
        }

        if (user.getAuthenticationType() == 1) {

           return Optional.ofNullable(this.getOne(Wrappers.<PersonalCertificate>lambdaQuery()
            .eq(PersonalCertificate::getCreateBy, user.getId())))
            .map(personalCertificate -> {

                String result = personCertInfo.invokeFindPersonCert(personalCertificate.getTransactionNo(),"1");

                if (JSONObject.parseObject(result).getIntValue("code") == 1) {

                    String data = JSONObject.parseObject(result).getString("data");
                    return new ResultUtil<>().setData(JSONObject.parseObject(data), "获取实名认证信息成功！");
                } else {

                    return new ResultUtil<>().setErrorMsg(JSONObject.parseObject(result).getIntValue("code"), JSONObject.parseObject(result).getString("msg"));
                }

            }).orElse(new ResultUtil<>().setErrorMsg(203, "暂时没有申请个人实名认证"));
        }else if (user.getAuthenticationType() == 2) {

            return Optional.ofNullable(enterpriseCertificationService.getOne(Wrappers.<EnterpriseCertification>lambdaQuery()
            .eq(EnterpriseCertification::getCreateBy, user.getId())))
            .map(enterpriseCertification -> {

                String result = personCertInfo.invokeFindPersonCert(enterpriseCertification.getTransactionNo(),"2");

                if (JSONObject.parseObject(result).getIntValue("code") == 1) {

                    String data = JSONObject.parseObject(result).getString("data");
                    return new ResultUtil<>().setData(JSONObject.parseObject(data), "获取实名认证信息成功！");
                } else {

                    return new ResultUtil<>().setErrorMsg(JSONObject.parseObject(result).getIntValue("code"), JSONObject.parseObject(result).getString("msg"));
                }

            }).orElse(new ResultUtil<>().setErrorMsg(203, "暂时没有申请企业实名认证"));

        }else {

            return new ResultUtil<>().setErrorMsg(204, "暂时没有申请认证！");
        }

    }


}