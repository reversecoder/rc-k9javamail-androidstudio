package com.reversecoder.javamail.androidstudio.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.reversecoder.javamail.androidstudio.R;
import com.reversecoder.javamail.androidstudio.k9.Account;
import com.reversecoder.javamail.androidstudio.k9.activity.K9Activity;

/**
 * Created by rashed on 3/30/17.
 */

public class SendEmailActivity extends K9Activity {

    private static final String EXTRA_ACCOUNT = "account";

    private static final String EXTRA_MAKE_DEFAULT = "makeDefault";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
    }

    public static void actionSendMail(Context context, Account account, boolean makeDefault) {
        Intent i = new Intent(context, SendEmailActivity.class);
        i.putExtra(EXTRA_ACCOUNT, account.getUuid());
        i.putExtra(EXTRA_MAKE_DEFAULT, makeDefault);
        context.startActivity(i);
    }
}
