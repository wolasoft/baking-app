package com.wolasoft.bakingapp.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wolasoft.bakingapp.R;
import com.wolasoft.bakingapp.data.models.Ingredient;
import com.wolasoft.bakingapp.databinding.IngredientListItemBinding;

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

        // View view = inflater.inflate(R.layout.ingredient_list_item, viewGroup, false);
        IngredientListItemBinding dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.ingredient_list_item, viewGroup, false
        );
        return new ViewHolder(dataBinding);
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
        private final IngredientListItemBinding dataBinding;

        ViewHolder(@NonNull IngredientListItemBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
        }

        void bind(Ingredient ingredient) {
            dataBinding.setIngredient(ingredient);
            dataBinding.executePendingBindings();
        }
    }
}
