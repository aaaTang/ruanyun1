package cn.ruanyun.backInterface.modules.business.orderAfterSale.service;

import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.VO.OrderAfterSaleVo;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.dto.OrderAfterCommitDto;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.dto.OrderAfterSaleDto;
import cn.ruanyun.backInterface.modules.business.orderAfterSale.pojo.OrderAfterSale;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * 售后申请接口
 * @author wj
 */
public interface IOrderAfterSaleService extends IService<OrderAfterSale> {


    /**
     * 提交售后订单
     * @param orderAfterCommitDto 实体
     */
    Result<Object> commitOrderAfterSale(OrderAfterCommitDto orderAfterCommitDto);


    /**
     * 移除售后订单表
     * @param ids ids
     */
    void removeOrderAfterSale(String ids);


    /**
     * 处理售后订单
     * @param orderAfterSaleDto orderAfterSaleDto
     */
    void resolveOrderAfterSale(OrderAfterSaleDto orderAfterSaleDto);


    /**
     * app通过订单id获取售后信息
     * @param orderId 订单id
     * @return OrderAfterSaleVO
     */
    OrderAfterSaleVo getByOrderId(String orderId);


    /**
     * 撤销售后订单
     * @param orderId 订单id
     */
    Result<Object> revocationAfterOrder(String orderId);

    /**
     * 获取当前售后订单的申请退款金额
     * @param orderId 订单id
     * @return BigDecimal
     */
    BigDecimal getOrderAfterSaleReturnMoney(String orderId);
}