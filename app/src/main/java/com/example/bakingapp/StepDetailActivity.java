package com.example.bakingapp;

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
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;

public class StepDetailActivity extends AppCompatActivity
{

    private static final String TAG = StepDetailActivity.class.getName();
    private ActivityStepDetailBinding mDataBinding;
    private Recipe mRecipe;
    private Bundle mBundle;
    private int mStepPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);

        Intent intent = getIntent();
        mBundle = intent.getExtras();
        mRecipe = mBundle.getParcelable(RecipeDetailActivity.BUNDLE_EXTRA_RECIPE_DETAIL);
        String key = intent.getStringExtra(RecipeDetailActivity.BUNDLE_EXTRA_KEY);

        if(key.equals(RecipeDetailActivity.BUNDLE_EXTRA_INGREDIENT_VALUE)){

           showIngredientDetail();
        }
        else if(key.equals(RecipeDetailActivity.BUNDLE_EXTRA_STEP_VALUE)){
            showStepDetail();
        }
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mRecipe.getName());
        }

    }

    private void showStepDetail(){

        showButtons();

        mStepPosition = mBundle.getInt(RecipeDetailActivity.BUNDLE_EXTRA_STEP_POSITION);
        Step step = mRecipe.getSteps()[mStepPosition];
        StepFragment stepFragment = new StepFragment();
        stepFragment.setmStep(step);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container_steps, stepFragment)
                .commit();
        checkButtonStates(mStepPosition);
    }

    private void showIngredientDetail()
    {
        hideButtons();
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setmIngredient(mRecipe.getIngredients());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container_steps, ingredientsFragment)
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
        Log.d(TAG, "::::::::::::::::::;forwardButton clicked");
        mStepPosition += 1;
        updateStepFragment(mStepPosition);
    }

    public void onBackClicked(View view){
        Log.d(TAG, "::::::::::::::::::;backButton clicked");
       mStepPosition -= 1;
       updateStepFragment(mStepPosition);
    }

    private void showButtons(){
        mDataBinding.buttonNextStep.setVisibility(View.VISIBLE);
        mDataBinding.buttonPrevStep.setVisibility(View.VISIBLE);
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
