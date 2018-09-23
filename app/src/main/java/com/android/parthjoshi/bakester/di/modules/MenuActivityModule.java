package com.android.parthjoshi.bakester.di.modules;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.LinearLayout;

import com.android.parthjoshi.bakester.data.Repository;
import com.android.parthjoshi.bakester.di.appcontroller.ActivityContext;
import com.android.parthjoshi.bakester.view.MenuActivity;
import com.android.parthjoshi.bakester.view.RecipeActivity;
import com.android.parthjoshi.bakester.view.adapters.RecipeGridAdapter;
import com.android.parthjoshi.bakester.view.adapters.RecipesAdapter;
import com.android.parthjoshi.bakester.viewmodel.menuactivity.MenuVMFactory;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class MenuActivityModule {

    @Provides
    static MenuVMFactory provideMenuVMFactory(Repository repository){
        return new MenuVMFactory(repository);
    }

    @Provides
    static RecipesAdapter provideAdapter(@ActivityContext Context context, MenuActivity activity){
        return new RecipesAdapter(context, activity);
    }

    @Provides
    @ActivityContext
    static Context provideActivityContext(MenuActivity activity){
        return activity;
    }

    @Provides
    static LinearLayoutManager provideLayoutManager(@ActivityContext Context context){
        return new LinearLayoutManager(context, LinearLayout.VERTICAL, false);
    }

    @Provides
    static RecipeGridAdapter provideRecipeGridAdapter(MenuActivity activity){
        return new RecipeGridAdapter(activity);
    }

    @Provides
    @Named("RecipeIntent")
    static Intent provideRecipeIntent(@ActivityContext Context context){
        return new Intent(context, RecipeActivity.class);
    }
}
