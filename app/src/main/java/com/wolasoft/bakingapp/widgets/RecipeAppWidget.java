package com.wolasoft.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.wolasoft.bakingapp.MainActivity;
import com.wolasoft.bakingapp.R;
import com.wolasoft.bakingapp.data.models.Recipe;
import com.wolasoft.bakingapp.data.repositories.RecipeRepository;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeAppWidget extends AppWidgetProvider {

    private Recipe recipe;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);
        Recipe recipe = RecipeRepository.getInstance(context).getLastSelectedRecipe();

        Intent mainActivity = new Intent(context, MainActivity.class);
        mainActivity.putExtra(MainActivity.KEY_SELECTED_RECIPE, recipe);

        if (recipe != null) {
            mainActivity.putExtra(MainActivity.KEY_SELECTED_RECIPE, recipe);
            views.setTextViewText(R.id.recipeName, recipe.getName());
            int numberOfIngredient = recipe.getIngredients().size();
            views.setTextViewText(R.id.numberOfIngredient, context.getResources().getQuantityString(
                    R.plurals.recipe_details_number_of_ingredient, numberOfIngredient, numberOfIngredient));
            int numberOfStep = recipe.getSteps().size();
            views.setTextViewText(R.id.numberOfStep, context.getResources().getQuantityString(
                    R.plurals.recipe_details_number_of_step, numberOfStep, numberOfStep));
        } else {
            views.setTextViewText(
                    R.id.recipeName,
                    context.getResources().getString(R.string.recipe_no_selected_recipe_label));
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, mainActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.recipeImage, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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

