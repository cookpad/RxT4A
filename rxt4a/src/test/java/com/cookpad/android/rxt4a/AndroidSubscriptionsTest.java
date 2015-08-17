package com.cookpad.android.rxt4a;

import com.cookpad.android.rxt4a.subscriptions.AndroidSubscriptions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.functions.Action0;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN, manifest = Config.NONE)
public class AndroidSubscriptionsTest {


    @Test
    public void testUnsubscribeOnMainThread_onMainThread() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                final Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
                    @Override
                    public void call() {
                        assertThat(Looper.myLooper(), is(Looper.getMainLooper()));
                        latch.countDown();
                    }
                });
                subscription.unsubscribe();
            }
        });

        assertThat(latch.await(1, TimeUnit.SECONDS), is(true));
    }

    @Test
    public void testUnsubscribeOnMainThread_onTestingThread() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        final Subscription subscription = AndroidSubscriptions.unsubscribeOnMainThread(new Action0() {
            @Override
            public void call() {
                assertThat(Looper.myLooper(), is(Looper.getMainLooper()));
                latch.countDown();
            }
        });
        subscription.unsubscribe();

        assertThat(latch.await(1, TimeUnit.SECONDS), is(true));
    }
}
