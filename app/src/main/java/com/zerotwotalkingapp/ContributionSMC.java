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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ContributionSMC extends AppCompatActivity {

    CardView submitButton;
    TextView doneButton, textView, deleteButton;
    String key;
    EditText questionEditText, answerEditText, nameEditText;

    private String savecurrentdate, savecurrentTime, postrandomName;


    private DatabaseReference User, DatabaseRef;
    FirebaseAuth mAuth;
    String CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribution_s_m_c);

        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        User = FirebaseDatabase.getInstance().getReference().child("User").child(CurrentUser);
        doneButton = findViewById(R.id.doneButtonId);

        submitButton = findViewById(R.id.submitContributionId);
        questionEditText = findViewById(R.id.questionId);
        answerEditText = findViewById(R.id.answerId);
        nameEditText = findViewById(R.id.conversationNameId);
        textView = findViewById(R.id.textid);
        deleteButton = findViewById(R.id.deleteButtonId);



        key = getIntent().getStringExtra("key");

        if (key.equals("add new"))
        {
            nameEditText.setText(null);
        }
        else
        {
            nameEditText.setText(key);
            deleteButton.setVisibility(View.VISIBLE);
        }



        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DeleteConversation();

            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendData();

            }
        });


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (key.equals("add new"))
                {
                    Intent SMCIntent = new Intent(ContributionSMC.this,SMCList.class);
                    SMCIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(SMCIntent);
                    finish();
                }
                else
                {
                    Intent RSMCIntent = new Intent(ContributionSMC.this,SelfMadeConversation.class);
                    RSMCIntent.putExtra("key", key);
                    RSMCIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(RSMCIntent);
                    finish();
                }


            }
        });
    }

    private void DeleteConversation() {



        ProgressDialog progress = new ProgressDialog(ContributionSMC.this);
        progress.setTitle("Deleting......");
        progress.show();

        User.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    progress.dismiss();


                    AlertDialog alertDialog = new AlertDialog.Builder(ContributionSMC.this).create();

                    alertDialog.setTitle("Deleted...\n\n");
                    alertDialog.setMessage("Question and Answer has been deleted successfully.\n" );

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent SMCIntent = new Intent(ContributionSMC.this,SMCList.class);
                            SMCIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(SMCIntent);
                            finish();

                            alertDialog.cancel();

                        }
                    });

                    alertDialog.show();
                }
            }
        });



    }

    private void SendData() {


        String question, answer, name;

        question = questionEditText.getText().toString();
        answer = answerEditText.getText().toString();
        name = nameEditText.getText().toString();

        question = question.toLowerCase();
        question = question.replaceAll("\\p{Punct}", "");
        question = question.trim();

        answer = answer.toLowerCase();
        answer = answer.replaceAll("\\p{Punct}", "");
        answer = answer.trim();


        if (TextUtils.isEmpty(question)) {
            questionEditText.setError("Please enter your question");
            questionEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(answer)) {
            answerEditText.setError("Please enter your answer");
            answerEditText.requestFocus();
            return;
        } else if (TextUtils.isEmpty(answer)) {
            nameEditText.setError("Please enter your answer");
            nameEditText.requestFocus();
            return;
        } else {

            ProgressDialog progress = new ProgressDialog(this);
            progress.setTitle("Uploading......");
            progress.show();


            HashMap userMap = new HashMap();
            userMap.put("question", question);
            userMap.put("answer", answer);
            userMap.put("name", name);


//
            Calendar callForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            savecurrentdate = currentDate.format(callForDate.getTime());

            Calendar callForTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            savecurrentTime = currentTime.format(callForTime.getTime());

            postrandomName = question + savecurrentdate + savecurrentTime;


            User.child(name).child(postrandomName).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if (task.isSuccessful()) {

                        progress.dismiss();


                        textView.setVisibility(View.VISIBLE);
                        doneButton.setVisibility(View.VISIBLE);


                        AlertDialog alertDialog = new AlertDialog.Builder(ContributionSMC.this).create();

                        alertDialog.setTitle("Data Added...\n\n");
                        alertDialog.setMessage("Your data has been added to the conversation successfully.\n");

                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                questionEditText.setText(null);
                                answerEditText.setText(null);

                                questionEditText.requestFocus();

                                alertDialog.cancel();

                            }
                        });

                        alertDialog.show();

                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(ContributionSMC.this, "Error Occured" + " " + message, Toast.LENGTH_LONG).show();
//                        loadingbar.dismiss();
                    }
                }
            });

        }
    }

}