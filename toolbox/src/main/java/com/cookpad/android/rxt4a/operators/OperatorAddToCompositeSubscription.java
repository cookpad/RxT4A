package com.cookpad.android.rxt4a.operators;

import com.cookpad.android.rxt4a.subscriptions.AndroidCompositeSubscription;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;

/**
 * A kind of {@code Observable.Operator} to add a subscription to {@code AndroidCompositeSubscription}
 * in operator chains.
 *
 * For example, when <code>s</code> is {@code AndroidCompositeSubscription} ,
 * <code>observable.lift(new OperatorAddToCompositeSubscription<String>(s)).subscribe(...)</code>
 * is the same as <code>s.add(observable.subscribe(...)</code>.
 */
public class OperatorAddToCompositeSubscription<T> implements Observable.Operator<T, T> {

    final AndroidCompositeSubscription compositeSubscription;

    public OperatorAddToCompositeSubscription(@NonNull AndroidCompositeSubscription subscription) {
        this.compositeSubscription = subscription;
    }

    @Override
    public Subscriber<? super T> call(Subscriber<? super T> subscriber) {
        compositeSubscription.add(subscriber);
        return subscriber;
    }
}
