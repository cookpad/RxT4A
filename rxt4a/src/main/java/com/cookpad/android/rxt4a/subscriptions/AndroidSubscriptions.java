package com.cookpad.android.rxt4a.subscriptions;

import android.os.Handler;
import android.os.Looper;

import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * A subscription factory for Android applications.
 */
public class AndroidSubscriptions {

    /**
     * Just like {@code Subscriptins#create(Action0 unsubscribe)} but {@code unsubscribe} is called on the main thread.
     *
     * @param unsubscribe An action called in unsubscribe.
     */
    public static Subscription unsubscribeOnMainThread(final Action0 unsubscribe) {
        return Subscriptions.create(new Action0() {
            @Override
            public void call() {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    unsubscribe.call();
                } else {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            unsubscribe.call();
                        }
                    });
                }
            }
        });
    }
}
