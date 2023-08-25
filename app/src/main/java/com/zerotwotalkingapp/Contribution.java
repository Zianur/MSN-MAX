package com.zerotwotalkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Contribution extends AppCompatActivity {

    CardView submitButton;
    String key;
    EditText questionEditText, answerEditText;


    private DatabaseReference UserContributionRef, DatabaseRef;
    private String savecurrentdate, savecurrentTime, postrandomName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribution);

        UserContributionRef = FirebaseDatabase.getInstance().getReference().child("UserContribution");
        DatabaseRef = FirebaseDatabase.getInstance().getReference().child("Database");

        submitButton = findViewById(R.id.submitContributionId);
        questionEditText = findViewById(R.id.questionId);
        answerEditText = findViewById(R.id.answerId);



        key = getIntent().getStringExtra("key");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (key.equals("Admin"))
                {
                    SendToDatabase();
                }
                else
                {
                    SendUserContribution();

                }


            }
        });
    }

    private void SendUserContribution() {


        String question, answer;

        question = questionEditText.getText().toString();
        answer = answerEditText.getText().toString();

        question = question.toLowerCase();
        question = question.replaceAll("\\p{Punct}", "");
        question = question.trim();

        answer = answer.toLowerCase();
        answer = answer.replaceAll("\\p{Punct}", "");
        answer = answer.trim();


        if(TextUtils.isEmpty(question))
        {
            questionEditText.setError("Please enter your question");
            questionEditText.requestFocus();
            return;
        }

        else if(TextUtils.isEmpty(answer))
        {
            answerEditText.setError("Please enter your answer");
            answerEditText.requestFocus();
            return;
        }
        else {

            ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Uploading......");
            progress.show();





            HashMap userMap = new HashMap();
            userMap.put("question", question);
            userMap.put("answer", answer);


//
            Calendar callForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            savecurrentdate = currentDate.format(callForDate.getTime());

            Calendar callForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            savecurrentTime = currentTime.format(callForTime.getTime());

            postrandomName = question + savecurrentdate + savecurrentTime;




            UserContributionRef.child(postrandomName).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()) {

                        progress.dismiss();

                        AlertDialog alertDialog = new AlertDialog.Builder(Contribution.this).create();

                        alertDialog.setTitle("Thank you...\n\n");
                        alertDialog.setMessage("Your Question and Answer will be examined by the Admin.\n" );

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                questionEditText.setText(null);
                                answerEditText.setText(null);

                                questionEditText.requestFocus();

//                                questionEditText.setText("");
//                                answerEditText.setText("");

//                                Intent contributeIntent = new Intent(Contribution.this, Admin.class);
//                                startActivity(contributeIntent);

                                alertDialog.cancel();

                            }
                        });

                        alertDialog.show();

                    }
                    else {
                        String message = task.getException().getMessage();
                        Toast.makeText(Contribution.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
//                        loadingbar.dismiss();
                    }
                }
            });
        }

    }




    private void SendToDatabase() {


        String question, answer;

        question = questionEditText.getText().toString();
        answer = answerEditText.getText().toString();

        question = question.toLowerCase();
        question = question.replaceAll("[^a-zA-Z]+$", "");
        question = question.trim();

        answer = answer.toLowerCase();
        answer = answer.replaceAll("[^a-zA-Z]+$", "");
        answer = answer.trim();


        if(TextUtils.isEmpty(question))
        {
            questionEditText.setError("Please enter your question");
            questionEditText.requestFocus();
            return;
        }

        else if(TextUtils.isEmpty(answer))
        {
            answerEditText.setError("Please enter your answer");
            answerEditText.requestFocus();
            return;
        }
        else {

            ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Uploading......");
            progress.show();




            HashMap userMap = new HashMap();
            userMap.put("question", question);
            userMap.put("answer", answer);


//
            Calendar callForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            savecurrentdate = currentDate.format(callForDate.getTime());

            Calendar callForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            savecurrentTime = currentTime.format(callForTime.getTime());

            postrandomName = question + savecurrentdate + savecurrentTime;



            DatabaseRef.child(postrandomName).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()) {

                        progress.dismiss();

                        AlertDialog alertDialog = new AlertDialog.Builder(Contribution.this).create();

//                        alertDialog.setTitle("Thank you...");
//                        alertDialog.setMessage("Your Question and Answer will be examined by the Admin." );

                        alertDialog.setTitle("Submission Successful!\n\n");
                        alertDialog.setMessage("Your Question and Answer has been uploaded.\n");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                questionEditText.setText(null);
                                answerEditText.setText(null);

                                questionEditText.requestFocus();

//                                Intent contributeIntent = new Intent(Contribution.this, Admin.class);
//                                startActivity(contributeIntent);

                                alertDialog.cancel();

                            }
                        });

                        alertDialog.show();

                    }
                    else {
                        String message = task.getException().getMessage();
                        Toast.makeText(Contribution.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
//                        loadingbar.dismiss();
                    }
                }
            });
        }
    }
}