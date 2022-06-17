package com.xlotus.lib.frame.viewtracker;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;

public class VisibilityUtil {

    public static boolean isVisible(@NonNull View rootView, View view,
                                    int minPercentageViewed, float minAlphaViewed) {
        // ListView & GridView both call detachFromParent() for views that can be recycled for
        // new data. This is one of the rare instances where a view will have a null parent for
        // an extended period of time and will not be the main window.
        // view.getGlobalVisibleRect() doesn't check that case, so if the view has visibility
        // of View.VISIBLE but it's group has no parent it is likely in the recycle bin of a
        // ListView / GridView and not on screen.
        if (rootView == null || view == null
                || (view.getVisibility() != View.VISIBLE) || rootView.getParent() == null) {
            return false;
        }
        if (view.getAlpha() < minAlphaViewed) {
            return false;
        }
        Rect visibleRect = new Rect();
        if (!view.getGlobalVisibleRect(visibleRect)) {
            return false;
        }
        long visibleViewArea = visibleRect.height() * visibleRect.width();
        long totalViewArea = view.getHeight() * view.getWidth();
        // 获取到的任何区域为0 返回false
        if (visibleViewArea == 0 || totalViewArea == 0) {
            return false;
        }
        return 100 * visibleViewArea >= minPercentageViewed * totalViewArea;
    }
}
