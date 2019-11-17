package piedel.piotr.thesis.util

//import android.view.View
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
//import androidx.test.espresso.matcher.ViewMatchers.withText
//import org.hamcrest.Matchers.allOf
//import org.mockito.ArgumentMatchers.matches
//import piedel.piotr.thesis.R

//object ErrorTestUtil {
//
//    fun checkErrorViewsDisplay() {
//        onView(allOf<View>(withText(R.string.error_title), isDisplayed()))
//                .check(matches(isDisplayed()))
//        onView(allOf<View>(withText(R.string.error_message), isDisplayed()))
//                .check(matches(isDisplayed()))
//    }
//
//    fun checkClickingReloadShowsContentWithText(expectedText: String) {
//        onView(allOf<View>(withText(R.string.error_reload), isDisplayed()))
//                .perform(click())
//        onView(withText(expectedText))
//                .check(matches(isDisplayed()))
//    }
//
//}
