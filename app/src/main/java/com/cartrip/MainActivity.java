package com.cartrip;

import android.content.Context;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
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
import com.cartrip.encryption.CipherWrapper;
import com.cartrip.mail.GmailSender;
import com.cartrip.model.KMViewModel;
import com.cartrip.preferences.EncryptedPreferenceDataStore;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.crypto.KeyGenerator;

public class MainActivity extends AppCompatActivity {

    private static final String AndroidKeyStore = "AndroidKeyStore";
    private static final String KEY_ALIAS = "CAR_TRIP_KEY_ALIAS";

    private KeyStore keyStore;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private int startKMCount;
    private int endKMCount;

    private void generateKey() throws GeneralSecurityException, IOException {
        keyStore = KeyStore.getInstance(AndroidKeyStore);
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, AndroidKeyStore);
            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setRandomizedEncryptionRequired(false)
                            .build());
            keyGenerator.generateKey();
        }
    }

    private java.security.Key getSecretKey(Context context) throws GeneralSecurityException {
        return keyStore.getKey(KEY_ALIAS, null);
    }

    private String getMailBody() {
        final String NL = System.lineSeparator();

        String startKMText = getString(R.string.mail_body_km_start, this.startKMCount);
        String endKMText = getString(R.string.mail_body_km_end, this.endKMCount);
        String diffKMText = getString(R.string.mail_body_km_diff, this.endKMCount - this.startKMCount);

        return startKMText + NL + endKMText + NL + diffKMText;
    }

    private boolean sendMail() {
        String KMText = getString(R.string.mail_subject_km);

        GmailSender gmailSender = new GmailSender("gionata.boccalini@gmail.com", ""); // FIXME password!!
        return gmailSender.sendMail(KMText, getMailBody(), "gionata.boccalini@gmail.com", "gionata.boccalini@marchesini.com");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            generateKey();
        } catch (GeneralSecurityException | IOException e) {
            Log.e("MainActivity", "Exception in key generation", e);
        }

        try {
            CipherWrapper cipherWrapper = new CipherWrapper();
            Key key = getSecretKey(getApplicationContext());
            Log.d("MainActivity KEY: ", key.toString());

            byte[] encData = cipherWrapper.encryptData(key, "test");

            Log.i("MainActivity data enc: ", new String(encData));
            Log.i("MainActivity data plain: ", cipherWrapper.decryptData(key, encData));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        final KMViewModel viewModel = new ViewModelProvider(this).get(KMViewModel.class);
        viewModel.getStartKMCount().observe(this, startKMCount -> {
            this.startKMCount = startKMCount;
            Log.i("startKMCount", String.valueOf(startKMCount));
        });
        viewModel.getEndKMCount().observe(this, endKMCount -> {
            this.endKMCount = endKMCount;
            Log.i("endKMCount", String.valueOf(endKMCount));
        });

        binding.fab.setOnClickListener(view -> {
            EncryptedPreferenceDataStore prefs = EncryptedPreferenceDataStore.getInstance(getApplicationContext());
            Log.i("enc_prefer",  prefs.getString("pref_sender_password", ""));

            // Create an executor that executes tasks in a background thread.
            ScheduledExecutorService backgroundExecutor = Executors.newSingleThreadScheduledExecutor();

            // Execute a task in the background thread.
            backgroundExecutor.execute(() -> {
                if (sendMail()) {
                    Snackbar.make(view, "Mail sent!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    Log.i("Email sending", "sent");
                } else {
                    Log.i("Email sending", "cannot send mail");
                }
            });
        });
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