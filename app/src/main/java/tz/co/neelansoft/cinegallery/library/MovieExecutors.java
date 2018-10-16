package tz.co.neelansoft.cinegallery.library;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieExecutors {
    private static final Object EXECUTOR_LOCK = new Object();
    private static MovieExecutors sExecutorInstance;
    private final Executor diskIO;
    private final Executor networkIO;
    private final Executor mainThread;

    //private constructor
    private MovieExecutors(Executor diskIO, Executor networkIO, Executor mainThread){
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    //get executor instance
    public static MovieExecutors getInstance(){
        if(sExecutorInstance == null){
            synchronized (EXECUTOR_LOCK){
                sExecutorInstance = new MovieExecutors(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3), new MainThreadExecutor());

            }
        }
        return sExecutorInstance;
    }

    public Executor diskIO(){
        return diskIO;
    }
    public Executor networkIO(){
        return networkIO;
    }
    public Executor mainThread(){
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor{

        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
