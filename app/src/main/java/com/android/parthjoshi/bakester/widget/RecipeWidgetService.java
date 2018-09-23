package com.android.parthjoshi.bakester.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.Repository;
import com.android.parthjoshi.bakester.data.model.Recipe;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;


public class RecipeWidgetService extends RemoteViewsService{

    @Inject Repository repository;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        AndroidInjection.inject(this);

        Log.d("RecipeWidgetService", "RecipeWidgetService Called");
        return new IngredientRemoteViewsFactory(this.getApplicationContext(), repository);
    }
}

class IngredientRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Repository repository;
    private List<Recipe.Ingredients> ingredients;

    public IngredientRemoteViewsFactory(Context appContext, Repository repository){
        mContext = appContext;
        this.repository = repository;
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {

        Log.d("WidgetService", "On DataSetChanged called");
        Recipe recipe = repository.getSavedRecipe();
        if (recipe != null)
            ingredients = recipe.getIngredients();
        Log.d("WidgetService", "Ingredients List: " + ingredients);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d("WidgetService", "Ingredients count:" + ingredients.size());
        return (ingredients != null ? ingredients.size() : 0);
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if(ingredients == null || ingredients.size() == 0)
            return null;

        Recipe.Ingredients ingredient = ingredients.get(position);
        String strIngredient = ingredient.getIngredient() + " - " + ingredient.getQuantity() + " " + ingredient.getMeasure();
        Log.d("WidgetService", "Ingredient String: " + strIngredient);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item);
        remoteViews.setTextViewText(R.id.recipe_ingredients, strIngredient);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}


