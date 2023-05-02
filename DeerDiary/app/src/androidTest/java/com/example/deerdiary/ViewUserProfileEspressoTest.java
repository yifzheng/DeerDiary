package com.example.deerdiary;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.SystemClock;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ViewUserProfileEspressoTest {
    @Rule
    public ActivityScenarioRule<ViewUserProfile> activityScenarioRule = new ActivityScenarioRule<ViewUserProfile>(ViewUserProfile.class);

    // Issue #23: Scenario 1
    @Test
    public void A_UIElementsDisplayedTest(){
        SystemClock.sleep(1500);
        onView(withId(R.id.user_profile_img)).check(matches(isDisplayed())); // check if imageview is rendered
        onView(withId(R.id.user_profile_name)).check(matches(isDisplayed())); // check if user name is displayed
        onView(withId((R.id.user_profile_email))).check((matches(isDisplayed())));// check if user email is displayed
        onView(withId((R.id.user_profile_edit_btn))).check(matches(isDisplayed())); // check if edit button is displayed
        onView(withId(R.id.user_profile_home_btn)).check(matches(isDisplayed())); // check if home button is displayed
    }

    @Test
    public void B_HomeButtonTest(){
        Intents.init();
        onView(withId(R.id.user_profile_home_btn)).perform(click());
        SystemClock.sleep(500);
        intended(hasComponent("com.example.deerdiary.MainActivity"));
        Intents.release();
    }
    // Issue #23: Scenario 2
    @Test
    public void C_EditButtonTest(){
        Intents.init();
        onView(withId(R.id.user_profile_edit_btn)).perform(click());
        SystemClock.sleep(500);
        intended(hasComponent("com.example.deerdiary.EditUserProfile"));
        Intents.release();
    }

}
