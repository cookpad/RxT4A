package com.cookpad.android.rxt4a;

import com.cookpad.android.rxt4a.subscriptions.AndroidCompositeSubscription;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.os.Build;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observers.TestSubscriber;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN, manifest = Config.NONE)
public class AndroidCompositionSubscriptionTest {

    @Test
    public void addSubscription() throws Exception {
        final AndroidCompositeSubscription s = new AndroidCompositeSubscription();

        TestSubscriber<String> ts1 = new TestSubscriber<>();
        TestSubscriber<String> ts2 = new TestSubscriber<>();

        s.add(Observable.just("foo")
                .delay(5, TimeUnit.MILLISECONDS)
                .subscribe(ts1));
        s.add(Observable.just("bar")
                .delay(5, TimeUnit.MILLISECONDS)
                .subscribe(ts2));

        s.unsubscribe();

        ts1.awaitTerminalEvent(10, TimeUnit.MILLISECONDS);
        ts1.assertNoTerminalEvent();

        ts2.awaitTerminalEvent(10, TimeUnit.MILLISECONDS);
        ts2.assertNoTerminalEvent();
    }

    @Test
    public void reuseAndroidCompositeSubscription() throws Exception {
        AndroidCompositeSubscription s = new AndroidCompositeSubscription();

        // 1st time with unsubscribe()
        {
            TestSubscriber<String> ts = new TestSubscriber<>();

            s.add(Observable.just("foo")
                    .delay(5, TimeUnit.MILLISECONDS)
                    .subscribe(ts));

            s.unsubscribe();

            ts.awaitTerminalEvent(10, TimeUnit.MILLISECONDS);
            ts.assertNoTerminalEvent();
        }

        // 2nd time without unsubscribe(); it should work!
        {
            TestSubscriber<String> ts = new TestSubscriber<>();

            s.add(Observable.just("foo")
                    .delay(5, TimeUnit.MILLISECONDS)
                    .subscribe(ts));

            ts.awaitTerminalEvent(10, TimeUnit.MILLISECONDS);
            ts.assertTerminalEvent();
        }

        // 3rd time with unsubscribe()
        {
            TestSubscriber<String> ts = new TestSubscriber<>();

            s.add(Observable.just("foo")
                    .delay(5, TimeUnit.MILLISECONDS)
                    .subscribe(ts));

            s.unsubscribe();

            ts.awaitTerminalEvent(10, TimeUnit.MILLISECONDS);
            ts.assertNoTerminalEvent();
        }
    }

}
