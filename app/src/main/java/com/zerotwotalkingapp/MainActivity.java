  package com.zerotwotalkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    TextToSpeech textToSpeech;
    ImageView micImage;
    public static final Integer recordAudioRequestCode = 1,REQUEST_CODE =1234;
    private SpeechRecognizer speechRecognizer;
    AlertDialog.Builder speechAlertDialog;
    AlertDialog alertDialog;
    SeekBar seekBar;
    String key;

    CardView commandCard, searchCard;


    public static final String PREFS_NAME = "MyPrefsFile1";
    public CheckBox dontShowAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        micImage = findViewById(R.id.speakButton);
        seekBar = findViewById(R.id.seekbarId);
        commandCard = findViewById(R.id.commandHelpId);
        searchCard = findViewById(R.id.serchHelpId);




        key = getIntent().getStringExtra("key");






        new LovelyInfoDialog(this)
                .setIcon(R.drawable.empty_icon)
                //This will add Don't show again checkbox to the dialog. You can pass any ID as argument
                .setNotShowAgainOptionEnabled(0)
                .setNotShowAgainOptionChecked(true)
                .setConfirmButtonText("ok")
                .setConfirmButtonColor(R.color.design_default_color_on_primary)
                .setTitle("This feature may not work properly in few devices due to their optimisation issues.\n\n\nIf this feature does not work properly please restart the app.")
                .show();





        commandCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this).create();

                alertDialog.setTitle("List of Commands....");
                alertDialog.setMessage(getString(R.string.command));

                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


//                        questionEditText.setText(null);
//                        answerEditText.setText(null);
//
//                        questionEditText.requestFocus();

//                                questionEditText.setText("");
//                                answerEditText.setText("");

//                                Intent contributeIntent = new Intent(Contribution.this, Admin.class);
//                                startActivity(contributeIntent);

                        alertDialog.cancel();

                    }
                });

                alertDialog.show();
            }
        });

        searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this).create();

                alertDialog.setTitle("The way to search");
                alertDialog.setMessage(getString(R.string.search));

                alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


//                        questionEditText.setText(null);
//                        answerEditText.setText(null);
//
//                        questionEditText.requestFocus();

//                                questionEditText.setText("");
//                                answerEditText.setText("");

//                                Intent contributeIntent = new Intent(Contribution.this, Admin.class);
//                                startActivity(contributeIntent);

                        alertDialog.cancel();

                    }
                });

                alertDialog.show();
            }
        });



        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });


        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)!=
                PackageManager.PERMISSION_GRANTED)
        {
            checkPermission();
        }

        speechRecognizer = speechRecognizer.createSpeechRecognizer(this);
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {


                ArrayList<String> arrayList = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
                String text = arrayList.get(0);
                checkText(text);
                alertDialog.dismiss();


            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

      micImage.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {
              ViewGroup viewGroup = findViewById(R.id.content);
              View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.alertcustomlayout,
                      viewGroup,false);

              if (key.equals("command"))
              {
                  speechAlertDialog = new AlertDialog.Builder(MainActivity.this);
                  speechAlertDialog.setMessage("is Listenig....");
                  speechAlertDialog.setView(dialogView);
                  speechAlertDialog.setTitle("Zero Two");
                  alertDialog = speechAlertDialog.create();
                  alertDialog.show();
                  speechRecognizer.startListening(speechIntent);
                  //    startActivityForResult(speechIntent, recordAudioRequestCode);
              }
              else
              {
                  speechAlertDialog = new AlertDialog.Builder(MainActivity.this);
                  speechAlertDialog.setMessage("Speak to search ....");
                  speechAlertDialog.setView(dialogView);
                  speechAlertDialog.setTitle("Search");
                  alertDialog = speechAlertDialog.create();
                  alertDialog.show();
                  speechRecognizer.startListening(speechIntent);
              }





          }
      });




    }

    private void checkText(String text) {

        float speed = (float) seekBar.getProgress()/50;
        if(speed<0.1) speed = 0.1f;
        textToSpeech.setSpeechRate(speed);

        if(key.equals("command"))
        {

            text = text.replace("open ","");


            if(text.equals("email"))
            {
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                startActivity(intent);
            }
            if(text.equals("YouTube"))
            {
                Intent youtubeintent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                startActivity(youtubeintent);
            }
            if(text.equals("Facebook"))
            {
                Intent  facebookintent = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
                startActivity(facebookintent);
            }
            if(text.equals("camera"))
            {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(takePictureIntent);
            }
            if(text.equals("music player"))
            {
                Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                startActivity(intent);
            }
            if(text.equals("dial pad"))
            {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                startActivity(intent);
            }



        }

        if(text.contains("search"))
        {
            text = text.replace("search","");


            try {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, text);
                startActivity(intent);
            } catch (Exception e) {
                // TODO: handle exception
            }


        }

    }

    private void checkPermission() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.RECORD_AUDIO},recordAudioRequestCode);
        }

    }

    public void onPause(){
        if(textToSpeech !=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
//        {
//            // Populate the wordsList with the String values the recognition engine thought it heard
//            final ArrayList< String > matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            if (!matches.isEmpty())
//            {
//                String Query = matches.get(0);
//                allText.setText(Query);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == recordAudioRequestCode && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}