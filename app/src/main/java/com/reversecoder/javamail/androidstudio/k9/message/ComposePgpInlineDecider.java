package com.reversecoder.javamail.androidstudio.k9.message;


import java.util.List;

import com.reversecoder.javamail.androidstudio.k9.crypto.MessageDecryptVerifier;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mail.Part;


public class ComposePgpInlineDecider {
    public boolean shouldReplyInline(Message localMessage) {
        // TODO more criteria for this? maybe check the User-Agent header?
        return messageHasPgpInlineParts(localMessage);
    }

    private boolean messageHasPgpInlineParts(Message localMessage) {
        List<Part> inlineParts = MessageDecryptVerifier.findPgpInlineParts(localMessage);
        return !inlineParts.isEmpty();
    }
}
