package com.example.labourondemand;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity {
    private String TAG = ForgotPasswordActivity.class.getName();
    private Button send;
    private EditText email;
    private FirebaseAuth auth;
    private String message = "Check your Inbox for an email with password reset link";
    private String emailString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        send = findViewById(R.id.forgot_password_send);
        email = findViewById(R.id.forgot_password_email);
        auth = FirebaseAuth.getInstance();


        Log.d(TAG, emailString + "!");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailString = email.getText().toString();
                if (email.getText().toString().matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")) {
//                    info.setVisibility(View.VISIBLE);

                    auth.sendPasswordResetEmail(emailString)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                        Intent loginActivity = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                        startActivity(loginActivity);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Couldn't send email");
                                }
                            });


                } else {
                    email.setError("Invalid Email address");
//                    info.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


}
