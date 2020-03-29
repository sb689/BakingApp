package com.example.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.util.Log;
import androidx.annotation.Nullable;
import com.example.bakingapp.R;
import com.example.bakingapp.model.Recipe;
import com.google.gson.Gson;

public class RecipeWidgetService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private static final String TAG = RecipeWidgetService.class.getName();
    private static final String ACTION_UPDATE_WIDGET = "update_widget";


    public RecipeWidgetService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent.getAction().equals(ACTION_UPDATE_WIDGET)){

            updateRecipeWidget();
        }
    }

    public static void startActionUpdateWidget(Context context){
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    private void updateRecipeWidget(){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listView_ingredients_widget);
        SharedPreferences sharedPref = getApplication().getSharedPreferences(getString(R.string.shared_pref_id), this.MODE_PRIVATE);
        String recipeStr = sharedPref.getString(getString(R.string.shared_pref_recipe_key), "");

        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(recipeStr, Recipe.class);
        RecipeWidgetProvider.updateRecipeWidget(this, appWidgetManager, appWidgetIds, recipe);
    }



}
