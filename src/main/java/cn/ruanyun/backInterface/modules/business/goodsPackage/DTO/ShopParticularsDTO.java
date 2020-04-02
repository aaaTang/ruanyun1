package cn.ruanyun.backInterface.modules.business.goodsPackage.DTO;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;

@Data
@Accessors(chain = true)
public class ShopParticularsDTO {

    private String id;


    /* *//**
     * 店铺评分
     *//*
    private String score;*/

    /**
     * 地址
     */
    private String address;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 轮播图
     */
    @Column(length = 1000)
    private String  pic ;
}
