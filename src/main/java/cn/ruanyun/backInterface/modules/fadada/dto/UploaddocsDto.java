package cn.ruanyun.backInterface.modules.fadada.dto;

import cn.ruanyun.backInterface.common.utils.CommonUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.File;

/**
 * @author Administrator
 */

@Data
@Accessors(chain = true)
public class UploaddocsDto {


    private String id;

    /**
     * 合同编号
     */
    private String contract_id = CommonUtil.getRandomNum();

    /**
     * 合同标题
     */
    private String doc_title;

    /**
     * 文档地址
     */
    private String doc_url;

    /**
     * PDF 文档
     */
    private File file;

    /**
     * 文档类型
     */
    private String doc_type = ".pdf";
}
