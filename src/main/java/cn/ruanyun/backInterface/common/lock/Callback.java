package cn.ruanyun.backInterface.common.lock;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author sunyujia@aliyun.com https://github.com/yujiasun/Distributed-Kit
 * @date 2016/2/23
 */
public interface Callback {

    /**
     * 成功获取锁后执行方法
     * @return
     * @throws InterruptedException
     */
    Object onGetLock() throws InterruptedException, JsonProcessingException;

    /**
     * 获取锁超时回调
     * @return
     * @throws InterruptedException
     */
    Object onTimeout() throws InterruptedException;
}
