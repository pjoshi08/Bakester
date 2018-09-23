package com.android.parthjoshi.bakester.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.util.Constants;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static com.android.parthjoshi.bakester.util.Constants.INGREDIENTS_KEY;

public class IngredientsDetailActivity extends AppCompatActivity implements HasSupportFragmentInjector{

    @Inject DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;
    @Inject IngredientsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_detail);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if(intent.hasExtra(INGREDIENTS_KEY))
                initFragment(intent.<Recipe.Ingredients>getParcelableArrayListExtra(INGREDIENTS_KEY));
            if(intent.hasExtra(Constants.RECIPE_NAME_KEY))
                setTitle(intent.getStringExtra(Constants.RECIPE_NAME_KEY));
        }

        if(getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initFragment(ArrayList<Recipe.Ingredients> ingredients){

        fragment.setIngredients(ingredients);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.ingredients_container, fragment)
                .commit();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
