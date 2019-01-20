package com.wolasoft.bakingapp.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wolasoft.bakingapp.R;
import com.wolasoft.bakingapp.data.models.Recipe;
import com.wolasoft.bakingapp.databinding.RecipeListItemBinding;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final List<Recipe> recipes;
    private final OnRecipeClickedListener listener;

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickedListener listener) {
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        RecipeListItemBinding dataBinding =
                DataBindingUtil.inflate(inflater, R.layout.recipe_list_item, viewGroup, false);
        return new ViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Recipe recipe = this.recipes.get(position);
        viewHolder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return this.recipes == null ? 0 : this.recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RecipeListItemBinding dataBinding;

        ViewHolder(@NonNull RecipeListItemBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
            itemView.setOnClickListener(this);
        }

        void bind(Recipe recipe) {
            this.dataBinding.setRecipe(recipe);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Recipe recipe = recipes.get(position);
            listener.recipeClicked(recipe);
        }
    }

    public interface OnRecipeClickedListener {
        void recipeClicked(Recipe recipe);
    }
}
