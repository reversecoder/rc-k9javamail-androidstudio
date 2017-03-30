package com.reversecoder.javamail.androidstudio.k9;

import android.support.annotation.StringRes;

import com.reversecoder.javamail.androidstudio.R;

/**
 * Created by rashed on 3/30/17.
 */

public enum Action {
    COMPOSE(R.string.compose_title_compose),
    REPLY(R.string.compose_title_reply),
    REPLY_ALL(R.string.compose_title_reply_all),
    FORWARD(R.string.compose_title_forward),
    EDIT_DRAFT(R.string.compose_title_compose);

    private final int titleResource;

    Action(@StringRes int titleResource) {
        this.titleResource = titleResource;
    }

    @StringRes
    public int getTitleResource() {
        return titleResource;
    }
}