// forked from https://github.com/ReactiveX/RxJava/blob/1.x/src/test/java/rx/internal/operators/OperatorFilterTest.java
package com.cookpad.android.rxt4a;

import com.cookpad.android.rxt4a.operators.OperatorFilterLite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.os.Build;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.internal.util.RxRingBuffer;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN, manifest = Config.NONE)
@SuppressWarnings("unchecked")
public class OperatorFilterLiteTest {

    @Test
    public void testFilter() throws Exception {
        Observable<String> w = Observable.just("one", "two", "three");
        Observable<String> observable = w.lift(new OperatorFilterLite<String>() {

            @Override
            public boolean test(String t1) {
                return t1.equals("two");
            }
        });

        Observer<String> observer = mock(Observer.class);
        observable.subscribe(observer);
        verify(observer, Mockito.never()).onNext("one");
        verify(observer, times(1)).onNext("two");
        verify(observer, Mockito.never()).onNext("three");
        verify(observer, Mockito.never()).onError(any(Throwable.class));
        verify(observer, times(1)).onCompleted();
    }

    static class TestException extends Exception {

    }

    @Test
    public void testOnError() throws Exception {
        Observable<String> w = Observable.error(new TestException());
        Observable<String> observable = w.lift(new OperatorFilterLite<String>() {

            @Override
            public boolean test(String t1) {
                return t1.equals("two");
            }
        });

        TestSubscriber<String> ts = new TestSubscriber<>();
        observable.subscribe(ts);

        ts.awaitTerminalEvent(10, TimeUnit.MILLISECONDS);

        ts.assertError(TestException.class);
    }

    /**
     * Make sure we are adjusting subscriber.request() for filtered items
     */
    @Test(timeout = 500)
    public void testWithBackpressure() throws Exception {
        Observable<String> w = Observable.just("one", "two", "three");
        Observable<String> o = w.lift(new OperatorFilterLite<String>() {

            @Override
            public boolean test(String t1) {
                return t1.equals("three");
            }
        });

        final CountDownLatch latch = new CountDownLatch(1);
        TestSubscriber<String> ts = new TestSubscriber<String>() {

            @Override
            public void onCompleted() {
                latch.countDown();
            }

            @Override
            public void onError(Throwable e) {
                latch.countDown();
            }

            @Override
            public void onNext(String t) {
                request(1);
            }

        };
        // this means it will only request "one" and "two", expecting to receive them before requesting more
        ts.requestMore(2);

        o.subscribe(ts);

        // this will wait forever unless OperatorTake handles the request(n) on filtered items
        latch.await();
    }

    /**
     * Make sure we are adjusting subscriber.request() for filtered items
     */
    @Test(timeout = 500000)
    public void testWithBackpressure2() throws Exception {
        Observable<Integer> w = Observable.range(1, RxRingBuffer.SIZE * 2);
        Observable<Integer> o = w.lift(new OperatorFilterLite<Integer>() {

            @Override
            public boolean test(Integer t1) {
                return t1 > 100;
            }
        });

        final CountDownLatch latch = new CountDownLatch(1);
        final TestSubscriber<Integer> ts = new TestSubscriber<Integer>() {

            @Override
            public void onCompleted() {
                latch.countDown();
            }

            @Override
            public void onError(Throwable e) {
                latch.countDown();
            }

            @Override
            public void onNext(Integer t) {
                request(1);
            }
        };
        // this means it will only request 1 item and expect to receive more
        ts.requestMore(1);

        o.subscribe(ts);

        // this will wait forever unless OperatorTake handles the request(n) on filtered items
        latch.await();
    }
}