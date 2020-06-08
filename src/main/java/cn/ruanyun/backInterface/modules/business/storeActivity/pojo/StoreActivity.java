package cn.ruanyun.backInterface.modules.business.storeActivity.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.enums.UserTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 店铺活动
 * @author z
 */
@Data
@Entity
@Table(name = "t_store_activity")
@TableName("t_store_activity")
public class StoreActivity extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 活动名称
     */
    private String title;

    /**
     * 活动背景图
     */
    private String pic;

    /**
     * 内容
     */
    private String content;

    /**
     * 连接地址
     */
    private String url;
    /**
     * 发布人角色
     */
    private UserTypeEnum userTypeEnum;
}