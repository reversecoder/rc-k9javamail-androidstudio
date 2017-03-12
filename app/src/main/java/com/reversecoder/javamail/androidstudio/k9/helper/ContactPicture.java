package com.reversecoder.javamail.androidstudio.k9.helper;

import android.content.Context;
import android.util.TypedValue;

import com.reversecoder.javamail.androidstudio.k9.K9;
import com.reversecoder.javamail.androidstudio.R;
import com.reversecoder.javamail.androidstudio.k9.activity.misc.ContactPictureLoader;

public class ContactPicture {

    public static ContactPictureLoader getContactPictureLoader(Context context) {
        final int defaultBgColor;
        if (!K9.isColorizeMissingContactPictures()) {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.contactPictureFallbackDefaultBackgroundColor,
                    outValue, true);
            defaultBgColor = outValue.data;
        } else {
            defaultBgColor = 0;
        }

        return new ContactPictureLoader(context, defaultBgColor);
    }
}
