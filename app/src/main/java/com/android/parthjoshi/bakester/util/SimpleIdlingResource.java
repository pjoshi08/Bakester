package com.android.parthjoshi.bakester.util;


import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleIdlingResource implements IdlingResource {

    @Nullable private volatile ResourceCallback mCallback;

    // Idleness is controlled by this boolean
    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIsIdleState(boolean isIdleNow){
        mIsIdleNow.set(isIdleNow);

        try {
            if(isIdleNow && mCallback != null)
                mCallback.onTransitionToIdle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
