package com.android.parthjoshi.bakester.data.db;

import com.android.parthjoshi.bakester.data.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.arch.persistence.room.TypeConverter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class RecipeTypeConverter {

    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<Recipe.Ingredients> fromIngredientsJson(String ingredientsJson){

        if(ingredientsJson.isEmpty())
            return Collections.emptyList();

        Type type = new TypeToken<List<Recipe.Ingredients>>(){}.getType();

        return gson.fromJson(ingredientsJson, type);
    }

    @TypeConverter
    public static String fromIngredientsList(List<Recipe.Ingredients> ingredients){

        if(ingredients == null || ingredients.size() == 0)
            return "";

        return gson.toJson(ingredients);
    }

    @TypeConverter
    public static List<Recipe.Steps> fromStepsJson(String stepsJson){

        if(stepsJson == null || stepsJson.isEmpty())
            return Collections.emptyList();

        Type type = new TypeToken<List<Recipe.Steps>>(){}.getType();

        return gson.fromJson(stepsJson, type);
    }

    @TypeConverter
    public static String fromStepsList(List<Recipe.Steps> steps){

        if(steps == null || steps.size() == 0)
            return "";

        return gson.toJson(steps);
    }
}
