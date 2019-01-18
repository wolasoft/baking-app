package com.wolasoft.bakingapp.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.wolasoft.bakingapp.widgets.RecipeAppWidget;

public class UpdateWidgetService extends IntentService {

    public static final String SHOW_LAST_SELECTED_RECIPE = "show_selected_recipe";
    public static final String TAG = "UpdateWidgetService";

    public UpdateWidgetService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals(SHOW_LAST_SELECTED_RECIPE)) {
                try {
                    AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());

                    ComponentName componentName =
                            new ComponentName(getApplicationContext(), RecipeAppWidget.class);

                    int []ids = manager.getAppWidgetIds(componentName);

                    Intent widgetIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                    getApplicationContext().sendBroadcast(widgetIntent);
                } catch (Exception error) {
                    error.getMessage();
                }
            }
        }
    }
}
