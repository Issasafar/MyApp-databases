package com.issasafar.myapp.firedb;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.issasafar.myapp.databinding.LoginLayoutBinding;

public class LoginActivity extends AppCompatActivity {
    private LoginLayoutBinding loginLayoutBinding;
    private TextInputEditText emailField;
    private TextInputEditText passwordField;
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
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("users");
                                    dataRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String name = snapshot.child("name").getValue(String.class);
                                            Toast.makeText(getApplicationContext(), "Logging in as: "+name, Toast.LENGTH_LONG).show();

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show());
                    }
                }
        );
    }
}
