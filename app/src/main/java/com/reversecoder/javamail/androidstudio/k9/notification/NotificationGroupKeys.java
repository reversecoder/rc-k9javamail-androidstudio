package com.reversecoder.javamail.androidstudio.k9.notification;


import com.reversecoder.javamail.androidstudio.k9.Account;


public class NotificationGroupKeys {
    private static final String NOTIFICATION_GROUP_KEY_PREFIX = "newMailNotifications-";
    
    
    public static String getGroupKey(Account account) {
        return NOTIFICATION_GROUP_KEY_PREFIX + account.getAccountNumber();
    }
}
