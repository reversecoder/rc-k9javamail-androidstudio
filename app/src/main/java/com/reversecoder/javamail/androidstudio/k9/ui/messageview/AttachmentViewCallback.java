package com.reversecoder.javamail.androidstudio.k9.ui.messageview;


import com.reversecoder.javamail.androidstudio.k9.mailstore.AttachmentViewInfo;


interface AttachmentViewCallback {
    void onViewAttachment(AttachmentViewInfo attachment);
    void onSaveAttachment(AttachmentViewInfo attachment);
    void onSaveAttachmentToUserProvidedDirectory(AttachmentViewInfo attachment);
}
