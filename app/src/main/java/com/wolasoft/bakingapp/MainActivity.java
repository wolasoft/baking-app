package com.wolasoft.bakingapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wolasoft.bakingapp.data.models.Recipe;
import com.wolasoft.bakingapp.data.models.Step;
import com.wolasoft.bakingapp.data.repositories.RecipeRepository;
import com.wolasoft.bakingapp.services.UpdateWidgetService;
import com.wolasoft.bakingapp.ui.fragments.RecipeDetailFragment;
import com.wolasoft.bakingapp.ui.fragments.RecipeListFragment;
import com.wolasoft.bakingapp.ui.fragments.RecipeStepDetailFragment;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity
        implements RecipeListFragment.OnRecipeFragmentInteractionListener,
        RecipeDetailFragment.OnRecipeDetailFragmentInteractionListener,
        RecipeStepDetailFragment.OnFragmentStepButtonInteractionListener {

    private static final String RECIPE_LIST_FRAGMENT_TAG = "recipe_list_fragment_tag";
    private static final String RECIPE_DETAIL_FRAGMENT_TAG = "recipe_detail_fragment_tag";
    private static final String RECIPE_STEP_FRAGMENT_TAG = "recipe_step_fragment_tag";
    private static final String FRAGMENT_TAG = "fragment_tag";
    private static final String BACK_STATE = "back_state";
    public static final String KEY_SELECTED_RECIPE = "key_selected_recipe";

    private View detailFragmentView;
    private View dividerView;
    private FragmentManager fragmentManager;
    private Toast toast;
    private RecipeListFragment recipeListFragment = RecipeListFragment.newInstance();

    private boolean isTablet;
    private boolean isUpMenuItemVisible = false;
    private String currentFragmentTag;
    private Recipe selectedRecipe;
    private Step firstStep;
    @Inject
    public RecipeRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BakingApplication.app().getAppComponent().inject(this);
        setTitle(R.string.app_name);

        detailFragmentView = findViewById(R.id.fragment_details);
        dividerView = findViewById(R.id.divider);
        fragmentManager = getSupportFragmentManager();

        if (detailFragmentView != null || getResources().getBoolean(R.bool.isTablet)) {
            isTablet = true;
        }

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        } else {
            firstTimeOpening();
        }
    }

    private void firstTimeOpening() {
        currentFragmentTag = RECIPE_LIST_FRAGMENT_TAG;
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, recipeListFragment, RECIPE_LIST_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();

        if (getIntent().hasExtra(KEY_SELECTED_RECIPE)
                && getIntent().getParcelableExtra(KEY_SELECTED_RECIPE) != null) {
            openRecipeDetailFragment((Recipe) getIntent().getParcelableExtra(KEY_SELECTED_RECIPE));

        }
    }

    private void restoreState(Bundle savedInstanceState) {
        isUpMenuItemVisible = savedInstanceState.getBoolean(BACK_STATE);

        if (isUpMenuItemVisible) {
            selectedRecipe = savedInstanceState.getParcelable(KEY_SELECTED_RECIPE);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        if (isTablet && isUpMenuItemVisible) {
            replaceFragment(
                    R.id.fragment_container,
                    fragmentManager.findFragmentByTag(RECIPE_DETAIL_FRAGMENT_TAG),
                    RECIPE_DETAIL_FRAGMENT_TAG);

            replaceFragment(
                    R.id.fragment_details,
                    fragmentManager.findFragmentByTag(RECIPE_STEP_FRAGMENT_TAG),
                    RECIPE_STEP_FRAGMENT_TAG);
        } else {
            currentFragmentTag = savedInstanceState.getString(FRAGMENT_TAG, RECIPE_LIST_FRAGMENT_TAG);
            Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
            replaceFragment(R.id.fragment_container,
                    currentFragment == null ? recipeListFragment : currentFragment, currentFragmentTag);
            fragmentManager.popBackStack();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(BACK_STATE, isUpMenuItemVisible);
        outState.putString(FRAGMENT_TAG, currentFragmentTag);

        if (selectedRecipe != null) {
            outState.putParcelable(KEY_SELECTED_RECIPE, selectedRecipe);
        }

        if (isTablet && isUpMenuItemVisible) {
            outState.putString(FRAGMENT_TAG, FRAGMENT_TAG);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if ((fragmentManager.findFragmentById(R.id.fragment_container)
                instanceof RecipeListFragment)) {
            finish();
        } else {
            super.onBackPressed();

            if ((isTablet)
                    && (fragmentManager.findFragmentByTag(RECIPE_STEP_FRAGMENT_TAG) == null)) {
                fragmentManager.popBackStackImmediate();
                detailFragmentView.setVisibility(View.GONE);
                dividerView.setVisibility(View.GONE);
            }

            if (fragmentManager.findFragmentByTag(RECIPE_DETAIL_FRAGMENT_TAG) == null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                isUpMenuItemVisible = false;
            }
        }
    }

    private void replaceFragment(int layoutId, Fragment fragment, String fragmentTag) {
        currentFragmentTag = fragmentTag;
        fragmentManager.beginTransaction()
                .replace(layoutId, fragment, fragmentTag)
                .addToBackStack(null)
                .commit();

        if (!(fragment instanceof RecipeListFragment)) {
            isUpMenuItemVisible = true;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        openRecipeDetailFragment(recipe);
        repository.saveLastSelectedRecipe(recipe);

        updateAppWidget();
    }

    private void openRecipeDetailFragment(Recipe recipe) {
        selectedRecipe = recipe;
        RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(recipe);
        replaceFragment(R.id.fragment_container, recipeDetailFragment, RECIPE_DETAIL_FRAGMENT_TAG);

        if (isTablet) {
            firstStep = this.selectedRecipe.getSteps().get(0);
            createStepFragment(firstStep);
            detailFragmentView.setVisibility(View.VISIBLE);
            dividerView.setVisibility(View.VISIBLE);
        }

        if (getIntent().hasExtra(KEY_SELECTED_RECIPE)) {
            getIntent().removeExtra(KEY_SELECTED_RECIPE);
        }
    }

    @Override
    public void onStepSelectedFragmentInteraction(Step step) {
        if (isTablet && step.equals(firstStep)) {
            return;
        }

        createStepFragment(step);
    }

    @Override
    public void onNextStepButtonClicked(Step step) {
        int position = selectedRecipe.getSteps().indexOf(step);
        if (position + 1 < selectedRecipe.getSteps().size()) {
            Step nextStep = selectedRecipe.getSteps().get(position + 1);
            createStepFragment(nextStep);
        } else {
            toast = Toast.makeText(
                    this,
                    getResources().getString(R.string.recipe_details_last_step_message),
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onPreviousStepButtonClicked(Step step) {
        int position = this.selectedRecipe.getSteps().indexOf(step);
        if (position - 1 >= 0) {
            Step nextStep = selectedRecipe.getSteps().get(position - 1);
            createStepFragment(nextStep);
        } else {
            toast = Toast.makeText(this,
                    getResources().getString(R.string.recipe_details_first_step_message),
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void createStepFragment(Step step) {
        RecipeStepDetailFragment recipeStepDetailFragment =
                RecipeStepDetailFragment.newInstance(step);

        if (isTablet) {
            replaceFragment(
                    R.id.fragment_details, recipeStepDetailFragment, RECIPE_STEP_FRAGMENT_TAG);
        } else {
            replaceFragment(
                    R.id.fragment_container, recipeStepDetailFragment, RECIPE_STEP_FRAGMENT_TAG);
        }
    }

    private void updateAppWidget() {
        Intent appWidgetService = new Intent(this, UpdateWidgetService.class);
        appWidgetService.setAction(UpdateWidgetService.SHOW_LAST_SELECTED_RECIPE);
        startService(appWidgetService);
    }
}
