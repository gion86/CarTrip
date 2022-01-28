package com.cartrip;

import org.junit.Test;

import static org.junit.Assert.*;

import androidx.fragment.app.Fragment;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void kmCountIsPositive() {
        FirstFragment f1 = new FirstFragment(22, 30);
        assertTrue(f1.getKMCount() > 0);
    }
}