package com.example.bakingapp;


import android.net.Uri;
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
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.net.SocketImpl;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = StepFragment.class.getName();
    private static final String BUNDLE_EXOPLAYER_POSITION = "exoplayer_position";
    private static final String  BUNDLE_EXOPLAYER_STATE = "exoplayer_state";

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
            mPlayerPosition = savedInstanceState.getLong(BUNDLE_EXOPLAYER_POSITION);
            mPlayerState = savedInstanceState.getBoolean(BUNDLE_EXOPLAYER_STATE);
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
            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            String recipeUriStr = mStep.getVideoURL();
            if(recipeUriStr.isEmpty()){
                recipeUriStr = mStep.getThumbnailURL();
            }
            if(recipeUriStr.isEmpty())
                return;

            if (mSimpleExoPlayer == null){
                mSimpleExoPlayer = new SimpleExoPlayer.Builder(getContext()).build();
                Uri recipeUri = Uri.parse(recipeUriStr);
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
        outState.putLong(BUNDLE_EXOPLAYER_POSITION, mPlayerPosition);
        outState.putBoolean(BUNDLE_EXOPLAYER_STATE, mPlayerState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
    }



    @Override
    public void onStop() {
        super.onStop();
        if(mSimpleExoPlayer != null){
            mPlayerPosition = mSimpleExoPlayer.getContentPosition();
            mPlayerState = mSimpleExoPlayer.getPlayWhenReady();
            releaseExoPlayer();
        }

    }

    private void releaseExoPlayer(){
        mSimpleExoPlayer.stop();
        mSimpleExoPlayer.release();
        mSimpleExoPlayer = null;
    }

}
