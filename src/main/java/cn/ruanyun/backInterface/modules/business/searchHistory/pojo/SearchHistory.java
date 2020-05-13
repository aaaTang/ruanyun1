package cn.ruanyun.backInterface.modules.business.searchHistory.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 搜索历史记录
 * @author z
 */
@Data
@Entity
@Table(name = "t_search_history")
@TableName("t_search_history")
public class SearchHistory extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 搜索的名称
     */
    private String title;
    /**
     * 搜索数量
     */
    private Integer count = new Integer(0) ;

}