package cn.ruanyun.backInterface.modules.business.orderDetail.serviceimpl;

import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.modules.business.discountMy.pojo.DiscountMy;
import cn.ruanyun.backInterface.modules.business.discountMy.service.IDiscountMyService;
import cn.ruanyun.backInterface.modules.business.itemAttrVal.service.IItemAttrValService;
import cn.ruanyun.backInterface.modules.business.orderDetail.vo.OrderDetailVo;
import cn.ruanyun.backInterface.modules.business.orderDetail.mapper.OrderDetailMapper;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;


/**
 * 子订单接口实现
 * @author wj
 */
@Slf4j
@Service
@Transactional
public class IOrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

    @Autowired
    private IItemAttrValService itemAttrValService;


    /**
     * 获取销量
     *
     * @param id id
     * @return Integer
     */
    @Override
    public Integer getGoodSalesVolume(String id) {
        return this.count(Wrappers.<OrderDetail>lambdaQuery()
                .eq(OrderDetail::getGoodId,id));
    }

    @Override
    public OrderDetailVo getOrderDetailByOrderId(String orderId) {

        return Optional.ofNullable(this.getOne(Wrappers.<OrderDetail>lambdaQuery()
                .eq(OrderDetail::getOrderId, orderId)
                .last("limit 1")))
                .map(orderDetail -> {

                    OrderDetailVo orderDetailVo = new OrderDetailVo();

                    //规格
                    orderDetailVo.setAttrSymbolPath(itemAttrValService.getItemAttrValVo(orderDetail.getAttrSymbolPath()));

                    //优惠券抵扣金额
                    orderDetailVo.setSubtractMoney(orderDetail.getSubtractMoney());

                    ToolUtil.copyProperties(orderDetail, orderDetailVo);

                    return orderDetailVo;

                }).orElse(null);
    }

}