package com.personal_finance_app;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SideMenuTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void sideMenuTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.emailLogin),
                        childAtPosition(
                                allOf(withId(R.id.login),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("user1@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.passwordLogin),
                        childAtPosition(
                                allOf(withId(R.id.login),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("123456"), closeSoftKeyboard());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.loginButton), withContentDescription("REGISTER"),
                        childAtPosition(
                                allOf(withId(R.id.login),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                3),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.proceedButton),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.menu_button), withContentDescription("Overflow Menu Button"),
                        childAtPosition(
                                allOf(withId(R.id.side_menu_fragment),
                                        childAtPosition(
                                                withId(R.id.expense_layout),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(android.R.id.title), withText("Help"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton4 = onView(
                allOf(withId(R.id.menu_button), withContentDescription("Overflow Menu Button"),
                        childAtPosition(
                                allOf(withId(R.id.side_menu_fragment),
                                        childAtPosition(
                                                withId(R.id.expense_layout),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageButton4.perform(click());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(android.R.id.title), withText("About"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView2.perform(click());

        ViewInteraction appCompatImageButton5 = onView(
                allOf(withId(R.id.menu_button), withContentDescription("Overflow Menu Button"),
                        childAtPosition(
                                allOf(withId(R.id.side_menu_fragment),
                                        childAtPosition(
                                                withId(R.id.about_activity),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageButton5.perform(click());

        ViewInteraction materialTextView3 = onView(
                allOf(withId(android.R.id.title), withText("Edit Profile"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView3.perform(click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.editUsername),
                        childAtPosition(
                                allOf(withId(R.id.containerFields),
                                        childAtPosition(
                                                withId(R.id.menu_edit_profile),
                                                1)),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("user2"), closeSoftKeyboard());

        pressBack();

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.saveButton), withText("Save Changes"),
                        childAtPosition(
                                allOf(withId(R.id.containerFields),
                                        childAtPosition(
                                                withId(R.id.menu_edit_profile),
                                                1)),
                                9),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.editPassword),
                        childAtPosition(
                                allOf(withId(R.id.containerFields),
                                        childAtPosition(
                                                withId(R.id.menu_edit_profile),
                                                1)),
                                8),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("12"), closeSoftKeyboard());

        pressBack();

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editPassword), withText("12"),
                        childAtPosition(
                                allOf(withId(R.id.containerFields),
                                        childAtPosition(
                                                withId(R.id.menu_edit_profile),
                                                1)),
                                8),
                        isDisplayed()));
        appCompatEditText5.perform(replaceText("123456"));

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editPassword), withText("123456"),
                        childAtPosition(
                                allOf(withId(R.id.containerFields),
                                        childAtPosition(
                                                withId(R.id.menu_edit_profile),
                                                1)),
                                8),
                        isDisplayed()));
        appCompatEditText6.perform(closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.saveButton), withText("Save Changes"),
                        childAtPosition(
                                allOf(withId(R.id.containerFields),
                                        childAtPosition(
                                                withId(R.id.menu_edit_profile),
                                                1)),
                                9),
                        isDisplayed()));
        materialButton3.perform(click());

        ViewInteraction appCompatImageButton6 = onView(
                allOf(withId(R.id.menu_button), withContentDescription("Overflow Menu Button"),
                        childAtPosition(
                                allOf(withId(R.id.side_menu_fragment),
                                        childAtPosition(
                                                withId(R.id.expense_layout),
                                                0)),
                                0),
                        isDisplayed()));
        appCompatImageButton6.perform(click());

        ViewInteraction materialTextView4 = onView(
                allOf(withId(android.R.id.title), withText("Log out"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                0),
                        isDisplayed()));
        materialTextView4.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
