package cn.ruanyun.backInterface.modules.jpush.dto;

import cn.ruanyun.backInterface.common.enums.PushTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class JpushTypeDto {


    /**
     * 推送类型
     */
    private PushTypeEnum pushType;


    /**
     * 推送值
     */
    private String pushValue;
}
