package com.example.deerdiary;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.intent.Intents.*;
import static androidx.test.espresso.intent.matcher.IntentMatchers.*;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class LoginActivityEspressoTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityActivityScenarioRule = new ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    @Test
    public void A_UIElementsDisplayedTest(){
        onView(withId(R.id.login_email)).check(matches(isDisplayed())); // check if field is rendered
        onView(withId(R.id.login_password)).check(matches(isDisplayed())); // check if field is rendered
        onView(withId(R.id.login_button)).check(matches(isDisplayed())); // check if button is rendered
        onView(withId(R.id.login_button)).check(matches(withText("Login")));
    }
    @Test
    public void B_MissingEmailFieldsTest() {
        onView(withId(R.id.login_password)).perform(typeText("password"), ViewActions.closeSoftKeyboard()); // type password
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_email)).check(matches(hasErrorText("Email cannot be empty")));
    }
    @Test
    public void C_MissingPasswordFieldsTest() {
        onView(withId(R.id.login_email)).perform(typeText("admin@gmail.com"), ViewActions.closeSoftKeyboard()); // type password
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_password)).check(matches(hasErrorText("Password cannot be empty")));
    }
    @Test
    public void D_RegisterHereRouteTest(){
        Intents.init();
        onView(withId(R.id.login_registerhere)).perform(click());
        intended(hasComponent("com.example.deerdiary.RegisterActivity"));
        Intents.release();
    }

}
