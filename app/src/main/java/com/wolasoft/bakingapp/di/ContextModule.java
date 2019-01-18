package com.wolasoft.bakingapp.di;

import android.content.Context;

import com.google.gson.Gson;
import com.wolasoft.bakingapp.data.preferences.RecipePreferences;
import com.wolasoft.bakingapp.data.repositories.RecipeRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    Context provideContext() {
        return context;
    }
}
