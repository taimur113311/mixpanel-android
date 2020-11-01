package com.taimurtanveer.samplemixpanelintegration;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String mixpanel_token = "Your Mixpanel Token";
    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mixpanel = MixpanelAPI.getInstance(this, mixpanel_token);

        customEvents();
        setDistanctID();
        addUserProperties();

    }

    private void customEvents() {
        //Custom event with only one property
        EventLogger.setMixpanelLogs(this, "Customer Info", "Name", "Taimur");

        //Custom event with multiple properties.
        EventLogger.setMixpanelListLogs(this, "Customer Info", new HashMap<String, String>() {{
            put("Name", "Taimur");
            put("Age", "25");
            put("AndroidDeveloper", "true");
        }});
    }

    private void setDistanctID() {
        if (mixpanel != null) {
            mixpanel.identify("12345");
            mixpanel.getPeople().identify("12345");
        }
    }

    private void addUserProperties() {
        MixpanelAPI.People userProperties = null;
        if (mixpanel != null) {
            userProperties = mixpanel.getPeople();
        }
        if (userProperties != null) {
            userProperties.set("Last name", "abx");
            userProperties.set("location", "pakistan");
            userProperties.set("Age", 25);
        }
    }

    @Override
    protected void onDestroy() {
        if (mixpanel != null) {
            mixpanel.flush();
        }
        super.onDestroy();
    }
}