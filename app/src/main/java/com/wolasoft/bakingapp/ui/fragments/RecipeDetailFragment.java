package com.wolasoft.bakingapp.ui.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolasoft.bakingapp.R;
import com.wolasoft.bakingapp.data.models.Recipe;
import com.wolasoft.bakingapp.data.models.Step;
import com.wolasoft.bakingapp.databinding.FragmentRecipeDetailBinding;
import com.wolasoft.bakingapp.ui.adapters.IngredientAdapter;
import com.wolasoft.bakingapp.ui.adapters.StepAdapter;


public class RecipeDetailFragment extends Fragment implements StepAdapter.OnStepClickedListener {
    private static final String SELECTED_RECIPE = "selected_recipe";

    private Recipe recipe;
    private FragmentRecipeDetailBinding dataBinding;

    private OnRecipeDetailFragmentInteractionListener mListener;

    public RecipeDetailFragment() { }

    public static RecipeDetailFragment newInstance(Recipe recipe) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(SELECTED_RECIPE, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipe = getArguments().getParcelable(SELECTED_RECIPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_recipe_detail, container, false);

        initViews();

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(SELECTED_RECIPE);
        }

        getActivity().setTitle(this.recipe.getName());
        return dataBinding.getRoot();
    }

    private void initViews() {
        IngredientAdapter ingredientAdapter = new IngredientAdapter(this.recipe.getIngredients());
        StepAdapter stepAdapter = new StepAdapter(this.recipe.getSteps(), this);
        dataBinding.ingredientsList.recyclerView.setAdapter(ingredientAdapter);
        dataBinding.stepList.recyclerView.setAdapter(stepAdapter);
        LinearLayoutManager ingredientLayoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        dataBinding.ingredientsList.recyclerView.setLayoutManager(ingredientLayoutManager);
        LinearLayoutManager stepLayoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        dataBinding.stepList.recyclerView.setLayoutManager(stepLayoutManager);
        dataBinding.ingredientsList.recyclerView.setHasFixedSize(true);
        dataBinding.stepList.recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SELECTED_RECIPE, recipe);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeDetailFragmentInteractionListener) {
            mListener = (OnRecipeDetailFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeDetailFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void stepClicked(Step step) {
        if (mListener != null) {
            mListener.onStepSelectedFragmentInteraction(step);
        }
    }

    public interface OnRecipeDetailFragmentInteractionListener {
        void onStepSelectedFragmentInteraction(Step step);
    }
}
