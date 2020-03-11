package cn.ruanyun.backInterface.base;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @author fei
 */
// 自定义接口 不会创建接口的实例 必须加此注解
@NoRepositoryBean
public interface RuanyunBaseDao<E, ID extends Serializable> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {

}
