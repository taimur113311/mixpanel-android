package com.taimurtanveer.samplemixpanelintegration;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EventLogger {
    private static MixpanelAPI mMixpanel;

    private static final String MIX_PANEL_LOG_KEY = "MIXPANEL_EVENTS";
    private static String mixpanel_token = "Your Mixpanel Token";

    private EventLogger() {
    }


    /*
    If you want to track one property please use this method
     */
    public static void setMixpanelLogs(Context context,
                                        String event_name, String propertyName, String propertyValue) {
        if (context == null) {
            return;
        }


        try {
            mMixpanel = MixpanelAPI.getInstance(context, mixpanel_token);
        } catch (Exception e) {

        }
        final JSONObject eventProperties = new JSONObject();
        try {
            if (!isNullOrEmpty(propertyValue)) {
                eventProperties.put(propertyName, propertyValue);
                mMixpanel.track(event_name, eventProperties);
                if (BuildConfig.DEBUG) {
                    Log.v(MIX_PANEL_LOG_KEY, event_name + " with properties " + eventProperties.toString());
                }
            } else {
                mMixpanel.track(event_name);

                if (BuildConfig.DEBUG) {
                    Log.v(MIX_PANEL_LOG_KEY, event_name);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    If you want to track event with multiple list of properties.
    Note: This method will only accept string properties and values.
     if you want to add some other data type please create another method or let me know
     */
    public synchronized static void setMixpanelListLogs(Context context,
                                                     String event_name, HashMap<String, String> keyValuePair) {
        if (context == null) {
            return;
        }

        try {
            mMixpanel = MixpanelAPI.getInstance(context, mixpanel_token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final JSONObject eventProperties = new JSONObject();

        if (keyValuePair != null) {
            for (Map.Entry<String, String> entry : keyValuePair.entrySet()) {
                try {
                    if (!isNullOrEmpty(entry.getValue())) {
                        String value = entry.getValue().substring(0, Math.min(entry.getValue().length(), 254));//Mixpanel only allow 254 character in property value
                        eventProperties.put(entry.getKey(), value);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (eventProperties.length() > 0) {
                mMixpanel.track(event_name, eventProperties);
                if (BuildConfig.DEBUG) {
                    Log.v(MIX_PANEL_LOG_KEY, event_name + " with properties " + eventProperties.toString());
                }
            } else {
                mMixpanel.track(event_name);
                if (BuildConfig.DEBUG) {
                    Log.v(MIX_PANEL_LOG_KEY, event_name);
                }
            }
        }


    }
    public static boolean isNullOrEmpty(String s) {
        if (s == null || s.trim().length() < 1) {
            return true;
        }
        return false;
    }
}
