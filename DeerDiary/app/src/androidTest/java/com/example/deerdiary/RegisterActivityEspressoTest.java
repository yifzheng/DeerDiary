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
public class RegisterActivityEspressoTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityActivityScenarioRule = new ActivityScenarioRule<RegisterActivity>(RegisterActivity.class);

    @Test
    public void A_UIElementsDisplayedTest(){}
    @Test
    public void B_MissingFieldErrorTest(){}
    @Test
    public void C_RegisterButtonClickTest(){}
    @Test
    public void D_LoginHereRouteTest(){}
}
