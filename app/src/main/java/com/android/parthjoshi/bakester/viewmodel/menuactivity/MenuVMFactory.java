package com.android.parthjoshi.bakester.viewmodel.menuactivity;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.android.parthjoshi.bakester.data.Repository;

import javax.inject.Inject;

public class MenuVMFactory implements ViewModelProvider.Factory {

    private Repository repository;

    @Inject
    public MenuVMFactory(Repository repository){
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if(modelClass.isAssignableFrom(MenuVM.class))
            return (T) new MenuVM(repository);

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
