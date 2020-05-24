package cn.ruanyun.backInterface.modules.jpush.serviceimpl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;
import cn.ruanyun.backInterface.common.enums.AudienceTypeEnum;
import cn.ruanyun.backInterface.common.enums.BooleanTypeEnum;
import cn.ruanyun.backInterface.common.utils.PageUtil;
import cn.ruanyun.backInterface.common.utils.ResultUtil;
import cn.ruanyun.backInterface.common.utils.SecurityUtil;
import cn.ruanyun.backInterface.common.utils.ToolUtil;
import cn.ruanyun.backInterface.common.vo.PageVo;
import cn.ruanyun.backInterface.common.vo.Result;
import cn.ruanyun.backInterface.modules.base.pojo.DataVo;
import cn.ruanyun.backInterface.modules.jpush.dto.JpushDto;
import cn.ruanyun.backInterface.modules.jpush.dto.JpushTypeDto;
import cn.ruanyun.backInterface.modules.jpush.mapper.JpushMapper;
import cn.ruanyun.backInterface.modules.jpush.pojo.Jpush;
import cn.ruanyun.backInterface.modules.jpush.service.IJpushService;
import cn.ruanyun.backInterface.modules.jpush.util.JPushUtil;
import cn.ruanyun.backInterface.modules.jpush.vo.JpushVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 虚拟号接口实现
 * @author z
 */
@Slf4j
@Service
@Transactional
public class IJpushServiceImpl extends ServiceImpl<JpushMapper, Jpush> implements IJpushService {

    @Autowired
    private JPushUtil jPushUtil;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 添加或者修改内容创作
     *
     * @param jpush 推送实体
     */
    @Override
    public void insertOrUpdateJpush(Jpush jpush) {

        if (ToolUtil.isEmpty(jpush.getId())) {

            jpush.setCreateBy(securityUtil.getCurrUser().getId());
        }else {

            jpush.setUpdateBy(securityUtil.getCurrUser().getId());
        }

        this.saveOrUpdate(jpush);
    }

    /**
     * 移除内容创作中心
     *
     * @param ids ids
     */
    @Override
    public void removeJpush(String ids) {

        this.removeByIds(ToolUtil.splitterStr(ids));
    }

    /**
     * 获取推送列表
     *
     * @param jpushDto 分页
     * @return JpushVo
     */
    @Override
    public DataVo<JpushVo> getJpushList(JpushDto jpushDto) {


        //查询条件
        Map<SFunction<Jpush, ?>, Object> param = Maps.newHashMap();
        param.put(Jpush::getCheckEnum, jpushDto.getCheckEnum());
        param.put(Jpush::getCreateBy, jpushDto.getUserId());

        //分页条件
        PageVo pageVo = new PageVo();
        ToolUtil.copyProperties(jpushDto, pageVo);

        //分页数据
        Page<Jpush> jpushPage = this.page(PageUtil.initMpPage(pageVo), Wrappers.<Jpush>lambdaQuery()
        .allEq(true, param, false)
        .orderByDesc(Jpush::getCreateTime));

        //返回数据
        DataVo<JpushVo> result = new DataVo<>();

        result.setDataResult(Optional.ofNullable(ToolUtil.setListToNul(jpushPage.getRecords()))
        .map(jpushes -> jpushes.parallelStream().flatMap(jpush -> {

            JpushVo jpushVo = new JpushVo();
            ToolUtil.copyProperties(jpush, jpushVo);
            return Stream.of(jpushVo);
        }).collect(Collectors.toList())).orElse(null))
        .setCurrentPageNum(jpushPage.getCurrent())
        .setTotalSize(jpushPage.getTotal());

        return result;

    }

    @Override
    public void checkJpush(JpushDto jpushDto) {

        Optional.ofNullable(this.getById(jpushDto.getId())).ifPresent(jpush -> {

            jpush.setCheckEnum(jpushDto.getCheckEnum())
                    .setCheckReason(jpushDto.getCheckReason())
                    .setCheckTime(DateUtil.date());

            this.updateById(jpush);
        });
    }

