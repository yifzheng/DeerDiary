package com.example.deerdiary;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.view.KeyEvent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class EditDiaryPostEspressoTest {
    private final String contentText = "Content body";
    private static int recyclerItemCount;
    private static boolean isSetupDone = false;

    @Rule
    public ActivityScenarioRule<MainActivity> activityActivityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);


    // This setup ensures that there is at least 1 entry available to test
    @Before
    public void TestSetup() {
        if (!isSetupDone) {
            String randomText = Long.toString(new Random().nextLong());

            onView(withId(R.id.menu_create_post)).perform(click());
            onView(withId(R.id.createpost_title_field)).perform(typeText(randomText));
            onView(withId(R.id.createpost_content_field)).perform(typeText(contentText), closeSoftKeyboard());
            onView(withId(R.id.createpost_create_button)).perform(click());

            isSetupDone = true;
        }

        recyclerItemCount = getRecyclerItemCount() - 1;
    }

    //<-----Issue #4: Scenario 1-------------->
    @Test
    public void A_UIElementsDisplayedTest(){
       onView(withId(R.id.recyclerview)).perform(actionOnItemAtPosition(recyclerItemCount, click()));
       onView(withId(R.id.datetimeView)).check(matches(isDisplayed()));
       onView(withId(R.id.titleView)).check(matches(isDisplayed()));
       onView(withId(R.id.contentView)).check(matches(isDisplayed()));
       onView(withId(R.id.edit_btn)).check(matches(isDisplayed()));
       onView(withId(R.id.return_delete_btn)).check(matches(isDisplayed()));
       onView(withId(R.id.return_delete_btn)).check(matches(withText("RETURN")));
       onView(withId(R.id.edit_btn)).perform(click());
       onView(withId(R.id.return_delete_btn)).check(matches(withText("DELETE")));
    }

    //<-----Issue #4: Scenario 1-------------->
    @Test
    public void B_EmptyContentTest(){
        onView(withId(R.id.recyclerview)).perform(actionOnItemAtPosition(recyclerItemCount, click()));
        onView(withId(R.id.edit_btn)).perform(click());
        onView(withId(R.id.contentView)).perform(click());

        // Deleting all the text within the content text box
        for (int i = 0; i < contentText.length(); i++){
            onView(withId(R.id.contentView)).perform(pressKey(KeyEvent.KEYCODE_DEL));
        }

        onView(withId(R.id.contentView)).perform(closeSoftKeyboard());
        onView(withId(R.id.edit_btn)).perform(click());
        onView(withId(R.id.contentView)).check(matches(hasErrorText("Content cannot be empty")));
    }

    //<-----Issue #4: Scenario 1-------------->
    @Test
    public void C_ReturnButtonTest(){
        onView(withId(R.id.recyclerview)).perform(actionOnItemAtPosition(recyclerItemCount, click()));
        onView(withId(R.id.return_delete_btn)).perform(click());
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()));
    }

    //<-----Issue #3: Scenario 1-------------->
    @Test
    public void D_DeleteDiaryTest(){
        int newRecyclerItemCount;

        onView(withId(R.id.recyclerview)).perform(actionOnItemAtPosition(recyclerItemCount, click()));
        onView(withId(R.id.edit_btn)).perform(click());
        onView(withId(R.id.return_delete_btn)).perform(click());

        // -1 to reflect 0 based numbering
        newRecyclerItemCount = getRecyclerItemCount() - 1;
        assertEquals(recyclerItemCount - 1, newRecyclerItemCount);
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()));
    }

    // Helper function to retrieve the number of items in recyclerView
    private int getRecyclerItemCount() {
        // Using atomic number to synchronize values between this thread and the scenario thread
        AtomicInteger itemCount = new AtomicInteger();
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        // Start MainActivity to retrieve number of items in recyclerView
        scenario.onActivity(activity -> {
            RecyclerView recyclerView = activity.findViewById(R.id.recyclerview);

            if (recyclerView != null && recyclerView.getAdapter() != null) {
                itemCount.set(recyclerView.getAdapter().getItemCount());
            }
        });

        return itemCount.get();
    }
}
