package com.wolasoft.bakingapp.di;

import com.wolasoft.bakingapp.MainActivity;
import com.wolasoft.bakingapp.data.api.services.RecipeService;
import com.wolasoft.bakingapp.data.preferences.RecipePreferences;
import com.wolasoft.bakingapp.data.repositories.RecipeRepository;
import com.wolasoft.bakingapp.ui.fragments.RecipeListFragment;
import com.wolasoft.bakingapp.viewmodels.RecipeViewModel;
import com.wolasoft.bakingapp.viewmodels.RecipeViewModelFactory;
import com.wolasoft.bakingapp.widgets.RecipeAppWidget;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class, ContextModule.class})
public interface AppComponent {
    RecipePreferences recipePreferences();
    RecipeService recipeService();
    RecipeRepository recipeRepository();
    RecipeViewModelFactory recipeViewModelFactory();

    void inject(RecipeViewModel recipeViewModel);
    void inject(RecipeAppWidget recipeAppWidget);
    void inject(MainActivity mainActivity);
    void inject(RecipeListFragment recipeListFragment);
}
