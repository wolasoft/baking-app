package com.wolasoft.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.wolasoft.bakingapp.BakingApplication;
import com.wolasoft.bakingapp.MainActivity;
import com.wolasoft.bakingapp.R;
import com.wolasoft.bakingapp.data.models.Recipe;
import com.wolasoft.bakingapp.data.repositories.RecipeRepository;

import javax.inject.Inject;


public class RecipeAppWidget extends AppWidgetProvider {

    @Inject
    public RecipeRepository repository;

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);
        BakingApplication.app().getAppComponent().inject(this);
        Recipe recipe = repository.getLastSelectedRecipe();

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
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) { }

    @Override
    public void onDisabled(Context context) { }


}

