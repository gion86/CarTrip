package com.cartrip.preferences;

import static com.cartrip.PreferenceConstants.PREF_KEY_SENDER_PWD;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.cartrip.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setPreferenceDataStore(EncryptedPreferenceDataStore.getInstance(getContext()));
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EditTextPreference passwordEdit = getPreferenceManager().findPreference(PREF_KEY_SENDER_PWD);
        passwordEdit.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
        passwordEdit.setSummaryProvider(preference -> "");
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}