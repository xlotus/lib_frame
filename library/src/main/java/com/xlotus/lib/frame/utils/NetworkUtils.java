package com.xlotus.lib.frame.utils;

import static android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Pair;

import com.xlotus.lib.core.Logger;
import com.xlotus.lib.core.lang.ObjectStore;
import com.xlotus.lib.core.net.NetUtils;
import com.xlotus.lib.core.os.AndroidHelper;

public class NetworkUtils {
    private static final String TAG  = "NetworkUtils";

    /**
     * 跳转Wifi设置
     * @param context
     */
    public static void gotoWifiSetting(Context context) {
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT > AndroidHelper.ANDROID_VERSION_CODE.GINGERBREAD_MR1)
                intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            else
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * 跳转4G设置
     * @param context
     */
    public static void goto4GSetting(Context context) {
        try {
            Intent intent = new Intent(ACTION_DATA_ROAMING_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            gotoWifiSetting(context);
        }
    }

    public static void gotoAuthNetworkSetting(Context context){
        gotoAuthNetworkSetting(context, null);
    }

    public static void gotoAuthNetworkSetting(Context context, NetworkStatusListener listener) {
        WifiManager wifiManager = (WifiManager) ObjectStore.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null) {
            boolean isEnable = wifiManager.isWifiEnabled();
            if (isEnable && Switch3G.isSimReady(context)) {
                if(android.os.Build.VERSION.SDK_INT <= AndroidHelper.ANDROID_VERSION_CODE.KITKAT) {
                    if(listener!=null){
                        listener.networkReadyOnLow();
                    }
                    Switch3G.setMobileDataEnabled(context, true);
                } else {
                    goto4GSetting(context);
                }
            }else {
                gotoWifiSetting(context);
            }
        } else {
            gotoWifiSetting(context);
        }
    }

    /**
     * 判断网络是否可用
     * @param context
     * @return 返回可用状态
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean result = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            result = isNetworkAvailable(connectivityManager);
        } catch (Exception e) {
            Logger.d(TAG,e.toString());
        }
        return result;
    }

    private static boolean isNetworkAvailable(ConnectivityManager connectivityManager) {
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }

    public static boolean isConnected(Context context) {
        Pair<Boolean, Boolean> connect = NetUtils.checkConnected(context);
        return connect.first || connect.second;
    }

    public static boolean isWifi(Context context) {
        Pair<Boolean, Boolean> connect = NetUtils.checkConnected(context);
        return !connect.first && connect.second;
    }

    public static boolean isMobile(Context context) {
        Pair<Boolean, Boolean> connect = NetUtils.checkConnected(context);
        return connect.first && !connect.second;
    }

    //网络状态监听器
    public interface NetworkStatusListener{
        //在低版本(<=KITKAT)Ready时回调
        public void networkReadyOnLow();
    }
}
