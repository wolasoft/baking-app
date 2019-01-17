package com.wolasoft.bakingapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.wolasoft.bakingapp.data.models.Recipe;
import com.wolasoft.bakingapp.data.repositories.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private final RecipeRepository repository;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        this.repository = RecipeRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Recipe>> getRecipes() {
        return this.repository.getAll();
    }
}
