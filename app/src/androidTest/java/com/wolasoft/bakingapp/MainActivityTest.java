package com.wolasoft.bakingapp;

import static android.support.test.espresso.Espresso.*;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;

import com.wolasoft.bakingapp.ui.fragments.RecipeListFragment;

import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String RECIPE_LIST_FRAGMENT_TAG = "recipe_list_fragment_tag";
    private static final String RECIPE_DETAIL_FRAGMENT_TAG = "recipe_detail_fragment_tag";
    private static final String RECIPE_STEP_FRAGMENT_TAG = "recipe_step_fragment_tag";

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
