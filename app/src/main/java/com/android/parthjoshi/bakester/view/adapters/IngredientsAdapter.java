package com.android.parthjoshi.bakester.view.adapters;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.databinding.IngredientListItemBinding;

import java.util.ArrayList;

import javax.inject.Inject;

public class IngredientsAdapter extends BaseAdapter {

    private ArrayList<Recipe.Ingredients> ingredients;

    @Inject
    public IngredientsAdapter(){}

    public void setIngredients(ArrayList<Recipe.Ingredients> ingredients){
        if(ingredients != null){
            this.ingredients = ingredients;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if(ingredients == null) return 0;
        return ingredients.size();
    }

    @Override
    public Object getItem(int i) {
        return ingredients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        IngredientListItemBinding binding;

        if(view == null){
                binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.ingredient_list_item, viewGroup, false);
                viewHolder = new ViewHolder(binding);
                view = binding.getRoot();
                view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.binding.ingredient.setText(ingredients.get(position).getIngredient());
        viewHolder.binding.quantity.setText(String.valueOf(ingredients.get(position).getQuantity()));
        viewHolder.binding.measure.setText(ingredients.get(position).getMeasure());

        return view;
    }

    private class ViewHolder{

        IngredientListItemBinding binding;

        public ViewHolder(IngredientListItemBinding binding){
            this.binding = binding;
        }
    }
}
