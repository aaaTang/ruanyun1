package cn.ruanyun.backInterface.modules.business.comment.serviceimpl;

import cn.ruanyun.backInterface.modules.business.comment.VO.CommontVO;
import cn.ruanyun.backInterface.modules.business.comment.mapper.CommonMapper;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Common;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommonService;
import cn.ruanyun.backInterface.modules.business.order.pojo.Order;
import cn.ruanyun.backInterface.modules.business.order.service.IOrderService;
import cn.ruanyun.backInterface.modules.business.orderDetail.pojo.OrderDetail;
import cn.ruanyun.backInterface.modules.business.orderDetail.service.IOrderDetailService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ThreadPoolUtil;


/**
 * 评价接口实现
 * @author fei
 */
@Slf4j
@Service
@Transactional
public class ICommonServiceImpl extends ServiceImpl<CommonMapper, Common> implements ICommonService {


       @Autowired
       private SecurityUtil securityUtil;
       @Autowired
       private IOrderService orderService;
       @Autowired
       private IOrderDetailService orderDetailService;

       @Override
       public void insertOrderUpdateCommon(Common common) {
           if (!StringUtils.isEmpty(common.getOrderId())){
               Order byId = orderService.getById(common.getOrderId());
               common.setTypeEnum(byId.getTypeEnum());
               common.setUserId(byId.getUserId());
               List<OrderDetail> list = orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, byId.getId()));
               if (list.size() > 0){
                   common.setGoodId(list.get(0).getGoodId());
               }
           }
           if (ToolUtil.isEmpty(common.getCreateBy())) {
               common.setCreateBy(securityUtil.getCurrUser().getId());
           } else {
               common.setUpdateBy(securityUtil.getCurrUser().getId());
           }
           Mono.fromCompletionStage(CompletableFuture.runAsync(() -> this.saveOrUpdate(common)))
                   .publishOn(Schedulers.fromExecutor(ThreadPoolUtil.getPool()))
                   .toFuture().join();
       }

      @Override
      public void removeCommon(String ids) {
          CompletableFuture.runAsync(() -> this.removeByIds(ToolUtil.splitterStr(ids)));
      }

    /**
     * 通过商品id获取评论信息
     * @param id
     * @return
     */
    @Override
    public List getCommentVOByGoodId(String id) {
        return  Optional.ofNullable(this.list(Wrappers.<Common>lambdaQuery()
        .eq(Common::getGoodId,id))).orElse(null);
    }

    /**
     *
     * @param common
     * @return
     */
    @Override
    public List<CommontVO> getCommonList(Common common) {
        return null;
    }
}