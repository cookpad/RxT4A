package com.cookpad.android.rxt4a.schedulers;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Set of Android-specific schedulers
 */
public class AndroidSchedulers {

    static final HandlerScheduler MAIN_THREAD_SCHEDULER = from(new Handler(Looper.getMainLooper()));

    private AndroidSchedulers() {
    }

    /**
     * A {@link Scheduler} which executes actions on the Android UI thread.
     */
    public static HandlerScheduler mainThread() {
        return MAIN_THREAD_SCHEDULER;
    }

    public static HandlerScheduler from(@NonNull Handler handler) {
        return new HandlerScheduler(handler);
    }
}