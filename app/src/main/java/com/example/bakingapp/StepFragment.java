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
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayer;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;



/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment {

    private static final String TAG = StepFragment.class.getName();
    private static final String STEP_POSITION_KEY = "step_position";
    private static final String RECIPE_KEY = "recipe_key";
    private static final String TABLET_VIEW_KEY = "tablet_view_key";



    private FragmentStepBinding mDataBinding;
    private SimpleExoPlayer mSimpleExoPlayer;
    private Step mStep;
    private PlayerView mPlayerView;
    private long mPlayerPosition = 0L;
    private boolean mPlayerState = true;
    private int mStepPosition;
    private Recipe mRecipe;
    private boolean mTabletView;

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(int position, Recipe recipe, boolean isTabletView){
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putInt(STEP_POSITION_KEY, position);
        args.putParcelable(RECIPE_KEY, recipe);
        args.putBoolean(TABLET_VIEW_KEY, isTabletView);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mDataBinding = FragmentStepBinding.inflate(inflater,container,false);
        mPlayerView = mDataBinding.videoExoView;

        if(getArguments().containsKey(STEP_POSITION_KEY))
        {
            mStepPosition = getArguments().getInt(STEP_POSITION_KEY);
        }
        if(getArguments().containsKey(RECIPE_KEY))
        {
            mRecipe = getArguments().getParcelable(RECIPE_KEY);
        }
        if(getArguments().containsKey(TABLET_VIEW_KEY))
        {
           mTabletView = getArguments().getBoolean(TABLET_VIEW_KEY);
           if(mTabletView) {
                mDataBinding.buttonNextStep.setVisibility(View.INVISIBLE);
               mDataBinding.buttonPrevStep.setVisibility(View.INVISIBLE);
           }
           else{
               mDataBinding.buttonNextStep.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       onForwardClicked();
                   }
               });
               mDataBinding.buttonPrevStep.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       onBackClicked();
                   }
               });
               checkButtonStates(mStepPosition);
           }
        }

        if(savedInstanceState != null){
            mStepPosition = savedInstanceState.getInt(STEP_POSITION_KEY);
            mPlayerPosition = savedInstanceState.getLong(getString(R.string.bundle_exoplayer_position));
            mPlayerState = savedInstanceState.getBoolean(getString(R.string.bundle_exoplayer_state));
        }

        mStep = mRecipe.getSteps()[mStepPosition];

        return mDataBinding.getRoot();

    }


    private void initializePlayer(){

        if (mStep.getVideoURL() == null &&
                mStep.getThumbnailURL() == null){
            return;
        }
        mDataBinding.tvStepDetailDescription.setText(mStep.getDescription());

        //get recipeUri
        String recipeUriStr = mStep.getVideoURL();
        if(recipeUriStr.isEmpty()){
            recipeUriStr = mStep.getThumbnailURL();
        }

        if(recipeUriStr.isEmpty())
            return;
        Uri recipeUri = Uri.parse(recipeUriStr);

        if(mSimpleExoPlayer == null) {

            DefaultTrackSelector trackSelector = new DefaultTrackSelector(getContext());
            trackSelector.setParameters(
                    trackSelector.buildUponParameters().setMaxVideoSizeSd());
            SimpleExoPlayer.Builder builder = new SimpleExoPlayer.Builder(getContext());
            builder.setTrackSelector(trackSelector);
            mSimpleExoPlayer = builder.build();
        }

        MediaSource mediaSource = buildMediaSource(recipeUri);
        mSimpleExoPlayer.prepare(mediaSource);
        mSimpleExoPlayer.setPlayWhenReady(mPlayerState);
        mSimpleExoPlayer.seekTo(mPlayerPosition);
        mPlayerView.setPlayer(mSimpleExoPlayer);

    }




    private MediaSource buildMediaSource(Uri recipeUri){
        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
        DataSource.Factory factory = new DefaultHttpDataSourceFactory(userAgent);
        MediaSource videoSource = new ProgressiveMediaSource.Factory(factory).createMediaSource(recipeUri);

        return videoSource;
    }

    private void updateVideo(int position){

        if(mSimpleExoPlayer != null){
            mSimpleExoPlayer.stop(true);
        }
        mPlayerState = true;
        mPlayerPosition = 0L;

        mStep = mRecipe.getSteps()[mStepPosition];
        checkButtonStates(position);
        initializePlayer();
    }


    private void checkButtonStates(int position){
        if(mTabletView)
        {
            return;
        }
        if((position + 1) > (mRecipe.getSteps().length - 1) ){
            mDataBinding.buttonNextStep.setVisibility(View.INVISIBLE);
        }
        else if(position - 1 < 0){
            mDataBinding.buttonPrevStep.setVisibility(View.INVISIBLE);
        }
        else{
            mDataBinding.buttonNextStep.setVisibility(View.VISIBLE);
            mDataBinding.buttonPrevStep.setVisibility(View.VISIBLE);
        }
    }


    public void onForwardClicked(){
        Log.d(TAG,"next button click received");
        mStepPosition += 1;
        updateVideo(mStepPosition);
    }

    public void onBackClicked(){

        mStepPosition -= 1;
        updateVideo(mStepPosition);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
       outState.putInt(STEP_POSITION_KEY, mStepPosition);
      // outState.putParcelable(RECIPE_KEY, mRecipe);
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
