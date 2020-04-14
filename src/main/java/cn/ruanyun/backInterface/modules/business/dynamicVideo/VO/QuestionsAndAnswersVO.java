package cn.ruanyun.backInterface.modules.business.dynamicVideo.VO;

import lombok.Data;

@Data
public class QuestionsAndAnswersVO {

    private String id;
    /**
     * 标题
     */
    private String title;

    /**
     *用户评论
     */
    private String userComment;
    /**
     * 喜欢数量
     */
    private Integer likeNum;
}
