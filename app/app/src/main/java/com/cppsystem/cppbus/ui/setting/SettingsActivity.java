package com.cppsystem.cppbus.ui.setting;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.cppsystem.cppbus.R;
import com.cppsystem.cppbus.ui.login.LoginActivity;
import com.cppsystem.cppbus.ui.scan.ScannerNfcActivity;
import com.cppsystem.cppbus.ui.sync.CppSynActivity;

import static com.cppsystem.cppbus.util.CppConstant.EXTRA_SYNC_USER_ONLY;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Paramètres");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference pref = findPreference("changeAccountInfo");
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Log.e("Selected: " , preference.getKey());
                    return false;
                }
            });
            Preference syncUser = findPreference("syncUser");
            syncUser.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), CppSynActivity.class).putExtra(EXTRA_SYNC_USER_ONLY,true));

                    return false;
                }
            });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        NfcManager manager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            // NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this, PendingIntent.getActivity(this, 0, new Intent(this, getClass()), 0), null, null);
            NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this, PendingIntent.getActivity(this, 0, new Intent(this, ScannerNfcActivity.class), 0), null, null);
            //Yes NFC available
        }

    }
}