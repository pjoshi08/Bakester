package com.android.parthjoshi.bakester.view;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.databinding.ActivityRecipeBinding;
import com.android.parthjoshi.bakester.util.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class RecipeActivity extends AppCompatActivity implements HasSupportFragmentInjector,
        RecipeMasterFragment.OnRecipeMasterInteractionListener {

    @Inject DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;
    @Inject RecipeMasterFragment masterFragment;
    @Inject IngredientsFragment ingredientsFragment;
    @Inject Intent ingredientsIntent;
    @Inject ArrayList<Recipe.Ingredients> ingredientsList;

    private Recipe recipe;
    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        ActivityRecipeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);

        if(binding.tabLayout != null)
            twoPane = true;

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            if(getIntent().hasExtra(Constants.RECIPE_KEY)) {
                recipe = bundle.getParcelable(Constants.RECIPE_KEY);
                if (recipe != null) {
                    setTitle(recipe.getName());
                }
            }
        }

        if(savedInstanceState != null){
            recipe = savedInstanceState.getParcelable(Constants.RECIPE_KEY);
            if (recipe != null) {
                setTitle(recipe.getName());
            }

            if(twoPane)
                initTwoPaneLayout();
            else
                initMasterFragment(recipe);
            return;
        }

        if(twoPane)
            initTwoPaneLayout();
        else if(recipe != null) {
            initMasterFragment(recipe);
        }

        if(getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public AndroidInjector<android.support.v4.app.Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    private void initMasterFragment(Recipe recipe){
        masterFragment.setRecipe(recipe);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.recipe_steps_container, masterFragment)
                .commit();
    }

    @Override
    public void showIngredients(List<Recipe.Ingredients> ingredients) {

        ingredientsList.addAll(ingredients);

        if(twoPane){
            ingredientsFragment.setIngredients(ingredientsList);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.recipe_detail_container, ingredientsFragment)
                    .commit();
        }
        else {

            ingredientsIntent.putParcelableArrayListExtra(Constants.INGREDIENTS_KEY, ingredientsList);
            ingredientsIntent.putExtra(Constants.RECIPE_NAME_KEY, recipe.getName());
            startActivity(ingredientsIntent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Constants.RECIPE_KEY, recipe);
    }

    private void initTwoPaneLayout(){

        masterFragment.setRecipe(recipe);
        masterFragment.setTwoPane(twoPane);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.recipe_master_container, masterFragment)
                .commit();

        ingredientsList = new ArrayList<>();
        ingredientsList.addAll(recipe.getIngredients());
        ingredientsFragment.setIngredients(ingredientsList);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.recipe_detail_container, ingredientsFragment)
                .commit();
    }

    public void showStepFragment(Recipe.Steps step){
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setStep(step);
        stepDetailFragment.setTwoPane(twoPane);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.recipe_detail_container, stepDetailFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
