package cn.ruanyun.backInterface.modules.fadada.serviceimpl;

import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.fadada.dto.*;
import cn.ruanyun.backInterface.modules.fadada.mapper.FadadaMapper;
import cn.ruanyun.backInterface.modules.fadada.pojo.Fadada;
import cn.ruanyun.backInterface.modules.fadada.service.IfadadaService;
import cn.ruanyun.backInterface.modules.fadada.vo.FadadaVo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fadada.sdk.client.FddClientBase;
import com.fadada.sdk.client.FddClientExtra;
import com.fadada.sdk.client.authForfadada.ApplyCert;
import com.fadada.sdk.client.authForfadada.GetCompanyVerifyUrl;
import com.fadada.sdk.client.authForfadada.GetPersonVerifyUrl;
import com.fadada.sdk.client.authForfadada.model.AgentInfoINO;
import com.fadada.sdk.client.authForfadada.model.BankInfoINO;
import com.fadada.sdk.client.authForfadada.model.CompanyInfoINO;
import com.fadada.sdk.client.authForfadada.model.LegalInfoINO;
import com.fadada.sdk.client.request.ExtsignReq;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author Administrator
 */
@Slf4j
@Service
@Transactional
public class IfadadaServiceImpl extends ServiceImpl<FadadaMapper, Fadada> implements IfadadaService {


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

