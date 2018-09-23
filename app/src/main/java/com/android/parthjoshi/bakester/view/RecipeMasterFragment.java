package com.android.parthjoshi.bakester.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.databinding.FragmentRecipeMasterBinding;
import com.android.parthjoshi.bakester.util.Constants;
import com.android.parthjoshi.bakester.view.adapters.RecipeMasterAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeMasterFragment.OnRecipeMasterInteractionListener} interface
 * to handle interaction events.
 */
public class RecipeMasterFragment extends Fragment implements RecipeMasterAdapter.onRecipeStepClickListener {

    @Inject RecipeMasterAdapter adapter;
    @Inject ArrayList<Recipe.Steps> stepsList;

    private Recipe recipe;
    private boolean twoPane = false;

    private OnRecipeMasterInteractionListener mListener;

    @Inject
    public RecipeMasterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentRecipeMasterBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_recipe_master, container, false);

        if(savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(Constants.RECIPE_KEY);
            twoPane = savedInstanceState.getBoolean(Constants.TWO_PANE_KEY);
        }

        adapter.setData(recipe.getSteps(), this, twoPane);
        binding.setAdapter(adapter);

        binding.ingredientsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.showIngredients(recipe.getIngredients());
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        if (context instanceof OnRecipeMasterInteractionListener) {
            mListener = (OnRecipeMasterInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeMasterInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onRecipeStepClick(Recipe.Steps step, int position, int count) {

        if(twoPane){
            try {
                ((RecipeActivity)getActivity()).showStepFragment(step);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            stepsList.addAll(recipe.getSteps());
            Intent stepIntent = new Intent(getContext(), StepDetailActivity.class);
            stepIntent.putExtra(Constants.STEP_KEY, step);
            stepIntent.putParcelableArrayListExtra(Constants.STEPS_LIST_KEY, stepsList);
            stepIntent.putExtra(Constants.STEP_ID_KEY, position);
            stepIntent.putExtra(Constants.STEP_COUNT_KEY, count - 1);
            stepIntent.putExtra(Constants.RECIPE_NAME_KEY, recipe.getName());
            startActivity(stepIntent);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnRecipeMasterInteractionListener {

        void showIngredients(List<Recipe.Ingredients> ingredients);
    }

    public void setRecipe(Recipe recipe){
        if(recipe != null)
            this.recipe = recipe;
    }

    public void setTwoPane(boolean twoPane){
        this.twoPane = twoPane;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Constants.RECIPE_KEY, recipe);
        outState.putBoolean(Constants.TWO_PANE_KEY, twoPane);
    }
}
