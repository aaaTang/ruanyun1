package cn.ruanyun.backInterface.modules.business.goodCategory.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @program: ruanyun
 * @description: 四大金刚分类数据
 * @author: fei
 * @create: 2020-05-29 12:46
 **/

@Data
@Accessors(chain = true)
public class FourDevarajasCategoryVo {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "分类名称")
    private String title;

}
