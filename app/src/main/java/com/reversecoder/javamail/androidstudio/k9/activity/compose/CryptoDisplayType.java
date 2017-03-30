package com.reversecoder.javamail.androidstudio.k9.activity.compose;

/**
 * Created by rashed on 3/30/17.
 */

public class CryptoDisplayType {

    public static final int VIEW_INDEX_HIDDEN = -1;
    public static final int VIEW_INDEX_CRYPTO_STATUS_DISABLED = 0;
    public static final int VIEW_INDEX_CRYPTO_STATUS_ERROR = 1;
    public static final int VIEW_INDEX_CRYPTO_STATUS_NO_RECIPIENTS = 2;
    public static final int VIEW_INDEX_CRYPTO_STATUS_ERROR_NO_KEY = 3;
    public static final int VIEW_INDEX_CRYPTO_STATUS_DISABLED_NO_KEY = 4;
    public static final int VIEW_INDEX_CRYPTO_STATUS_UNTRUSTED = 5;
    public static final int VIEW_INDEX_CRYPTO_STATUS_TRUSTED = 6;
    public static final int VIEW_INDEX_CRYPTO_STATUS_SIGN_ONLY = 0;

    public static final int VIEW_INDEX_CRYPTO_SPECIAL_PGP_INLINE = 0;
    public static final int VIEW_INDEX_CRYPTO_SPECIAL_SIGN_ONLY = 1;
    public static final int VIEW_INDEX_CRYPTO_SPECIAL_SIGN_ONLY_PGP_INLINE = 2;

    public enum CryptoStatusDisplayType {
        UNCONFIGURED(VIEW_INDEX_HIDDEN),
        UNINITIALIZED(VIEW_INDEX_HIDDEN),
        DISABLED(VIEW_INDEX_CRYPTO_STATUS_DISABLED),
        SIGN_ONLY(VIEW_INDEX_CRYPTO_STATUS_SIGN_ONLY),
        OPPORTUNISTIC_EMPTY(VIEW_INDEX_CRYPTO_STATUS_NO_RECIPIENTS),
        OPPORTUNISTIC_NOKEY(VIEW_INDEX_CRYPTO_STATUS_DISABLED_NO_KEY),
        OPPORTUNISTIC_UNTRUSTED(VIEW_INDEX_CRYPTO_STATUS_UNTRUSTED),
        OPPORTUNISTIC_TRUSTED(VIEW_INDEX_CRYPTO_STATUS_TRUSTED),
        PRIVATE_EMPTY(VIEW_INDEX_CRYPTO_STATUS_NO_RECIPIENTS),
        PRIVATE_NOKEY(VIEW_INDEX_CRYPTO_STATUS_ERROR_NO_KEY),
        PRIVATE_UNTRUSTED(VIEW_INDEX_CRYPTO_STATUS_UNTRUSTED),
        PRIVATE_TRUSTED(VIEW_INDEX_CRYPTO_STATUS_TRUSTED),
        ERROR(VIEW_INDEX_CRYPTO_STATUS_ERROR);


        public final int childToDisplay;

        CryptoStatusDisplayType(int childToDisplay) {
            this.childToDisplay = childToDisplay;
        }
    }

    public enum CryptoSpecialModeDisplayType {
        NONE(VIEW_INDEX_HIDDEN),
        PGP_INLINE(VIEW_INDEX_CRYPTO_SPECIAL_PGP_INLINE),
        SIGN_ONLY(VIEW_INDEX_CRYPTO_SPECIAL_SIGN_ONLY),
        SIGN_ONLY_PGP_INLINE(VIEW_INDEX_CRYPTO_SPECIAL_SIGN_ONLY_PGP_INLINE);


        public final int childToDisplay;

        CryptoSpecialModeDisplayType(int childToDisplay) {
            this.childToDisplay = childToDisplay;
        }
    }
}
