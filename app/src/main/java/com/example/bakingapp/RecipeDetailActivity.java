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


import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;

public class RecipeDetailActivity extends AppCompatActivity implements MasterListFragment.IngredientsClickedListener,
                MasterListFragment.StepClickedListenerForward{


   // private ActivityRecipeDetailBinding mDataBinding;
    private static final String TAG = RecipeDetailActivity.class.getName();
    public static final String BUNDLE_EXTRA_RECIPE_DETAIL = "bundle_extra_recipe_detail";
    public static final String BUNDLE_EXTRA_KEY = "extra_key";
    public static final String BUNDLE_EXTRA_INGREDIENT_VALUE = "ingredient";
    public static final String BUNDLE_EXTRA_STEP_VALUE = "step";
    public static final String BUNDLE_EXTRA_STEP_POSITION = "step_position";


    private static Recipe mRecipe;

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
        setContentView(R.layout.activity_recipe_detail);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(mRecipe.getName());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipe =  savedInstanceState.getParcelable(BUNDLE_EXTRA_RECIPE_DETAIL);
    }

    @Override
    public void ingredientsClicked() {

       Intent intent = new Intent(this, StepDetailActivity.class);
       Bundle bundle = new Bundle();
       bundle.putParcelable(BUNDLE_EXTRA_RECIPE_DETAIL, mRecipe);
       bundle.putString(BUNDLE_EXTRA_KEY, BUNDLE_EXTRA_INGREDIENT_VALUE);
       intent.putExtras(bundle);

       startActivity(intent);

    }

    @Override
    public void stepClickedForward(int position) {

        Intent intent = new Intent(this, StepDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RECIPE_DETAIL, mRecipe);
        bundle.putString(BUNDLE_EXTRA_KEY, BUNDLE_EXTRA_STEP_VALUE);
        bundle.putInt(BUNDLE_EXTRA_STEP_POSITION, position);
        intent.putExtras(bundle);

        startActivity(intent);
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
}
