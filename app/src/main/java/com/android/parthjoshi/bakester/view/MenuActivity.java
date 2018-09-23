package com.android.parthjoshi.bakester.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.databinding.ActivityMenuBinding;
import com.android.parthjoshi.bakester.util.SimpleIdlingResource;
import com.android.parthjoshi.bakester.view.adapters.RecipeGridAdapter;
import com.android.parthjoshi.bakester.view.adapters.RecipesAdapter;
import com.android.parthjoshi.bakester.viewmodel.menuactivity.MenuVM;
import com.android.parthjoshi.bakester.viewmodel.menuactivity.MenuVMFactory;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjection;

public class MenuActivity extends AppCompatActivity implements RecipesAdapter.OnItemClickListener,
        RecipeGridAdapter.OnItemClickListener {

    @Inject MenuVMFactory factory;
    @Inject RecipesAdapter adapter;
    @Inject LinearLayoutManager layoutManager;
    @Inject RecipeGridAdapter gridAdapter;

    @Inject
    @Named("RecipeIntent")
    Intent recipeIntent;

    private MenuVM vm;
    private ActivityMenuBinding binding;
    private boolean twoPane;

    // Idling Resource which will be null in production
    @Nullable private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        getIdlingResource();

        if(mIdlingResource != null)
            mIdlingResource.setIsIdleState(false);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);

        binding.setLifecycleOwner(this);

        vm = ViewModelProviders.of(this, factory).get(MenuVM.class);
        binding.setViewmodel(vm);

        setObservers();

        if(binding.recipeMenuGrid != null)
            twoPane = true;
    }

    private void setObservers(){

        vm.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(!twoPane) {
                    binding.recipesRecyclerView.setLayoutManager(layoutManager);
                    binding.setAdapter(adapter);
                    adapter.setData(recipes);
                }else {
                    binding.setGridAdapter(gridAdapter);
                    gridAdapter.setRecipes(recipes);
                }

                if(mIdlingResource != null)
                    mIdlingResource.setIsIdleState(true);
            }
        });
    }

    @Override
    public void onClick(int position) {
        vm.onClick(position, this);
    }

    @Override
    public void onGridItemClick(int position) {
        vm.onClick(position, this);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource(){
        if(mIdlingResource == null)
            mIdlingResource = new SimpleIdlingResource();

        return mIdlingResource;
    }
}
