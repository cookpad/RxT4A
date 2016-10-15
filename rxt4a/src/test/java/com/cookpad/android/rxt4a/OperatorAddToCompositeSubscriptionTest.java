package com.cookpad.android.rxt4a;

import com.cookpad.android.rxt4a.operators.OperatorAddToCompositeSubscription;
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
public class OperatorAddToCompositeSubscriptionTest {

    @Test
    public void testOperatorAddToCompositeSubscription() throws Exception {
        AndroidCompositeSubscription s = new AndroidCompositeSubscription();

        TestSubscriber<String> ts = new TestSubscriber<>();

        Observable.just("foo")
                .lift(new OperatorAddToCompositeSubscription<String>(s))
                .delay(5, TimeUnit.MILLISECONDS)
                .subscribe(ts);

        s.unsubscribe();

        ts.awaitTerminalEvent(10, TimeUnit.MILLISECONDS);
        ts.assertNoTerminalEvent();
    }

    @Test
    public void testCreateOperator() throws Exception {
        AndroidCompositeSubscription s = new AndroidCompositeSubscription();

        TestSubscriber<String> ts = new TestSubscriber<>();

        Observable.just("foo")
                .lift(s.<String>createOperatorAddSubscription())
                .delay(5, TimeUnit.MILLISECONDS)
                .subscribe(ts);

        s.unsubscribe();

        ts.awaitTerminalEvent(10, TimeUnit.MILLISECONDS);
        ts.assertNoTerminalEvent();
    }

}
