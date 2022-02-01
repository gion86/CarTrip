package com.cartrip;

import static android.content.Context.MODE_PRIVATE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cartrip.KMCountFragment.SHARED_PREF_FILE;
import static com.cartrip.PreferenceConstants.PREF_KEY_END_KM;
import static com.cartrip.PreferenceConstants.PREF_KEY_START_KM;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@MediumTest
@RunWith(MockitoJUnitRunner.class)
public class KMCountFragmentIT {

    private static final int START_KM_COUNT_TEST_VALUE = 10;
    private static final int END_KM_COUNT_TEST_VALUE = 90;

    private SharedPreferences sharedPreferences;

    private int startKMCount;
    private int endKMCount;
    private boolean clearPref;

    @Before
    public void setUp() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        sharedPreferences = appContext.getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);

        if (sharedPreferences.contains(PREF_KEY_START_KM)) {
            clearPref = false;
            startKMCount = sharedPreferences.getInt(PREF_KEY_START_KM, 0);
        } else {
            clearPref = true;
            startKMCount = START_KM_COUNT_TEST_VALUE;
            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            preferencesEditor.putInt(PREF_KEY_START_KM, startKMCount);
            preferencesEditor.apply();
        }

        if (sharedPreferences.contains(PREF_KEY_END_KM)) {
            clearPref = false;
            endKMCount = sharedPreferences.getInt(PREF_KEY_END_KM, 0);
        } else {
            clearPref = true;
            endKMCount = END_KM_COUNT_TEST_VALUE;
            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            preferencesEditor.putInt(PREF_KEY_END_KM, endKMCount);
            preferencesEditor.apply();
        }

        Log.d("startKMCount = ", String.valueOf(startKMCount));
        Log.d("endKMCount = ", String.valueOf(endKMCount));
        Log.d("clearPref = ", String.valueOf(clearPref));
    }

    @After
    public void tearDown() {
        if (clearPref) {
            sharedPreferences.edit().clear().apply();
            Log.d("clearPref = ", "CLEARED");
        }
    }

    @Test
    public void testFragmentCountersInitState() {
        FragmentScenario<KMCountFragment> scenario = FragmentScenario.launchInContainer(KMCountFragment.class);
        scenario.moveToState(Lifecycle.State.RESUMED);

        onView(withId(R.id.startKMCount)).check(matches(withText(String.valueOf(startKMCount))));
        onView(withId(R.id.endKMCount)).check(matches(withText(String.valueOf(endKMCount))));
    }
}