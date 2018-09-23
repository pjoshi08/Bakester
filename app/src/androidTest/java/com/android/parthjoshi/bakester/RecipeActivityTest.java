package com.android.parthjoshi.bakester;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android.parthjoshi.bakester.data.db.RecipeDB;
import com.android.parthjoshi.bakester.data.db.RecipeDao;
import com.android.parthjoshi.bakester.util.RecyclerViewMatcher;
import com.android.parthjoshi.bakester.util.TestDataGenerator;
import com.android.parthjoshi.bakester.view.RecipeActivity;
import com.android.parthjoshi.bakester.view.RecipeMasterFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    @Rule public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<RecipeActivity>(RecipeActivity.class){
                /*@Override
                protected Intent getActivityIntent() {
                    //return super.getActivityIntent();
                    Intent intent = new Intent();
                    intent.putExtra(Constants.RECIPE_KEY, TestDataGenerator.getMockRecipe());
                    return intent;
                }*/
            };

    private RecipeDao recipeDao;

    @Before
    public void setUp(){

        recipeDao = RecipeDB.getInstance(InstrumentationRegistry.getTargetContext()).getRecipeDao();
        recipeDao.addRecipes(TestDataGenerator.getMockedRecipeList());

        RecipeMasterFragment fragment = new RecipeMasterFragment();
        fragment.setRecipe(TestDataGenerator.getMockRecipe());
        mActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_steps_container, fragment).commit();
    }

    @Test
    public void recipeFirstStepTest(){

        onView(withRecyclerView(R.id.stepsRecyclerView).atPosition(0))
                .check(matches(hasDescendant(withText(
                        TestDataGenerator.getMockedRecipeList().get(0)
                                .getSteps().get(0).getShortDescription()))));
    }

    @After
    public void tearDown(){
        recipeDao.clearAll();
    }

    // Convenience Helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId){
        return new RecyclerViewMatcher(recyclerViewId);
    }
}
