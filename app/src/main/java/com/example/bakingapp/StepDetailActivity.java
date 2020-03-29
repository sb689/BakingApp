package com.example.bakingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.bakingapp.databinding.ActivityStepDetailBinding;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;

public class StepDetailActivity extends AppCompatActivity
{

    private static final String TAG = StepDetailActivity.class.getName();

    private ActivityStepDetailBinding mDataBinding;
    private Recipe mRecipe;
    private int mStepPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);

        boolean isTabletView = getResources().getBoolean(R.bool.isLarge);


        if(savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            mRecipe = bundle.getParcelable(getString(R.string.bundle_extra_recipe_obj));

            String key = intent.getStringExtra(getString(R.string.bundle_extra_key));

            if (key.equals(getString(R.string.bundle_extra_value_ingredient))) {
                showIngredientDetail();
            }
            else if (key.equals(getString(R.string.bundle_extra_value_step))) {
                mStepPosition = bundle.getInt(getString(R.string.bundle_extra_step_position));
                showStepDetail();
            }

        }else{
            mStepPosition =  savedInstanceState.getInt(getString(R.string.bundle_extra_step_position));
            mRecipe = savedInstanceState.getParcelable(getString(R.string.bundle_extra_recipe_obj));

        }
        checkButtonStates(mStepPosition);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mRecipe.getName());
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(getString(R.string.bundle_extra_step_position), mStepPosition);
        outState.putParcelable(getString(R.string.bundle_extra_recipe_obj), mRecipe);

        super.onSaveInstanceState(outState);

    }

    private void showStepDetail(){

        Step step = mRecipe.getSteps()[mStepPosition];
        StepFragment stepFragment = new StepFragment();
        stepFragment.setmStep(step);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_steps, stepFragment)
                .commit();
        checkButtonStates(mStepPosition);
    }

    public void showIngredientDetail()
    {
        hideButtons();
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setmIngredient(mRecipe.getIngredients());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_steps, ingredientsFragment)
                .commit();
    }

    private void updateStepFragment(int position){

        Step step = mRecipe.getSteps()[mStepPosition];
        StepFragment stepFragment = new StepFragment();
        stepFragment.setmStep(step);


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_steps, stepFragment)
                .commit();
        checkButtonStates(position);

    }

    private void checkButtonStates(int position){
        if((position + 1) > (mRecipe.getSteps().length - 1) ){
            mDataBinding.buttonNextStep.setVisibility(View.GONE);
        }
        else if(position - 1 < 0){
            mDataBinding.buttonPrevStep.setVisibility(View.GONE);
        }
        else{
            mDataBinding.buttonNextStep.setVisibility(View.VISIBLE);
            mDataBinding.buttonPrevStep.setVisibility(View.VISIBLE);
        }
    }

    public void onForwardClicked(View view){

        mStepPosition += 1;
        updateStepFragment(mStepPosition);
    }

    public void onBackClicked(View view){

       mStepPosition -= 1;
       updateStepFragment(mStepPosition);
    }


    private void hideButtons(){
        mDataBinding.buttonNextStep.setVisibility(View.GONE);
        mDataBinding.buttonPrevStep.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
