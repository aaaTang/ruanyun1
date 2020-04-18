package cn.ruanyun.backInterface.modules.base.dto;


import lombok.Data;

@Data
public class UserUpdateDTO {


    private  String  id;

    /**
     * 用戶昵称
     */
    private String nickName;
    /**
     * 用户头像
     */
    private  String  avatar;
    /**
     * 用户性别
     */
    private  String  sex;
    /**
     * 婚期
     */
    private  String  weddingDay;
    /**
     * 地区
     */
    private  String  address;
    /**
     * 	个人简介
     */
    private  String  individualResume;

    /**
     * 店铺名称
     */
    private String shopName;


}
