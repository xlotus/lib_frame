package com.xlotus.lib.imageloader;

import android.content.Context;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xlotus.lib.core.io.FileUtils;
import com.xlotus.lib.core.lang.ObjectStore;
import com.xlotus.lib.core.utils.ui.ViewUtils;

import java.util.concurrent.ExecutionException;

public class GlideUtils {
    private static final String GLIDE_CACHE = "glide_cache";
    private static String sCachePath = "";

    public static String getCachePath(Context context) {
        if (!TextUtils.isEmpty(sCachePath))
            return sCachePath;
        sCachePath = FileUtils.getCacheDirectory(context, GLIDE_CACHE);
        return sCachePath;
    }

    public static long getCacheSize(Context context) {
        return FileUtils.getFolderSize(getCachePath(context));
    }

    public static RequestManager getRequestManager(Context context) {
        if (ViewUtils.activityIsDead(context))
            return Glide.with(context.getApplicationContext());
        return Glide.with(context);
    }

    public static RequestManager getRequestManager(Fragment fragment) {
        if (fragment.getContext() == null)
            return Glide.with(ObjectStore.getContext());
        if (ViewUtils.activityIsDead(fragment.getContext()))
            return Glide.with(fragment.getContext().getApplicationContext());
        return Glide.with(fragment);
    }

    public static String getAbsolutePath(Context context, String url, int width, int height) throws ExecutionException, InterruptedException {

        return Glide.with(context).load(url).downloadOnly(width, height).get().getAbsolutePath();
    }
}
