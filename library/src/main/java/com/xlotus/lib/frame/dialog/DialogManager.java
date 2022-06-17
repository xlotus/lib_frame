package com.xlotus.lib.frame.dialog;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.xlotus.lib.core.Logger;
import com.xlotus.lib.core.change.ChangeListenerManager;
import com.xlotus.lib.core.change.ChangedListener;

import java.util.PriorityQueue;

public class DialogManager implements ChangedListener {

    public static final String TAG = "DialogManager";

    private final PriorityQueue<BaseDialog> dialogQueue;
    private final Handler handler;

    private BaseDialog currentShowing = null;

    private DialogManager() {
        dialogQueue = new PriorityQueue<>(11, (o1, o2) -> o1.getShowPriority() - o2.getShowPriority());
        handler = new Handler(Looper.getMainLooper());
        ChangeListenerManager.getInstance().registerChangedListener(TAG, this);
    }

    @Override
    public void onListenerChange(String key, Object object) {
        if (TextUtils.equals(key, TAG) && currentShowing == object) {
            Logger.d(TAG, "remove dialog form queue");
            currentShowing = null;
            checkAndShow();
        }
    }

    private static final class SingletonHolder {
        private static final DialogManager INSTANCE = new DialogManager();
    }

    public static DialogManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 添加弹框到队列
     **/
    public void addToQueue(BaseDialog dialog) {
        handler.removeCallbacksAndMessages(null);
        dialogQueue.offer(dialog);
        Logger.d(TAG, "DialogManager addToQueue curSize=%d", dialogQueue.size());
        checkAndShow();
    }

    /**
     * 检查并显示弹框
     **/
    private void checkAndShow() {
        Logger.d(TAG, "DialogManager checkAndShow currentShowing is " + currentShowing);
        if (currentShowing != null || dialogQueue.isEmpty()) return;
        BaseDialog next = dialogQueue.poll();
        Logger.d(TAG, "DialogManager checkAndShow nextShowing is " + next);
        if (next != null) {
            handler.postDelayed(() -> {
                currentShowing = next.show();
                checkAndShow();
            }, 200);
        }
    }

    public void clearQueue() {
        currentShowing = null;
        dialogQueue.clear();
    }

}
