package cn.ruanyun.backInterface.modules.fadada.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class SignatureDto {

    @ApiModelProperty(value = "图片file", dataType = "file")
    private MultipartFile imageFile;

    @ApiModelProperty("图片路径")
    private String imgUrl;
}
