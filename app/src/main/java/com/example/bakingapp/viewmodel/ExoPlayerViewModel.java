package com.example.bakingapp.viewmodel;

import android.content.Context;
import android.net.Uri;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import androidx.lifecycle.ViewModel;

import com.example.bakingapp.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class ExoPlayerViewModel extends ViewModel {

    private MutableLiveData<SimpleExoPlayer> mSimpleExoPlayer;
    private Context mContext;
    private String mRecipeUriStr;
    private long mContentPosition = 0L;
    private boolean mPlayWhenReady = true;

    public void setmSimpleExoPlayer(MutableLiveData<SimpleExoPlayer> mSimpleExoPlayer) {
        this.mSimpleExoPlayer = mSimpleExoPlayer;
    }

    public ExoPlayerViewModel(Context context, String recipeUriStr) {
      mContext = context;
      mRecipeUriStr = recipeUriStr;

    }
    public LiveData<SimpleExoPlayer> getSimpleExoPlayer() {
        setupPlayer(mRecipeUriStr);
        return mSimpleExoPlayer;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        releaseExoPlayer();
       // ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
    }

    private void setupPlayer(String recipeUriStr) {

        if(recipeUriStr == null || recipeUriStr.isEmpty()){
            return;
        }
        if (mSimpleExoPlayer == null){
            SimpleExoPlayer player = new SimpleExoPlayer.Builder(mContext).build();
            Uri recipeUri = Uri.parse(recipeUriStr);
            MediaSource mediaSource = buildMediaSource(recipeUri);
            player.prepare(mediaSource);
            player.setPlayWhenReady(mPlayWhenReady);
            player.seekTo(mContentPosition);
            mSimpleExoPlayer = new MutableLiveData<SimpleExoPlayer>(player);
        }
    }

    private MediaSource buildMediaSource(Uri recipeUri){
        String userAgent = Util.getUserAgent(mContext, mContext.getString(R.string.app_name));
        DataSource.Factory factory = new DefaultHttpDataSourceFactory(userAgent);
        MediaSource videoSource = new ProgressiveMediaSource.Factory(factory).createMediaSource(recipeUri);

        return videoSource;
    }

    private void releaseExoPlayer(){
        if(mSimpleExoPlayer == null)
        {
            return;
        }
        mContentPosition = mSimpleExoPlayer.getValue().getContentPosition();
        mPlayWhenReady = mSimpleExoPlayer.getValue().getPlayWhenReady();
        mSimpleExoPlayer.getValue().release();
        mSimpleExoPlayer .setValue(null);
        mSimpleExoPlayer = null;
    }

}
