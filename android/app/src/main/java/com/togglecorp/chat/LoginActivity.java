package com.togglecorp.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar mLoginProgress;
    private View mLoginForm;
    private String mUsername;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        if (preferences.getBoolean("logged_in", false)) {
            startMainActivity();
            return;
        }

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        assert signInButton != null;
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    private void signIn() {
        mLoginProgress = (ProgressBar) findViewById(R.id.login_progress);
        mLoginForm = findViewById(R.id.login_form);
        TextView usernameView = (TextView) findViewById(R.id.username);
        TextView passwordView = (TextView) findViewById(R.id.password);

        assert mLoginProgress != null && mLoginForm != null
                && usernameView != null && passwordView != null;

        mUsername = usernameView.getText().toString();
        mPassword = passwordView.getText().toString();

        mLoginForm.setVisibility(View.GONE);
        mLoginProgress.setVisibility(View.VISIBLE);

        new SignInTask().execute();
    }

    private class SignInTask extends AsyncTask<Void, Void, Void> {

        private boolean success = false;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Client client = new Client(mUsername, mPassword);
                client.post("api/v1/user/verify/");
                success = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (success) {
                SharedPreferences preferences =
                        PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                preferences.edit()
                        .putString("username", mUsername)
                        .putString("password", mPassword)
                        .putBoolean("logged_in", true)
                        .apply();
                startMainActivity();
            }
            else {
                Toast.makeText(LoginActivity.this, "Couldn't login.", Toast.LENGTH_SHORT)
                        .show();
                mLoginProgress.setVisibility(View.GONE);
                mLoginForm.setVisibility(View.VISIBLE);
            }
        }
    }

    void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}