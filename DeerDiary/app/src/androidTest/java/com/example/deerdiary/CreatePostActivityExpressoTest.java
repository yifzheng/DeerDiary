package com.example.deerdiary;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.intent.Intents.*;
import static androidx.test.espresso.intent.matcher.IntentMatchers.*;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Random;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class CreatePostActivityExpressoTest {
    @Rule
    public ActivityScenarioRule<CreatePostActivity> activityActivityScenarioRule = new ActivityScenarioRule<CreatePostActivity>(CreatePostActivity.class);

    @Test
    public void A_UIElementsDisplayedTest(){
        onView(withId(R.id.createpost_title)).check(matches(isDisplayed())); // is title field rendered
        onView(withId(R.id.createpost_content)).check(matches(isDisplayed())); // is content field rendered
        onView(withId(R.id.createpost_create_button)).check(matches(isDisplayed())); // is create button rendered
        onView(withId(R.id.createpost_create_button)).check(matches(withText("CREATE"))); // does create button has text "CREATE"
        onView(withId(R.id.createpost_discard_button)).check(matches(isDisplayed())); // is discard button rendered
        onView(withId(R.id.createpost_discard_button)).check(matches(withText("DISCARD"))); // does discard button has text "DISCARD"
    }

    //<-----Issue #8: Scenario 1-------------->
    @Test
    public void B_MissingTitleFieldTest(){
        onView(withId(R.id.createpost_content_field)).perform(typeText("Content body"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.createpost_create_button)).perform(click());
        onView(withId(R.id.createpost_title_field)).check(matches(hasErrorText("Title cannot be empty")));
    }

    //<-----Issue #8: Scenario 1-------------->
    @Test
    public void C_MissingContentFieldTest(){
        onView(withId(R.id.createpost_title_field)).perform(typeText("Title name"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.createpost_create_button)).perform(click());
        onView(withId((R.id.createpost_content_field))).check(matches(hasErrorText("Content cannot be empty")));
    }

    //<-----Issue #8: Scenario 1-------------->
    @Test
    public void D_DiscardButtonRouteTest(){
        Intents.init();
        onView(withId(R.id.createpost_discard_button)).perform(click());
        intended(hasComponent("com.example.deerdiary.MainActivity"));
        Intents.release();
    }

    //<-----Issue #8: Scenario 2-------------->
    @Test
    public void E_DuplicateTitleTest(){

        // Create a random long number to be the title
        String randomText = Long.toString(new Random().nextLong());

        // Creating the diary for the first time with randomText as title
        onView(withId(R.id.createpost_title_field)).perform(typeText(randomText));
        onView(withId(R.id.createpost_content_field)).perform(typeText("Content body"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.createpost_create_button)).perform(click());

        // Going back to create post activity
        onView(withId(R.id.menu_create_post)).perform(click());

        // Creating the diary for the second with the same title
        onView(withId(R.id.createpost_title_field)).perform(typeText(randomText));
        onView(withId(R.id.createpost_content_field)).perform(typeText("Content body"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.createpost_create_button)).perform(click());
        onView(withId(R.id.createpost_title_field)).check(matches(hasErrorText("Title already exists")));
    }
}
