package com.example.bakingapp.utiils;

import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;



public class SimpleIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback mCallBack;
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
        mCallBack = callback;
    }

    public void setIdleState(boolean isIdleNow){
        mIsIdleNow.set(isIdleNow);
        if(mIsIdleNow != null && mCallBack != null){
            mCallBack.onTransitionToIdle();
        }
    }
}
