package com.cookpad.android.rxt4a.subscriptions;

import com.cookpad.android.rxt4a.operators.OperatorAddToCompositeSubscription;

import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Reusable variant of {@link CompositeSubscription}
 *
 * @see <a href="https://github.com/ReactiveX/RxJava/issues/2959">Why doesn't CompositeSubscription have a way to re-initialize
 * its instance? #2959</a>
 */
public class AndroidCompositeSubscription {

    CompositeSubscription compositeSubscription;

    public void add(@NonNull Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    public void unsubscribe() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            compositeSubscription = null;
        }
    }

    public <T> OperatorAddToCompositeSubscription<T> createOperatorAddSubscription() {
        return new OperatorAddToCompositeSubscription<>(this);
    }
}
