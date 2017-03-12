package com.reversecoder.javamail.androidstudio.k9.mailstore;

import com.fsck.k9.mail.Message;

public interface MessageRemovalListener {
    public void messageRemoved(Message message);
}
