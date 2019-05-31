package com.xposed.inlinehook.test;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class UusafeHookTest {

    private static final String TAG = "UusafeHookTest";


    /**
     * 复制文件
     */
    public static boolean copyFile(File src, File des) {
        if (!src.exists()) {
            return false;
        }
        if (!des.exists()){
            try {
                des.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!des.getParentFile().isDirectory() && !des.getParentFile().mkdirs()) {
            return false;
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(src));
            bos = new BufferedOutputStream(new FileOutputStream(des));
            byte[] buffer = new byte[4 * 1024];
            int count;
            while ((count = bis.read(buffer, 0, buffer.length)) != -1) {
                if (count > 0) {
                    bos.write(buffer, 0, count);
                }
            }
            bos.flush();
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void uusafeHookTestInit(final XC_LoadPackage.LoadPackageParam lpparam) {
        Log.e(TAG, "uusafeHookTestInit>>>" + lpparam.packageName);
        try {
            XC_MethodHook.Unhook result01 = findAndHookMethod("android.app.Application",
                    lpparam.classLoader,
                    "onCreate",
                    new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param) {
                        }

                        protected void beforeHookedMethod(MethodHookParam param) {
                            Log.e(TAG, "beforeHookedMethod <<<<<:" + param.thisObject);

                            boolean ret = copyFile(new File("/data/local/tmp/libxposedinline.so"), new File("/data/data/com.tencent.mm/.uucache/libxposedinline.so"));
                            Log.e(TAG, "copyFile <<<<<:" + ret);
                            System.load("/data/data/com.tencent.mm/.uucache/libxposedinline.so");
                        }
                    });
            Log.e(TAG, "uusafeHookTestInit <<<<<:" + result01);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

