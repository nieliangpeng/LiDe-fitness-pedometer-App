package cn.bluemobi.dylan.step.step.utils;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

/**
 * Created by 夏天 on 2018/8/9.
 */

//计时器，引用xfmax写的算法
public abstract class CountDownTimer {
    private final long mMillisInFuture;
    private final long mCountdownInterval;
    private long mStopTimeInFuture;
    private boolean mCancelled = false;
    public CountDownTimer(long millisInFuture, long countDownInterval) {
        mMillisInFuture = millisInFuture;
        mCountdownInterval = countDownInterval;
    }
    public final void cancel() {
        mHandler.removeMessages(MSG);
        mCancelled = true;
    }
    public synchronized final CountDownTimer start() {
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture = SystemClock.elapsedRealtime() + mMillisInFuture;
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        mCancelled = false;
        return this;
    }
    public abstract void onTick(long millisUntilFinished);
    public abstract void onFinish();
    private static final int MSG = 1;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            synchronized (CountDownTimer.this) {
                final long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();
                if (millisLeft <= 0) {
                    onFinish();
                } else if (millisLeft < mCountdownInterval) {
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisLeft);
                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();
                    while (delay < 0) delay += mCountdownInterval;
                    if (!mCancelled) {
                        sendMessageDelayed(obtainMessage(MSG), delay);
                    }
                }
            }
        }
    };
}
