package com.reversecoder.javamail.androidstudio.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.fsck.k9.mail.AuthType;
import com.fsck.k9.mail.ConnectionSecurity;
import com.fsck.k9.mail.ServerSettings;
import com.fsck.k9.mail.Transport;
import com.fsck.k9.mail.store.RemoteStore;
import com.reversecoder.javamail.androidstudio.R;
import com.reversecoder.javamail.androidstudio.k9.Account;
import com.reversecoder.javamail.androidstudio.k9.CheckDirection;
import com.reversecoder.javamail.androidstudio.k9.EmailAddressValidator;
import com.reversecoder.javamail.androidstudio.k9.K9;
import com.reversecoder.javamail.androidstudio.k9.Preferences;
import com.reversecoder.javamail.androidstudio.k9.account.AccountCreator;
import com.reversecoder.javamail.androidstudio.k9.activity.setup.AccountSetupNames;
import com.reversecoder.javamail.androidstudio.k9.helper.UrlEncodingHelper;
import com.reversecoder.javamail.androidstudio.k9.helper.Utility;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class AccountSetupBasicActivity extends AppCompatActivity {
    private final static String EXTRA_ACCOUNT = "com.fsck.k9.AccountSetupBasics.account";
    private final static String STATE_KEY_PROVIDER =
            "com.fsck.k9.AccountSetupBasics.provider";
    private final static String STATE_KEY_CHECKED_INCOMING =
            "com.fsck.k9.AccountSetupBasics.checkedIncoming";
    private final static int DIALOG_NOTE = 1;
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mNextButton;
    private Button mManualSetupButton;
    private Provider mProvider;
    private CheckBox mShowPasswordCheckBox;
    private Account mAccount;
    private EmailAddressValidator mEmailValidator = new EmailAddressValidator();
    private boolean mCheckedIncoming = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup_basic);

        initUI();
        initActions();
    }

    private void initActions() {
        mShowPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showPassword(isChecked);
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNext();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAccount != null) {
            outState.putString(EXTRA_ACCOUNT, mAccount.getUuid());
        }
        if (mProvider != null) {
            outState.putSerializable(STATE_KEY_PROVIDER, mProvider);
        }
        outState.putBoolean(STATE_KEY_CHECKED_INCOMING, mCheckedIncoming);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(EXTRA_ACCOUNT)) {
            String accountUuid = savedInstanceState.getString(EXTRA_ACCOUNT);
            mAccount = Preferences.getPreferences(this).getAccount(accountUuid);
        }

        if (savedInstanceState.containsKey(STATE_KEY_PROVIDER)) {
            mProvider = (Provider) savedInstanceState.getSerializable(STATE_KEY_PROVIDER);
        }

        mCheckedIncoming = savedInstanceState.getBoolean(STATE_KEY_CHECKED_INCOMING);

        updateViewVisibility();

        showPassword(mShowPasswordCheckBox.isChecked());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        /*
         * We wait until now to initialize the listeners because we didn't want
         * the OnCheckedChangeListener active while the
         * mClientCertificateCheckBox state was being restored because it could
         * trigger the pop-up of a ClientCertificateSpinner.chooseCertificate()
         * dialog.
         */
        initializeViewListeners();
        validateFields();
    }

    private void initializeViewListeners() {
        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateFields();
            }
        });
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateFields();
            }
        });

        mShowPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showPassword(isChecked);
            }
        });

    }

    private void validateFields() {
        String email = mEmailView.getText().toString();

        boolean valid = Utility.requiredFieldValid(mEmailView)
                && Utility.requiredFieldValid(mPasswordView)
                && mEmailValidator.isValidAddressOnly(email);

        mNextButton.setEnabled(valid);
        mManualSetupButton.setEnabled(valid);
        /*
         * Dim the next button's icon to 50% if the button is disabled.
         * TODO this can probably be done with a stateful drawable. Check into it.
         * android:state_enabled
         */
        Utility.setCompoundDrawablesAlpha(mNextButton, mNextButton.isEnabled() ? 255 : 128);
    }

    private void updateViewVisibility() {
        // show password fields, hide client certificate spinner
        mPasswordView.setVisibility(View.VISIBLE);
        mShowPasswordCheckBox.setVisibility(View.VISIBLE);
    }

    private void initUI() {
        mEmailView = (EditText) findViewById(R.id.account_email);
        mEmailView.setText("rashedul.alam@bjitgroup.com");
        mPasswordView = (EditText) findViewById(R.id.account_password);
        mPasswordView.setText("");
        mNextButton = (Button) findViewById(R.id.next);
        mManualSetupButton = (Button) findViewById(R.id.manual_setup);
        mManualSetupButton.setVisibility(View.GONE);
        mShowPasswordCheckBox = (CheckBox) findViewById(R.id.show_password);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (!mCheckedIncoming) {
                //We've successfully checked incoming.  Now check outgoing.
                mCheckedIncoming = true;
                AccountSetupCheckSettingsActivity.actionCheckSettings(this, mAccount, CheckDirection.OUTGOING);
            } else {
                //We've successfully checked outgoing as well.
                mAccount.setDescription(mAccount.getEmail());
                mAccount.save(Preferences.getPreferences(this));
                K9.setServicesEnabled(this);
                AccountSetupNames.actionSetNames(this, mAccount);
                finish();
            }
        }
    }

    @Override
    public Dialog onCreateDialog(int id) {
        if (id == DIALOG_NOTE) {
            if (mProvider != null && mProvider.note != null) {
                return new AlertDialog.Builder(this)
                        .setMessage(mProvider.note)
                        .setPositiveButton(
                                getString(R.string.okay_action),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finishAutoSetup();
                                    }
                                })
                        .setNegativeButton(
                                getString(R.string.cancel_action),
                                null)
                        .create();
            }
        }
        return null;
    }

    private void onNext() {
        mProvider = findProviderForDomain(getDomainFromEmailAddress(mEmailView.getText().toString()));
        if (mProvider == null) {
            /*
             * We don't have default settings for this account, start the manual
             * setup process.
             */
            onManualSetup();
            return;
        }

        if (mProvider.note != null) {
            showDialog(DIALOG_NOTE);
        } else {
            finishAutoSetup();
        }
    }

    private void finishAutoSetup() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String[] emailParts = splitEmail(email);
        String user = emailParts[0];
        String domain = emailParts[1];
        try {
            String userEnc = UrlEncodingHelper.encodeUtf8(user);
            String passwordEnc = UrlEncodingHelper.encodeUtf8(password);

            String incomingUsername = mProvider.incomingUsernameTemplate;
            incomingUsername = incomingUsername.replaceAll("\\$email", email);
            incomingUsername = incomingUsername.replaceAll("\\$user", userEnc);
            incomingUsername = incomingUsername.replaceAll("\\$domain", domain);

            URI incomingUriTemplate = mProvider.incomingUriTemplate;
            URI incomingUri = new URI(incomingUriTemplate.getScheme(), incomingUsername + ":" + passwordEnc,
                    incomingUriTemplate.getHost(), incomingUriTemplate.getPort(), null, null, null);

            String outgoingUsername = mProvider.outgoingUsernameTemplate;

            URI outgoingUriTemplate = mProvider.outgoingUriTemplate;


            URI outgoingUri;
            if (outgoingUsername != null) {
                outgoingUsername = outgoingUsername.replaceAll("\\$email", email);
                outgoingUsername = outgoingUsername.replaceAll("\\$user", userEnc);
                outgoingUsername = outgoingUsername.replaceAll("\\$domain", domain);
                outgoingUri = new URI(outgoingUriTemplate.getScheme(), outgoingUsername + ":"
                        + passwordEnc, outgoingUriTemplate.getHost(), outgoingUriTemplate.getPort(), null,
                        null, null);

            } else {
                outgoingUri = new URI(outgoingUriTemplate.getScheme(),
                        null, outgoingUriTemplate.getHost(), outgoingUriTemplate.getPort(), null,
                        null, null);


            }
            if (mAccount == null) {
                mAccount = Preferences.getPreferences(this).newAccount();
            }
            mAccount.setName(getOwnerName());
            mAccount.setEmail(email);
            mAccount.setStoreUri(incomingUri.toString());
            mAccount.setTransportUri(outgoingUri.toString());

            setupFolderNames(incomingUriTemplate.getHost().toLowerCase(Locale.US));

            ServerSettings incomingSettings = RemoteStore.decodeStoreUri(incomingUri.toString());
            mAccount.setDeletePolicy(AccountCreator.getDefaultDeletePolicy(incomingSettings.type));

            // Check incoming here.  Then check outgoing in onActivityResult()
            AccountSetupCheckSettingsActivity.actionCheckSettings(this, mAccount, CheckDirection.INCOMING);
        } catch (URISyntaxException use) {
            /*
             * If there is some problem with the URI we give up and go on to
             * manual setup.
             */
            onManualSetup();
        }
    }

    private void setupFolderNames(String domain) {
        mAccount.setDraftsFolderName(getString(R.string.special_mailbox_name_drafts));
        mAccount.setTrashFolderName(getString(R.string.special_mailbox_name_trash));
        mAccount.setSentFolderName(getString(R.string.special_mailbox_name_sent));
        mAccount.setArchiveFolderName(getString(R.string.special_mailbox_name_archive));

        // Yahoo! has a special folder for Spam, called "Bulk Mail".
        if (domain.endsWith(".yahoo.com")) {
            mAccount.setSpamFolderName("Bulk Mail");
        } else {
            mAccount.setSpamFolderName(getString(R.string.special_mailbox_name_spam));
        }
    }

    private void onManualSetup() {
        String email = mEmailView.getText().toString();
        String[] emailParts = splitEmail(email);
        String user = email;
        String domain = emailParts[1];

        String password = null;
        String clientCertificateAlias = null;
        AuthType authenticationType;
        authenticationType = AuthType.PLAIN;
        password = mPasswordView.getText().toString();

        if (mAccount == null) {
            mAccount = Preferences.getPreferences(this).newAccount();
        }
        mAccount.setName(getOwnerName());
        mAccount.setEmail(email);

        // set default uris
        // NOTE: they will be changed again in AccountSetupAccountType!
        ServerSettings storeServer = new ServerSettings(ServerSettings.Type.IMAP, "mail." + domain, -1,
                ConnectionSecurity.SSL_TLS_REQUIRED, authenticationType, user, password, clientCertificateAlias);
        ServerSettings transportServer = new ServerSettings(ServerSettings.Type.SMTP, "mail." + domain, -1,
                ConnectionSecurity.SSL_TLS_REQUIRED, authenticationType, user, password, clientCertificateAlias);
        String storeUri = RemoteStore.createStoreUri(storeServer);
        String transportUri = Transport.createTransportUri(transportServer);
        mAccount.setStoreUri(storeUri);
        mAccount.setTransportUri(transportUri);

        setupFolderNames(domain);

        AccountSetupAccountTypeActivity.actionSelectAccountType(this, mAccount, false);

        finish();
    }

    private String getOwnerName() {
        String name = null;
        try {
            name = getDefaultAccountName();
        } catch (Exception e) {
            Log.e("ERROR", "Could not get default account name");
        }

        if (name == null) {
            name = "";
        }
        return name;
    }

    private String getDefaultAccountName() {
        String name = null;
        Account account = Preferences.getPreferences(this).getDefaultAccount();
        if (account != null) {
            name = account.getName();
        }
        return name;
    }

    private void showPassword(boolean show) {
        int cursorPosition = mPasswordView.getSelectionStart();
        if (show) {
            mPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            mPasswordView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        mPasswordView.setSelection(cursorPosition);
    }

    public boolean requiredFieldValid(TextView view) {
        return view.getText() != null && view.getText().length() > 0;
    }

    public static String getDomainFromEmailAddress(String email) {
        int separatorIndex = email.lastIndexOf('@');
        if (separatorIndex == -1 || separatorIndex + 1 == email.length()) {
            return null;
        }

        return email.substring(separatorIndex + 1);
    }

    static class Provider implements Serializable {
        private static final long serialVersionUID = 8511656164616538989L;

        public String id;

        public String label;

        public String domain;

        public URI incomingUriTemplate;

        public String incomingUsernameTemplate;

        public URI outgoingUriTemplate;

        public String outgoingUsernameTemplate;

        public String note;
    }

    private Provider findProviderForDomain(String domain) {
        try {
            XmlResourceParser xml = getResources().getXml(R.xml.providers);
            int xmlEventType;
            Provider provider = null;
            while ((xmlEventType = xml.next()) != XmlResourceParser.END_DOCUMENT) {
                if (xmlEventType == XmlResourceParser.START_TAG
                        && "provider".equals(xml.getName())
                        && domain.equalsIgnoreCase(getXmlAttribute(xml, "domain"))) {
                    provider = new Provider();
                    provider.id = getXmlAttribute(xml, "id");
                    provider.label = getXmlAttribute(xml, "label");
                    provider.domain = getXmlAttribute(xml, "domain");
                    provider.note = getXmlAttribute(xml, "note");
                } else if (xmlEventType == XmlResourceParser.START_TAG
                        && "incoming".equals(xml.getName())
                        && provider != null) {
                    provider.incomingUriTemplate = new URI(getXmlAttribute(xml, "uri"));
                    provider.incomingUsernameTemplate = getXmlAttribute(xml, "username");
                } else if (xmlEventType == XmlResourceParser.START_TAG
                        && "outgoing".equals(xml.getName())
                        && provider != null) {
                    provider.outgoingUriTemplate = new URI(getXmlAttribute(xml, "uri"));
                    provider.outgoingUsernameTemplate = getXmlAttribute(xml, "username");
                } else if (xmlEventType == XmlResourceParser.END_TAG
                        && "provider".equals(xml.getName())
                        && provider != null) {
                    return provider;
                }
            }
        } catch (Exception e) {
            Log.e("ERROR", "Error while trying to load provider settings.");
        }
        return null;
    }


    /**
     * Attempts to get the given attribute as a String resource first, and if it fails
     * returns the attribute as a simple String value.
     *
     * @param xml
     * @param name
     * @return
     */
    private String getXmlAttribute(XmlResourceParser xml, String name) {
        int resId = xml.getAttributeResourceValue(null, name, 0);
        if (resId == 0) {
            return xml.getAttributeValue(null, name);
        } else {
            return getString(resId);
        }
    }

    private String[] splitEmail(String email) {
        String[] retParts = new String[2];
        String[] emailParts = email.split("@");
        retParts[0] = (emailParts.length > 0) ? emailParts[0] : "";
        retParts[1] = (emailParts.length > 1) ? emailParts[1] : "";
        return retParts;
    }
}
