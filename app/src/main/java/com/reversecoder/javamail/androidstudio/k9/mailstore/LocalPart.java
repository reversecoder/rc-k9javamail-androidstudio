package com.reversecoder.javamail.androidstudio.k9.mailstore;


public interface LocalPart {
    String getAccountUuid();
    long getId();
    long getSize();
    LocalMessage getMessage();
}
