/*

//Testing still in progress
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.eventwiz.AddEventDetailActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AddEventDetailActivityTest {

    @Before
    public void launchActivity() {
        // Launch the activity before each test
        ActivityScenario.launch(AddEventDetailActivity.class);
    }

    @Test
    public void testEventCreation() {
        // Enter event details and click on the "Next" button
        Espresso.onView(ViewMatchers.withId(R.id.etEventName)).perform(ViewActions.typeText("Sample Event"));
        Espresso.onView(ViewMatchers.withId(R.id.etEventDescription)).perform(ViewActions.typeText("Sample Event Description"));
        // Continue entering other details as needed...

        // Click on the "Next" button
        Espresso.onView(ViewMatchers.withId(R.id.btnNext)).perform(ViewActions.click());

        // You can add assertions here to verify the expected behavior after clicking the "Next" button
    }

    // Add more test methods for different scenarios as needed
}

*/

