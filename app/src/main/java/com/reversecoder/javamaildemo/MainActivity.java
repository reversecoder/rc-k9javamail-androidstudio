package com.reversecoder.javamaildemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.reversecoder.javamail.JavaMail;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JavaMail javaMail=new JavaMail(MainActivity.this);
        javaMail.sendEmail();
    }
}
