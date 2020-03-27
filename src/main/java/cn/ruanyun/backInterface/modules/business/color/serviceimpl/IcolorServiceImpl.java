package cn.ruanyun.backInterface.modules.business.color.serviceimpl;


import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.RedisUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.color.VO.ColorInfoVO;
import cn.ruanyun.backInterface.modules.business.color.VO.ColorVO;
import cn.ruanyun.backInterface.modules.business.color.entity.Color;
import cn.ruanyun.backInterface.modules.business.color.mapper.ColorMapper;
import cn.ruanyun.backInterface.modules.business.color.service.IcolorService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 商品颜色接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class IcolorServiceImpl extends ServiceImpl<ColorMapper, Color> implements IcolorService {



    /**
     * 插入颜色分类
     *
     * @param color
     */
    @Override
    public void insertColorCategory(Color color) {

        CompletableFuture.allOf(

                //插入分类
                CompletableFuture.runAsync(() -> {

                    //1如果添加的有父类id，则更新分类的是否有节点字段
                    //2如果没有则设置为0
                    if (ToolUtil.isNotEmpty(color.getParentId())) {
                        Color colorParent = this.getById(color.getParentId());
                        colorParent.setIsParent(true);
                        this.updateById(colorParent);
                    }else {
                        color.setParentId(CommonConstant.PARENT_ID);
                    }

                    //3.插入分类数据
                    this.save(color);
                }),

                //删除缓存
                CompletableFuture.runAsync(() -> RedisUtil.del("color"))
        );


    }

    /**
     * 删除颜色分类
     *
     * @param id
     */
    @Override
    public void deleteColorCategory(String id) {

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> this.removeById(id)),
                CompletableFuture.runAsync(() -> RedisUtil.del("color"))
        );
    }

    /**
     * 修改颜色分类
     *
     * @param color
     */
    @Override
    public void updateColorCategory(Color color) {

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {

                    Color colorOld = this.getById(color.getId());
                    ToolUtil.copyProperties(color,colorOld);
                    this.updateById(colorOld);

                }),
                CompletableFuture.runAsync(() -> RedisUtil.del("color"))

        );
    }

    @Override
    public String getColorName(String id) {

        return Optional.ofNullable(this.getById(id))
                .map(Color::getTitle).orElse(null);
    }

    /**
     * 获取颜色列表
     *
     * @param pid
     * @param userId
     * @return
     */
    @Override
    public List<ColorVO> getColorList(String pid, String userId) {
        //1.获取一级分类
        CompletableFuture<Optional<String>> parentId = CompletableFuture.supplyAsync(() -> Optional.ofNullable(pid)
                , ThreadPoolUtil.getPool());

        //2.获取封装之前的数据
        CompletableFuture<Optional<List<Color>>> colorList = parentId.thenApplyAsync(optionalS -> optionalS
                .map(optional -> Optional.ofNullable(ColorList(optional))).orElseGet(() ->
                        Optional.ofNullable(ColorList(CommonConstant.PARENT_ID))),ThreadPoolUtil.getPool());

        //3.获取封装之后的数据
        CompletableFuture<List<ColorVO>> colorVos = colorList.thenApplyAsync(colors ->
                colors.map(colors1 -> colors1.parallelStream().flatMap(Color -> {
                    ColorVO colorVO = new ColorVO();
                    ToolUtil.copyProperties(Color,colorVO);
                    return Stream.of(colorVO);
                }).collect(Collectors.toList())).orElse(null),ThreadPoolUtil.getPool());

        return colorVos.join();
    }

    @Override
    public List<ColorInfoVO> getColorInfoVO(String ids) {

        return Optional.ofNullable(this.listByIds(ToolUtil.splitterStr(ids)))
                .map(colors -> colors.parallelStream().flatMap(color -> {

                    ColorInfoVO colorInfoVO = new ColorInfoVO();
                    ToolUtil.copyProperties(color,colorInfoVO);
                    return Stream.of(colorInfoVO);

                }).collect(Collectors.toList())).orElse(null);
    }

    /**
     * 获取分类列表
     * @param pid
     * @return
     */
    public List<Color> ColorList(String pid) {

        return ToolUtil.setListToNul(this.list(Wrappers.<Color>lambdaQuery()
                .eq(Color::getParentId,pid).orderByDesc(Color::getCreateTime)));
    }


}