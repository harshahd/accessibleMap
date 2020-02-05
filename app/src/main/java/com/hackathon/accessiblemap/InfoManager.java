package com.hackathon.accessiblemap;

import android.content.Context;
import android.preference.PreferenceManager;

public class InfoManager {
    private static final String otp_key = "otp";
    private Context context;

    public InfoManager(Context context2) {
        this.context = context2;
    }

    public void StoreOtp(int otp) {
        PreferenceManager.getDefaultSharedPreferences(this.context).edit().putInt(otp_key, otp).apply();
    }

    public int getStoredOtp() {
        return PreferenceManager.getDefaultSharedPreferences(this.context).getInt(otp_key, 0);
    }

    public void removeStoredOtp() {
        PreferenceManager.getDefaultSharedPreferences(this.context).edit().remove(otp_key).apply();
    }
}
