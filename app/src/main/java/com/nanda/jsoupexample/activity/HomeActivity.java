package com.nanda.jsoupexample.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.nanda.jsoupexample.R;
import com.nanda.jsoupexample.helper.UpdateChecker;

public class HomeActivity extends AppCompatActivity {

    private String playStoreAppUrl;
    private TextView tvCurrentVersionValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvCurrentVersionValue = findViewById(R.id.tv_version_value);

        playStoreAppUrl = "YOUR_PLAYSTORE_APP_URL HERE...";

        new UpdateChecker(this, playStoreAppUrl, new UpdateChecker.VersionUpdateListner() {
            @Override
            public void onVersionChecked(String result, boolean isNewVersionAvailable) {

                if (isNewVersionAvailable) {
                    tvCurrentVersionValue.setText("Need to update the app");
                } else {
                    tvCurrentVersionValue.setText("The app is up to date...");
                }
            }
        }).execute();

    }

}
