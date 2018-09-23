package com.android.parthjoshi.bakester.view.adapters;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.databinding.RecipeItemBinding;

import java.util.List;

import javax.inject.Inject;

public class RecipeGridAdapter extends BaseAdapter {

    private List<Recipe> recipes;
    private OnItemClickListener listener;

    @Inject
    public RecipeGridAdapter(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setRecipes(List<Recipe> recipes){
        if(recipes != null) {
            this.recipes = recipes;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if(recipes == null || recipes.size() == 0)
            return 0;
        return recipes.size();
    }

    @Override
    public Object getItem(int i) {
        return recipes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        Holder holder;
        RecipeItemBinding binding;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        if(view == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.recipe_item, viewGroup, false);
            holder = new Holder(binding);
            view = binding.getRoot();
            view.setTag(holder);
        }else {
            holder = (Holder) view.getTag();
        }

        holder.binding.recipeItem.setText(recipes.get(position).getName());
        holder.binding.ingredientsCount.setText(String.valueOf(recipes.get(position).getIngredients().size()));
        holder.binding.stepCount.setText(String.valueOf(recipes.get(position).getSteps().size()));
        holder.binding.serveCount.setText(String.valueOf(recipes.get(position).getServings()));

        holder.binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGridItemClick(position);
            }
        });

        return view;
    }

    private class Holder{
        RecipeItemBinding binding;

        public Holder(RecipeItemBinding binding){
            this.binding = binding;
        }
    }

    public interface OnItemClickListener{
        void onGridItemClick(int position);
    }
}
