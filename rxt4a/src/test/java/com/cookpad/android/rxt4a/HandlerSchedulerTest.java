package com.cookpad.android.rxt4a;

import com.cookpad.android.rxt4a.schedulers.AndroidSchedulers;
import com.cookpad.android.rxt4a.schedulers.HandlerScheduler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLooper;

import android.os.Build;
import android.os.Handler;

import rx.Observable;
import rx.Scheduler;
import rx.Scheduler.Worker;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN, manifest = Config.NONE)
@SuppressWarnings("unchecked")
public class HandlerSchedulerTest {

    @Test
    public void shouldScheduleImmediateActionOnHandlerThread() {
        Handler handler = mock(Handler.class);
        Action0 action = mock(Action0.class);

        Scheduler scheduler = AndroidSchedulers.from(handler);
        Worker inner = scheduler.createWorker();
        inner.schedule(action);

        // verify that we post to the given Handler
        ArgumentCaptor<Runnable> runnable = ArgumentCaptor.forClass(Runnable.class);
        verify(handler).post(runnable.capture());

        // verify that the given handler delegates to our action
        runnable.getValue().run();
        verify(action).call();
    }

    @Test
    public void shouldScheduleDelayedActionOnHandlerThread() {
        Handler handler = mock(Handler.class);
        Action0 action = mock(Action0.class);

        Scheduler scheduler = AndroidSchedulers.from(handler);
        Worker inner = scheduler.createWorker();
        inner.schedule(action, 1, SECONDS);

        // verify that we post to the given Handler
        ArgumentCaptor<Runnable> runnable = ArgumentCaptor.forClass(Runnable.class);
        verify(handler).postDelayed(runnable.capture(), eq(1000L));

        // verify that the given handler delegates to our action
        runnable.getValue().run();
        verify(action).call();
    }

    @Test
    public void shouldRemoveCallbacksFromHandlerWhenUnsubscribedSubscription() {
        Handler handler = spy(new Handler());
        Observable.OnSubscribe<Integer> onSubscribe = mock(Observable.OnSubscribe.class);
        Subscription subscription = Observable.create(onSubscribe)
                .subscribeOn(AndroidSchedulers.from(handler))
                .subscribe();

        verify(onSubscribe).call(any(Subscriber.class));

        subscription.unsubscribe();

        verify(handler).removeCallbacks(any(Runnable.class));
    }

    @Test
    public void shouldNotCallOnSubscribeWhenSubscriptionUnsubscribedBeforeDelay() {
        Observable.OnSubscribe<Integer> onSubscribe = mock(Observable.OnSubscribe.class);
        Handler handler = spy(new Handler());

        final Worker worker = spy(new HandlerScheduler.HandlerWorker(handler));
        Scheduler scheduler = new Scheduler() {
            @Override
            public Worker createWorker() {
                return worker;
            }
        };

        Subscription subscription = Observable.create(onSubscribe)
                .delaySubscription(1, MINUTES, scheduler)
                .subscribe();

        verify(worker).schedule(any(Action0.class), eq(1L), eq(MINUTES));
        verify(handler).postDelayed(any(Runnable.class), eq(MINUTES.toMillis(1)));

        subscription.unsubscribe();

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        verify(onSubscribe, never()).call(any(Subscriber.class));
        verify(handler).removeCallbacks(any(Runnable.class));
    }
}