    @Override
    public void updateJpushByAfterPush(JpushDto jpushDto) {

        if (ObjectUtil.equal(jpushDto.getAudienceType(), AudienceTypeEnum.ALL)) {

            //更新数据
            Optional.ofNullable(this.getById(jpushDto.getId())).ifPresent(jpush -> {

                jpush.setPushSuccess(BooleanTypeEnum.YES)
                        .setPlatformType(jpushDto.getPlatformType())
                        .setAudienceType(AudienceTypeEnum.ALL)
                        .setUpdateBy(securityUtil.getCurrUser().getId());

                this.updateById(jpush);
            });
        }else {

            Jpush jpush = new Jpush();
            ToolUtil.copyProperties(jpushDto, jpush);
            jpush.setPushSuccess(BooleanTypeEnum.YES)
                    .setCreateBy(securityUtil.getCurrUser().getId());
            this.save(jpush);
        }
    }

    @Override
    public Result<Object> pushArticleToUser(JpushDto jpushDto) {

        PushPayload.Builder builder = PushPayload.newBuilder();

        //Gson gson = new Gson();

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("pushType", jpushDto.getPushType());
        paramMap.put("pushValue", jpushDto.getPushValue());


        Message.Builder messageBuilder = Message.newBuilder();
        messageBuilder.setTitle(jpushDto.getContent())
                .setMsgContent(jpushDto.getTitle())
                .addExtra("param", JSONObject.toJSONString(paramMap));


        List<String> tagAdd = Lists.newArrayList();

        if (ObjectUtil.equal(jpushDto.getAudienceType(), AudienceTypeEnum.TAG)) {

            // TODO: 2020/5/20 目前只有推用户id推送的
            tagAdd.add(jpushDto.getUserId());

            jpushDto.setTags(ToolUtil.joinerList(tagAdd));
        }

        //判断设备
        switch (jpushDto.getPlatformType()) {

            case ALL:

                //1 全部
                builder.setPlatform(Platform.all());
                break;

            case IOS:

                //2 ios
                builder.setPlatform(Platform.ios());
                break;

            case ANDROID:

                //3 安卓
                builder.setPlatform(Platform.android());
                break;

            default:
                break;
        }

        //判断推送目标

        switch (jpushDto.getAudienceType()) {

            case ALL:

                //1. 全部
                builder.setAudience(Audience.all());
                break;

            case TAG:

                //2. tag
                if (ToolUtil.isEmpty(jpushDto.getTags())) {

                    return new ResultUtil<>().setErrorMsg(212, "按标签推送时，tags参数不能为空！");
                }
                builder.setAudience(Audience.tag(ToolUtil.splitterStr(jpushDto.getTags())));
                break;

            case ALIAS:

                //3. 卡号别名

                if (ToolUtil.isEmpty(jpushDto.getAlias())) {

                    return new ResultUtil<>().setErrorMsg(203, "按照卡号别名进行推送时,alias不能为空！");
                }
                builder.setAudience(Audience.alias(ToolUtil.splitterStr(jpushDto.getAlias())));
                break;

            default:
                break;
        }


        //设置推送标题
        builder.setNotification(Notification.newBuilder()
                //指定当前推送的android通知
                .addPlatformNotification(AndroidNotification.newBuilder()

                        .setAlert(jpushDto.getContent())
                        .setTitle(jpushDto.getTitle())
                        //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                        .addExtra("param", JSONObject.toJSONString(paramMap))

                        .build())
                //指定当前推送的iOS通知
                .addPlatformNotification(IosNotification.newBuilder()
                        //传一个IosAlert对象，指定apns title、title、subtitle等
                        .setAlert(jpushDto.getContent())
                        //直接传alert
                        //此项是指定此推送的badge自动加1
                        .incrBadge(1)
                        //此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                        // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                        .setSound("default")
                        //此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                        .addExtra("param",JSONObject.toJSONString(paramMap))
                        //此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
                        //取消此注释，消息推送时ios将无法在锁屏情况接收
                        // .setContentAvailable(true)

                        .build())


                .build());

        //设置推送内容
        builder.setMessage(messageBuilder.build());

        //推送
        try {

            jPushUtil.sendPush(builder.build());

            updateJpushByAfterPush(jpushDto);
            return new ResultUtil<>().setSuccessMsg("推送成功！");
        } catch (APIConnectionException | APIRequestException e) {

            e.printStackTrace();
            return new ResultUtil<>().setErrorMsg(215, "推送失败！");
        }
    }
}