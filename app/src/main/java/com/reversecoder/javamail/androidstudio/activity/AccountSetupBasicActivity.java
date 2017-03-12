package com.reversecoder.javamail.androidstudio.activity;

import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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
import com.reversecoder.javamail.androidstudio.k9.Preferences;
import com.reversecoder.javamail.androidstudio.k9.activity.setup.AccountSetupAccountType;
import com.reversecoder.javamail.androidstudio.k9.view.ClientCertificateSpinner;

import java.io.Serializable;
import java.net.URI;
import java.util.regex.Pattern;

public class AccountSetupBasicActivity extends AppCompatActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private CheckBox mClientCertificateCheckBox;
    private ClientCertificateSpinner mClientCertificateSpinner;
    private Button mNextButton;
    private Button mManualSetupButton;
    private Provider mProvider;
    private CheckBox mShowPasswordCheckBox;

    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup_basic);

        initUI();
        initActions();
    }

    private void initActions(){
        mShowPasswordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                showPassword(isChecked);
            }
        });
    }

    public boolean isValidAddressOnly(CharSequence text) {
        Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );
        return EMAIL_ADDRESS_PATTERN.matcher(text).matches();
    }

    private void initUI(){
        mEmailView = (EditText)findViewById(R.id.account_email);
        mPasswordView = (EditText)findViewById(R.id.account_password);
        mClientCertificateCheckBox = (CheckBox)findViewById(R.id.account_client_certificate);
        mClientCertificateSpinner = (ClientCertificateSpinner)findViewById(R.id.account_client_certificate_spinner);
        mNextButton = (Button)findViewById(R.id.next);
        mManualSetupButton = (Button)findViewById(R.id.manual_setup);
        mShowPasswordCheckBox = (CheckBox) findViewById(R.id.show_password);
    }

    private void onNext() {
//        if (mClientCertificateCheckBox.isChecked()) {
//
//            // Auto-setup doesn't support client certificates.
//            onManualSetup();
//            return;
//        }

//        String email = mEmailView.getText().toString();
//        String[] emailParts = splitEmail(email);
//        String domain = emailParts[1];
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
    private void onManualSetup() {
        String email = mEmailView.getText().toString();
        String[] emailParts = splitEmail(email);
        String user = email;
        String domain = emailParts[1];

        String password = null;
        String clientCertificateAlias = null;
        AuthType authenticationType;
//        if (mClientCertificateCheckBox.isChecked()) {
//            authenticationType = AuthType.EXTERNAL;
//            clientCertificateAlias = mClientCertificateSpinner.getAlias();
//        } else {
//            authenticationType = AuthType.PLAIN;
//            password = mPasswordView.getText().toString();
//        }
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

        AccountSetupAccountType.actionSelectAccountType(this, mAccount, false);

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
