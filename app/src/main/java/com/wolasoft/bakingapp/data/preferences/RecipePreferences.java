package com.wolasoft.bakingapp.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wolasoft.bakingapp.data.models.Recipe;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class RecipePreferences {

    private static final String PREFS_NAME = "com.wolasoft.preferences";
    private static final String KEY_RECIPE = "preferences.recipe.key";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static Object LOCK = new Object();
    private final Gson gson;
    private static RecipePreferences instance = null;

    @Inject
    public RecipePreferences(Context context) {
        this.preferences = context.getApplicationContext().getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();
    }

    public void saveRecipe(Recipe recipe) {
        String data = gson.toJson(recipe);
        editor.putString(KEY_RECIPE, data);
        editor.commit();
    }

    public Recipe getRecipe() {
        if (preferences != null) {
            Recipe recipe = gson.fromJson(preferences.getString(KEY_RECIPE, null), Recipe.class);
            return recipe;
        }
        return null;
    }
}
