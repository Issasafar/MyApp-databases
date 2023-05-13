package com.issasafar.myapp.sqlf;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.issasafar.myapp.databinding.LoginLayoutBinding;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private LoginLayoutBinding loginLayoutBinding;
    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private final JSONParser mJSONParser = new JSONParser();

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.issasafar.myapp.databinding.LoginLayoutBinding loginLayoutBinding = LoginLayoutBinding.inflate(getLayoutInflater());
        setContentView(loginLayoutBinding.getRoot());
        Button loginButton = loginLayoutBinding.loginButton;
        emailField = loginLayoutBinding.loginEmail;
        passwordField = loginLayoutBinding.loginPassword;
        loginButton.setOnClickListener(v->{
            String email = String.valueOf(emailField.getText());
            String password = String.valueOf(passwordField.getText());
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && MainActivity.isValidEmail(email)) {
                AttemptLogin attemptLogin = new AttemptLogin();
                attemptLogin.execute("", email, password);
            }
                }
        );
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
            String URL = "http://10.0.2.2/test_android/index.php";
            return mJSONParser.makeHttpRequest(URL, "POST", params);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(), result.getString("message"), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to recieve data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
