// forked from https://github.com/ReactiveX/RxJava/blob/1.x/src/main/java/rx/internal/operators/OperatorFilter.java
package com.cookpad.android.rxt4a.operators;

import com.cookpad.android.rxt4a.functions.Predicate;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;

/**
 * A variant of {@link rx.internal.operators.OperatorFilter} that uses {@code boolean} instead of {@code Boolean}.
 */
public abstract class OperatorFilterLite<T> implements Observable.Operator<T, T>, Predicate<T> {

    public abstract boolean test(T value);

    @Override
    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        return new Subscriber<T>(child) {

            @Override
            public void onCompleted() {
                child.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                child.onError(e);
            }

            @Override
            public void onNext(T t) {
                try {
                    if (test(t)) {
                        child.onNext(t);
                    } else {
                        // TODO consider a more complicated version that batches these
                        request(1);
                    }
                } catch (Throwable e) {
                    child.onError(OnErrorThrowable.addValueAsLastCause(e, t));
                }
            }

        };
    }

}
