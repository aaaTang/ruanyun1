package cn.ruanyun.backInterface.modules.business.userAnswer.VO;

import lombok.Data;

@Data
public class AppUserAnswerVO {

    private String id;

    /**
     * 问答标题
     */
    private String title;

    /**
     * 精选用户回答
     */
    private String handpickAnswer;

    /**
     * 点赞数量
     */
    private String giveLikeNum;
}
