package cn.ruanyun.backInterface.modules.business.goodCategory.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import cn.ruanyun.backInterface.common.constant.CommonConstant;
import cn.ruanyun.backInterface.common.utils.*;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.mapper.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserRoleService;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import cn.ruanyun.backInterface.modules.business.goodCategory.VO.*;
import cn.ruanyun.backInterface.modules.business.goodCategory.entity.GoodCategory;
import cn.ruanyun.backInterface.modules.business.goodCategory.mapper.GoodCategoryMapper;
import cn.ruanyun.backInterface.modules.business.goodCategory.service.IGoodCategoryService;
import cn.ruanyun.backInterface.modules.business.grade.service.IGradeService;
import com.alipay.api.domain.CategoryVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
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

    @Resource
    private UserMapper userMapper;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IGoodCategoryService iGoodCategoryService;

    @Autowired
    private IUserService userService;

    @Autowired
    private SecurityUtil securityUtil;

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

        return this.list(Wrappers.<GoodCategory>lambdaQuery()
                .eq(GoodCategory::getParentId,pid)
                .orderByAsc(GoodCategory::getSortOrder));
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
                .orderByDesc(GoodCategory::getSortOrder)))).map(goodCategories -> goodCategories.parallelStream()
                .collect(Collectors.groupingBy(GoodCategory::getParentId)))
                .orElse(null);
    }

    @Autowired
    private IGradeService gradeService;
    @Autowired
    private ICommentService commentService;


    /**
     * 按分类获取商家列表
     * @return
     */
    @Override
    public List getCategoryShop(String classId, String areaId) {


        List<GoodCategory> categoryList = new ArrayList<>();

            //1.先查一级分类
            GoodCategory goodCategory = this.getById(classId);

        if(ToolUtil.isNotEmpty(goodCategory)){
            //放入集合
            categoryList.add(goodCategory);

            //2.查询二级分类
            List<GoodCategory> list = this.list(new QueryWrapper<GoodCategory>().lambda()
                    .eq(GoodCategory::getParentId,classId)
                    .eq(GoodCategory::getDelFlag,0));
            //二级分类放入集合
            categoryList.addAll(list);

            List<User> users = new ArrayList<>();

            //3.按分类查询商家
            for (GoodCategory category : categoryList) {

                List<User> userList = userMapper.selectList(new QueryWrapper<User>().lambda()
                        .eq(User::getClassId,category.getId())
                        .eq((ToolUtil.isNotEmpty(areaId)),User::getAreaId,areaId)
                        .eq(User::getDelFlag,CommonConstant.STATUS_NORMAL)
                        .eq(User::getStatus,CommonConstant.STATUS_NORMAL)
                );
                users.addAll(userList);

            }

            //4.查询商家信息
            List<CategoryShopVO> categoryShopVOList = new ArrayList<>();

            for (User userAll : users){
                CategoryShopVO categoryShopVO = new CategoryShopVO();

                ToolUtil.copyProperties(userAll,categoryShopVO);
                categoryShopVO.setPic((ToolUtil.isNotEmpty(userAll.getPic())?userAll.getPic().split(",")[0]:""))
                        .setGrade(Double.parseDouble(gradeService.getShopScore(userAll.getId())))
                        .setCommentNum(Optional.ofNullable(commentService.getCommentVOByGoodId(userAll.getId()))
                                .map(List::size)
                                .orElse(0))
                        .setStoreType(judgeStoreType(userAll));

                categoryShopVOList.add(categoryShopVO);
            }

            return categoryShopVOList;
        }


       return null;
    }


    /**
     * 判断类型
     * @param user user
     * @return Integer
     */
    public Integer judgeStoreType(User user) {

        if (ObjectUtil.equal(iGoodCategoryService.getGoodCategoryName(user.getClassId()), "婚宴酒店")) {

            return 1;
        }else if (ObjectUtil.equal(iGoodCategoryService.getGoodCategoryName(user.getClassId()), "四大金刚")) {

            return 2;
        }else {

            return 3;
        }
    }

    @Override
    public Result<List<FourDevarajasCategoryVo>> getFourDearestsCategory() {

        return Optional.ofNullable(getFourCategoryId()).map(categoryId ->

                Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<GoodCategory>lambdaQuery()
                        .eq(GoodCategory::getParentId, categoryId)))).map(goodCategories ->
                        new ResultUtil<List<FourDevarajasCategoryVo>>().setData(goodCategories.parallelStream().flatMap(goodCategory -> {

                            FourDevarajasCategoryVo fourDevarajasCategoryVo = new FourDevarajasCategoryVo();
                            ToolUtil.copyProperties(goodCategory, fourDevarajasCategoryVo);
                            return Stream.of(fourDevarajasCategoryVo);
                        }).collect(Collectors.toList()), "获取四大金刚分类数据成功！")

                ).orElse(new ResultUtil<List<FourDevarajasCategoryVo>>().setErrorMsg(201, "暂无数据！"))
        ).orElse(new ResultUtil<List<FourDevarajasCategoryVo>>().setErrorMsg(201, "暂无数据！"));
    }

    /**
     * 获取后台管理系统的分类列表
     *
     * @return CategoryVO
     */
    @Override
    public Result<List<CategoryVo>> getBackStoreCategoryList() {


        User user = userService.getById(securityUtil.getCurrUser().getId());

        return Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<GoodCategory>lambdaQuery()
        .eq(GoodCategory::getParentId, user.getClassId())
        .orderByDesc(GoodCategory::getCreateTime))))
        .map(goodCategories -> {

            List<CategoryVo> result = goodCategories.parallelStream().flatMap(goodCategory -> {

                CategoryVo categoryVO = new CategoryVo();

                ToolUtil.copyProperties(goodCategory, categoryVO);

                categoryVO.setHavaNext(Optional.ofNullable(ToolUtil.setListToNul(this.list(Wrappers.<GoodCategory>lambdaQuery()
                        .eq(GoodCategory::getParentId, goodCategory.getId())))).isPresent());

                return Stream.of(categoryVO);
            }).collect(Collectors.toList());

            return new ResultUtil<List<CategoryVo>>().setData(result, "获取分类数据成功！");
        }).orElse(new ResultUtil<List<CategoryVo>>().setErrorMsg(201, "暂无数据！"));
    }

    @Override
    public Result<List<CategoryVo>> getGoodServiceGoodCategory() {

        return Optional.ofNullable(this.list(Wrappers.<GoodCategory>lambdaQuery()
        .eq(GoodCategory::getParentId, CommonConstant.PARENT_ID)
        .ne(GoodCategory::getTitle, "四大金刚")))
        .map(goodCategories -> {

            goodCategories.addAll(this.list(Wrappers.<GoodCategory>lambdaQuery()
            .eq(GoodCategory::getParentId, getFourCategoryId())));

            List<CategoryVo> categoryVos = goodCategories.parallelStream().flatMap(goodCategory -> {


                CategoryVo categoryVo = new CategoryVo();
                ToolUtil.copyProperties(goodCategory, categoryVo);

                return Stream.of(categoryVo);
            }).collect(Collectors.toList());

            return new ResultUtil<List<CategoryVo>>().setData(categoryVos, "获取优质服务分类成功！");
        }).orElse(new ResultUtil<List<CategoryVo>>().setErrorMsg(201, "暂无数据！"));
    }

    @Override
    public Result<Object> getCategoryState(String goodCategoryId) {

        return Optional.ofNullable(this.getById(goodCategoryId)).map(goodCategory -> new ResultUtil<>()
        .setData(goodCategory.getBuyState(), "获取购买状态成功！"))
                .orElse(new ResultUtil<>().setErrorMsg(201, "暂无分类数据"));
    }


    /**
     * 获取四大金刚的id
     * @return
     */
    public String getFourCategoryId() {

        return Optional.ofNullable(this.getOne(Wrappers.<GoodCategory>lambdaQuery()
                .eq(GoodCategory::getTitle, "四大金刚")))
                .map(GoodCategory::getId)
                .orElse(null);
    }


}