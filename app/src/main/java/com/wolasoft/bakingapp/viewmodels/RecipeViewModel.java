package com.wolasoft.bakingapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.wolasoft.bakingapp.data.models.Recipe;
import com.wolasoft.bakingapp.data.repositories.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends ViewModel {

    public RecipeRepository repository;

    // @Inject
    public RecipeViewModel(RecipeRepository repository) {
        // BakingApplication.app().getAppComponent().inject(this);
        this.repository = repository;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return this.repository.getAll();
    }
}
