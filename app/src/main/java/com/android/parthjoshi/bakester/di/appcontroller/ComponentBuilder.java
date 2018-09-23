package com.android.parthjoshi.bakester.di.appcontroller;

import com.android.parthjoshi.bakester.di.modules.IngredientsDetailModule;
import com.android.parthjoshi.bakester.di.modules.RecipeMasterModule;
import com.android.parthjoshi.bakester.di.modules.RecipeWidgetModule;
import com.android.parthjoshi.bakester.di.modules.StepDetailModule;
import com.android.parthjoshi.bakester.view.IngredientsDetailActivity;
import com.android.parthjoshi.bakester.view.IngredientsFragment;
import com.android.parthjoshi.bakester.view.MenuActivity;
import com.android.parthjoshi.bakester.di.modules.MenuActivityModule;
import com.android.parthjoshi.bakester.view.RecipeActivity;
import com.android.parthjoshi.bakester.view.RecipeMasterFragment;
import com.android.parthjoshi.bakester.view.StepDetailActivity;
import com.android.parthjoshi.bakester.view.StepDetailFragment;
import com.android.parthjoshi.bakester.widget.RecipeWidgetService;
import com.android.parthjoshi.bakester.widget.RecipeWidgetProvider;
import com.android.parthjoshi.bakester.widget.WidgetUpdateService;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
abstract class ComponentBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = MenuActivityModule.class)
    abstract MenuActivity bindMenuActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = {RecipeMasterModule.class})
    abstract RecipeActivity bindRecipeActivity();

    @PerFragment
    @ContributesAndroidInjector(modules = {RecipeMasterModule.class})
    abstract RecipeMasterFragment bindRecipeMasterFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = IngredientsDetailModule.class)
    abstract IngredientsDetailActivity bindIngredientsDetailActivity();

    @PerFragment
    @ContributesAndroidInjector(modules = IngredientsDetailModule.class)
    abstract IngredientsFragment bindIngredientsFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = StepDetailModule.class)
    abstract StepDetailActivity bindStepDetailActivity();

    @PerFragment
    @ContributesAndroidInjector(modules = StepDetailModule.class)
    abstract StepDetailFragment bindStepDetailFragment();

    @PerWidget
    @ContributesAndroidInjector(modules = RecipeWidgetModule.class)
    abstract RecipeWidgetProvider bindRecipeWidgetProvider();

    @PerService
    @ContributesAndroidInjector(modules = RecipeWidgetModule.class)
    abstract RecipeWidgetService bindIngredientsWidgetService();

    @PerService
    @ContributesAndroidInjector(modules = RecipeWidgetModule.class)
    abstract WidgetUpdateService bindWidgetUpdateService();
}
