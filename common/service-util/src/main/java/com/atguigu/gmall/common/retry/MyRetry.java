package com.atguigu.gmall.common.retry;

import feign.RetryableException;
import feign.Retryer;

/**
 * 自定义feign重试器
 */
public class MyRetry implements Retryer {
    @Override
    public void continueOrPropagate(RetryableException e) {
        //一次都不重试 失败直接停
        throw e;
    }

    @Override
    public Retryer clone() {
        return this;
    }
}
