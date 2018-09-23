package com.android.parthjoshi.bakester.viewmodel.recipestep;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class RecipeStepVMFactory implements ViewModelProvider.Factory {

    @Inject
    public RecipeStepVMFactory(){}

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(RecipeStepVM.class))
            return (T) new RecipeStepVM();
        throw new IllegalArgumentException("Unknown ViewModel Class");
    }
}
