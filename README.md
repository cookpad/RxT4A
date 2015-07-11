# NAME

RxT4A - RxJava Toolbox for Android

# SYNOPSIS

```java
// AndroidSchedulers

Scheduler scheduler = AndroidSchedulers.mainThread();
Scheduler scheduler = AndroidSchedulers.from(new Handler());

// AndroidCompositeSubscription

final AndroidCompositeSubscription s = new AndroidCompositeSubscription();

s.add(subscription1);
s.add(subscription2);

s.unsubscribe(); // unsubscribe 1 and 2

// it is reusable!
s.add(subscription3);
s.add(subscription4);

s.unsubscribe(); // unsubscribe 3 and 4, too!
```


# DESCRIPTION

This is a fork of [RxAndroid](https://github.com/ReactiveX/RxAndroid).

## AndroidSchedulers

This is almost the same as RxAndroid's `AndroidSchedulers`.

## AndroidCompositeSubscription

This is a variation of `CompositeSubscription` but can be reused multiple times.

# LICENSE

The MIT License

# AUTHOR

Cookpad Inc.

