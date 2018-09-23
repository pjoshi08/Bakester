package com.android.parthjoshi.bakester;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.android.parthjoshi.bakester.util.RecyclerViewMatcher;
import com.android.parthjoshi.bakester.view.MenuActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static org.hamcrest.Matchers.allOf;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import android.support.test.espresso.contrib.RecyclerViewActions;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
public class MenuActivityTest {

    private static final String RECIPE_NAME = "Brownies";

    @Rule public ActivityTestRule<MenuActivity> mActivityTestRule =
            new ActivityTestRule<>(MenuActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource(){

        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();

        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void recyclerViewFirstItemTest(){

        // Test to check the recylcerView data is getting loaded properly at first position
        onView(withRecyclerView(R.id.recipes_recycler_view).atPosition(0))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
    }

    @Test
    public void recyclerViewLastItemTest(){

        // recyclerview scroll to position
        onView(withId(R.id.recipes_recycler_view)).perform(RecyclerViewActions.scrollToPosition(3));

        // Test to check the last item of Recipes Recyclerview
        onView(withRecyclerView(R.id.recipes_recycler_view).atPosition(3))
                .check(matches(hasDescendant(withText("Cheesecake"))));
    }

    @Test
    public void recyclerViewOnItemClickTest(){

        // Find and Perform action of RecyclerView Item
        onView(withId(R.id.recipes_recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // Check if the action performed yeilded correct result
        onView(allOf(instanceOf(TextView.class), withParent(withResourceName("action_bar"))))
                .check(matches(withText(RECIPE_NAME)));
    }

    @After
    public void unregisterIdlingRes(){
        if(mIdlingResource != null)
            IdlingRegistry.getInstance().unregister(mIdlingResource);
    }

    // Convenience Helper
    public static RecyclerViewMatcher withRecyclerView(int recyclerViewId){
        return new RecyclerViewMatcher(recyclerViewId);
    }
}
