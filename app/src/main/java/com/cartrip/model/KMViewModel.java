package com.cartrip.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

/**
 * Simple model for two km counters: start and end of trip.
 */
public class KMViewModel extends ViewModel {

    private final MutableLiveData<Integer> startKMCount = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> endKMCount = new MutableLiveData<Integer>();

    public MutableLiveData<Integer> getStartKMCount() {
        return startKMCount;
    }

    public MutableLiveData<Integer> getEndKMCount() {
        return endKMCount;
    }

    public void updateStartKMCount(int startKMCount) {
        this.startKMCount.setValue(startKMCount);
    }

    public void updateEndKMCount(int endKMCount) {
        this.endKMCount.setValue(endKMCount);
    }
}