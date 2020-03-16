package cn.ruanyun.backInterface.modules.base.service.mybatis;


import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.common.vo.SearchVo;
import cn.ruanyun.backInterface.modules.base.dto.UserDTO;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.vo.AppUserVO;
import cn.ruanyun.backInterface.modules.base.vo.BackUserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Page;



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
     * @param user
     * @return
     */
    Result<Object> updateAppUserInfo(User user);

    /*-----------------------后台管理系统模块---------------------------*/


    /**
     * 后台管理系统添加用户
     * @param user
     * @param roleIds
     * @return
     */
    Result<Object> addUser(User user, String roleIds);

    /**
     *
     */
    Result<Page<User>> getByCondition(User user, SearchVo searchVo, PageVo pageVo);

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
     * 获取后台用户信息
     * @param username
     * @return
     */
    BackUserInfo getBackUserInfo(String username);

}
