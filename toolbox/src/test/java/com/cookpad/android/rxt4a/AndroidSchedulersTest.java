package com.cookpad.android.rxt4a;

import com.cookpad.android.rxt4a.schedulers.AndroidSchedulers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
public class AndroidSchedulersTest {

    @Test
    public void testMainThreadSchedulers() throws Exception {
        assertThat(AndroidSchedulers.mainThread().getHandler().getLooper(),
                is(Looper.getMainLooper()));
    }

    @Test
    public void testCreateHandlerScheduler() throws Exception {
        Handler handler = new Handler();
        assertThat(AndroidSchedulers.from(handler).getHandler(), is(handler));
    }
}
