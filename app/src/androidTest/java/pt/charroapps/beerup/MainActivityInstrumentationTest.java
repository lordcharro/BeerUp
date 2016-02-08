package pt.charroapps.beerup;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.os.AsyncTask;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit.RestAdapter;
import retrofit.android.MainThreadExecutor;

/**
 * Created by ivolopes on 08/02/16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentationTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void ScrollTo(){
        onView(withId(R.id.rv))
                .perform(RecyclerViewActions.scrollToPosition(55))
                .check(matches(isDisplayed()));

    }

    @Test
    public void Retrofit(){
        new RestAdapter.Builder()
                .setEndpoint("https://api.brewerydb.com/v2")
                .setExecutors(AsyncTask.THREAD_POOL_EXECUTOR,
                        new MainThreadExecutor())
                .build();
    }



}
