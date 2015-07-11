package com.cookpad.android.rxt4a;

import android.os.Looper;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class TestUtils {

    public static void assertMainThread() {
        assertThat(Looper.myLooper(), is(Looper.getMainLooper()));
    }

    public static void assertNotMainThread() {
        assertThat(Looper.myLooper(), is(not(Looper.getMainLooper())));
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
