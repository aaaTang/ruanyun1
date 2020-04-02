package cn.ruanyun.backInterface.modules.business.selectedStore.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 严选商家
 * @author fei
 */
@Data
@Entity
@Table(name = "t_selected_store")
@TableName("t_selected_store")
public class SelectedStore extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 严选商家id
     */
    private String userId;

    // TODO: 2020/3/16 后期可能有新的字段 
}