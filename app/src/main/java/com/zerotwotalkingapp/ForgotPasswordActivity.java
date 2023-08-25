package com.zerotwotalkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView resetButton, msgText;
    EditText emailEditText;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();


        resetButton = findViewById(R.id.resetButtonId);
        msgText = findViewById(R.id.msgFPId);
        emailEditText = findViewById(R.id.emailFPId);


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendingEmail();

            }
        });

    }

    private void sendingEmail()
    {
        String userEmail = emailEditText.getText().toString();

        if (TextUtils.isEmpty(userEmail))
        {
            emailEditText.setError("Please enter password");
            emailEditText.requestFocus();
            return;
        }
        else
        {
            mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(ForgotPasswordActivity.this, "Please check your Email.....", Toast.LENGTH_LONG).show();

                        Intent loginIntent = new Intent(ForgotPasswordActivity.this, LogIn.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginIntent);
                        finish();


                    }
                    else
                    {
                        String error = task.getException().getMessage();
                        Toast.makeText(ForgotPasswordActivity.this, "Error " + error, Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

}