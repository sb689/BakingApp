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
import android.widget.LinearLayout;


import com.example.bakingapp.databinding.ActivityRecipeDetailBinding;
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.example.bakingapp.utiils.DisplayUtils;

public class RecipeDetailActivity extends AppCompatActivity implements MasterListFragment.IngredientsClickedListener,
                MasterListFragment.StepClickedListenerForward{


    private ActivityRecipeDetailBinding mDataBinding;
    private static final String TAG = RecipeDetailActivity.class.getName();
    public static final String BUNDLE_EXTRA_RECIPE_DETAIL = "bundle_extra_recipe_detail";
    public static final String BUNDLE_EXTRA_KEY = "extra_key";
    public static final String BUNDLE_EXTRA_INGREDIENT_VALUE = "ingredient";
    public static final String BUNDLE_EXTRA_STEP_VALUE = "step";
    public static final String BUNDLE_EXTRA_STEP_POSITION = "step_position";
    public static final String BUNDLE_EXTRA_STEP_TABLET_VIEW = "step_tablet";

    private static Recipe mRecipe;
    private boolean mTabletView;


    public static Recipe getRecipe(){
        return mRecipe;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            mRecipe = bundle.getParcelable(MainActivity.BUNDLE_EXTRA);
        }else{

            mRecipe = savedInstanceState.getParcelable(BUNDLE_EXTRA_RECIPE_DETAIL);
        }
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);


        ActionBar actionBar = getSupportActionBar();
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mRecipe.getName());
        }
        try {
            LinearLayout linearLayout = mDataBinding.linearLayoutRecipeDetailTablet;

            if (linearLayout == null) {
                Log.d(TAG, ":::::::::::::::::::::::::inside recipeDetailActivity, tabletLayout is null");
                mTabletView = false;
            } else {
                mTabletView = true;
                Log.d(TAG, ":::::::::::::::::::::::::inside recipeDetailActivity, tabletLayout is not null");
                showIngredientDetail();
            }
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipe =  savedInstanceState.getParcelable(BUNDLE_EXTRA_RECIPE_DETAIL);
    }

    @Override
    public void ingredientsClicked() {

        if(!mTabletView) {
            Log.d(TAG, "::::::::::::::; inside ingredientsClicked, mTableVIew is false");
            Intent intent = new Intent(this, StepDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(BUNDLE_EXTRA_RECIPE_DETAIL, mRecipe);
            bundle.putString(BUNDLE_EXTRA_KEY, BUNDLE_EXTRA_INGREDIENT_VALUE);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            //when the view is tabletView
            Log.d(TAG, "::::::::::::::; inside ingredientsClicked, mTableVIew is true");
            showIngredientDetail();
        }
    }


    @Override
    public void stepClickedForward(int position) {

        if(!mTabletView) {
            Intent intent = new Intent(this, StepDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(BUNDLE_EXTRA_RECIPE_DETAIL, mRecipe);
            bundle.putString(BUNDLE_EXTRA_KEY, BUNDLE_EXTRA_STEP_VALUE);
            bundle.putInt(BUNDLE_EXTRA_STEP_POSITION, position);
            bundle.putBoolean(BUNDLE_EXTRA_STEP_TABLET_VIEW, mTabletView);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            //tabletView
            showStepDetail(position);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putParcelable(BUNDLE_EXTRA_RECIPE_DETAIL, mRecipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //////////code for tablet support//////////////////////////

    private void showStepDetail(int position){

        Step step = mRecipe.getSteps()[position];
        StepFragment stepFragment = new StepFragment();
        stepFragment.setmStep(step);
        stepFragment.setmTabletView(mTabletView);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(mDataBinding.containerStepsSw600.getId(), stepFragment)
                .commit();
    }

    private void showIngredientDetail()
    {
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setmIngredient(mRecipe.getIngredients());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(mDataBinding.containerStepsSw600.getId(), ingredientsFragment)
                .commit();
    }

    private void setContainerSize(){
        DisplayUtils.getScreenSize(this);
        int width = DisplayUtils.mScreenWidth * 2/3;
        mDataBinding.containerStepsSw600.getLayoutParams().width = width;

    }



}
