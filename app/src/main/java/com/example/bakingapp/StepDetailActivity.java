package com.example.bakingapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.bakingapp.databinding.ActivityStepDetailBinding;
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;

public class StepDetailActivity extends AppCompatActivity {

    private ActivityStepDetailBinding mDataBinding;
    private Recipe mRecipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_step_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mRecipe = bundle.getParcelable(RecipeDetailActivity.BUNDLE_EXTRA_RECIPE_DETAIL);
        String key = intent.getStringExtra(RecipeDetailActivity.BUNDLE_EXTRA_KEY);

        if(key.equals(RecipeDetailActivity.BUNDLE_EXTRA_INGREDIENT_VALUE)){
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setmIngredient(mRecipe.getIngredients());

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.container_steps, ingredientsFragment)
                    .commit();
        }

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
