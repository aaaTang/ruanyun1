package cn.ruanyun.backInterface.common.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author sunyujia@aliyun.com https://github.com/yujiasun/Distributed-Kit
 * @date 2016/2/26
 */
public interface DistributedReentrantLock {

    /**
     * 上锁
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException;


    /**
     * 解锁
     */
    void unlock();
}
