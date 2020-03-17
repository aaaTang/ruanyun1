package cn.ruanyun.backInterface.modules.business.classification.serviceimpl;

import cn.ruanyun.backInterface.modules.business.classification.VO.AppCategoryListVO;
import cn.ruanyun.backInterface.modules.business.classification.VO.AppCategoryVO;
import cn.ruanyun.backInterface.modules.business.classification.VO.BackAreaListVO;
import cn.ruanyun.backInterface.modules.business.classification.VO.BackAreaVO;
import cn.ruanyun.backInterface.modules.business.classification.mapper.ClassificationMapper;
import cn.ruanyun.backInterface.modules.business.classification.pojo.Classification;
import cn.ruanyun.backInterface.modules.business.classification.service.IClassificationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;

import javax.annotation.Resource;


/**
 * 分类管理接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IClassificationServiceImpl extends ServiceImpl<ClassificationMapper, Classification> implements IClassificationService {

       @Autowired
       private SecurityUtil securityUtil;

       @Resource
       private ClassificationMapper classificationMapper;

    @Override
       public void insertOrderUpdateClassification(Classification classification) {

           if (ToolUtil.isEmpty(classification.getCreateBy())) {

               classification.setCreateBy(securityUtil.getCurrUser().getId());
           }else {

               classification.setUpdateBy(securityUtil.getCurrUser().getId());
           }
                   Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(classification)))
                           .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                           .toFuture().join();
       }

      @Override
      public void removeClassification(String ids) {

          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * 获取APP分类集合一级加二级
     */
    @Override
    public List<AppCategoryListVO> getAppCategoryList() {

       /* List<Classification> list = this.list(new QueryWrapper<Classification>().lambda().eq(Classification::getIsParent,true));
        List<AppCategoryListVO> appCategoryListVOList = list.parallelStream().map(classic->{
            AppCategoryListVO appCategoryListVO = new AppCategoryListVO();
            ToolUtil.copyProperties(classic,appCategoryListVO);
            return appCategoryListVO;
        }).collect(Collectors.toList());*/

        List<Classification> list = this.list(new QueryWrapper<Classification>().lambda().eq(Classification::getIsParent,true));
        List<AppCategoryListVO> appCategoryListVOList  =new ArrayList<>();
        for (Classification classification : list) {

            List<AppCategoryVO> categoryVOS = classificationMapper.getAppCategoryList(classification.getId());//获取二级分类

            AppCategoryListVO appCategoryVO =new AppCategoryListVO();
            appCategoryVO.setId(classification.getId())
                        .setTitle(classification.getTitle())
                        .setCategoryVOS(categoryVOS);

            appCategoryListVOList.add(appCategoryVO);
        }

        return appCategoryListVOList;
    }


    /**
     * 按一级分类ID查询二级分类
     */
    @Override
    public List<AppCategoryVO> getSecondLevelCategory(String ids){
        List<AppCategoryVO> categoryVOS = classificationMapper.getAppCategoryList(ids);//获取二级分类
        return categoryVOS;
    }

    /**
     * 后端查询一级及二级
     */
    @Override
    public  List<BackAreaListVO> getCategoryList(){

        List<Classification> list = this.list(new QueryWrapper<Classification>().lambda().eq(Classification::getIsParent,true));
        List<BackAreaListVO> backAreaListVOS  =new ArrayList<>();
        for (Classification classification : list) {

            List<BackAreaVO> backAreaVOS = classificationMapper.getCategoryList(classification.getId());//获取二级分类

            BackAreaListVO backAreaListVO =new BackAreaListVO();
            backAreaListVO.setId(classification.getId()).setTitle(classification.getTitle()).setPic(classification.getPic())
                    .setIsParent(classification.getIsParent()).setSortOrder(classification.getSortOrder())
                    .setStatus(classification.getStatus()).setParentId(classification.getParentId())
                    .setBackAreaVOS(backAreaVOS);

            backAreaListVOS.add(backAreaListVO);
        }
        return backAreaListVOS;

    }

    @Override
    public String getClassificationName(String id) {
        String name = "";
        Classification classification = super.getById(id);
        if (ToolUtil.isNotEmpty(classification)) {
            if (ToolUtil.isNotEmpty(classification.getParentId()) && !"0".equals(classification.getParentId()) && !"1".equals(classification.getParentId())) {
                name = spliceName(name, id);
            } else {
                name = name.concat(classification.getTitle());
            }
        }
        return name;
    }

    //拼接详细地址
    public String spliceName(String name, String id) {
        Classification classification = super.getById(id);
        if (ToolUtil.isNotEmpty(classification.getParentId()) && !"0".equals(classification.getParentId()) && !"1".equals(classification.getParentId())) {
            name = classification.getTitle() + name;
            name = spliceName(name, classification.getParentId());
        } else {
            name = (classification.getTitle()).concat(name);
        }
        return name;
    }
}