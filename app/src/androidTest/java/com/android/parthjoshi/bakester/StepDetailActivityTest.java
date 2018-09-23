package com.android.parthjoshi.bakester;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.action.ViewActions.click;
import com.android.parthjoshi.bakester.data.model.Recipe;
import com.android.parthjoshi.bakester.util.Constants;
import com.android.parthjoshi.bakester.util.TestDataGenerator;
import com.android.parthjoshi.bakester.view.StepDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class StepDetailActivityTest {

    @Rule public ActivityTestRule<StepDetailActivity> mActivityTestRule =
            new ActivityTestRule<StepDetailActivity>(StepDetailActivity.class){
                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.STEP_KEY,
                            TestDataGenerator.getMockRecipe().getSteps().get(0));
                    intent.putExtra(Constants.STEP_ID_KEY, 0);
                    intent.putExtra(Constants.STEP_COUNT_KEY,
                            TestDataGenerator.getMockRecipe().getSteps().size() - 1);
                    ArrayList<Recipe.Steps> steps = new ArrayList<>();
                    steps.addAll(TestDataGenerator.getMockRecipe().getSteps());
                    intent.putExtra(Constants.STEPS_LIST_KEY, steps);

                    return intent;
                }
            };

    @Test
    public void shortDescriptionViewTest(){

        onView(withId(R.id.tvShortDescription)).check(matches(isDisplayed()));

        onView(withId(R.id.tvShortDescription))
                .check(matches(withText(
                        TestDataGenerator.getMockRecipe().getSteps().get(0).getShortDescription())));
    }

    @Test
    public void nextStepTest(){

        onView(withId(R.id.next_step)).perform(click());

        onView(withId(R.id.tvShortDescription))
                .check(matches(withText(
                        TestDataGenerator.getMockRecipe().getSteps().get(1).getShortDescription())));
    }

    @Test
    public void prevStepTest(){

        int stepCount = TestDataGenerator.getMockRecipe().getSteps().size();
        Recipe.Steps currentStep = TestDataGenerator.getMockRecipe().getSteps().get(stepCount - 1);

        onView(withId(R.id.prev_step)).perform(click());

        onView(withId(R.id.tvShortDescription))
                .check(matches(withText(currentStep.getShortDescription())));
    }
}
