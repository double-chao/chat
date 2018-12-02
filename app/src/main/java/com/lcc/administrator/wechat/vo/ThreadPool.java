package com.lcc.administrator.wechat.vo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    public static final int THREAD_LIMiT_COUNT = 6;

    public static ExecutorService LIMIT_TASK_EXECUTOR;

    static {
        LIMIT_TASK_EXECUTOR= Executors.newFixedThreadPool(THREAD_LIMiT_COUNT);
    }
}
