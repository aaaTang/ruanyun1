package cn.ruanyun.backInterface.modules.fadada.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.goodCategory.service.IGoodCategoryService;
import cn.ruanyun.backInterface.modules.fadada.dto.ExtSignDto;
import cn.ruanyun.backInterface.modules.fadada.dto.UploaddocsDto;
import cn.ruanyun.backInterface.modules.fadada.mapper.ElectronicContractMapper;
import cn.ruanyun.backInterface.modules.fadada.pojo.ElectronicContract;
import cn.ruanyun.backInterface.modules.fadada.service.IElectronicContractService;
import cn.ruanyun.backInterface.modules.fadada.vo.ElectronicContractVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fadada.sdk.client.FddClientBase;
import com.fadada.sdk.client.FddClientExtra;
import com.fadada.sdk.client.request.ExtsignReq;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


/**
 * 电子合同接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IElectronicContractServiceImpl extends ServiceImpl<ElectronicContractMapper, ElectronicContract> implements IElectronicContractService {


    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private IGoodCategoryService goodCategoryService;

    @Autowired
    private IUserService userService;

    @Override
    public Result<Object> insertElectronicContract(UploaddocsDto uploaddocsDto) {


        User user = userService.getById(securityUtil.getCurrUser().getId());

        FddClientBase base = new FddClientBase(CommonConstant.F_APP_ID, CommonConstant.F_APP_SECRET, CommonConstant.F_VERSION, CommonConstant.F_HOST);

        List<UploaddocsDto> uploaddocsDtos = Lists.newArrayList();

        for (int i = 0 ; i < uploaddocsDto.getUploadCount(); i++) {

            uploaddocsDto.setContractId(CommonUtil.getRandomNum());
            uploaddocsDtos.add(uploaddocsDto);

        }

        uploaddocsDtos.parallelStream().forEach(uploaddocsDtoUse -> {

            try {

                base.invokeUploadDocs(uploaddocsDtoUse.getContractId(), uploaddocsDtoUse.getDocTitle(),
                        /*MultipartFileToFile.multipartFileToFile(uploaddocsDtoUse.getPdfFile())*/null, uploaddocsDtoUse.getDocUrl(), uploaddocsDtoUse.getDocType());


                //异步添加数据到数据库
                CompletableFuture.runAsync(() -> {

                    ElectronicContract electronicContract = new ElectronicContract();
                    ToolUtil.copyProperties(uploaddocsDto, electronicContract);
                    electronicContract.setCreateBy(user.getId());
                    this.save(electronicContract);

                }).join();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return new ResultUtil<>().setSuccessMsg("上传成功！");

    }

    /**
     * 移除电子合同
     *
     * @param ids ids
     * @return Object
     */
    @Override
    public Result<Object> removeElectronicContract(String ids) {

        this.removeByIds(ToolUtil.splitterStr(ids));

        return new ResultUtil<>().setSuccessMsg("移除成功！");
    }

    /**
     * 获取电子合同列表
     *
     * @param pageVo 分页
     * @return 电子合同vo
     */
    @Override
    public Result<DataVo<ElectronicContractVo>> getElectronicContractList(PageVo pageVo) {


        Page<ElectronicContract> electronicContractPage = this.page(PageUtil.initMpPage(pageVo), Wrappers.<ElectronicContract>lambdaQuery()
        .orderByDesc(ElectronicContract::getCreateTime));

        if (ToolUtil.isEmpty(electronicContractPage.getRecords())) {

            return new ResultUtil<DataVo<ElectronicContractVo>>().setErrorMsg(201, "暂无数据");
        }

        DataVo<ElectronicContractVo> result = new DataVo<>();

        result.setDataResult(electronicContractPage.getRecords().parallelStream().flatMap(electronicContract -> {

            ElectronicContractVo electronicContractVo = new ElectronicContractVo();
            ToolUtil.copyProperties(electronicContract, electronicContractVo);
            electronicContractVo.setGoodCategoryName(goodCategoryService.getGoodCategoryName(electronicContract.getGoodCategoryId()));

            return Stream.of(electronicContractVo);

        }).collect(Collectors.toList())).setTotalSize(electronicContractPage.getTotal())
                .setCurrentPageNum(electronicContractPage.getCurrent())
                .setTotalPage(electronicContractPage.getPages());

        return new ResultUtil<DataVo<ElectronicContractVo>>().setData(result, "获取合同模板成功！");
    }

    @Override
    public String getContractIdByRandom() {

        return Optional.ofNullable(this.getOne(Wrappers.<ElectronicContract>lambdaQuery()
                .eq(ElectronicContract::getGoodCategoryId, securityUtil.getCurrUser().getClassId())
                .eq(ElectronicContract::getContractFiling, BooleanTypeEnum.NO)
                .orderByDesc(ElectronicContract::getCreateTime)
                .last("limit 1")))
                .map(ElectronicContract::getContractId)
                .orElse(null);
    }

    @Override
    public void updateContractFiling(String contractId) {

        Optional.ofNullable(this.getOne(Wrappers.<ElectronicContract>lambdaQuery()
        .eq(ElectronicContract::getContractId, contractId)))
        .ifPresent(electronicContract -> {

            electronicContract.setContractFiling(BooleanTypeEnum.YES);
            this.updateById(electronicContract);
        });
    }

    @Override
    public Result<Object> viewContract(String id) {

        FddClientExtra extra = new FddClientExtra(CommonConstant.F_APP_ID,CommonConstant.F_APP_SECRET,CommonConstant.F_VERSION,CommonConstant.F_HOST);

        return Optional.ofNullable(this.getById(id)).map(electronicContract -> {

            String viewUrl= extra.invokeViewPdfURL(electronicContract.getContractId());
            return new ResultUtil<>().setData(viewUrl, "获取查看合同链接成功！");

        }).orElse(new ResultUtil<>().setErrorMsg(201, "不存在数据！"));

    }


}