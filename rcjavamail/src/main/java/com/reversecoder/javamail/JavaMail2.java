package com.reversecoder.javamail;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by alam on 3/2/17.
 */

public class JavaMail2 {

    Context mContext;
    public JavaMail2(Context context){
        mContext=context;
    }

    public void sendEmail(){
        Toast.makeText(mContext,"I am from java mail.",Toast.LENGTH_SHORT).show();
    }
}
