package cn.ruanyun.backInterface.modules.base.serviceimpl.mybatis;


import cn.ruanyun.backInterface.modules.base.dao.mapper.UserMapper;
import cn.ruanyun.backInterface.modules.base.entity.User;
import cn.ruanyun.backInterface.modules.base.service.mybatis.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author fei
 */
@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
