package com.reversecoder.javamail.androidstudio.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.reversecoder.javamail.androidstudio.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new sendEmail().execute();
    }

    private class sendEmail extends AsyncTask<String,Integer, String> {

        @Override
        protected String doInBackground(String... params) {
//            Email email = new Email();
//
////            email.setFromAddress("Rashed", "rashedul.alam@bjitgroup.com");
////            email.addRecipient("Rashed", "rashedul.alam@bjitgroup.com", Message.RecipientType.TO);
////            email.setSubject("My Bakery is finally open!");
////            email.setText("Mom, Dad. We did the opening ceremony of our bakery!!!");
////
////            new Mailer(new ServerConfig("bd1.bjitgroup.com", 25, "rashedul.alam", "dicosta011"), TransportStrategy.SMTP_TLS).sendMail(email);
//
//
//
//
//            email.setFromAddress("Rashed", "rashed.droid@gmail.com");
//            email.addRecipient("Rashed", "rashed.droid@gmail.com", Message.RecipientType.TO);
//            email.setSubject("My Bakery is finally open!");
//            email.setText("Mom, Dad. We did the opening ceremony of our bakery!!!");
//
//            new Mailer(new ServerConfig("smtp.gmail.com",465, "rashed.droid", "dimariodicosta")).sendMail(email);


            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Integer... values) {}
    }
}
