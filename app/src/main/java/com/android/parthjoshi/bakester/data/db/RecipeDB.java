package com.android.parthjoshi.bakester.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.android.parthjoshi.bakester.data.model.Recipe;

@Database(entities = {Recipe.class, Recipe.Ingredients.class, Recipe.Steps.class},
        version = 1, exportSchema = false)
public abstract class RecipeDB extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DB_NAME = "recipes_db";
    private static RecipeDB instance;

    public static RecipeDB getInstance(@NonNull Context context){

        if(instance == null){
            synchronized (LOCK){
                instance = Room.databaseBuilder(context, RecipeDB.class, DB_NAME)
                        .build();
            }
        }

        return instance;
    }

    public abstract RecipeDao getRecipeDao();
}
