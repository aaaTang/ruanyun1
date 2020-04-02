package cn.ruanyun.backInterface.modules.business.good.pojo;

import cn.ruanyun.backInterface.base.RuanyunBaseEntity;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableName;
import dm.jdbc.stat.support.json.JSONArray;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 商品
 * @author fei
 */
@Data
@Entity
@Table(name = "t_good")
@TableName("t_good")
public class Good extends RuanyunBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    private String goodCategoryId;


    /**
     * 商品名称
     */
    private String goodName;


    /**
     * 商品图片
     */
    private String goodPics;


    /**
     * 商品详情
     */
    private String goodDetails;


    /**
     * 商品旧价格
     */
    private BigDecimal goodOldPrice;


    /**
     * 商品新价格
     */
    private BigDecimal goodNewPrice;


    /**
     * 颜色数据
     */
    private String colorIds;


    /**
     * 尺寸信息
     */
    private String sizeIds;


    /**
     * 商品库存
     */
    private Integer inventory;


    /**
     * 积分
     */
    private Integer integral;


    public static void main(String[] args) {
        String s = "[{\"sizeId\":\"256005326325682176\",\"buyCount\":5,\"colorId\":\"256004810652782592\",\"goodId\":\"1243581862115827714\"},{\"sizeId\":\"256005326325682176\",\"buyCount\":5,\"colorId\":\"256004810652782592\",\"goodId\":\"1243581862115827714\"}]";
        JSONArray jsonArray = new JSONArray(s);
        for (int i = 0; i < jsonArray.length(); i++) {
           /* OrderDetail orderDetail = JSON.parseObject(jsonArray.get(i).toString(), OrderDetail.class);
            orderDetail.setOrderId(order.getId());
            Good byId = goodService.getById(orderDetail.getGoodId());
            byId.setId(null);
            ToolUtil.copyProperties(byId,orderDetail);
            orderDetailService.insertOrderUpdateOrderDetail(orderDetail);*/
        }
    }


}