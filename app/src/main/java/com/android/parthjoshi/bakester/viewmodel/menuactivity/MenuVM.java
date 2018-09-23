package com.android.parthjoshi.bakester.viewmodel.menuactivity;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.Intent;

import com.android.parthjoshi.bakester.data.Repository;
import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.util.Constants;
import com.android.parthjoshi.bakester.view.RecipeActivity;
import com.android.parthjoshi.bakester.widget.RecipeWidgetProvider;

import java.util.List;

public class MenuVM extends ViewModel {

    private MutableLiveData<List<Recipe>> recipes;
    private Repository repository;

    public MenuVM(Repository repository){
        this.repository = repository;

        recipes = new MutableLiveData<>();
        loadData();
    }

    private void loadData(){
        repository.getRecipesFromNetwork();
        recipes = repository.getRecipes();
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public void onClick(int position, Context context){
        try {
            Recipe recipe = recipes.getValue().get(position);

            repository.saveCurrentRecipe(recipe.getId(), recipe.getName());
            RecipeWidgetProvider.updateWidgets(context);

            Intent intent = new Intent(context, RecipeActivity.class);
            intent.putExtra(Constants.RECIPE_KEY, recipe);

            context.startActivity(intent);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
