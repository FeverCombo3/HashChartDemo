package com.yjq.hashchart;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;


public class UIUtils {


    /**
     * 得到上下文
     *
     * @return
     */
    public static Context getContext() {
        return MyApp.getContext();
    }

    /**
     * 得到resources对象
     *
     * @return
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * 得到string.xml中的字符串
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    /**
     * 得到string.xml中的字符串，带点位符
     *
     * @return
     */
    public static String getString(int id, Object... formatArgs) {
        return getResource().getString(id, formatArgs);
    }

    /**
     * 得到string.xml中和字符串数组
     *
     * @param resId
     * @return
     */
    public static String[] getStringArr(int resId) {
        return getResource().getStringArray(resId);
    }

    /**
     * 得到colors.xml中的颜色
     *
     * @param colorId
     * @return
     */
    public static int getColor(int colorId) {
        return getResource().getColor(colorId);
    }

    /**
     * 得到应用程序的包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

//    /**
//     * 得到主线程Handler
//     *
//     * @return
//     */
//    public static Handler getMainThreadHandler() {
//        return MyApp.getMainHandler();
//    }
//
//    /**
//     * 得到主线程id
//     *
//     * @return
//     */
//    public static long getMainThreadId() {
//        return MyApp.getMainThreadId();
//    }

//    /**
//     * 安全的执行一个任务
//     *
//     * @param task
//     */
//    public static void postTaskSafely(Runnable task) {
//        int curThreadId = android.os.Process.myTid();
//        // 如果当前线程是主线程
//        if (curThreadId == getMainThreadId()) {
//            task.run();
//        } else {
//            // 如果当前线程不是主线程
//            getMainThreadHandler().post(task);
//        }
//    }

//    /**
//     * 延迟执行任务
//     *
//     * @param task
//     * @param delayMillis
//     */
//    public static void postTaskDelay(Runnable task, int delayMillis) {
//        getMainThreadHandler().postDelayed(task, delayMillis);
//    }
//
//    /**
//     * 移除任务
//     */
//    public static void removeTask(Runnable task) {
//        getMainThreadHandler().removeCallbacks(task);
//    }

    /**
     * dip-->px
     */
    public static int dip2Px(int dip) {
        // px/dip = density;
        // density = dpi/160
        // 320*480 density = 1 1px = 1dp
        // 1280*720 density = 2 2px = 1dp

        float density = getResource().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f);
    }

    /**
     * px-->dip
     */
    public static int px2dip(int px) {

        float density = getResource().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    /**
     * sp-->px
     */
    public static int sp2px(int sp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResource().getDisplayMetrics()) + 0.5f);
    }
}