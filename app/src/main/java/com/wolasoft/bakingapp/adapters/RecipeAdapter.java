package com.wolasoft.bakingapp.adapters;

import android.content.Context;
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

        View view = inflater.inflate(R.layout.recipe_list_item, viewGroup, false);
        return new ViewHolder(view);
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

        private final ImageView recipeImage;
        private final TextView recipeNameTV;
        private final Context context;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeNameTV = itemView.findViewById(R.id.recipeNameTV);

            itemView.setOnClickListener(this);
        }

        void bind(Recipe recipe) {
            if (recipe.getImage().isEmpty()) {
                recipeImage.setImageDrawable(
                        this.context.getResources().getDrawable(R.drawable.recipe));
            } else {
                Picasso.get()
                        .load(recipe.getImage())
                        .error(R.drawable.recipe)
                        .placeholder(R.drawable.recipe)
                        .into(recipeImage);
            }
            this.recipeNameTV.setText(recipe.getName());
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
