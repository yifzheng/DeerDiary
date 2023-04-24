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
public class EditUserProfileEspressoTest {
    @Rule
    public ActivityScenarioRule<ViewUserProfile> scenarioRule = new ActivityScenarioRule<ViewUserProfile>(ViewUserProfile.class);

    @Test
    public void A_UIElementsDisplayedTest(){
        onView(withId(R.id.user_profile_edit_btn)).perform(click());
        SystemClock.sleep(1500);
        onView(withId(R.id.edit_user_text)).check(matches(isDisplayed()));
        onView(withId(R.id.user_profile_img)).check(matches(isDisplayed())); // check if UI is displayed
        onView(withId(R.id.edit_user_pic_text)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_user_first_name)).check(matches(isDisplayed()));
        onView(withId(R.id.edit_user_last_name)).check(matches(isDisplayed()));
        onView(withId(R.id.save_profile_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.cancel_profile_btn)).check(matches(isDisplayed()));
    }

    @Test
    public void A_DiscardChangedTest(){
        onView(withId(R.id.user_profile_edit_btn)).perform(click());
        SystemClock.sleep(1000);
        Intents.init();
        onView(withId(R.id.cancel_profile_btn)).perform(click());
        intended(hasComponent("com.example.deerdiary.ViewUserProfile"));
        Intents.release();
    }

}
