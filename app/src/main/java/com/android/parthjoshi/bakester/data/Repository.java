package com.android.parthjoshi.bakester.data;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.parthjoshi.bakester.data.db.RecipeDB;
import com.android.parthjoshi.bakester.data.db.RecipeDao;
import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.data.networking.APIInterface;
import com.android.parthjoshi.bakester.util.AppExecutors;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private static final String PREFS_NAME = "recipe_prefs";
    private static final String PREF_KEY_RECIPE_ID = "recipe_id";
    private static final String PREF_KEY_RECIPE_NAME = "recipe_name";
    private final APIInterface apiInterface;
    private final RecipeDao dao;
    private final SharedPreferences preferences;
    private MutableLiveData<List<Recipe>> recipes;

    @Inject
    public Repository(Context context, APIInterface apiInterface, RecipeDB db){
        this.apiInterface = apiInterface;
        this.dao = db.getRecipeDao();
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        recipes = new MutableLiveData<>();
    }

    public void getRecipesFromNetwork(){
        Call<List<Recipe>> call = apiInterface.getRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if(response.body() != null) {
                    setRecipes(response.body());
                    addRecipes(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public MutableLiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    private void setRecipes(List<Recipe> recipes) {
        this.recipes.setValue(recipes);
    }

    private void addRecipes(final List<Recipe> recipes){
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                dao.addRecipes(recipes);
            }
        });
    }

    public void saveCurrentRecipe(int recipeId, String recipeName){
        preferences.edit()
                .putInt(PREF_KEY_RECIPE_ID, recipeId)
                .putString(PREF_KEY_RECIPE_NAME, recipeName)
                .apply();
    }

    public String getRecipeName(){
        return preferences.getString(PREF_KEY_RECIPE_NAME, null);
    }

    /*public int getRecipeId(){
        return preferences.getInt(PREF_KEY_RECIPE_ID, -1);
    }*/

    public Recipe getSavedRecipe(){
        int recipeId = preferences.getInt(PREF_KEY_RECIPE_ID, 0);
        return dao.getRecipeSync(recipeId);
    }

    public void clearWidgetData(){
        preferences.edit().clear().apply();
    }
}
