package com.wolasoft.bakingapp.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.wolasoft.bakingapp.data.repositories.RecipeRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RecipeViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private RecipeRepository repository;

    @Inject
    public RecipeViewModelFactory(RecipeRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeViewModel(repository);
    }
}
