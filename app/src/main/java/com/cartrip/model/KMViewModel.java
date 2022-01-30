package com.cartrip.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Simple model for two km counters: start and end of trip.
 */
public class KMViewModel extends ViewModel {

    private final MutableLiveData<Integer> startKMCount = new MutableLiveData<>();
    private final MutableLiveData<Integer> endKMCount = new MutableLiveData<>();

    public MutableLiveData<Integer> getStartKMCount() {
        return startKMCount;
    }

    public MutableLiveData<Integer> getEndKMCount() {
        return endKMCount;
    }

    public void updateStartKMCount(int startKM) {
        startKMCount.setValue(startKM);
    }

    public void updateEndKMCount(int endKM) {
        endKMCount.setValue(endKM);
    }
}