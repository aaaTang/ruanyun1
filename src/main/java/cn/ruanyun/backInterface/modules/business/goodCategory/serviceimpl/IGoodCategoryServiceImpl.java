package cn.ruanyun.backInterface.modules.business.goodCategory.serviceimpl;

import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.RedisUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.goodCategory.VO.GoodCategoryListVO;
import cn.ruanyun.backInterface.modules.business.goodCategory.VO.GoodCategorySonVO;
import cn.ruanyun.backInterface.modules.business.goodCategory.VO.GoodCategoryVO;
import cn.ruanyun.backInterface.modules.business.goodCategory.VO.StoreCategoryVO;
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import cn.ruanyun.backInterface.modules.business.goodCategory.service.IGoodCategoryService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 商品分类接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IGoodCategoryServiceImpl extends ServiceImpl<GoodCategoryMapper, GoodCategory> implements IGoodCategoryService {

    /**
     * 插入分类
     *
     * @param goodCategory
     */
    @Override
    public void insertGoodCategory(GoodCategory goodCategory) {
        CompletableFuture.allOf(

                //插入分类
                CompletableFuture.runAsync(() -> {

                    //1如果添加的有父类id，则更新分类的是否有节点字段
                    //2如果没有则设置为0
                    if (ToolUtil.isNotEmpty(goodCategory.getParentId())) {

                        GoodCategory goodCategoryParent = this.getById(goodCategory.getParentId());
                        goodCategoryParent.setIsParent(true);
                        this.updateById(goodCategoryParent);

                    }else {
                        goodCategory.setParentId(CommonConstant.PARENT_ID);
                    }

                    //3.插入分类数据
                    this.save(goodCategory);
                })
        );

    }

    /**
     * 删除分类
     *
     * @param id
     */
    @Override
    public void deleteGoodCategory(String id) {
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> this.removeById(id))
        );
    }

    /**
     * 修改
     *
     * @param goodCategory
     */
    @Override
    public void updateGoodCategory(GoodCategory goodCategory) {
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {

                    GoodCategory goodCategoryOld = this.getById(goodCategory.getId());
                    ToolUtil.copyProperties(goodCategory,goodCategoryOld);
                    this.updateById(goodCategoryOld);

                })
        );
    }

    /**
     * 获取分类列表
     *
     * @param pid
     * @return
     */
    @Override
    public List<GoodCategoryVO> getGoodCategoryList(String pid) {

        //1.获取一级分类
         CompletableFuture<Optional<String>> parentId = CompletableFuture.supplyAsync(() -> Optional.ofNullable(pid)
        , ThreadPoolUtil.getPool());

        //2.获取封装之前的数据
        CompletableFuture<Optional<List<GoodCategory>>> goodCategorys = parentId.thenApplyAsync(optionalS -> optionalS
            .map(optional -> Optional.ofNullable(goodCategoryList(optional))).orElseGet(() ->
                        Optional.ofNullable(goodCategoryList(CommonConstant.PARENT_ID))),ThreadPoolUtil.getPool());

        //3.获取封装之后的数据
        CompletableFuture<List<GoodCategoryVO>> goodCategoryVOs = goodCategorys.thenApplyAsync(goodCategories ->
            goodCategories.map(goodCategorys1 -> goodCategorys1.parallelStream().flatMap(goodCategory -> {
                GoodCategoryVO goodCategoryVO = new GoodCategoryVO();
                ToolUtil.copyProperties(goodCategory,goodCategoryVO);
                return Stream.of(goodCategoryVO);
            }).collect(Collectors.toList())).orElse(null),ThreadPoolUtil.getPool());

        return goodCategoryVOs.join();
    }

    /**
     * 获取分类列表
     * @param pid
     * @return
     */
    public List<GoodCategory> goodCategoryList(String pid) {

        return ToolUtil.setListToNul(this.list(Wrappers.<GoodCategory>lambdaQuery()
            .eq(GoodCategory::getParentId,pid).orderByAsc(GoodCategory::getCreateTime)));
    }


    /**
     * 获取app分类列表
     *
     * @return
     */
    @Override
    public List<GoodCategoryListVO> getAppGoodCategoryList() {


        //1.先分区
        CompletableFuture<Optional<Map<String,List<GoodCategory>>>> goodCategoryGroup = CompletableFuture.supplyAsync(() ->
                Optional.ofNullable(getGroupCategory()));

        //2.组装数据
        CompletableFuture<List<GoodCategoryListVO>> goodCategoryList = goodCategoryGroup.thenApplyAsync(goodCategoryGroups ->
                goodCategoryGroups.map(goodCategoryGroups1 -> {
                    List<GoodCategoryListVO> goodCategoryListVOS = new CopyOnWriteArrayList<>();
                    goodCategoryGroups1.forEach((k,v) -> {
                        GoodCategoryListVO goodCategoryListVO = new GoodCategoryListVO();

                        //封装一级分类数据
                        goodCategoryListVO.setParentId(k)
                                .setParentName(Optional.ofNullable(super.getById(k)).map(GoodCategory::getTitle)
                                .orElse("暂无！"));

                        //分装二级数据
                        goodCategoryListVO.setGoodCategorySonVOS(v.parallelStream().flatMap(goodCategory -> {
                            GoodCategorySonVO goodCategorySonVO = new GoodCategorySonVO();
                            ToolUtil.copyProperties(goodCategory,goodCategorySonVO);
                            return Stream.of(goodCategorySonVO);
                        }).collect(Collectors.toList()));
                        goodCategoryListVOS.add(goodCategoryListVO);
                    });
                    return goodCategoryListVOS;
                }).orElse(null));

        return goodCategoryList.join();
    }

    @Override
    public Set<String> getIdByParentId(String pid) {

        return Optional.ofNullable(this.list(Wrappers.<GoodCategory>lambdaQuery()
            .eq(GoodCategory::getParentId,pid).orderByDesc(GoodCategory::getCreateTime)))
            .map(goodCategories -> goodCategories.parallelStream().map(GoodCategory::getId)
            .collect(Collectors.toSet()))
            .orElse(null);
    }

    @Override
    public String getGoodCategoryName(String id) {

        return Optional.ofNullable(this.getById(id)).map(GoodCategory::getTitle)
                .orElse("未知");
    }

    @Override
    public Boolean judgeGoodCategoryHaveParent(String id) {

      return Optional.ofNullable(this.getById(id)).map(goodCategory -> !CommonConstant
              .PARENT_ID.equals(goodCategory.getParentId())).orElse(false);
    }

    /**
     * 获取分组数据
     * @return
     */
    public Map<String,List<GoodCategory>> getGroupCategory() {

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<GoodCategory>lambdaQuery()
                .ne(GoodCategory::getParentId,CommonConstant.PARENT_ID)
                .orderByDesc(GoodCategory::getCreateTime)))).map(goodCategories -> goodCategories.parallelStream()
                .collect(Collectors.groupingBy(GoodCategory::getParentId)))
                .orElse(null);
    }

}