package com.android.parthjoshi.bakester.di.modules;

import android.content.Context;
import android.content.Intent;

import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.di.appcontroller.ActivityContext;
import com.android.parthjoshi.bakester.view.IngredientsDetailActivity;
import com.android.parthjoshi.bakester.view.IngredientsFragment;
import com.android.parthjoshi.bakester.view.RecipeActivity;
import com.android.parthjoshi.bakester.view.RecipeMasterFragment;
import com.android.parthjoshi.bakester.view.StepDetailFragment;
import com.android.parthjoshi.bakester.view.adapters.RecipeMasterAdapter;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class RecipeMasterModule {

    @Provides
    static RecipeMasterFragment provideRecipeMasterFragment(){
        return new RecipeMasterFragment();
    }

    @Provides
    static RecipeMasterAdapter provideRecipeMasterAdapter(){
        return new RecipeMasterAdapter();
    }

    @Provides
    @ActivityContext
    static Context provideRecipeMasterContext(RecipeActivity activity){
        return activity;
    }

    @Provides
    static Intent ingredientIntent(@ActivityContext Context context){
        return new Intent(context, IngredientsDetailActivity.class);
    }

    @Provides
    static IngredientsFragment provideIngredientsFragment(){
        return new IngredientsFragment();
    }

    @Provides
    static StepDetailFragment provideStepDetailFragment(){
        return new StepDetailFragment();
    }

    @Provides
    static ArrayList<Recipe.Ingredients> provideIngredientsList(){
        return new ArrayList<>();
    }

    @Provides
    static ArrayList<Recipe.Steps> provideStepsList(){
        return new ArrayList<>();
    }
}
