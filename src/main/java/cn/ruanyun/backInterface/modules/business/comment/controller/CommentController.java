package cn.ruanyun.backInterface.modules.business.comment.controller;

import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.business.comment.DTO.CommentDTO;
import cn.ruanyun.backInterface.modules.business.comment.pojo.Comment;
import cn.ruanyun.backInterface.modules.business.comment.service.ICommentService;
import com.google.api.client.util.Maps;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author wj
 * 评论管理接口
 */
@Slf4j
@RestController
@RequestMapping("/ruanyun/comment")
@Transactional
public class CommentController {

    @Autowired
    private ICommentService iCommentService;


   /**
     * 更新或者插入数据
     * @return
    */
    @PostMapping(value = "/insertOrderUpdateComment")
    public Result<Object> insertOrderUpdateComment(CommentDTO commentDTO){
        try {
            iCommentService.insertOrderUpdateComment(commentDTO);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * 后台回复评论
     * @return
     */
    @PostMapping(value = "/replyComment")
    public Result<Object> replyComment(Comment comment){
        try {
            iCommentService.replyComment(comment);
            return new ResultUtil<>().setSuccessMsg("插入或者更新成功!");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }


    /**
     * 移除数据
     * @param ids
     * @return
    */
    @PostMapping(value = "/removeComment")
    public Result<Object> removeComment(String ids){
        try {
            iCommentService.removeComment(ids);
            return new ResultUtil<>().setSuccessMsg("移除成功！");
        }catch (Exception e) {

            return new ResultUtil<>().setErrorMsg(201, e.getMessage());
        }
    }

    /**
     * app 商品获取评论 商家获取评论
     * @param comment
     * @param pageVo
     * @return
     */
    @PostMapping("/getAppCommonList")
    public Result<Object> getAppGoodList(Comment comment, PageVo pageVo) {
        return Optional.ofNullable(iCommentService.getCommentList(comment))
                .map(commentListVOS -> {
                    Map<String,Object> result = Maps.newHashMap();
                    result.put("size",commentListVOS.size());
                    result.put("data", PageUtil.listToPage(pageVo,commentListVOS));
                    return new ResultUtil<>().setData(result,"获取商品列表成功！");
                })
                .orElse(new ResultUtil<>().setErrorMsg(201,"暂无数据！"));
    }


    /**
     * 后端按订单获取评论
     * @param //goodId 商品id
     * @return
     */
    @PostMapping(value = "/PcGetGoodsComment")
    public Result<Object> PcGetGoodsComment(Comment comment){
       List list = iCommentService.PcGetGoodsComment(comment);

        if(ToolUtil.isNotEmpty(list)){
            return new ResultUtil<>().setData(list,"获取数据成功！");
        } else {
            return new ResultUtil<>().setErrorMsg(201,"暂无数据！");
        }

    }



    @ApiOperation("获取隐私协议")
    @GetMapping("/getSecretContent")
    public String getSecretContent() {

        return "特别提示\n" +
                "您的信任对我们非常重要，婚前婚后深知个人信息对您的重要性，我们将按照法律法规要求，采取相应安全保护措施，尽力保护您的个人信息。您在使用婚前婚后的产品或服务时，我们可能会收集和使用您的相关信息。希望您在使用我们的产品或服务前仔细阅读并确认您已经充分理解本《隐私政策》所写明的内容。您使用或继续使用我们的产品或服务，即意味着您同意本《隐私政策》(含更新版本)内容，并且同意我们按照本《隐私政策》收集、使用、保存、分享或以其他方式运用您的相关信息。\n" +
                "我们可能收集的信息\n" +
                "我们提供服务时，可能会通过如下方式收集与您有关的信息。如果您不提供相关信息，可能无法成为我们的用户或无法享受我们的某些服务。\n" +
                "1.您提供的信息\n" +
                "1）您在注册账户时需要向我们提供相关个人信息，如您电话号码、您的姓名等。\n" +
                "2）您在办理相关业务时，需要您提供相应的车辆行驶证信息，如车牌号、持有人、品牌型号、车架号、发动机号、及注册日期等信息。\n" +
                "3）您在参加运营活动时，需要您提供出行人及联系人姓名、联系方式、证件号码等信息。\n" +
                "4）您在购买商品时，需要您提供收货人的姓名、联系方式、收货地址等信息。\n" +
                "5）您在使用婚前婚后其他业务时，根据业务性质及为了实现服务，均有可能需要您提供相关信息。\n" +
                "6）为完成交易，可能需要您提供账号相关信息，并选择付款方式，以便我们了解您的支付状态。\n" +
                "7）您通过我们的服务向其他方提供的共享信息，以及您使用我们的服务时所储存的信息。\n" +
                "2.其他分享的您的信息\n" +
                "1）其他使用我们服务时所提供有关您的共享信息。\n" +
                "3.我们获取的您的信息\n" +
                "您使用服务时我们可能收集如下信息：\n" +
                "1）日志信息，使用我们的服务时，系统可能通过cookies或其他方式自动采集的技术信息，包括但不限于：IP地址信息、硬件设备或软件信息、SDK或API版本等。\n" +
                "2）位置信息，我们将收集的有关您位置的信息，以便您不需要手动输入地理坐标即可获得相关的服务。\n" +
                "3）打开摄像头，我们将调用摄像头完成拍摄，实现您拍照识别证件、上传图片以及更换头像等操作。\n" +
                "4）打开相册，我们将打开相册上传您所需的图片，实现您上传图片以及更换头像等操作。\n" +
                "我们如何使用您的消息\n" +
                "婚前婚后利用收集的信息来提供、维护、保护和改进服务，同时开发新的服务为用户创造更好的体验，并提高我们的总体服务品质。\n" +
                "1）向您提供服务并维护这些服务。\n" +
                "2）在我们提供服务时，用于身份验证、客户服务、安全防范、诈骗监测、存档和备份用途，确保我们向您提供的产品和服务的安全性。\n" +
                "3）帮助我们设计新服务，改善我们现有服务。\n" +
                "4）评估我们服务中的促销及推广活动的效果，并加以改善。\n" +
                "5）婚前婚后可能会对产品使用情况进行统计，并可能会与公众分享这些统计信息，以展示我们服务的整体使用趋势。\n" +
                "6）其他有利于本公司更好的为您提供服务的情形。\n" +
                "信息披露\n" +
                "婚前婚后将以高度的勤勉义务对待用户信息，不会将这些信息对外公开或向婚前婚后管理公司之外的第三方提供。除非：\n" +
                "1）事先获得用户的明确授权。\n" +
                "2）您使用了必须披露个人信息才能正常使用的婚前婚后或其合作方提供产品或服务。\n" +
                "3）根据法律、法规要求或政府主管部门的强制性要求，或者为了维护公共利益。\n" +
                "4）在紧急的情况下，为了保护婚前婚后及其用户之安全。\n" +
                "5）为维护婚前婚后的合法权益，或婚前婚后服务的正常运营，例如查找、预防、处理欺诈或安全方面的问题。\n" +
                "6）我们可能会向第三方（包括但不限于关联公司、合作伙伴等）共享您的订单信息、账户信息、位置信息等，以保障为您提供的服务顺利完成。但我们仅会出于合法、正当、必要、特定、明确的目的共享您的个人信息，并且只会共享提供服务所必要的个人信息。\n" +
                "7）其他符合法律，以及符合《婚前婚后用户使用协议》或其他相关协议、规定、指引的情形。\n" +
                "敏感信息\n" +
                "某些个人信息因其特殊性可能被认为是敏感信息。请注意，您在使用我们的服务时所提供、上传或发布的内容和信息，例如有关您在活动中发布的照片等信息，可能会泄露您的敏感个人信息。您需要谨慎地考虑，是否在使用我们的服务时披露相关敏感个人信息。\n" +
                "您同意按本《隐私政策》来处理您的敏感个人信息。\n" +
                "信息存储\n" +
                "所收集的用户信息和资料可能将保存在婚前婚后及其关联公司的服务器上，或婚前婚后委托机构的服务器上。\n" +
                "信息安全\n" +
                "婚前婚后尽力保护您的信息安全，以防信息的丢失、不当使用、未经授权的披露等，我们将为此采取合理的预防措施，使用符合行业标准的加密技术来保护您在使用本服务中涉及的数据。但是，希望您理解并确认，任何人无法保证互联网数据100%的安全，如果出现数据安全，将会由您个人承担。\n" +
                "未成年人信息保护\n" +
                "婚前婚后非常重视对未成年人信息的保护。若您是18周岁以下的未成年人，在使用婚前婚后的产品或服务前，应事先取得您的家长或法定监护人的同意，并请要求您的父母或监护人阅读本《隐私政策》。\n" +
                "变更\n" +
                "婚前婚后将根据法律、法规或政策，或婚前婚后产品或服务的变化和技术的更新，或其他婚前婚后认为合理的原因，对本《隐私政策》进修改变更。变更以合适的形式告知用户。若您不接受变更后条款的，应立即停止使用，若您在本《隐私政策》变更后继续使用的，视为接受修订后的所有条款。\n" +
                "适用范围\n" +
                "1)本隐私政策适用于婚前婚后APP提供的所有服务，但是不包括附有独立隐私政策的服务（如第三方提供的产品和服务）。\n" +
                "2)本隐私政策不适用第三方提供的产品和服务，例如在婚前婚后APP上由第三方提供的产品和服务，以及在我们的服务中链接到其他网站，这些产品、服务或网站会有独立的隐私政策予以规范，请另行查阅相应的政策规定。";
    }






}
