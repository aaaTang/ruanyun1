package cn.ruanyun.backInterface.modules.fadada.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author Administrator
 */

@Data
@Accessors(chain = true)
public class UploaddocsDto {

    @ApiModelProperty("服务分类")
    private String goodCategoryId;

    @ApiModelProperty("合同标题")
    private String docTitle;

    @ApiModelProperty(value = "合同编号", hidden = true)
    private String contractId;

    @ApiModelProperty("合同公网下载地址")
    private String docUrl;

    /*@ApiModelProperty(value = "PDF 文档", dataType = "file")
    private MultipartFile pdfFile;*/

    @ApiModelProperty(value = "文档类型", hidden = true)
    private String docType = ".pdf";

    @ApiModelProperty(value = "上传数量")
    private Integer uploadCount;
}
