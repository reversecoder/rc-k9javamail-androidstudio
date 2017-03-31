package com.reversecoder.javamail.androidstudio.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.reversecoder.javamail.androidstudio.R;
import com.reversecoder.javamail.androidstudio.k9.Account;
import com.reversecoder.javamail.androidstudio.k9.Preferences;
import com.reversecoder.javamail.androidstudio.k9.activity.K9Activity;
import com.reversecoder.javamail.androidstudio.k9.activity.MessageCompose;

import static com.reversecoder.javamail.androidstudio.activity.MessageComposeActivity.EXTRA_ACCOUNT;

/**
 * Created by rashed on 3/30/17.
 */

public class SendEmailActivity extends K9Activity {

    private Account mAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        String accountUuid = getIntent().getStringExtra(EXTRA_ACCOUNT);
        mAccount = Preferences.getPreferences(this).getAccount(accountUuid);

        ((Button) findViewById(R.id.btn_compose_email)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageComposeActivity.actionCompose(SendEmailActivity.this, mAccount);
                finish();
            }
        });
    }

    public static void actionSendMail(Context context, Account account) {
        Intent i = new Intent(context, SendEmailActivity.class);
        i.putExtra(EXTRA_ACCOUNT, account.getUuid());
        i.setAction(MessageCompose.ACTION_COMPOSE);
        context.startActivity(i);
    }

}
