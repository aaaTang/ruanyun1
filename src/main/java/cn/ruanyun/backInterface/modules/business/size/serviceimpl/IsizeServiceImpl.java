package cn.ruanyun.backInterface.modules.business.size.serviceimpl;


import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.size.VO.SizeInfoVO;
import cn.ruanyun.backInterface.modules.business.size.VO.SizeVO;
import cn.ruanyun.backInterface.modules.business.size.entity.Size;
import cn.ruanyun.backInterface.modules.business.size.mapper.SizeMapper;
import cn.ruanyun.backInterface.modules.business.size.service.IsizeService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * 商品尺寸接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IsizeServiceImpl extends ServiceImpl<SizeMapper, Size> implements IsizeService {

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 插入尺寸
     *
     * @param size
     */
    @Override
    public void insertSize(Size size) {

        size.setCreateBy(securityUtil.getCurrUser().getId());
        CompletableFuture.runAsync(() -> this.save(size));
    }

    /**
     * 删除尺寸
     *
     * @param id
     */
    @Override
    public void deleteSize(String id) {

        CompletableFuture.runAsync(() -> this.removeById(id));
    }

    /**
     * 修改尺寸
     *
     * @param size
     */
    @Override
    public void updateSize(Size size) {

        CompletableFuture.runAsync(() -> {

            Size sizeOld = this.getById(size.getId());
            ToolUtil.copyProperties(size,sizeOld);
            this.updateById(size);
        });
    }

    /**
     * 获取尺寸列表
     *
     * @param goodCategoryId
     * @return
     */
    @Override
    public List<SizeVO> getSizeList(String goodCategoryId) {

        CompletableFuture<Optional<String>> categoryId = CompletableFuture.supplyAsync(() -> Optional
           .ofNullable(goodCategoryId));


        //获取初始数据
        CompletableFuture<Optional<List<Size>>> sizeLists = categoryId.thenApplyAsync(cId -> cId.map(cid ->
                Optional.ofNullable(ToolUtil.setListToNul(getSizeListByGoodCategory(cid))))
                .orElse(Optional.ofNullable(getSizeAllList())));

        //获取封装数据
        CompletableFuture<List<SizeVO>> sizeVOList = sizeLists.thenApplyAsync(sizes -> sizes.map(sizes1 -> sizes1
           .parallelStream().flatMap(size -> {

               SizeVO sizeVO = new SizeVO();
               ToolUtil.copyProperties(size,sizeVO);
               return Stream.of(sizeVO);

                }).collect(Collectors.toList())).orElse(null));

        //返回结果
        return sizeVOList.join();
    }

    @Override
    public String getSizeName(String sizeId) {
        return Optional.ofNullable(this.getById(sizeId))
                .map(Size::getName)
                .orElse("未知");
    }

    @Override
    public List<SizeInfoVO> getSizeVoByIds(String ids) {

        return Optional.ofNullable(this.listByIds(ToolUtil.splitterStr(ids)))
                .map(sizes -> sizes.parallelStream().flatMap(size -> {

                    SizeInfoVO sizeInfoVO = new SizeInfoVO();
                    ToolUtil.copyProperties(size,sizeInfoVO);
                    return Stream.of(sizeInfoVO);

                }).collect(Collectors.toList())).orElse(null);
    }


    /**
     * 获取全部数据
     * @return
     */
    public List<Size> getSizeAllList() {
        return this.list(Wrappers.<Size>lambdaQuery().orderByDesc(Size::getCreateTime));
    }


    /**
     * 根据分类筛选数据
     * @param goodCategoryId
     * @return
     */
    public List<Size> getSizeListByGoodCategory(String goodCategoryId) {
        return this.list(Wrappers.<Size>lambdaQuery().eq(Size::getGoodCategoryId,goodCategoryId)
           .orderByDesc(Size::getCreateTime));
    }

}