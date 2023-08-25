package com.zerotwotalkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private EditText UserEmail, UserPassword, UserConfirmPassword;
    private TextView CreatAccountButton;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingbar;
    private String email, placeName, bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mAuth = FirebaseAuth.getInstance();

        UserEmail = (EditText) findViewById(R.id.signup_email);
//        userName = (EditText) findViewById(R.id.nameSignUpId);
//        userPhone = (EditText) findViewById(R.id.phoneSignUpId);
//        donationNumber = (EditText) findViewById(R.id.donationNumSignUpId);
        UserPassword = (EditText) findViewById(R.id.signup_password);
        UserConfirmPassword = (EditText) findViewById(R.id.signup_confirmpassword);
        CreatAccountButton =  findViewById(R.id.signup_button);




        loadingbar = new ProgressDialog(this);


        CreatAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                CreateNewAccount();
            }
        });



    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if(currentUser!=null)
//        {
//            SendUserToMainActivity();
//        }
//
//    }

//    private void SendUserToMainActivity()
//    {
//
//        Intent mainactivityIntent = new Intent(SignUp.this,MainActivity.class);
//        mainactivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainactivityIntent);
//        finish();
//    }

    private void CreateNewAccount()
    {
        email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirmpassword = UserConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            UserEmail.setError("Please enter a valid email address");
            UserEmail.requestFocus();
            return;
        }
        else if(TextUtils.isEmpty(password))
        {
            UserPassword.setError("Please enter password");
            UserPassword.requestFocus();
            return;

        }
        else if(TextUtils.isEmpty(confirmpassword))
        {
            UserConfirmPassword.setError("Please confirm password");
            UserConfirmPassword.requestFocus();
            return;
        }

        else if(!password.equals(confirmpassword))
        {
            UserPassword.setError("Password does not match");
            UserPassword.requestFocus();
            return;
        }
        else
        {

            loadingbar.setTitle("Creating Account");
            loadingbar.setMessage("Please wait while we are creating your account");
            loadingbar.show();
            loadingbar.setCanceledOnTouchOutside(true);


            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful())
                            {

                                sendEmailVerificationmessage();
                                loadingbar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(SignUp.this, "Error Occured at authentication" + " " + message, Toast.LENGTH_LONG).show();
                                loadingbar.dismiss();
                            }

                        }
                    });
        }

    }

    private void sendEmailVerificationmessage()
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {

            currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(SignUp.this, "We have send an email to verify your account\nPlease check your email", Toast.LENGTH_LONG).show();


                        SendUserToLoginActivity();
//                        SendUserToSetupActivity();
//                        mAuth.signOut();
//                        SendUserToMainActivity();
                    }
                    else
                    {
                        String error = task.getException().getMessage();
                        Toast.makeText(SignUp.this, "Error " + error, Toast.LENGTH_SHORT).show();
//                        mAuth.signOut();
                    }

                }
            });
        }
    }

//    private void SendUserToSetupActivity()
//    {
//
//
//        Intent setupIntent = new Intent(SignUp.this,SetupActivity.class);
//        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(setupIntent);
//        finish();
//    }


    private void SendUserToLoginActivity()
    {

        Intent loginIntent = new Intent(SignUp.this, LogIn.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();

    }
}