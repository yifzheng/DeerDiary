package com.example.deerdiary;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.intent.Intents.*;
import static androidx.test.espresso.intent.matcher.IntentMatchers.*;

import static org.junit.Assert.assertEquals;

import android.content.Intent;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class StartActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<StartActivity> activityActivityScenarioRule = new ActivityScenarioRule<>(StartActivity.class);

    @Test
    public void A_UIElementsDisplayedTest()
    {
        onView(withId(R.id.app_logo)).check(matches(isDisplayed())); // check if logo is displayed
        onView(withId(R.id.appName)).check(matches(isDisplayed())); // check if app name is displayed
        onView(withId(R.id.start_login_btn)).check(matches(isDisplayed())); // check if login button is displayed
        onView(withId(R.id.start_login_btn)).check(matches(withText("Login"))); // check if login button has 'login' text
        onView(withId(R.id.start_register_btn)).check(matches(isDisplayed())); // check if register button is displayed
        onView(withId(R.id.start_register_btn)).check(matches(withText("Register"))); // check if register button has 'register' text
    }

    @Test
    public void B_LoginButtonClickTest(){
        Intents.init();
        onView(withId(R.id.start_login_btn)).perform(click()); // test clicks button
        intended(hasComponent("com.example.deerdiary.LoginActivity")); // check if page routed to login activity
        Intents.release();
    }

    @Test
    public void C_RegisterButtonClickTest() {
        Intents.init();
        onView(withId(R.id.start_register_btn)).perform(click()); // test clicks button
        intended(hasComponent("com.example.deerdiary.RegisterActivity")); // check if page routed to register activity
        Intents.release();
    }
}
