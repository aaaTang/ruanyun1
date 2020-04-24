package cn.ruanyun.backInterface.modules.business.userVideo.service;

import cn.ruanyun.backInterface.modules.business.userVideo.DTO.UserVideoDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.ruanyun.backInterface.modules.business.userVideo.pojo.UserVideo;

import java.util.List;

/**
 * 用户视频接口
 * @author z
 */
public interface IUserVideoService extends IService<UserVideo> {


      /**
        * 插入或者更新userVideo
        * @param userVideo
       */
     void insertOrderUpdateUserVideo(UserVideo userVideo);



      /**
       * 移除userVideo
       * @param ids
       */
     void removeUserVideo(String ids);

    /**
     * 获取用户视频列表数据
     */
    List getUserVideoList(UserVideoDTO userVideoDTO);
}