package com.reversecoder.javamail.androidstudio.application;

import android.app.Application;
import android.content.Context;

import com.fsck.k9.mail.ssl.LocalKeyStore;

/**
 * Created by rashed on 3/30/17.
 */

public class JavaMailApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        LocalKeyStore.setKeyStoreLocation(getDir("KeyStore", MODE_PRIVATE).toString());
    }

    public static Context getGlobalContext() {
        return mContext;
    }

}