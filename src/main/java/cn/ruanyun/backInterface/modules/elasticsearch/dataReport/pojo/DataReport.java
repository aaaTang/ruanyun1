package cn.ruanyun.backInterface.modules.elasticsearch.dataReport.pojo;

import cn.ruanyun.backInterface.common.utils.SnowFlakeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据上报
 * @author fei
 */
@Data
@Document(indexName = "data_report", type = "docs", shards = 1, replicas = 0, refreshInterval = "-1")
public class DataReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一id
     */
    @Id
    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());


    /**
     * 用户id
     */
    @Field(type = FieldType.Keyword)
    private String userId;


    /**
     * ip
     */
    @Field(type = FieldType.Keyword)
    private String ip;


    /**
     * 识别码
     */
    @Field(type = FieldType.Keyword)
    private String identificationCode;

    /**
     * 精准位置
     */
    @Field(type = FieldType.Keyword)
    private String addressDetail;


    /**
     * 经度
     */
    private String longitude;


    /**
     * 纬度
     */
    private String latitude;


    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Field(type = FieldType.Date, index = false, format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

}