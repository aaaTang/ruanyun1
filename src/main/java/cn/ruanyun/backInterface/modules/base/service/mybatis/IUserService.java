package cn.ruanyun.backInterface.modules.base.service.mybatis;


import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.dto.UserDTO;
import cn.ruanyun.backInterface.modules.base.pojo.User;
import cn.ruanyun.backInterface.modules.base.vo.AppUserVO;
import com.baomidou.mybatisplus.extension.service.IService;

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




}
