package com.android.parthjoshi.bakester.di.modules;

import com.android.parthjoshi.bakester.view.StepDetailFragment;
import com.android.parthjoshi.bakester.viewmodel.recipestep.RecipeStepVMFactory;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class StepDetailModule {

    @Provides
    static StepDetailFragment provideStepDetailFragment(){
        return new StepDetailFragment();
    }

    @Provides
    static RecipeStepVMFactory provideRecipeStepVMFactory(){
        return new RecipeStepVMFactory();
    }
}
