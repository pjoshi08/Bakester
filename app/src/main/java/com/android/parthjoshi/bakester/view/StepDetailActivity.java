package com.android.parthjoshi.bakester.view;

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

public class StepDetailActivity extends AppCompatActivity implements HasSupportFragmentInjector,
        StepDetailFragment.OnStepChangeListener {

    @Inject DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject StepDetailFragment fragment;

    private Recipe.Steps step;
    private int stepNumber = 0;
    private int stepCount;
    private ArrayList<Recipe.Steps> steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            step = bundle.getParcelable(Constants.STEP_KEY);
            stepNumber = bundle.getInt(Constants.STEP_ID_KEY, 0);
            stepCount = bundle.getInt(Constants.STEP_COUNT_KEY);
            steps = bundle.getParcelableArrayList(Constants.STEPS_LIST_KEY);
            setTitle(bundle.getString(Constants.RECIPE_NAME_KEY));
        }

        if (savedInstanceState == null) {
            initStepFragment(step);
        }

        if(getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initStepFragment(Recipe.Steps step){

        fragment.setStep(step);
        fragment.setStepExtras(stepNumber, stepCount);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.step_container, fragment)
                .commit();

    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    public void prevStep(int position) {
        fragment = new StepDetailFragment();

        if(position == 0) {
            stepNumber = steps.size() - 1;
            Recipe.Steps step = steps.get(stepNumber);
            fragment.setStep(step);
            fragment.setStepExtras(stepNumber, stepCount);
        } else {
            stepNumber = position - 1;
            fragment.setStep(steps.get(stepNumber));
            fragment.setStepExtras(stepNumber, stepCount);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.step_container, fragment)
                .commit();

    }

    @Override
    public void nextStep(int position) {

        fragment = new StepDetailFragment();

        if(position == steps.size() - 1) {
            stepNumber = 0;
            fragment.setStep(steps.get(stepNumber));
            fragment.setStepExtras(stepNumber, stepCount);
        } else {
            stepNumber = position + 1;
            fragment.setStep(steps.get(stepNumber));
            fragment.setStepExtras(stepNumber, stepCount);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.step_container, fragment)
                .commit();
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
