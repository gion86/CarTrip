package com.cartrip;

import static com.cartrip.PreferenceConstants.PREF_KEY_RECIPIENT_MAIL;
import static com.cartrip.PreferenceConstants.PREF_KEY_SENDER_MAIL;
import static com.cartrip.PreferenceConstants.PREF_KEY_SENDER_PWD;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cartrip.databinding.ActivityMainBinding;
import com.cartrip.mail.GmailSender;
import com.cartrip.model.KMViewModel;
import com.cartrip.preferences.EncryptedPreferenceDataStore;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private String senderMail;
    private String senderPassword;
    private String recipientMail;

    private int startKMCount;
    private int endKMCount;

    private String getMailBody() {
        final String NL = System.lineSeparator();

        String startKMText = getString(R.string.mail_body_km_start, startKMCount);
        String endKMText = getString(R.string.mail_body_km_end, endKMCount);
        String diffKMText = getString(R.string.mail_body_km_diff, endKMCount - startKMCount);

        return startKMText + NL + endKMText + NL + diffKMText;
    }

    private boolean sendMail() {
        String KMText = getString(R.string.mail_subject_km);

        GmailSender gmailSender = new GmailSender(senderMail, senderPassword);
        return gmailSender.sendMail(KMText, getMailBody(), senderMail, recipientMail);
    }

    private boolean isKmCountOK() {
        return startKMCount > 0 && endKMCount > 0 && (endKMCount - startKMCount) > 0;
    }

    private boolean isMailSettingOK() {
        return !senderMail.isEmpty() && !senderPassword.isEmpty() && !recipientMail.isEmpty();
    }

    private boolean isSendButtonEnabled() {
        return isKmCountOK() && isMailSettingOK();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        final KMViewModel viewModel = new ViewModelProvider(this).get(KMViewModel.class);
        viewModel.getStartKMCount().observe(this, startKM -> {
            startKMCount = startKM;
            binding.fab.setEnabled(isSendButtonEnabled());
        });

        viewModel.getEndKMCount().observe(this, endKM -> {
            endKMCount = endKM;
            binding.fab.setEnabled(isSendButtonEnabled());
        });

        binding.fab.setOnClickListener(view -> {
            // Create an executor that executes tasks in a background thread.
            ScheduledExecutorService backgroundExecutor = Executors.newSingleThreadScheduledExecutor();

            // Execute a task in the background thread.
            backgroundExecutor.execute(() -> {
                if (sendMail()) {
                    Snackbar.make(view, "Mail sent!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.i("Email sending", "sent");
                } else {
                    Snackbar.make(view, "Cannot send mail!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.i("Email sending", "cannot send mail");
                }
            });
        });

        EncryptedPreferenceDataStore encryptedPrefs = EncryptedPreferenceDataStore.getInstance(getApplicationContext());
        senderMail = encryptedPrefs.getString(PREF_KEY_SENDER_MAIL, "");
        senderPassword = encryptedPrefs.getString(PREF_KEY_SENDER_PWD, "");
        recipientMail = encryptedPrefs.getString(PREF_KEY_RECIPIENT_MAIL, "");

        encryptedPrefs.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
                    switch (key) {
                        case PREF_KEY_SENDER_MAIL:
                            senderMail = encryptedPrefs.getString(PREF_KEY_SENDER_MAIL, "");
                            break;
                        case PREF_KEY_SENDER_PWD:
                            senderPassword = encryptedPrefs.getString(PREF_KEY_SENDER_PWD, "");
                            break;
                        case PREF_KEY_RECIPIENT_MAIL:
                            recipientMail = encryptedPrefs.getString(PREF_KEY_RECIPIENT_MAIL, "");
                            break;
                    }
                    binding.fab.setEnabled(isSendButtonEnabled());
                }
        );

        binding.fab.setEnabled(isSendButtonEnabled());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.open_settings_fragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}