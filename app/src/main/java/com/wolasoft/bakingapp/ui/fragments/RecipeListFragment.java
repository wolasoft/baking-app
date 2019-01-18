package com.wolasoft.bakingapp.ui.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolasoft.bakingapp.BakingApplication;
import com.wolasoft.bakingapp.R;
import com.wolasoft.bakingapp.adapters.RecipeAdapter;
import com.wolasoft.bakingapp.data.models.Recipe;
import com.wolasoft.bakingapp.data.repositories.RecipeRepository;
import com.wolasoft.bakingapp.databinding.FragmentRecipeListBinding;
import com.wolasoft.bakingapp.utils.NetworkUtils;
import com.wolasoft.bakingapp.viewmodels.RecipeViewModel;
import com.wolasoft.bakingapp.viewmodels.RecipeViewModelFactory;

import java.util.List;

import javax.inject.Inject;


public class RecipeListFragment extends Fragment implements RecipeAdapter.OnRecipeClickedListener {

    private int orientation;
    private FragmentRecipeListBinding dataBinding;
    private OnRecipeFragmentInteractionListener mListener;
    @Inject
    public RecipeRepository repository;
    @Inject
    public RecipeViewModelFactory factory;

    public RecipeListFragment() { }

    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_list, container,false);
        BakingApplication.app().getAppComponent().inject(this);
        getActivity().setTitle(R.string.app_name);

        final RecipeViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);

        orientation = getResources().getConfiguration().orientation;

        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                initViews(recipes);
            }
        });

        return dataBinding.getRoot();
    }

    private void initViews(List<Recipe> recipes) {
        showProgress();
        if (!NetworkUtils.isInternetAvailable(getContext())) {
            showMessage(R.string.recipe_network_error_message);
            hideProgress();

            return;
        }

        if (recipes == null ) {
            showMessage(R.string.recipe_no_data_available_message);
            hideProgress();

            return;
        }

        RecipeAdapter adapter = new RecipeAdapter(recipes, this);
        dataBinding.recipeList.recyclerView.setAdapter(adapter);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    getContext(), LinearLayoutManager.VERTICAL, false);
            dataBinding.recipeList.recyclerView.setLayoutManager(layoutManager);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int spanCount = 3;
            GridLayoutManager layoutManager = new GridLayoutManager(
                    getContext(), spanCount, LinearLayoutManager.VERTICAL, false);
            dataBinding.recipeList.recyclerView.setLayoutManager(layoutManager);
        }

        dataBinding.recipeList.recyclerView.setHasFixedSize(true);
        hideMessage();
        hideProgress();
    }

    private void showProgress() {
        dataBinding.recipeList.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        dataBinding.recipeList.progressBar.setVisibility(View.GONE);
    }

    private void showMessage(int id) {
        dataBinding.recipeList.messageTV.setText(id);
        dataBinding.recipeList.messageTV.setVisibility(View.VISIBLE);
    }

    private void hideMessage() {
        dataBinding.recipeList.messageTV.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeFragmentInteractionListener) {
            mListener = (OnRecipeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void recipeClicked(Recipe recipe) {
        repository.saveLastSelectedRecipe(recipe);

        if (mListener != null) {
            mListener.onRecipeSelected(recipe);
        }
    }

    public interface OnRecipeFragmentInteractionListener {
        void onRecipeSelected(Recipe recipe);
    }
}
