package com.example.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.media.session.MediaButtonReceiver;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.databinding.FragmentStepBinding;
import com.example.bakingapp.model.Step;
import com.example.bakingapp.utiils.DisplayUtils;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.spherical.SphericalGLSurfaceView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = StepFragment.class.getName();
    private static final String STEP_BUNDLE = "step_bundle";
    private static final String EXOPLAYER_POSITION = "exoplayer_position";
    private static final String EXOPLAYER_STATE = "exoplayer_state";

    private FragmentStepBinding mDataBinding;
    private SimpleExoPlayer mSimpleExoPlayer;
    private Step mStep;
    private boolean mTabletView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private PlayerView mPlayerView;
    private int mScreenWidth;
    private int mScreenHeight;

    public void setmStep(Step mStep) {
        this.mStep = mStep;
    }

    public void setmTabletView(boolean mTabletView) {
        this.mTabletView = mTabletView;
    }

    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mDataBinding = FragmentStepBinding.inflate(inflater,container,false);
        mPlayerView = mDataBinding.videoExoView;
        Log.d(TAG, ":::::::::::::: inside onCreate, sdk version is =" + Util.SDK_INT);
        if(savedInstanceState != null){
            mStep = savedInstanceState.getParcelable(STEP_BUNDLE);
        }else {
            mDataBinding.tvStepDetailDescription.setText(mStep.getDescription());
        }
        //initializePlayer();
        InitializeMediaSession();

        return mDataBinding.getRoot();
    }




    private void initializePlayer(){
        if (mStep.getVideoURL() == null &&
                mStep.getVideoURL().isEmpty() &&
                mStep.getThumbnailURL() == null &&
                mStep.getThumbnailURL().isEmpty()) {
            return;
        }
        if(mSimpleExoPlayer == null) {

            mSimpleExoPlayer = new SimpleExoPlayer.Builder(getActivity()).build();
            //mSimpleExoPlayer = new SimpleExoPlayer.Builder(getContext()).build();
            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            Configuration config = getResources().getConfiguration();
            updateViewWithConfigurationChange(config);
            mPlayerView.setPlayer(mSimpleExoPlayer);
            String recipeUriStr = mStep.getVideoURL();
            if(recipeUriStr.isEmpty()){
                recipeUriStr = mStep.getThumbnailURL();
            }
            Uri recipeUri = Uri.parse(recipeUriStr);
            MediaSource mediaSource = buildMediaSource(recipeUri);
            mSimpleExoPlayer.prepare(mediaSource);
            mSimpleExoPlayer.setPlayWhenReady(true);
            mSimpleExoPlayer.addListener(this);
        }

    }

    private MediaSource buildMediaSource(Uri recipeUri){
        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
        DataSource.Factory factory = new DefaultHttpDataSourceFactory(userAgent);
        MediaSource videoSource = new ProgressiveMediaSource.Factory(factory).createMediaSource(recipeUri);

        return videoSource;
    }

    private void InitializeMediaSession(){
        mMediaSession = new MediaSessionCompat(this.getContext(), TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY|
                        PlaybackStateCompat.ACTION_PAUSE|
                        PlaybackStateCompat.ACTION_PLAY_PAUSE|
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS|
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if(playbackState == ExoPlayer.STATE_READY && playWhenReady){
            mStateBuilder.setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    mSimpleExoPlayer.getContentPosition(),
                    1f);
        }else if(ExoPlayer.STATE_READY == playbackState){
            mStateBuilder.setState(
                    PlaybackStateCompat.STATE_PAUSED,
                    mSimpleExoPlayer.getContentPosition(),
                    1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }


    public static class MediaReceiver extends BroadcastReceiver {


        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    private class MySessionCallback extends MediaSessionCompat.Callback{

        @Override
        public void onPlay() {
            mSimpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
          mSimpleExoPlayer.setPlayWhenReady(false);
        }
    }



    private void updateViewWithConfigurationChange(Configuration newConfig){
        Log.d(TAG, ":::::::::::::::::::::::::: inside updateViewWithConfigurationChange, mTabletView = " + mTabletView);
        DisplayUtils.getScreenSize(getActivity());

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && !mTabletView) {
            Log.d(TAG, ":::::::::::::::::::::::::: inside ORIENTATION_LANDSCAPE && !mTabletView");
            mDataBinding.videoExoView.getLayoutParams().height = DisplayUtils.mScreenHeight;
        }
        else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE && mTabletView){
            Log.d(TAG, ":::::::::::::::::::::::::: inside .ORIENTATION_LANDSCAPE && mTabletView");
            int height = DisplayUtils.mScreenHeight -500;
            int width = DisplayUtils.mScreenWidth-400;
            mDataBinding.videoExoView.getLayoutParams().height = height;
            mDataBinding.videoExoView.getLayoutParams().width= width;
        }
        else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && mTabletView){
            int height = DisplayUtils.mScreenHeight / 2;
            int width = DisplayUtils.mScreenWidth-400;
            mDataBinding.videoExoView.getLayoutParams().height = height;
            mDataBinding.videoExoView.getLayoutParams().width= width;
        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT && !mTabletView){
            int height = 500;
            int width = DisplayUtils.mScreenWidth;
            mDataBinding.videoExoView.getLayoutParams().height = height;
            mDataBinding.videoExoView.getLayoutParams().width= width;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(STEP_BUNDLE, mStep);
        if(mSimpleExoPlayer != null){
            outState.putLong(EXOPLAYER_POSITION, mSimpleExoPlayer.getCurrentPosition());
            outState.putBoolean(EXOPLAYER_STATE, mSimpleExoPlayer.getPlayWhenReady());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            Log.d(TAG, "::::::::::::::::::; called initializePlayer from onStart");
            initializePlayer();
            if (mPlayerView != null) {
                mPlayerView.onResume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mSimpleExoPlayer == null) {
            Log.d(TAG, "::::::::::::::::::; called initializePlayer from onResume");
            initializePlayer();
            if (mPlayerView != null) {
                mPlayerView.onResume();
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            Log.d(TAG, "::::::::::::::::::; release player from onPause");
            if (mPlayerView != null) {
                mPlayerView.onPause();
            }
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            Log.d(TAG, "::::::::::::::::::; release player from onPause");
            if (mPlayerView != null) {
                mPlayerView.onPause();
            }
            releasePlayer();
        }
    }

    private void releasePlayer(){
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.removeListener(this);
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
        mMediaSession.setActive(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      if(getActivity().isChangingConfigurations()){
          return;
      }
      releasePlayer();
    }

}
