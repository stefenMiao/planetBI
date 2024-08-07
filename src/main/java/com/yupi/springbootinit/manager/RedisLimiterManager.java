package com.yupi.springbootinit.manager;

import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 提供RedisLimiter限流服务
 */
@Service
public class RedisLimiterManager {
    @Resource
    private RedissonClient redissonClient;

    /**
     * 限流操作
     * @param key 区分不同的限流器，比如不同的用户id应该分别统计
     */
    public void doRateLimit(String key) {
        // 获取限流器 1s限制2次
        RRateLimiter rateLimiter=redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL,2,1, RateIntervalUnit.SECONDS);

        // 尝试获取令牌
        boolean tryAcquire = rateLimiter.tryAcquire();
        // 如果获取失败，则抛出异常
        if (!tryAcquire) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }
}
