package com.wolasoft.bakingapp.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolasoft.bakingapp.R;
import com.wolasoft.bakingapp.adapters.RecipeAdapter;
import com.wolasoft.bakingapp.data.models.Recipe;
import com.wolasoft.bakingapp.data.repositories.RecipeRepository;
import com.wolasoft.bakingapp.databinding.FragmentRecipeListBinding;
import com.wolasoft.bakingapp.viewmodels.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;


public class RecipeListFragment extends Fragment implements RecipeAdapter.OnRecipeClickedListener {
    private static final String RECIPE_LIST = "recipe_list";

    private int orientation;
    private List<Recipe> recipes;
    private FragmentRecipeListBinding dataBinding;
    private OnRecipeFragmentInteractionListener mListener;

    public RecipeListFragment() { }

    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_list, container,false);
        getActivity().setTitle(R.string.app_name);
        RecipeViewModel viewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        orientation = getResources().getConfiguration().orientation;

        if (savedInstanceState != null && savedInstanceState.containsKey(RECIPE_LIST)) {
            this.recipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
        } else {
            recipes = viewModel.getRecipes();
            recipes = RecipeRepository.getInstance(getContext()).getAll();
        }

        initViews();

        return dataBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_LIST, (ArrayList<? extends Parcelable>) this.recipes);
    }


    private void initViews() {
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
        if (mListener != null) {
            mListener.onRecipeSelected(recipe);
        }
    }

    public interface OnRecipeFragmentInteractionListener {
        void onRecipeSelected(Recipe recipe);
    }
}
