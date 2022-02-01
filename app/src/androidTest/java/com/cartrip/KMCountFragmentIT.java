package com.cartrip;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.cartrip.KMCountFragment.PREF_KEY_START_KM;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import androidx.test.filters.MediumTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

@MediumTest
@RunWith(MockitoJUnitRunner.class)
public class KMCountFragmentIT {

//    @Rule
//    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    Context context;

    @Mock
    SharedPreferences sharedPrefs;

//    @Mock
//    Fragment fragment;

//    @Mock
//    SharedPreferences.Editor mockEditor;

//    private AutoCloseable closeable;

    @Before
    public void setUp() {
//        closeable = MockitoAnnotations.openMocks(this);
//        fragment = mock(Fragment.class);
        sharedPrefs = mock(SharedPreferences.class);
        context = mock(Context.class);

//        when(fragment.getContext()).thenReturn(context);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(sharedPrefs);
    }

    @After
    public void tearDown() throws Exception {
//        closeable.close();
    }

    @Test
    public void testFragmentCountersInitStata() {
//        when(sharedPrefs.getInt(eq(PREF_KEY_START_KM), anyInt())).thenReturn(20);
        when(sharedPrefs.getInt(anyString(), anyInt())).thenReturn(20);

        FragmentScenario<KMCountFragment> scenario = FragmentScenario.launchInContainer(KMCountFragment.class);
        scenario.moveToState(Lifecycle.State.RESUMED);

        onView(withId(R.id.startKMCount)).check(matches(withText("20")));
    }
}