package cn.ruanyun.backInterface.modules.business.userRelationship.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@Data
public class AppRelationUserVO {

    private String id;

    @ApiModelProperty("用戶昵称")
    private String nickName;

    @ApiModelProperty("用户头像")
    @Column(length = 1000)
    private String avatar;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("地址")
    private String address;
}
