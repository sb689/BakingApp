package com.example.bakingapp.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ExoPlayerViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Context mContext;
    private final String mRecipeUriStr;


    public ExoPlayerViewModelFactory(Context mContext, String mRecipeUriStr) {
        this.mContext = mContext;
        this.mRecipeUriStr = mRecipeUriStr;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ExoPlayerViewModel(mContext, mRecipeUriStr);
    }
}
