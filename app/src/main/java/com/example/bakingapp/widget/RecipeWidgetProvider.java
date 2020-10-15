package com.example.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.bakingapp.R;
import com.example.bakingapp.RecipeDetailActivity;
import com.example.bakingapp.model.Recipe;
import com.google.gson.Gson;


/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String TAG = RecipeWidgetProvider.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {


        // Construct the RemoteViews object
        RemoteViews views = getIngredientListRemoteView(context, recipe);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getIngredientListRemoteView(Context context, Recipe recipe) {
        //get view
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_recipe_widget);
        //set adapter
        Intent intent = new Intent(context, RecipeRemoteViewService.class);
        views.setRemoteAdapter(R.id.listView_ingredients_widget, intent);

        //set intent
        Intent appIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(
                context,
                0,
                appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        views.setPendingIntentTemplate(R.id.listView_ingredients_widget, appPendingIntent);
        // Handle empty gardens
        views.setEmptyView(R.id.listView_ingredients_widget, R.id.tv_listView_empty_text);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RecipeWidgetProvider.updateRecipeWidget(context, appWidgetManager, appWidgetIds);
    }

    public static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.shared_pref_id), context.MODE_PRIVATE);
        String recipeStr = sharedPref.getString(context.getString(R.string.shared_pref_recipe_key), "");
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(recipeStr, Recipe.class);
        System.out.println("updateRecipeWidget called, recipe is : "+ recipe.getName());
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    public static void updateRecipeWidgetHelper(Context context){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listView_ingredients_widget);
        RecipeWidgetProvider.updateRecipeWidget(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {

        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        int[] appWidgetIds = {appWidgetId};
        RecipeWidgetProvider.updateRecipeWidget(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

