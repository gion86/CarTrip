package com.cartrip;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cartrip.databinding.KmCountFragmentBinding;
import com.cartrip.model.KMViewModel;

public class KMCountFragment extends Fragment {

    private static final String SHARED_PREF_FILE = "cartrip_sharedpref";
    private static final String PREF_KEY_START_KM = "pref_start_km";
    private static final String PREF_KEY_END_KM = "pref_end_km";

    private SharedPreferences mPreferences;

    private int startKMCount;
    private int endKMCount;

    private KmCountFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = getContext().getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);

        startKMCount = mPreferences.getInt(PREF_KEY_START_KM, 0);
        endKMCount = mPreferences.getInt(PREF_KEY_END_KM, 0);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = KmCountFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final KMViewModel viewModel = new ViewModelProvider(requireActivity()).get(KMViewModel.class);

        binding.startKMCount.setText(String.valueOf(startKMCount));
        binding.startKMCount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = charSequence.toString();
                startKMCount = 0;
                if (!value.isEmpty()) {
                    startKMCount = Integer.parseInt(value);
                }
                viewModel.updateStartKMCount(startKMCount);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.endKMCount.setText(String.valueOf(endKMCount));
        binding.endKMCount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String value = charSequence.toString();
                endKMCount = 0;
                if (!value.isEmpty()) {
                    endKMCount = Integer.parseInt(value);
                }
                viewModel.updateEndKMCount(endKMCount);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.startKMCount.setSelectAllOnFocus(true);
        binding.endKMCount.setSelectAllOnFocus(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putInt(PREF_KEY_START_KM, startKMCount);
        preferencesEditor.putInt(PREF_KEY_END_KM, endKMCount);
        preferencesEditor.apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}