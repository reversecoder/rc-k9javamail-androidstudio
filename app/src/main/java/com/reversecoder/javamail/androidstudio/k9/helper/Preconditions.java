package com.reversecoder.javamail.androidstudio.k9.helper;


public class Preconditions {
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }

        return reference;
    }
}
