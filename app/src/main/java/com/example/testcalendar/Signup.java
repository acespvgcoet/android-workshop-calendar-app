package com.example.testcalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    public static final String TAG = "TAG";

    EditText mFullName,mEmail,mPassword,mPhone;
    Button mRegisterButton;
    TextView mLoginButton;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFullName   = findViewById(R.id.fullName_text_edit);
        mEmail      = findViewById(R.id.Email_text_edit);
        mPassword   = findViewById(R.id.password_text_edit);
        mPhone      = findViewById(R.id.phone_text_edit);
        mRegisterButton= findViewById(R.id.register_button);
        mLoginButton   = findViewById(R.id.login_here_textView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        // When user clicks on Register button this method gets call
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Getting inputs store in variables
                final String email = mEmail.getText().toString();
                final String fullname = mFullName.getText().toString();
                final String phone = mPhone.getText().toString();
                String password = mPassword.getText().toString();

                // Checks whether email input is empty or not
                if(TextUtils.isEmpty(email))    {
                    mEmail.setError("Email is required");
                    return;
                }

                // Checks whether password input is empty or not
                if(TextUtils.isEmpty(password))    {
                    mEmail.setError("Password is required");
                    return;
                }

                // Checks whether password input length is long enough or not
                if(password.length() < 6)    {
                    mEmail.setError("Password Must be >= 6 Characters");
                    return;
                }

                // Checks whether phone number input is empty or not
                if(TextUtils.isEmpty(phone))    {
                    mEmail.setError("Mobile number is required");
                    return;
                }

                // Shows progress Bar on screen
                progressBar.setVisibility(View.VISIBLE);

                // Creates new user using email and password
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Checks whether task is successful or not
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                            // Sends verification email to user
                            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Signup.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });

                            // Toast.makeText(Signup.this, "User Created.", Toast.LENGTH_SHORT).show();

                            userID = firebaseUser.getUid();
                            DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("fullName",fullname);
                            user.put("email",email);
                            user.put("phone",phone);

                            // Stores user information in database using user Unique ID
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });

                            // Opens main page after signup process is completed
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else {
                            Toast.makeText(Signup.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failure : "+e.toString());
                    }
                });
            }
        });

        // Opens login page for those who have already registered
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });


    }
}