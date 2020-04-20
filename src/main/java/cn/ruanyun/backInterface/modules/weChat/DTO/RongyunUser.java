package cn.ruanyun.backInterface.modules.weChat.DTO;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class RongyunUser {


    /**
     * 用户唯一标识，支持大小写英文字母、数字、部分特殊符号 + | = - _ 的组合方式，最大长度 64 字节。
     */
    private String id;

    /**
     * 用户名称，最大长度 128 字节。用来在 Push 推送时显示用户的名称。
     */
    private String name;

    /**
     * 用户头像地址，最大长度 1024 字节，例: http://rongcloud.cn/portrait.jpg 类型不限制, 建议: jpg、png
     */
    private String portrait;
}
