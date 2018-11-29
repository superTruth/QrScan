package cn.truth.ibdrqlib.helpful;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/***************************************************************************************************
 *                                  Copyright (C), Truth.                                      *
 *                                                                              *
 ***************************************************************************************************
 * usage           : 
 * Version         : 1
 * Author          : Truth
 * Date            : 2018/8/3
 * Modify          : create file
 **************************************************************************************************/
public class IBDQRScanExecutor {
    private static IBDQRScanExecutor INSTANCE;

    private ScheduledExecutorService executorService;
    private IBDQRScanExecutor(){
        executorService = Executors.newScheduledThreadPool (5);
    }

    public static IBDQRScanExecutor getInstance(){
        if(INSTANCE == null){
            INSTANCE = new IBDQRScanExecutor();
        }
        return INSTANCE;
    }

    public ScheduledFuture<?> schedule(Runnable runnable, long delay){
        ScheduledFuture<?> scheduledFuture = executorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
        return scheduledFuture;
    }

    public void schedule(Runnable runnable){
        executorService.execute(runnable);
    }

}
