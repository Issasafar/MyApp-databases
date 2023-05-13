package com.issasafar.myapp.sqlitef;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.issasafar.myapp.R;
import com.issasafar.myapp.databinding.LoginLayoutBinding;

public class LoginActivity extends AppCompatActivity {
    private LoginLayoutBinding mLoginLayoutBinding;
    private Button loginButton;
    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginLayoutBinding = LoginLayoutBinding.inflate(getLayoutInflater());
        setContentView(mLoginLayoutBinding.getRoot());
        loginButton=mLoginLayoutBinding.loginButton;
        emailField = mLoginLayoutBinding.loginEmail;
        passwordField = mLoginLayoutBinding.loginPassword;
        loginButton.setOnClickListener(v -> {
            handleLogin();
        });
    }

    private void handleLogin() {
        User user;
        String email = String.valueOf(emailField.getText());
        String password = String.valueOf(passwordField.getText());
        if (!MainActivity.isValidEmail(email)) {
            emailField.setError(getString(R.string.email_err));
        } else if (TextUtils.isEmpty(password)) {
            passwordField.setError(getString(R.string.password_empty));
        }else {
        DbHelper dbHelper = new DbHelper(this);

        user=(User) (dbHelper.getUserByEmail(email));
            if (user != null&& password.equals(user.getPassword())) {
                Toast.makeText(this, "Signing in as: " + user.getName(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Invalid Password or Email", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
