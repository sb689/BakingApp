package com.example.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.bakingapp.model.Ingredient;
import com.example.bakingapp.model.Recipe;
import com.google.gson.Gson;

public class RecipeRemoteViewService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewFactory(this.getApplicationContext());
    }
}

class RecipeRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory{

    public static final String TAG = RecipeRemoteViewFactory.class.getSimpleName();
    private Recipe mRecipe;
    private Context mContext;

    public RecipeRemoteViewFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.shared_pref_id), mContext.MODE_PRIVATE);
        String recipeStr = sharedPref.getString(mContext.getString(R.string.shared_pref_recipe_key), "");
        Gson gson = new Gson();
        mRecipe = gson.fromJson(recipeStr, Recipe.class);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mRecipe == null)
            return 0;
        else
            return mRecipe.getIngredients().length;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget);

        Ingredient ingredient = mRecipe.getIngredients()[position];

        String ingredientText = ingredient.getIngredient();
        views.setTextViewText(R.id.tv_ingredients_item_widget, ingredientText);
        Bundle extras = new Bundle();
        extras.putParcelable(mContext.getString(R.string.bundle_extra_recipe_obj),mRecipe);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.relative_layout_widget_item, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}