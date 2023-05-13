package com.issasafar.myapp.sqlf;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.issasafar.myapp.R;
import com.issasafar.myapp.databinding.RegisterLayoutBinding;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText nameField;
    private TextInputEditText emailField;
    private TextInputEditText passField;
    private TextInputEditText confirmField;
    private JSONParser mJSONParser = new JSONParser();
    private String URL = "http://10.0.2.2/test_android/index.php";
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.issasafar.myapp.databinding.RegisterLayoutBinding registerLayoutBinding = RegisterLayoutBinding.inflate(getLayoutInflater());
        setContentView(registerLayoutBinding.getRoot());
        nameField = registerLayoutBinding.registerUsername;
        emailField = registerLayoutBinding.registerEmail;
        passField = registerLayoutBinding.registerPassword;
        confirmField = registerLayoutBinding.registerPasswordConfirm;
        Button regiButton = registerLayoutBinding.registerButton;
        Button loginButton = registerLayoutBinding.loginButton;
        loginButton.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });
        regiButton.setOnClickListener(v -> {
            AttemptLogin attemptLogin = new AttemptLogin();
            String name = String.valueOf(nameField.getText());
            String email = String.valueOf(emailField.getText());
            String password = String.valueOf(passField.getText());
            String password2 = String.valueOf(confirmField.getText());
            if (checkNoSyntaxErrors(name,email,password,password2)) {
                attemptLogin.execute(name, email, password);
            }
        });
    }
    private boolean checkNoSyntaxErrors(String name, String email, String password, String password2) {
        if (TextUtils.isEmpty(name)) {
            nameField.setError(getString(R.string.user_name_empty_error));
            return false;
        }else if (!isValidEmail(email)) {
            emailField.setError(getString(R.string.email_err));
            return false;
        } else if(!isPassStrong(password)) {
            passField.setError(getString(R.string.password_short));
            return false;
        } else if (!password2.equals(password)) {
            confirmField.setError(getString(R.string.pass_conf_err));
            return false;
        } else {
            return true;
        }
    }
    private boolean isPassStrong(String password) {
        return password.length() >= 6;
    }
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email)&& Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

     private class AttemptLogin extends AsyncTask<String, String, JSONObject> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            String name = strings[0];
            String email = strings[1];
            String password = strings[2];
            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", name));
            if (email.length()>0) {
                params.add(new BasicNameValuePair("email", email));
            }
            params.add(new BasicNameValuePair("password", password));
            return mJSONParser.makeHttpRequest(URL, "POST", params);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to receive data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}