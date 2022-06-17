package com.xlotus.lib.frame.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;

import com.xlotus.lib.core.Logger;
import com.xlotus.lib.core.lang.Reflector;

public final class Switch3G {
    private static final String TAG = "Switch3G";

    private Switch3G() {}

    public static boolean isSimReady(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
        } catch (IllegalStateException e) {
            Logger.d(TAG, "getSimState exception", e);
            return false;
        }
    }

    public static boolean isMobileDataEnabled(Context context) {
        if (!isSimReady(context))
            return false;

        ConnectivityManager cm = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Object cmService = Reflector.getFieldValue(cm, "mService");
            Logger.d(TAG, "getMobileDataEnabled() return=?");
            return (Boolean)Reflector.invokeMethod(cmService, "getMobileDataEnabled", null, null);
        } catch (Exception e) {
            Logger.d(TAG, "getMobileDataEnabled method failed from service!");
            try {
                // patch: if failed, invoke "getMobileDataEnabled" method from ConnectivityManager class directly
                // for AndroidL version
                return (Boolean)Reflector.invokeMethod(cm, "getMobileDataEnabled", null, null);
            } catch (Exception e1) {
                Logger.w(TAG, e1);
            }
            return false;
        }
    }

    public static boolean setMobileDataEnabled(Context context, boolean enabled) {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Object cmService = Reflector.getFieldValue(cm, "mService");
            if (cmService != null) {
                Reflector.invokeMethod(cmService, "setMobileDataEnabled", new Class[] { Boolean.TYPE }, new Object[] { enabled });
                Logger.d(TAG, "ConnectivityManager.mService.setMobileDataEnabled(" + enabled + ") return=true");
                result = true;
            }
        } catch (Exception e) {
            Logger.d(TAG, "ConnectivityManager.mService.setMobileDataEnabled(" + enabled + ") exception!");
        }

        if (!result) {
            try {
                // patch: if failed, invoke "setMobileDataEnabled" method from ConnectivityManager class directly
                Reflector.invokeMethod(cm, "setMobileDataEnabled", new Class[] { Boolean.TYPE }, new Object[] { enabled });
                Logger.d(TAG, "ConnectivityManager.setMobileDataEnabled(" + enabled + ") return=true");
                result = true;
            } catch (Exception e) {
                Logger.d(TAG, "ConnectivityManager.setMobileDataEnabled(" + enabled + ") exception!");
            }
        }

        return result;
    }
}
