package cn.ruanyun.backInterface.modules.fadada.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.*;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.vo.AppUserVO;
import cn.ruanyun.backInterface.modules.fadada.dto.ExtSignDto;
import cn.ruanyun.backInterface.modules.fadada.mapper.ContractSigningMapper;
import cn.ruanyun.backInterface.modules.fadada.pojo.ContractSigning;
import cn.ruanyun.backInterface.modules.fadada.service.IContractSigningService;
import cn.ruanyun.backInterface.modules.fadada.service.IElectronicContractService;
import cn.ruanyun.backInterface.modules.fadada.vo.ContractSigningVo;
import cn.ruanyun.backInterface.modules.jpush.dto.JpushDto;
import cn.ruanyun.backInterface.modules.jpush.service.IJpushService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fadada.sdk.client.FddClientBase;
import com.fadada.sdk.client.FddClientExtra;
import com.fadada.sdk.client.request.ExtsignReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


/**
 * 合同签署接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IContractSigningServiceImpl extends ServiceImpl<ContractSigningMapper, ContractSigning> implements IContractSigningService {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IElectronicContractService electronicContractService;

    @Autowired
    private IJpushService jpushService;


    @Override
    public Result<ContractSigningVo> getCustomerSigning() {

        return Optional.ofNullable(this.getOne(Wrappers.<ContractSigning>lambdaQuery()
        .eq(ContractSigning::getCreateBy, securityUtil.getCurrUser().getId())))
        .map(contractSigning -> {

            ContractSigningVo contractSigningVo = new ContractSigningVo();
            ToolUtil.copyProperties(contractSigning, contractSigningVo);
            contractSigningVo.setCheck(contractSigning.getCheckStatus().getCode());

            return new ResultUtil<ContractSigningVo>().setData(contractSigningVo, "获取客户手动签署合同数据成功！");
        }).orElse(new ResultUtil<ContractSigningVo>().setErrorMsg(202, "暂未签署合同"));
    }

    /**
     * 手动签署合同
     *
     * @param extsignDto 参数
     * @return Object
     */
    @Override
    public Result<Object> extSign(ExtSignDto extsignDto) {

        //如果用户有审核通过的合同,直接返回查看合同链接
        //如果用户有审核失败合同, 提示审核失败以及审核失败原因字段,旁边有一个按钮是修改合同(修改合同接口)
        //如果用户没有任何审核合同记录，则点开直接是签署合同链接以及默认生成待审核列表

        String userId = securityUtil.getCurrUser().getId();

        //交易号
        String randomNum = CommonUtil.getRandomNum();

        // 签署合同
        FddClientBase base = new FddClientBase(CommonConstant.F_APP_ID,CommonConstant.F_APP_SECRET,CommonConstant.F_VERSION,CommonConstant.F_HOST);

        ExtsignReq req = new ExtsignReq();

        //客户编号
        req.setCustomer_id(securityUtil.getCurrUser().getCustomerId());

        //交易号
        req.setTransaction_id(randomNum);

        //合同编号

        if (ToolUtil.isEmpty(extsignDto.getId())) {

            req.setContract_id(electronicContractService.getContractIdByRandom());
        }else {

            req.setContract_id(Optional.ofNullable(this.getById(extsignDto.getId()))
            .map(ContractSigning::getContractId).orElse(null));
        }

        //合同标题
        req.setDoc_title(extsignDto.getDocTitle());

        //页面跳转url
        req.setReturn_url("www.baidu.com");

        //跳转合同链接
        String signUrl= base.invokeExtSign(req);


        if (ToolUtil.isEmpty(extsignDto.getId())) {

            //生成待审核记录
            ContractSigning contractSigningNew = new ContractSigning();
            contractSigningNew.setContractId(req.getContract_id())
                    .setPartTwoDocTitle(req.getDoc_title())
                    .setPartTwoTransactionId(req.getTransaction_id())
                    .setCreateBy(userId);

            this.save(contractSigningNew);

        }else {

            Optional.ofNullable(this.getById(extsignDto.getId())).ifPresent(contractSigning -> {

                contractSigning.setPartOneTransactionId(req.getTransaction_id())
                        .setPartOneDocTitle(extsignDto.getDocTitle());

                this.updateById(contractSigning);
            });
        }

        return new ResultUtil<>().setData(signUrl, "获取签署合同链接成功！");

    }

    @Override
    public Result<Object> updateExtSign(ExtSignDto extsignDto) {

       return Optional.ofNullable(this.getById(extsignDto.getId())).map(contractSigning -> {

            //交易号
            String randomNum = CommonUtil.getRandomNum();

            // 签署合同
            FddClientBase base = new FddClientBase(CommonConstant.F_APP_ID,CommonConstant.F_APP_SECRET,CommonConstant.F_VERSION,CommonConstant.F_HOST);

            ExtsignReq req = new ExtsignReq();

            //客户编号
            req.setCustomer_id(securityUtil.getCurrUser().getCustomerId());

            //交易号
            req.setTransaction_id(randomNum);

            //合同编号

            req.setContract_id(contractSigning.getContractId());

            //合同标题
            req.setDoc_title(extsignDto.getDocTitle());

            //页面跳转url
            req.setReturn_url("www.baidu.com");

            //跳转合同链接
            String signUrl= base.invokeExtSign(req);


            //修改审核状态
            contractSigning.setCheckStatus(CheckEnum.PRE_CHECK);
            this.updateById(contractSigning);

            return new ResultUtil<>().setData(signUrl, "获取签署url成功！");

        }).orElse(new ResultUtil<>().setErrorMsg(204, "请求错误！"));
    }


    /**
     * 获取审核列表
     *
     * @param pageVo 分页
     * @return ContractSigningVo
     */
    @Override
    public Result<DataVo<ContractSigningVo>> getContractSigningList(PageVo pageVo) {

        Page<ContractSigning> contractSigningPage = this.page(PageUtil.initMpPage(pageVo), Wrappers.<ContractSigning>lambdaQuery()
        .orderByDesc(ContractSigning::getCreateTime));

        if (ToolUtil.isEmpty(contractSigningPage.getRecords())) {

            return new ResultUtil<DataVo<ContractSigningVo>>().setErrorMsg(201, "暂无数据！");
        }

        DataVo<ContractSigningVo> result = new DataVo<>();

        result.setDataResult(contractSigningPage.getRecords().parallelStream().flatMap(contractSigning -> {

            ContractSigningVo contractSigningVo = new ContractSigningVo();
            ToolUtil.copyProperties(contractSigning, contractSigningVo);
            contractSigningVo.setCheck(contractSigning.getCheckStatus().getCode());

            return Stream.of(contractSigningVo);

        }).collect(Collectors.toList())).setTotalSize(contractSigningPage.getTotal())
                .setTotalPage(contractSigningPage.getPages())
                .setCurrentPageNum(contractSigningPage.getCurrent());

        return new ResultUtil<DataVo<ContractSigningVo>>().setData(result, "获取签署合同数据成功！");
    }

    /**
     * 后台审核手动签署合同
     *
     * @param contractSigning contractSigning
     * @return Object
     */
    @Override
    public Result<Object> checkContractSigning(ContractSigning contractSigning) {

       return Optional.ofNullable(this.getById(contractSigning.getId())).map(contractSigningNew -> {

            contractSigningNew.setCheckStatus(contractSigning.getCheckStatus())
                    .setCheckReason(contractSigning.getCheckReason());

            this.updateById(contractSigningNew);

            //异步推送
            CompletableFuture.runAsync(() -> {

                JpushDto jpushDto = new JpushDto();
                jpushDto.setUserId(contractSigningNew.getCreateBy())
                        .setPlatformType(PlatformTypeEnum.ALL)
                        .setAudienceType(AudienceTypeEnum.TAG)
                        .setPushType(PushTypeEnum.ELECTRONIC_CONTRACT)
                        .setTitle("电子合同审核通知")
                        .setContent("你的电子合同" + contractSigning.getCheckStatus() + ", 审核原因为:" + contractSigning.getCheckReason());

                jpushService.pushArticleToUser(jpushDto);

            }).join();

            return new ResultUtil<>().setSuccessMsg("操作成功！");
        }).orElse(new ResultUtil<>().setErrorMsg(202, "操作失败！不存在此记录！"));
    }


    /**
     * 合同归档
     *
     * @param id id
     * @return Object
     */
    @Override
    public Result<Object> contractFiling(String id) {


        FddClientBase base = new FddClientBase(CommonConstant.F_APP_ID,CommonConstant.F_APP_SECRET,CommonConstant.F_VERSION,CommonConstant.F_HOST);

        return Optional.ofNullable(this.getById(id)).map(contractSigning -> {

            String result = base.invokeContractFilling(contractSigning.getContractId());

            if (JSONObject.parseObject(result).getIntValue("code") == 1) {

                //异步更改合同归档状态
                CompletableFuture.runAsync(() -> electronicContractService.updateContractFiling(contractSigning.getContractId())).join();

                return new ResultUtil<>().setSuccessMsg("归档成功！");
            }else {

                return new ResultUtil<>().setErrorMsg(203, JSONObject.parseObject(result).getString("msg"));
            }

        }).orElse(new ResultUtil<>().setErrorMsg(201, "不存在数据！"));
    }


    /**
     * 查看合同
     *
     * @param id 法大大id
     * @return Object
     */
    @Override
    public Result<Object> viewContract(String id) {

        FddClientExtra extra = new FddClientExtra(CommonConstant.F_APP_ID,CommonConstant.F_APP_SECRET,CommonConstant.F_VERSION,CommonConstant.F_HOST);

        if (ToolUtil.isNotEmpty(id)) {

            return Optional.ofNullable(this.getById(id)).map(contractSigning -> {

                String viewUrl= extra.invokeViewPdfURL(contractSigning.getContractId());
                return new ResultUtil<>().setData(viewUrl, "获取查看合同链接成功！");

            }).orElse(new ResultUtil<>().setErrorMsg(201, "当前数据为空！"));
        }else {

            if (ToolUtil.isEmpty(securityUtil.getCurrUser().getContractId())) {

                return new ResultUtil<>().setErrorMsg(204, "暂未签署合同！");
            }

            return new ResultUtil<>().setData(extra.invokeViewPdfURL(securityUtil.getCurrUser().getContractId()), "获取查看合同链接成功！");
        }
    }

    /**
     * 下载合同
     *
     * @param id id
     * @return Object
     */
    @Override
    public Result<Object> downLoadContract(String id) {

        FddClientExtra extra = new FddClientExtra(CommonConstant.F_APP_ID,CommonConstant.F_APP_SECRET,CommonConstant.F_VERSION,CommonConstant.F_HOST);

        if (ToolUtil.isNotEmpty(id)) {

            return Optional.ofNullable(this.getById(id)).map(contractSigning -> {

                String downloadUrl = extra.invokeDownloadPdf(contractSigning.getContractId());
                return new ResultUtil<>().setData(downloadUrl, "获取下载合同链接成功！");

            }).orElse(new ResultUtil<>().setErrorMsg(201, "当前数据为空！"));

        }else {

            if (ToolUtil.isEmpty(securityUtil.getCurrUser().getContractId())) {

                return new ResultUtil<>().setErrorMsg(204, "暂未签署合同！");
            }

            return new ResultUtil<>().setData(extra.invokeDownloadPdf(securityUtil.getCurrUser().getContractId()), "获取下载合同链接成功！");
        }

    }
}