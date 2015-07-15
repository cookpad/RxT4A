package com.cookpad.android.rxt4a.schedulers;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.schedulers.ScheduledAction;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public class HandlerScheduler extends Scheduler {

    final Handler handler;

    public HandlerScheduler(@NonNull Handler handler) {
        this.handler = handler;
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    public Scheduler.Worker createWorker() {
        return new HandlerWorker(handler);
    }

    public static class HandlerWorker extends Scheduler.Worker {

        final Handler handler;

        final CompositeSubscription compositeSubscription = new CompositeSubscription();

        public HandlerWorker(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void unsubscribe() {
            compositeSubscription.unsubscribe();
        }

        @Override
        public boolean isUnsubscribed() {
            return compositeSubscription.isUnsubscribed();
        }

        ScheduledAction createScheduledAction(Action0 action) {
            final ScheduledAction scheduledAction = new ScheduledAction(action);

            scheduledAction.add(Subscriptions.create(new Action0() {
                @Override
                public void call() {
                    handler.removeCallbacks(scheduledAction);
                }
            }));
            scheduledAction.addParent(compositeSubscription);
            compositeSubscription.add(scheduledAction);

            return scheduledAction;
        }

        @Override
        public Subscription schedule(@NonNull Action0 action, long delayTime, @NonNull TimeUnit unit) {
            final ScheduledAction scheduledAction = createScheduledAction(action);
            handler.postDelayed(scheduledAction, unit.toMillis(delayTime));
            return scheduledAction;
        }

        @Override
        public Subscription schedule(final Action0 action) {
            final ScheduledAction scheduledAction = createScheduledAction(action);
            handler.post(scheduledAction);
            return scheduledAction;
        }
    }
}
