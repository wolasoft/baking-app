package com.wolasoft.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;

import com.wolasoft.bakingapp.ui.fragments.RecipeListFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String RECIPE_LIST_FRAGMENT_TAG = "recipe_list_fragment_tag";

    private RecipeListFragment recipeListFragment;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        mainActivity = mainActivityTestRule.getActivity();
        recipeListFragment = RecipeListFragment.newInstance();
        fragmentManager = mainActivityTestRule.getActivity().getSupportFragmentManager();
    }

    @Test
    public void ensureContainerFragmentIsPresent() {
        View fragmentContainer = mainActivity.findViewById(R.id.fragment_container);
        assertThat(fragmentContainer, notNullValue());
        assertThat(fragmentContainer, instanceOf(FrameLayout.class));
        fragmentManager.beginTransaction();
    }

    @Test
    public void openRecipeListFragment() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, recipeListFragment, RECIPE_LIST_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }
}
