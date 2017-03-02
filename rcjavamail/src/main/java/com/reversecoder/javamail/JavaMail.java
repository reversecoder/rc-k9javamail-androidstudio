package com.reversecoder.javamail;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by alam on 3/2/17.
 */

public class JavaMail {

    Context mContext;
    public JavaMail(Context context){
        mContext=context;
    }

    public void sendEmail(){
        Toast.makeText(mContext,"I am from java mail.",Toast.LENGTH_SHORT).show();
    }
}
