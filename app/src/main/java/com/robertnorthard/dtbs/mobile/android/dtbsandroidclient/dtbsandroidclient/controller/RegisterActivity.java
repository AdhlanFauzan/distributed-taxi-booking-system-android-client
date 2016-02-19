package com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.R;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.model.Account;
import com.robertnorthard.dtbs.mobile.android.dtbsandroidclient.dtbsandroidclient.service.AuthenticationService;

import org.json.JSONException;

import java.io.IOException;

/**
 * Controller for registering new accounts.
 */
public class RegisterActivity extends AppCompatActivity {

    // TAG used for logging.
    private static final String TAG = RegisterActivity.class.getName();

    private static String ACCOUNT_ROLE = "PASSENGER";

    private AuthenticationService authenticationService;

    private EditText txtCommonName;
    private EditText txtFamilyName;
    private EditText txtUsername;
    private EditText txtEmail;
    private EditText txtPhoneNumber;
    private EditText txtPassword;
    private Button btnRegister;

    public RegisterActivity(){
        this.authenticationService = new AuthenticationService();
    }

    /**
     * The system calls this method when creating the fragment.
     * Initialises essential components of the fragment
     * @param savedInstanceState state to restore.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.txtCommonName = (EditText)findViewById(R.id.txt_register_common_name);
        this.txtFamilyName = (EditText)findViewById(R.id.txt_register_family_name);
        this.txtUsername = (EditText)findViewById(R.id.txt_register_username);
        this.txtEmail = (EditText)findViewById(R.id.txt_register_email);
        this.txtPhoneNumber = (EditText)findViewById(R.id.txt_register_phone_number);
        this.txtPassword = (EditText)findViewById(R.id.txt_register_password);
        this.btnRegister = (Button)findViewById(R.id.btn_register_register);

        // register button access listener.
        this.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Async registration task to prevent blocking UI thread */
                new AsyncTask<String, Void, Void>() {

                    private final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
                    private Exception exception = null;
                    private AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();

                    /**
                     * Invoked on sign in button press.
                     */
                    protected void onPreExecute() {
                        this.dialog.setMessage(getString(R.string.pd_registering));
                        this.dialog.show();
                    }

                    /**
                     * Authentication background task.
                     *
                     * @param params username and password to authenticate with.
                     * @return user's account if authenticated, else null.
                     */
                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            // register new user account
                            Account account = new Account();

                            account.setUsername(params[0]);
                            account.setCommonName(params[1]);
                            account.setFamilyName(params[2]);
                            account.setEmail(params[3]);
                            account.setPhoneNumber(params[4]);
                            account.setPassword(params[5]);
                            account.setRole(ACCOUNT_ROLE);

                            authenticationService.registerAccount(account);

                        } catch (IOException | JSONException | IllegalArgumentException ex) {
                            Log.e(TAG, ex.getMessage());
                            exception = ex;
                        }

                        return null;
                    }

                    /**
                     * Handler to manage result of background task.
                     *
                     */
                    @Override
                    protected void onPostExecute(final Void result) {

                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.cancel();
                            }
                        });

                        if (this.dialog.isShowing()) {
                            this.dialog.dismiss();
                        }

                        if(!(exception == null)) {
                            alertDialog.setTitle(exception.getMessage());
                            alertDialog.show();
                        }else{
                            Intent newIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(newIntent);
                        }
                    }

                }.execute(txtUsername.getText().toString(),
                        txtCommonName.getText().toString(),
                        txtFamilyName.getText().toString(),
                        txtEmail.getText().toString(),
                        txtPhoneNumber.getText().toString(),
                        txtPassword.getText().toString());
            }
        });
    }
}
