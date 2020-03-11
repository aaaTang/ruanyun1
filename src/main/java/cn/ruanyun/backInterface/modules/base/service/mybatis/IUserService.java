package cn.ruanyun.backInterface.modules.base.service.mybatis;


import cn.ruanyun.backInterface.modules.base.pojo.User;
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
}
