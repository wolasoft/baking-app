package com.wolasoft.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolasoft.bakingapp.R;
import com.wolasoft.bakingapp.data.models.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final List<Ingredient> ingredients;

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.ingredient_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Ingredient Ingredient = this.ingredients.get(position);
        viewHolder.bind(Ingredient);
    }

    @Override
    public int getItemCount() {
        return this.ingredients == null ? 0 : this.ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView ingredientTV;
        private final TextView quantityTV;
        private final TextView measureTV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientTV = itemView.findViewById(R.id.ingredientTV);
            quantityTV = itemView.findViewById(R.id.quantityTV);
            measureTV = itemView.findViewById(R.id.measureTV);
        }

        void bind(Ingredient ingredient) {
            this.ingredientTV.setText(ingredient.getIngredient());
            this.quantityTV.setText(String.format("%2f", ingredient.getQuantity()));
            this.measureTV.setText(ingredient.getMeasure());
        }
    }
}
