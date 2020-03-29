package com.example.bakingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.bakingapp.databinding.ActivityMainBinding;
import com.example.bakingapp.model.Recipe;

import com.example.bakingapp.utiils.NetworkUtils;
import com.example.bakingapp.utiils.SimpleIdlingResource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>,
        RecipeAdapter.RecipeAdapterClickListener {


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RECIPE_LOADER_ID = 14;
    public static final int GRID_SPAN_COUNT = 3;

    private ActivityMainBinding mDataBinding;
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;

    @Nullable private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource(){
        if(mIdlingResource == null){
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mRecyclerView = mDataBinding.rvRecipes;
        FrameLayout frameLayout = mDataBinding.frameLayoutTabletMainView;

        boolean isTabletView;
        if(frameLayout == null){
            isTabletView = false;
        }
        else{
            isTabletView = true;
        }
        mRecipeAdapter = new RecipeAdapter(this, isTabletView, this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        if(isTabletView)
        {
            GridLayoutManager layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);
            layoutManager.setReverseLayout(false);
            layoutManager.setOrientation(GridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

        }
        else{
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            layoutManager.setReverseLayout(false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);
        }

        LoaderManager.getInstance(this).initLoader(RECIPE_LOADER_ID, null, this);
    }

    private void showErrorMessage()
    {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mDataBinding.tvErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showRecipes()
    {
        mDataBinding.tvErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {

        Log.v(TAG, "--------------------------inside onCreateLoader : " );
        return new AsyncTaskLoader<List<Recipe>>(this) {

            List<Recipe> mRecipes = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(mIdlingResource != null){
                    mIdlingResource.setIdleState(false);
                }
                if(mRecipes != null){
                    deliverResult(mRecipes);
                }else {
                    mDataBinding.pbLoading.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(@Nullable List<Recipe> data) {
                mRecipes = data;
                super.deliverResult(data);

            }

            @Nullable
            @Override
            public List<Recipe> loadInBackground() {

                ArrayList<Recipe> recipes;
                try {
                    URL recipeUrl = NetworkUtils.getRecipeUrl();
                    String recipeResponseJson = NetworkUtils.getResponseFromHttpUrl(recipeUrl);
                    //Log.v(TAG, "--------------------------recipeResponse : " + recipeResponseJson);

                    Gson gson = new Gson();
                    Type recipeListType = new TypeToken<ArrayList<Recipe>>(){}.getType();
                    recipes = gson.fromJson(recipeResponseJson, recipeListType);

                    Log.v(TAG, "--------------------------received recipes array, length :" + recipes.size());
                    return recipes;

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

            @Override
            public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> data) {
                mDataBinding.pbLoading.setVisibility(View.INVISIBLE);
                if(mIdlingResource != null){
                    mIdlingResource.setIdleState(true);
                }
                if(data == null){
                    showErrorMessage();
                }else{
                    showRecipes();
                    mRecipeAdapter.setmRecipeList(data);
                }
            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {

            }


    @Override
    public void RecipeClicked(int position) {

        Bundle bundle = new Bundle();
        Recipe recipe = mRecipeAdapter.getmRecipeList().get(position);
        bundle.putParcelable(getString(R.string.bundle_extra_recipe_obj), recipe);
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtras(bundle);

        //update shared pref
        saveRecipe(recipe);
        // start intent
        startActivity(intent);
    }

    private void saveRecipe(Recipe recipe){
        Gson gson = new Gson();
        String recipeStr = gson.toJson(recipe);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_pref_id) , this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.shared_pref_recipe_key), recipeStr);
        editor.apply();
    }


}

