package com.issasafar.myapp.sqlitef;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.issasafar.myapp.R;
import com.issasafar.myapp.databinding.RegisterLayoutBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    private TextInputEditText nameField;
    private TextInputEditText emailField;
    private TextInputEditText passField;
    private TextInputEditText confirmField;

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
        confirmField.setOnEditorActionListener((v, actionId, event) -> handleRegister(actionId));
        regiButton.setOnClickListener(v -> handleRegister(confirmField.getImeActionId()));
        loginButton.setOnClickListener(v ->{
            Intent intent=new Intent(this,LoginActivity.class);
//            finish();
            startActivity(intent);
        } );
    }

    private boolean isPassStrong(String password) {
        return password.length() >= 6;
    }

    private boolean handleRegister( int actionId) {
        String name = String.valueOf(nameField.getText());
        String email = String.valueOf(emailField.getText());
        String password = String.valueOf(passField.getText());
        String password2 = String.valueOf(confirmField.getText());
        if (actionId == confirmField.getImeActionId()) {
            Log.d("myappregi","attempt to register");
            if (checkNoSyntaxErrors(name, email, password, password2)) {
                DbHelper dbHelper = new DbHelper(this);
                if (dbHelper.insertData(name, email, password)) {
                    Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Error email: "+email+"  is taken", Toast.LENGTH_LONG).show();
                }
            }
        }
        return false;
    }

    private boolean checkNoSyntaxErrors(String name, String email, String password, String password2) {
        if (TextUtils.isEmpty(name)) {
            nameField.setError(getString(R.string.user_name_empty_error));
            return false;
        }else if (!isValidEmail(String.valueOf(emailField.getText()))) {
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

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email)&& Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}