package com.yupi.springbootinit.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadPoolExecutorConfig {
    // 创建一个自定义的线程工厂，用于为线程命名
    ThreadFactory threadFactory = new ThreadFactory() {
        private int count=1; // 计数器，用于生成线程名

        @Override
        public Thread newThread(@NotNull Runnable r) {
            // 创建一个新的线程，并使用计数器生成线程名
            return new Thread(r,"thread-"+count++);
        }
    };

    /**
     * 线程池配置
     *
     * @return 配置好的ThreadPoolExecutor实例
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        // 核心线程数为2，最大线程数为4，线程闲置100秒后回收，
        // 使用LinkedBlockingQueue作为工作队列，使用自定义线程工厂创建线程
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,
                4,
                100,
                java.util.concurrent.TimeUnit.SECONDS,
                new java.util.concurrent.LinkedBlockingQueue<>(4),
                threadFactory
        );
        return threadPoolExecutor;
    }
}
