package com.android.parthjoshi.bakester.view;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.databinding.FragmentIngredientsBinding;
import com.android.parthjoshi.bakester.util.Constants;
import com.android.parthjoshi.bakester.view.adapters.IngredientsAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {

    @Inject IngredientsAdapter adapter;
    private ArrayList<Recipe.Ingredients> ingredients;

    @Inject
    public IngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*FragmentIngredientsBinding binding*/
        FragmentIngredientsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_ingredients, container, false);

        if(savedInstanceState != null)
            ingredients = savedInstanceState.getParcelableArrayList(Constants.INGREDIENTS_KEY);

        adapter.setIngredients(ingredients);
        binding.setAdapter(adapter);

        return binding.getRoot();
    }

    public void setIngredients(ArrayList<Recipe.Ingredients> ingredients){

        if(ingredients != null)
            this.ingredients = ingredients;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(Constants.INGREDIENTS_KEY, ingredients);
    }
}
