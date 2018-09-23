package com.android.parthjoshi.bakester.di.modules;

import com.android.parthjoshi.bakester.view.IngredientsFragment;
import com.android.parthjoshi.bakester.view.adapters.IngredientsAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class IngredientsDetailModule {

    @Provides
    static IngredientsFragment provideIngredientsFragment(){
        return new IngredientsFragment();
    }

    @Provides
    static IngredientsAdapter provideIngredientsAdapter(){
        return new IngredientsAdapter();
    }
}
