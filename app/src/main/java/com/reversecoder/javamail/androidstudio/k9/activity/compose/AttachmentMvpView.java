package com.reversecoder.javamail.androidstudio.k9.activity.compose;

import com.reversecoder.javamail.androidstudio.k9.activity.misc.Attachment;

public interface AttachmentMvpView {
        public void showWaitingForAttachmentDialog(WaitingAction waitingAction);
        public void dismissWaitingForAttachmentDialog();
        public void showPickAttachmentDialog(int requestCode);

        public void addAttachmentView(Attachment attachment);
        public void removeAttachmentView(Attachment attachment);
        public void updateAttachmentView(Attachment attachment);

        // TODO these should not really be here :\
        public void performSendAfterChecks();
        public void performSaveAfterChecks();

        public void showMissingAttachmentsPartialMessageWarning();
    }