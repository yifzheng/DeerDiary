package com.example.deerdiary;

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
    public ActivityScenarioRule<EditUserProfile> scenarioRule = new ActivityScenarioRule<EditUserProfile>(EditUserProfile.class);

    @Test
    public void A_UIElementsDisplayedTest(){}

    @Test
    public void B_DiscardChangedTest(){}

    @Test
    public void C_EditEntryTest(){}
}
