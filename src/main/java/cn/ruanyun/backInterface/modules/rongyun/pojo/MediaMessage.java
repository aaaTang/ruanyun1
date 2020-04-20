package cn.ruanyun.backInterface.modules.rongyun.pojo;

import io.rong.messages.BaseMessage;
import io.rong.util.GsonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MediaMessage extends BaseMessage {

    // 自定义消息标志
    private static final transient String TYPE = "go:media";

    private String content = "";


    private String targetId ;

    private String sendId;

    private Long sendTime;

    private Long receptTime;

    private String userAge;

    private String userCount;


    public MediaMessage(String content) {
        this.content = content;
    }

    @Override
    public String getType() {
        return "rx:media";
    }

    @Override
    public String toString() {
        return GsonUtil.toJson(this, MediaMessage.class);
    }
}

