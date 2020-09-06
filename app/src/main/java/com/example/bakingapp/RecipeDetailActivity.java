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
import android.widget.LinearLayout;

import com.example.bakingapp.databinding.ActivityRecipeDetailBinding;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.model.Step;
import com.example.bakingapp.widget.RecipeWidgetService;


public class RecipeDetailActivity extends AppCompatActivity implements MasterListFragment.IngredientsClickedListener,
                MasterListFragment.StepClickedListenerForward{


    private ActivityRecipeDetailBinding mDataBinding;
    private static final String TAG = RecipeDetailActivity.class.getName();

    private static Recipe mRecipe;
    private boolean mTabletView;
    private int mStepPosition;


    public static Recipe getRecipe(){
        return mRecipe;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            mRecipe = bundle.getParcelable(getString(R.string.bundle_extra_recipe_obj));
        }else{
            mRecipe = savedInstanceState.getParcelable(getString(R.string.bundle_extra_recipe_obj));
            mStepPosition = savedInstanceState.getInt(getString(R.string.bundle_extra_step_position));
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
                mTabletView = false;

            } else {
                mTabletView = true;
               if(savedInstanceState == null){
                   showIngredientDetail();
               }
            }
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
        RecipeWidgetService.startActionUpdateWidget(this);
    }


    @Override
    public void ingredientsClicked() {

        if(!mTabletView) {
            Intent intent = new Intent(this, StepDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.bundle_extra_recipe_obj), mRecipe);
            bundle.putString(getString(R.string.bundle_extra_recipe_step_key), getString(R.string.bundle_extra_value_ingredient));
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            //when the view is tabletView
            showIngredientDetail();
        }
    }


    @Override
    public void stepClickedForward(int position) {

        if(!mTabletView) {
            Intent intent = new Intent(this, StepDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.bundle_extra_recipe_obj), mRecipe);
            bundle.putString(getString(R.string.bundle_extra_recipe_step_key),
                    getString(R.string.bundle_extra_value_step));
            bundle.putInt(getString(R.string.bundle_extra_step_position), position);
            intent.putExtras(bundle);
            startActivity(intent);
        }else{
            //tabletView
            showStepDetail(position);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putParcelable(getString(R.string.bundle_extra_recipe_obj), mRecipe);
        outState.putInt(getString(R.string.bundle_extra_step_position), mStepPosition);
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

        mStepPosition = position;
        StepFragment stepFragment = StepFragment.newInstance(position, mRecipe, true);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(mDataBinding.containerStepsSw600.getId(), stepFragment)
                .commit();
    }

    private void showIngredientDetail()
    {
        IngredientsFragment ingredientsFragment = IngredientsFragment.newInstance(mRecipe);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(mDataBinding.containerStepsSw600.getId(), ingredientsFragment)
                .commit();
    }


}
