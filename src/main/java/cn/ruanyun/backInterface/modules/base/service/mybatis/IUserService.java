package cn.ruanyun.backInterface.modules.base.service.mybatis;


import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.dto.UserDTO;
import cn.ruanyun.backInterface.modules.base.dto.UserUpdateDTO;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.google.api.client.util.ArrayMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * @author fei
 */
public interface IUserService extends IService<User> {


    /**
     * 通过用户名获取id
     * @param userName
     * @return
     */
    String getUserIdByName(String userName);


    /**
     * APP通过邀请码获取用户信息
     * @param user
     * @return
     */
    Result<Object> appGetinvitationCode(UserDTO user);

    /**
     * 通过手机号注册
     * @return
     */
    Result<Object> registerByPhoneAndPassword(UserDTO user);


    /**
     * 通过手机号和密码登录
     * @return
     */
    Result<Object> loginByPhoneAndPassword(UserDTO user);


    /**
     * 获取app用户信息
     * @return
     */
    AppUserVO getAppUserInfo();

    /**
     * 更新app用户信息
     * @return
     */
    Result<Object> updateAppUserInfo(UserUpdateDTO userUpdateDTO);

    /*-----------------------后台管理系统模块---------------------------*/

    /**
     * 后台管理系统添加用户
     * @param user
     * @param roleIds
     * @return
     */
    Result<Object> addUser(User user, String roleIds);


    /**
     * 获取管理员用户
     * @return
     */
    List<BackUserVO> getBackUserAdminList(UserDTO userDTO);


    /**
     * 获取商家用户
     * @return
     */
    List<BackUserVO> getBackUserStoreList(UserDTO userDTO);


    /**
     * 获取普通消费者
     * @return
     */
    List<BackUserVO> getBackUserCommonList(UserDTO userDTO);


    /**
     * 获取个人商家用户
     * @param userDTO
     * @return
     */
    List<BackUserVO> getBackUserPersonStoreList(UserDTO userDTO);


    /**
     * 用户管理
     * @param userDTO
     * @return
     */
    List<BackUserInfo> getUserList(UserDTO userDTO);

    /**
     * 重置密码
     * @param userIds
     * @return
     */
    Result<Object> resetPass(String userIds);


    /**
     * 修改用户资料
     * @param u
     * @return
     */
    Result<Object> editOwn(User u);


    /**
     * 修改密码
     * @param password
     * @param newPass
     * @return
     */
    Result<Object> modifyPass( String password, String newPass);


    /**
     * 冻结账号
     * @param userId
     * @return
     */
    Result<Object> freezeAccount(String userId);


    /**
     * 获取后台用户信息
     * @param username
     * @return
     */
    BackUserInfo getBackUserInfo(String username);

    /**
     * 忘记密码
     * @param user
     * @return
     */
    Result<Object> forgetPassword(UserDTO user);

    /**
     * 获取账户余额
     */
    BigDecimal getAccountBalance(String userId);

    /**
     * 获取店铺名称
     * @param createBy
     * @return
     */
    String getUserIdByUserName(String createBy);


    /**
     * 获取店铺图片
     * @param createBy
     * @return
     */
    String getUserIdByUserPic(String createBy);

    /**
     * 后端获取用户详情
     * @return
     */
    BackUserVO getBackUserParticulars(String userId,UserTypeEnum userTypeEnum);


    /**
     * 获取龙虎榜排名列表
     * @return
     */
    List<UserProfitVO> getUserProfitList();


    /**
     * 设置支付密码
     * @param userPayPasswordVo 实体
     */
    Result<Object> setPayPassword(UserPayPasswordVo userPayPasswordVo);


    /**
     * 修改支付密码
     * @param userPayPasswordVo 实体
     * @return Result<Object>
     */
    Result<Object> updatePayPassword(UserPayPasswordVo userPayPasswordVo);


    /**
     * 获取精准客户
     * @return StoreCustomVo
     */
    List<StoreCustomVo> getStoreAccurateCustomer(String storeId);


    /**
     * 获取潜在客户
     * @return StoreCustomVo
     */
    List<StoreCustomVo> getStoreProspectiveCustomer();

}