        return new ResultUtil<>().setData(result, "获取企业使命认证地址成功！");

    }

    /**
     * 获取个人实名认证地址
     *
     * @param personVerifyDto 参数
     * @return Object
     */
    @Override
    public Result<Object> getPersonVerifyUrl(PersonVerifyDto personVerifyDto) {

        GetPersonVerifyUrl personVerify = new GetPersonVerifyUrl(APP_ID, APP_SECRET, VERSION, HOST);

        String result = personVerify.invokePersonVerifyUrl(securityUtil.getCurrUser().getCustomerId(), personVerifyDto.getVerifiedWay(), personVerifyDto.getPageModify(),
                personVerifyDto.getNotifyUrl(), personVerifyDto.getReturnUrl(), personVerifyDto.getCustomerName(), personVerifyDto.getCustomerIdentType(),personVerifyDto.getCustomerIdentNo()
        ,personVerifyDto.getMobile(), personVerifyDto.getIdentFrontPath(), personVerifyDto.getResultType(), personVerifyDto.getCertFlag());

        log.info(result);

        JSONObject jsonResult = JSONObject.parseObject(result);
        User user = userService.getById(securityUtil.getCurrUser().getId());
        user.setTransactionNo(jsonResult.getJSONObject("data").getString("transactionNo"));

        userService.updateById(user);

        return new ResultUtil<>().setSuccessMsg("实名认证成功！");
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

    @Override
    public Result<Object> uploadDocs(UploaddocsDto uploaddocsDto) {

        FddClientBase base = new FddClientBase(APP_ID, APP_SECRET, VERSION, HOST );

        List<UploaddocsDto> uploaddocsDtos = Lists.newArrayList();

        for (int i = 0 ; i < uploaddocsDto.getUploadCount(); i++) {

            uploaddocsDto.setContractId(CommonUtil.getRandomNum());
            uploaddocsDtos.add(uploaddocsDto);

        }

        uploaddocsDtos.parallelStream().forEach(uploaddocsDtoUse -> {

            try {

               base.invokeUploadDocs(uploaddocsDtoUse.getContractId(), uploaddocsDtoUse.getDocTitle(),
                        MultipartFileToFile.multipartFileToFile(uploaddocsDtoUse.getPdfFile()), uploaddocsDtoUse.getDocUrl(), uploaddocsDtoUse.getDocType());


               //异步添加数据到数据库
                CompletableFuture.runAsync(() -> {

                    Fadada fadada = new Fadada();
                    ToolUtil.copyProperties(uploaddocsDto, fadada);
                    this.save(fadada);

                }).join();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return new ResultUtil<>().setSuccessMsg("上传成功！");
    }


    @Override
    public Result<Object> extSign(ExtSignDto extSignDto) {


        FddClientBase base = new FddClientBase(APP_ID, APP_SECRET, VERSION, HOST);

        ExtsignReq req = new ExtsignReq();

        if (ToolUtil.isNotEmpty(extSignDto.getId())) {

            return Optional.ofNullable(this.getById(extSignDto.getId())).map(fadada -> {

                //客户编号
                req.setCustomer_id(securityUtil.getCurrUser().getCustomerId());

                //交易号
                req.setTransaction_id(CommonUtil.getRandomNum());

                //合同编号
                req.setContract_id(fadada.getContractId());

                //文档标题
                req.setDoc_title(extSignDto.getDocTitle());

                //页面跳转 URL（签署结果同步通知）
                req.setReturn_url("");

                //回调地址
                String signUrl = base.invokeExtSign(req);


                //修改信息
                fadada.setPartOneDocTitle(extSignDto.getDocTitle());
                this.updateById(fadada);

                return new ResultUtil<>().setData(signUrl, "获取回调签署地址成功！");
            }).orElse(new ResultUtil<>().setErrorMsg(201, "暂无资源数据！"));

        }

        // 查询最近的一条没有被使用的合同
        return Optional.ofNullable(this.getOne(Wrappers.<Fadada>lambdaQuery().eq(Fadada::getContractFiling, BooleanTypeEnum.NO)
                .orderByDesc(Fadada::getCreateTime).last("limit 1")))
                .map(fadada -> {

                    User user = userService.getById(securityUtil.getCurrUser().getId());

                    //客户编号
                    req.setCustomer_id(securityUtil.getCurrUser().getCustomerId());

                    //交易号
                    req.setTransaction_id(CommonUtil.getRandomNum());

                    //合同编号
                    req.setContract_id(fadada.getContractId());

                    //文档标题
                    req.setDoc_title(extSignDto.getDocTitle());

                    //页面跳转 URL（签署结果同步通知）
                    req.setReturn_url("");

                    //回调地址
                    String signUrl = base.invokeExtSign(req);

                    //更新法大大表
                    fadada.setPartTwoDocTitle(extSignDto.getDocTitle());
                    fadada.setPartTwoExtSignUserId(user.getId());
                    this.updateById(fadada);

                    //更新用户表
                    user.setContractId(fadada.getContractId());
                    userService.updateById(user);

                    return new ResultUtil<>().setData(signUrl, "获取回调签署地址成功！");
                }).orElse(new ResultUtil<>().setErrorMsg(203, "暂无可用合同资源"));
    }

    @Override
    public Result<Object> viewContract(String id) {

        FddClientExtra extra = new FddClientExtra(APP_ID, APP_SECRET, VERSION, HOST);

        if (ToolUtil.isNotEmpty(id)) {

           return Optional.ofNullable(this.getById(id)).map(fadada -> {

                String viewUrl= extra.invokeViewPdfURL(fadada.getContractId());
                return new ResultUtil<>().setData(viewUrl, "获取查看合同链接成功！");

            }).orElse(new ResultUtil<>().setErrorMsg(201, "当前数据为空！"));
        }else {

            User user = userService.getById(id);

            if (ToolUtil.isEmpty(user.getContractId())) {

                return new ResultUtil<>().setErrorMsg(204, "暂未签署合同！");
            }

            return new ResultUtil<>().setData(extra.invokeViewPdfURL(user.getContractId()), "获取查看合同链接成功！");
        }


    }

    @Override
    public Result<Object> downLoadContract(String id) {

        FddClientExtra extra = new FddClientExtra(APP_ID, APP_SECRET, VERSION, HOST);

        if (ToolUtil.isNotEmpty(id)) {

            return Optional.ofNullable(this.getById(id)).map(fadada -> {

                String downloadUrl = extra.invokeDownloadPdf(fadada.getContractId());
                return new ResultUtil<>().setData(downloadUrl, "获取下载合同链接成功！");

            }).orElse(new ResultUtil<>().setErrorMsg(201, "当前数据为空！"));

        }else {

            User user = userService.getById(id);

            if (ToolUtil.isEmpty(user.getContractId())) {

                return new ResultUtil<>().setErrorMsg(204, "暂未签署合同！");
            }

            return new ResultUtil<>().setData(extra.invokeDownloadPdf(user.getContractId()), "获取下载合同链接成功！");
        }
    }

    @Override
    public Result<Object> contractFiling(String id) {

        FddClientBase base = new FddClientBase(APP_ID, APP_SECRET, VERSION, HOST);

       return Optional.ofNullable(this.getById(id)).map(fadada -> {

            String result = base.invokeContractFilling(fadada.getContractId());

            fadada.setContractFiling(BooleanTypeEnum.YES);
            this.updateById(fadada);

            return new ResultUtil<>().setData(JSONObject.parseObject(result));
        }).orElse(new ResultUtil<>().setErrorMsg(201, "不存在数据！"));
    }

    /**
     * 获取列表
     *
     * @param pageVo 分页参数
     * @return Page<Fadada>
     */
    @Override
    public Result<DataVo<FadadaVo>> getFadadaList(PageVo pageVo) {

        Page<Fadada> fadadaPage = this.page(PageUtil.initMpPage(pageVo), Wrappers.<Fadada>lambdaQuery()
        .orderByDesc(Fadada::getCreateTime));

        if (ToolUtil.isEmpty(fadadaPage.getRecords())) {

            return new ResultUtil<DataVo<FadadaVo>>().setErrorMsg(201, "暂无数据！");
        }

        DataVo<FadadaVo> result = new DataVo<>();

        result.setDataResult(fadadaPage.getRecords().parallelStream().flatMap(fadada -> {

            FadadaVo fadadaVo = new FadadaVo();

            ToolUtil.copyProperties(fadada, fadadaVo);

            fadadaVo.setPartTwoExtSignName( Optional.ofNullable(userService.getById(fadada.getPartTwoExtSignUserId()))
                    .map(User::getNickName).orElse("-"));

            return Stream.of(fadadaVo);

        }).collect(Collectors.toList())).setTotalSize(fadadaPage.getSize())
                .setCurrentPageNum(fadadaPage.getCurrent()).setTotalPage(fadadaPage.getTotal());

        return new ResultUtil<DataVo<FadadaVo>>().setData(result, "获取合同数据成功！");
    }
}
