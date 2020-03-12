package com.openclassrooms.mareu.ui;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.openclassrooms.mareu.R;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

public class ListMeetingActivityTest {

    /* test fait à partir de DummyMeetingGenerator */
    private ListMeetingActivity mActivity;
    private static int ITEMS_COUNT = 6;

    @Rule
    public ActivityTestRule<ListMeetingActivity> mActivityRule =
            new ActivityTestRule(ListMeetingActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    @Test
    public void AddMeeting_isDisplay() {
        onView(ViewMatchers.withId(R.id.listMeetingActivity))
                .check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.fab))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(ViewMatchers.withId(R.id.newMeeting)).check(matches(isDisplayed()));
    }

    @Test
    public void Filters_isDisplay() {
        onView(ViewMatchers.withId(R.id.listMeetingActivity))
                .check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.action_filters))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(ViewMatchers.withId(R.id.filters)).check(matches(isDisplayed()));
    }

    @Test
    public void deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(ViewMatchers.withId(R.id.main_recyclerView)).check(new RecyclerViewItemCountAssertion(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(ViewMatchers.withId(R.id.main_recyclerView))
                .perform( RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.item_delete_btn)));
        // Then : the number of element is 11
        onView(ViewMatchers.withId(R.id.main_recyclerView)).check(new RecyclerViewItemCountAssertion(ITEMS_COUNT-1));
    }

    @Test
    public void addAction_shouldAddItem() {
        onView(ViewMatchers.withId(R.id.main_recyclerView)).check(new RecyclerViewItemCountAssertion(ITEMS_COUNT));
        onView(ViewMatchers.withId(R.id.fab)).perform(click());
        onView(ViewMatchers.withId(R.id.topicEditText)).perform(typeText("Test"));
        onView(ViewMatchers.withId(R.id.dateEditText)).perform(click());
        onView(ViewMatchers.withClassName(Matchers.equalTo (DatePicker.class.getName())))
                .perform(PickerActions.setDate(2020, 4, 23));
        onView (withText ("OK")).perform (click ());
        onView(ViewMatchers.withId(R.id.timeEditText)).perform(click());
        onView(ViewMatchers.withClassName(Matchers.equalTo (TimePicker.class.getName())))
                .perform(PickerActions.setTime(7, 00));
        onView (withText ("OK")).perform (click ());
        onView(ViewMatchers.withId(R.id.emailEditText)).perform(typeText("xyz@gmail.com"));
        onView(ViewMatchers.withId(R.id.add_email_btn)).perform(click());
        onView(ViewMatchers.withId(R.id.action_ok)).perform(click());

        onView(ViewMatchers.withId(R.id.main_recyclerView)).check(new RecyclerViewItemCountAssertion(ITEMS_COUNT+1));
    }



    @Test
    public void checkFilterDay() {
        onView(ViewMatchers.withId(R.id.action_filters)).perform(click());
        onView(ViewMatchers.withId(R.id.filters)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.filters_date))
                .perform(click());

        onView(ViewMatchers.withClassName(Matchers.equalTo (DatePicker.class.getName())))
                .perform(PickerActions.setDate(2020, 2, 23));

        onView (withText ("OK")).perform (click ());

        onView(ViewMatchers.withId(R.id.action_ok))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(ViewMatchers.withId(R.id.listMeetingActivity)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.main_recyclerView)).check(matches(hasChildCount(0)));
    }

    @Test
    public void checkFilterRoom() {
        onView(ViewMatchers.withId(R.id.action_filters)).perform(click());
        onView(ViewMatchers.withId(R.id.filters)).check(matches(isDisplayed()));

        onView(withText(containsString("09")))
                .perform(click());
        onView(ViewMatchers.withId(R.id.action_ok)).perform(click());

        onView(ViewMatchers.withId(R.id.listMeetingActivity)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.main_recyclerView)).check(matches(hasChildCount(0)));
    }
}

class RecyclerViewItemCountAssertion implements ViewAssertion {
    private final int expectedCount;

    public RecyclerViewItemCountAssertion(int expectedCount) {
        this.expectedCount = expectedCount;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter.getItemCount(), is(expectedCount));
    }
}

class MyViewAction {

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

}