package com.android.parthjoshi.bakester.view.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.databinding.RecipeItemBinding;

import java.util.List;

import javax.inject.Inject;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Recipe> recipes;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    @Inject
    public RecipesAdapter(Context context, OnItemClickListener listener){
        if(inflater == null)
            inflater = LayoutInflater.from(context);

        onItemClickListener = listener;
    }

    public void setData(List<Recipe> recipes){
        if(recipes != null){
            this.recipes = recipes;

            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecipeItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.recipe_item,
                parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipesAdapter.ViewHolder holder, int position) {
        holder.binding.recipeItem.setText(recipes.get(position).getName());
        holder.binding.ingredientsCount.setText(String.valueOf(recipes.get(position).getIngredients().size()));
        holder.binding.stepCount.setText(String.valueOf(recipes.get(position).getSteps().size()));
        holder.binding.serveCount.setText(String.valueOf(recipes.get(position).getServings()));

        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(recipes.size() == 0) return 0;
        else return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        RecipeItemBinding binding;

        public ViewHolder(RecipeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
