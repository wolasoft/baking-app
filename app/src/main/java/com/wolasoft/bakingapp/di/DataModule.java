package com.wolasoft.bakingapp.di;

import android.content.Context;

import com.google.gson.Gson;
import com.wolasoft.bakingapp.data.api.ApiConnector;
import com.wolasoft.bakingapp.data.api.services.RecipeService;
import com.wolasoft.bakingapp.data.preferences.RecipePreferences;
import com.wolasoft.bakingapp.data.repositories.RecipeRepository;
import com.wolasoft.bakingapp.viewmodels.RecipeViewModelFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {

    @Provides
    RecipePreferences provideRecipePreferences(Context context) {
        return new RecipePreferences(context);
    }

    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Singleton
    @Provides
    RecipeService provideRecipeService() {
        return ApiConnector.createRetrofitService(RecipeService.class);
    }

    @Provides
    RecipeRepository provideRecipeRepository(RecipeService recipeService, RecipePreferences preferences, Gson gson) {
        return new RecipeRepository(recipeService, preferences, gson);
    }

    @Provides
    RecipeViewModelFactory provideRecipeViewModelFactory(RecipeRepository repository) {
        return new RecipeViewModelFactory(repository);
    }

}
