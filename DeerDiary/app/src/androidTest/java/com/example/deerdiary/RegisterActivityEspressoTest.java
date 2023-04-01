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
public class RegisterActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityActivityScenarioRule = new ActivityScenarioRule<RegisterActivity>(RegisterActivity.class);

    @Test
    public void A_UIElementsDisplayedTest(){
        onView(withId(R.id.register_firstname)).check(matches(isDisplayed())); // check if the edit text field is rendered
        onView(withId(R.id.register_lastname)).check(matches(isDisplayed())); // check if the edit text field is rendered
        onView(withId(R.id.register_email)).check(matches(isDisplayed())); // check if the edit text field is rendered
        onView(withId(R.id.register_password)).check(matches(isDisplayed())); // check if the edit text field is rendered
        onView(withId(R.id.register_button)).check(matches(isDisplayed())); // check if the button is rendered
        onView(withId(R.id.register_button)).check(matches(withText("Register")));
    }
    @Test
    public void B_MissingPasswordErrorTest(){
        onView(withId(R.id.register_firstname)).perform(typeText("Admin"));
        onView(withId(R.id.register_lastname)).perform(typeText("User"));
        onView(withId(R.id.register_email)).perform(typeText("admin@gmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());
        onView(withId(R.id.register_password)).check(matches(hasErrorText("Password cannot be empty")));
    }
    @Test
    public void C_MissingEmailErrorTest(){
        onView(withId(R.id.register_firstname)).perform(typeText("Admin"));
        onView(withId(R.id.register_lastname)).perform(typeText("User"));
        onView(withId(R.id.register_password)).perform(typeText("password"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());
        onView(withId(R.id.register_email)).check(matches(hasErrorText("Email cannot be empty")));
    }
    @Test
    public void D_MissingLastNameErrorTest(){
        onView(withId(R.id.register_firstname)).perform(typeText("Admin"));
        onView(withId(R.id.register_email)).perform(typeText("admin@gmail.com"));
        onView(withId(R.id.register_password)).perform(typeText("password"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());
        onView(withId(R.id.register_lastname)).check(matches(hasErrorText("Last Name cannot be empty")));
    }
    @Test
    public void E_MissingFirstNameErrorTest(){
        onView(withId(R.id.register_lastname)).perform(typeText("User"));
        onView(withId(R.id.register_email)).perform(typeText("admin@gmail.com"));
        onView(withId(R.id.register_password)).perform(typeText("password"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.register_button)).perform(click());
        onView(withId(R.id.register_firstname)).check(matches(hasErrorText("First Name cannot be empty")));
    }
    @Test
    public void F_LoginHereRouteTest(){
        Intents.init();
        onView(withId(R.id.register_loginhere)).perform(click());
        intended(hasComponent("com.example.deerdiary.LoginActivity"));
        Intents.release();
    }
}
