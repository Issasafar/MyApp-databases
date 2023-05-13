package com.issasafar.myapp.firedb;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.issasafar.myapp.R;
import com.issasafar.myapp.databinding.ActivityMainBinding;
import com.issasafar.myapp.databinding.RegisterLayoutBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;
    private TextInputEditText nameField;
    private TextInputEditText emailField;
    private TextInputEditText passField;
    private TextInputEditText confirmField;
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
            String name = String.valueOf(nameField.getText());
            String email = String.valueOf(emailField.getText());
            String password = String.valueOf(passField.getText());
            String password2 = String.valueOf(confirmField.getText());
            if (checkNoSyntaxErrors(name,email,password,password2)) {
              attemptRegister(name,email,password);
            }
        });
    }

    public void attemptRegister(String name,String email,String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.getResult().getSignInMethods().isEmpty()) {
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user= auth.getCurrentUser();
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                        userRef.child(user.getUid()).child("name").setValue(name);
                        Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
            } else {

              Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(Throwable::printStackTrace);
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
}

