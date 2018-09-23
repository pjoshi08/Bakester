package com.android.parthjoshi.bakester.data.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.android.parthjoshi.bakester.data.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addRecipes(List<Recipe> recipes);

    @Query("SELECT * FROM recipes WHERE id = :id")
    Recipe getRecipeSync(int id);

    @Query("DELETE FROM recipes")
    void clearAll();
}
