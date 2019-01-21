package com.wolasoft.bakingapp.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.wolasoft.bakingapp.data.api.services.RecipeService;
import com.wolasoft.bakingapp.data.models.Recipe;
import com.wolasoft.bakingapp.data.preferences.RecipePreferences;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@Singleton
public class RecipeRepository {

    private static final String TAG = "RecipeRepository";
    private RecipeService recipeService;
    private RecipePreferences recipePreferences;
    private Gson gson;


    @Inject
    public RecipeRepository(RecipeService service, RecipePreferences preferences, Gson gson) {
        this.recipeService = service;
        this.recipePreferences = preferences;
        this.gson = gson;
    }

    public LiveData<List<Recipe>> getAll() {
        final MutableLiveData<List<Recipe>> data = new MutableLiveData<>();
        Call call = recipeService.getAll();
        Log.d(TAG, "Fetching recipes");
        call.enqueue(new Callback<JsonArray>() {

            @Override
            public void onResponse(@NonNull Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful()) {
                    Type listType = new TypeToken<ArrayList<Recipe>>(){}.getType();
                    List<Recipe> recipes = gson.fromJson(response.body().toString(), listType);
                    data.setValue(recipes);
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }

    public void saveLastSelectedRecipe(Recipe recipe) {
        this.recipePreferences.saveRecipe(recipe);
    }

    public Recipe getLastSelectedRecipe() {
        return this.recipePreferences.getRecipe();
    }
}
