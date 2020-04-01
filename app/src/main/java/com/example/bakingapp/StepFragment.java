package com.example.bakingapp;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.databinding.FragmentStepBinding;
import com.example.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayer;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;



/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = StepFragment.class.getName();


    private FragmentStepBinding mDataBinding;
    private SimpleExoPlayer mSimpleExoPlayer;
    private Step mStep;
    private PlayerView mPlayerView;
    private long mPlayerPosition = 0L;
    private boolean mPlayerState = true;

    public void setmStep(Step mStep) {
        this.mStep = mStep;
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

        if(savedInstanceState != null){
            mStep = savedInstanceState.getParcelable(getString(R.string.bundle_step));
            mPlayerPosition = savedInstanceState.getLong(getString(R.string.bundle_exoplayer_position));
            mPlayerState = savedInstanceState.getBoolean(getString(R.string.bundle_exoplayer_state));
        }
        mDataBinding.tvStepDetailDescription.setText(mStep.getDescription());
        return mDataBinding.getRoot();
    }


    private void initializePlayer(){

        if (mStep.getVideoURL() == null &&
                mStep.getThumbnailURL() == null){
            return;
        }
        if(mSimpleExoPlayer == null) {
           // mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            String recipeUriStr = mStep.getVideoURL();
            if(recipeUriStr.isEmpty()){
                recipeUriStr = mStep.getThumbnailURL();
            }
            if(recipeUriStr.isEmpty())
                return;

            if (mSimpleExoPlayer == null){

                Uri recipeUri = Uri.parse(recipeUriStr);

                DefaultTrackSelector trackSelector = new DefaultTrackSelector(getContext());
                trackSelector.setParameters(
                        trackSelector.buildUponParameters().setMaxVideoSizeSd());
                SimpleExoPlayer.Builder builder = new SimpleExoPlayer.Builder(getContext());
                builder.setTrackSelector(trackSelector);
                mSimpleExoPlayer = builder.build();

                MediaSource mediaSource = buildMediaSource(recipeUri);
                mSimpleExoPlayer.prepare(mediaSource);
                mSimpleExoPlayer.setPlayWhenReady(mPlayerState);
                mSimpleExoPlayer.seekTo(mPlayerPosition);
                mPlayerView.setPlayer(mSimpleExoPlayer);
            }
        }
    }


    private MediaSource buildMediaSource(Uri recipeUri){
        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
        DataSource.Factory factory = new DefaultHttpDataSourceFactory(userAgent);
        MediaSource videoSource = new ProgressiveMediaSource.Factory(factory).createMediaSource(recipeUri);

        return videoSource;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(getString(R.string.bundle_step), mStep);
        if(Util.SDK_INT < Build.VERSION_CODES.P) {
            savePlayerState();
        }
        outState.putLong(getString(R.string.bundle_exoplayer_position), mPlayerPosition);
        outState.putBoolean(getString(R.string.bundle_exoplayer_state), mPlayerState);
        super.onSaveInstanceState(outState);
    }

    private void savePlayerState() {
        if (mSimpleExoPlayer != null) {
            mPlayerPosition = mSimpleExoPlayer.getContentPosition();
            mPlayerState = mSimpleExoPlayer.getPlayWhenReady();
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        if(Util.SDK_INT >= 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Util.SDK_INT < 23 ){
            if(mSimpleExoPlayer == null) {
                initializePlayer();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Util.SDK_INT < 23 ){
            if(mSimpleExoPlayer != null){
                releaseExoPlayer();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Util.SDK_INT >= 23 && mSimpleExoPlayer != null){
            if(Util.SDK_INT >= Build.VERSION_CODES.P){
                savePlayerState();
            }
            releaseExoPlayer();
        }
    }

    private void releaseExoPlayer(){
       mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
    }

}
