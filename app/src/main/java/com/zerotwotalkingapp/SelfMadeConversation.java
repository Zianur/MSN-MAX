package com.zerotwotalkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.ArrayList;
import java.util.Locale;

public class SelfMadeConversation extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, ConversationAdapter.OnConversationClickListener {

    CardView answerQCard, enterButton, translateButton, pronounceButton;
    LinearLayout settingButton;

    RecyclerView recyclerView;
    private ArrayList<ConversationModule> conversationModuleArrayList;
    private ConversationAdapter conversationAdapter;

    EditText youEditText;
    TextView allText, zeroTwoText, speakText, YLT, TLT, modeText, NIcon;
    TextToSpeech textToSpeech;
    ImageView micImage, micButtonC;
    public static final Integer recordAudioRequestCode = 1,REQUEST_CODE =1234;
    private SpeechRecognizer speechRecognizer;
    AlertDialog.Builder speechAlertDialog;
    AlertDialog alertDialog;
    SeekBar seekBar;
    FirebaseAuth mAuth;
    String yourLanguage, translatorLanguage, YLSS, TLSS, conversationMode, MSS;


    private DatabaseReference DatabaseRef;
    private String key, answerText, CurrentUser;


    public static final String PREFS_NAME = "MyPrefsFile1";
    public CheckBox dontShowAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_made_conversation);

        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();


        answerQCard =  findViewById(R.id.contributeButtonId);

        allText = findViewById(R.id.alltext);
        recyclerView = findViewById(R.id.recyclerViewId);
        micImage = findViewById(R.id.speakButton);
        micButtonC = findViewById(R.id.CspeakButton);
        seekBar = findViewById(R.id.seekbarId);
        zeroTwoText = findViewById(R.id.zero_two_text);
        enterButton = findViewById(R.id.enterButtonId);
        youEditText = findViewById(R.id.youEditTextId);
        speakText = findViewById(R.id.textid);
        NIcon = findViewById(R.id.navigationIconId);
//        YLT = findViewById(R.id.yourlanguageTextId);
//        TLT = findViewById(R.id.translatorlanguageTextId);
//        modeText = findViewById(R.id.modeId);

        settingButton = findViewById(R.id.settingButtonId);
        translateButton = findViewById(R.id.translateButtonId);
        pronounceButton = findViewById(R.id.pronounceId);


        DatabaseRef = FirebaseDatabase.getInstance().getReference().child("User").child(CurrentUser);


        yourLanguage = "English";
        translatorLanguage = "English";
        conversationMode = "Single";




        new LovelyInfoDialog(this)
                .setIcon(R.drawable.empty_icon)
                //This will add Don't show again checkbox to the dialog. You can pass any ID as argument
                .setNotShowAgainOptionEnabled(0)
                .setNotShowAgainOptionChecked(true)
                .setConfirmButtonText("ok")
                .setConfirmButtonColor(R.color.design_default_color_on_primary)
                .setTitle("This feature may not work properly in few devices due to their optimisation issues.\n\n\nIf this feature does not work properly please restart the app.")
                .show();







        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        conversationModuleArrayList = new ArrayList<>();





        key = getIntent().getStringExtra("key");


        Spinner yourLanguageSpinner = findViewById(R.id.yourLanguageId);
        Spinner translatorLanguageSpinner = findViewById(R.id.translateLanguageId);
        Spinner modeSpinner = findViewById(R.id.conversationModeId);



        String[] yourLanguageString = getResources().getStringArray(R.array.your_language);
        String[] translatorLanguageString = getResources().getStringArray(R.array.translator_language);
        String[] modeString = getResources().getStringArray(R.array.mode);



        ArrayAdapter<String> yourLanguageAdapter = new ArrayAdapter<String>(SelfMadeConversation.this,
                R.layout.spinner_language_layout,R.id.spinnerLayoutId,
                yourLanguageString);

        yourLanguageSpinner.setAdapter(yourLanguageAdapter);

        ArrayAdapter<String> translatorLanguageAdapter = new ArrayAdapter<String>(SelfMadeConversation.this,
                R.layout.spinner_language_layout,R.id.spinnerLayoutId,
                translatorLanguageString);

        translatorLanguageSpinner.setAdapter(translatorLanguageAdapter);

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(SelfMadeConversation.this,
                R.layout.spinner_view,R.id.spinnerLayoutId,
                modeString);

        modeSpinner.setAdapter(modeAdapter);





        yourLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                yourLanguage = yourLanguageString[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        translatorLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                translatorLanguage = translatorLanguageString[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                conversationMode = modeString[position];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        NIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(SelfMadeConversation.this, v);
                popup.setOnMenuItemClickListener(SelfMadeConversation.this);
                popup.inflate(R.menu.smc_menu);
                popup.show();
            }
        });

        micButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                conversationMode = "Continuous";
                SpeakNowListening();
            }
        });


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dialogueString = youEditText.getText().toString();

                if (dialogueString.isEmpty())
                {
                    youEditText.setError("Please enter your dialogue");
                    youEditText.requestFocus();
                }
                else
                {
                    dialogueString = dialogueString.toLowerCase();
                    dialogueString = dialogueString.replaceAll("\\p{Punct}", "");
                    dialogueString = dialogueString.trim();

                    conversationModuleArrayList.add(new ConversationModule(dialogueString));
                    conversationAdapter = new ConversationAdapter(SelfMadeConversation.this,conversationModuleArrayList);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(conversationAdapter);
                    conversationAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition( conversationAdapter.getItemCount());
                    conversationAdapter.setOnRecyclerViewClickListener(SelfMadeConversation.this);


                    youEditText.setText(null);
                    checkText(dialogueString);

                }

            }
        });





        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    if (yourLanguage.equals("Germany"))
                    {
                        textToSpeech.setLanguage(new Locale("de-De"));
                    }
                    else if (yourLanguage.equals("Bengali"))
                    {
                        textToSpeech.setLanguage(new Locale("bn_IN"));
                    }
                    else
                    {
                        textToSpeech.setLanguage(Locale.UK);
                    }
                }
            }
        });


        if(ContextCompat.checkSelfPermission(SelfMadeConversation.this, Manifest.permission.RECORD_AUDIO)!=
                PackageManager.PERMISSION_GRANTED)
        {
            checkPermission();
        }



        micImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                answerQCard.setVisibility(View.VISIBLE);

//                ViewGroup viewGroup = findViewById(R.id.content);
//                View dialogView = LayoutInflater.from(RandomConversation.this).inflate(R.layout.alertcustomlayout,
//                        viewGroup,false);
//                speechAlertDialog = new AlertDialog.Builder(RandomConversation.this);
//                speechAlertDialog.setMessage("is Listenig....");
//                speechAlertDialog.setView(dialogView);
//                speechAlertDialog.setTitle("Zero Two");
//                alertDialog = speechAlertDialog.create();
//                alertDialog.show();
//                speechRecognizer.startListening(speechIntent);

                //    startActivityForResult(speechIntent, recordAudioRequestCode);


                conversationMode = "Single";
                SpeakNowListening();


            }
        });





        answerQCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent contributeIntent = new Intent(SelfMadeConversation.this, ContributionSMC.class);
                contributeIntent.putExtra("key", key);
                startActivity(contributeIntent);



            }
        });


        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (yourLanguage.equals(translatorLanguage))
                {
                    Toast.makeText(SelfMadeConversation.this, "Please select different translator language", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    switch (translatorLanguage) {
                        case "English": {

                            TranslateAPI translateAPI = new TranslateAPI(
                                    Language.AUTO_DETECT,   //Source Language
                                    Language.ENGLISH,         //Target Language
                                    answerText);           //Query Text


                            translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                                @Override
                                public void onSuccess(String translatedText) {
                                    zeroTwoText.setText(translatedText);
                                }

                                @Override
                                public void onFailure(String ErrorText) {

                                }
                            });

                            break;
                        }
                        case "Germany": {

                            TranslateAPI translateAPI = new TranslateAPI(
                                    Language.AUTO_DETECT,   //Source Language
                                    Language.GERMAN,         //Target Language
                                    answerText);           //Query Text


                            translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                                @Override
                                public void onSuccess(String translatedText) {
                                    zeroTwoText.setText(translatedText);
                                }

                                @Override
                                public void onFailure(String ErrorText) {

                                }
                            });

                            break;
                        }
                        case "Bengali": {

                            TranslateAPI translateAPI = new TranslateAPI(
                                    Language.AUTO_DETECT,   //Source Language
                                    Language.BENGALI,         //Target Language
                                    answerText);           //Query Text


                            translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                                @Override
                                public void onSuccess(String translatedText) {
                                    zeroTwoText.setText(translatedText);
                                }

                                @Override
                                public void onFailure(String ErrorText) {

                                }
                            });
                            break;
                        }
                    }
                }
            }
        });

        pronounceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                float speed = (float) seekBar.getProgress()/50;
                if(speed<0.1) speed = 0.1f;
                textToSpeech.setSpeechRate(speed);

                textToSpeech.speak(answerText, TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }

    private void SpeakNowListening() {


        speakText.setText("Speak Now Zero Two is Listening");


//        micImage.setVisibility(View.INVISIBLE);
//        micImage.setImageResource(R.drawable.settings_voice_icon_white);


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);


        switch (yourLanguage) {
            case "English":
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                break;
            case "Germany":
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-De");
                break;
            case "Bengali":
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-IN");
                break;
        }



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

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onEndOfSpeech() {

                speakText.setText("Tap to speak");
//                micImage.setVisibility(View.VISIBLE);
//                micImage.setImageDrawable(getDrawable(R.drawable.white_mic_icon));//this works though
//                micImage.setImageResource(R.drawable.white_mic_icon);

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {


                ArrayList<String> arrayList = results.getStringArrayList(speechRecognizer.RESULTS_RECOGNITION);
//                allText.setText(arrayList.get(0));
                String text = arrayList.get(0);
                text = text.toLowerCase();

                conversationModuleArrayList.add(new ConversationModule(text));
                conversationAdapter = new ConversationAdapter(SelfMadeConversation.this,conversationModuleArrayList);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(conversationAdapter);
                conversationAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition( conversationAdapter.getItemCount());
                conversationAdapter.setOnRecyclerViewClickListener(SelfMadeConversation.this);



//                allText.setText(text);

//                checkText(text);

//                GiveAnswer();
//                alertDialog.dismiss();



                float speed = (float) seekBar.getProgress()/50;
                if(speed<0.1) speed = 0.1f;
                textToSpeech.setSpeechRate(speed);


                Query query = DatabaseRef.child(key).orderByChild("question").equalTo(text);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


//

                        if(dataSnapshot.getChildrenCount() == 0)
                        {
                            answerText = "Sorry! I don't know the answer";


                            conversationModuleArrayList.add(new ConversationModule(answerText));
                            conversationAdapter = new ConversationAdapter(SelfMadeConversation.this,conversationModuleArrayList);
                            recyclerView.setAdapter(conversationAdapter);
                            conversationAdapter.notifyDataSetChanged();
                            recyclerView.smoothScrollToPosition( conversationAdapter.getItemCount());
                            conversationAdapter.setOnRecyclerViewClickListener(SelfMadeConversation.this);


                            speakText.setText("Zero Two is speaking");
//                            micImage.setVisibility(View.INVISIBLE);
//                            micImage.setImageResource(R.drawable.pronounce_icon_white);

                            textToSpeech.speak(answerText, TextToSpeech.QUEUE_FLUSH, null);


                            if(conversationMode.equals("Continuous"))
                            {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (textToSpeech.isSpeaking())
                                        {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    SpeakNowListening();
                                                }
                                            },4000);
                                        }
                                        else
                                        {
                                            SpeakNowListening();
                                        }

                                    }
                                },2000);
                            }
                            else
                            {
                                speakText.setText("Tap to speak");
//                                micImage.setVisibility(View.VISIBLE);
//                                micImage.setImageResource(R.drawable.white_mic_icon);
                            }


                        }


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                            if (snapshot.exists())
                            {
                                answerText = snapshot.child("answer").getValue().toString();
                                conversationModuleArrayList.add(new ConversationModule(answerText));
                                conversationAdapter = new ConversationAdapter(SelfMadeConversation.this,conversationModuleArrayList);
                                recyclerView.setAdapter(conversationAdapter);
                                conversationAdapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition( conversationAdapter.getItemCount());
                                conversationAdapter.setOnRecyclerViewClickListener(SelfMadeConversation.this);

//                                zeroTwoText.setText(answerText);

                                speakText.setText("Zero Two is speaking");
//                            micImage.setVisibility(View.INVISIBLE);
//                                micImage.setImageResource(R.drawable.pronounce_icon_white);

                                textToSpeech.speak(answerText, TextToSpeech.QUEUE_FLUSH, null);

                                if(conversationMode.equals("Continuous"))
                                {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (textToSpeech.isSpeaking())
                                            {
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        SpeakNowListening();
                                                    }
                                                },4000);
                                            }
                                            else
                                            {
                                                SpeakNowListening();
                                            }

                                        }
                                    },2000);
                                }
                                else
                                {
                                    speakText.setText("Tap to speak");
//                                micImage.setVisibility(View.VISIBLE);
//                                    micImage.setImageResource(R.drawable.white_mic_icon);
                                }

                            }
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(SelfMadeConversation.this, "Error Occured", Toast.LENGTH_LONG).show();

                    }
                });





            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });



        speechRecognizer.startListening(speechIntent);

    }


    private void checkText(String text) {



        float speed = (float) seekBar.getProgress()/50;
        if(speed<0.1) speed = 0.1f;
        textToSpeech.setSpeechRate(speed);


        Query query = DatabaseRef.child(key).orderByChild("question").equalTo(text);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.getChildrenCount() == 0)
                {
                    answerText = "Sorry! I don't know the answer";

                    conversationModuleArrayList.add(new ConversationModule(answerText));
                    conversationAdapter = new ConversationAdapter(SelfMadeConversation.this,conversationModuleArrayList);
                    recyclerView.setAdapter(conversationAdapter);
                    conversationAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition( conversationAdapter.getItemCount());
                    conversationAdapter.setOnRecyclerViewClickListener(SelfMadeConversation.this);

//                    zeroTwoText.setText(answerText);

                    speakText.setText("Zero Two is speaking");
//                            micImage.setVisibility(View.INVISIBLE);
//                    micImage.setImageResource(R.drawable.pronounce_icon_white);


                    textToSpeech.speak(answerText, TextToSpeech.QUEUE_FLUSH, null);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (textToSpeech.isSpeaking())
                            {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        speakText.setText("Tap to speak");
//                                micImage.setVisibility(View.VISIBLE);
//                                        micImage.setImageResource(R.drawable.white_mic_icon);
                                    }
                                },4000);
                            }
                            else
                            {
                                speakText.setText("Tap to speak");
//                                micImage.setVisibility(View.VISIBLE);
//                                micImage.setImageResource(R.drawable.white_mic_icon);
                            }

                        }
                    },2000);


                }




                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    if(snapshot.exists())
                    {
                        answerText = snapshot.child("answer").getValue().toString();
                        conversationModuleArrayList.add(new ConversationModule(answerText));
                        conversationAdapter = new ConversationAdapter(SelfMadeConversation.this,conversationModuleArrayList);
                        recyclerView.setAdapter(conversationAdapter);
                        conversationAdapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition( conversationAdapter.getItemCount());
                        conversationAdapter.setOnRecyclerViewClickListener(SelfMadeConversation.this);


//                        zeroTwoText.setText(answerText);

                        speakText.setText("Zero Two is speaking");
//                            micImage.setVisibility(View.INVISIBLE);
//                        micImage.setImageResource(R.drawable.pronounce_icon_white);

                        textToSpeech.speak(answerText, TextToSpeech.QUEUE_FLUSH, null);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (textToSpeech.isSpeaking())
                                {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            speakText.setText("Tap to speak");

//                                micImage.setVisibility(View.VISIBLE);
//                                            micImage.setImageResource(R.drawable.white_mic_icon);
                                        }
                                    },4000);
                                }
                                else
                                {

                                    speakText.setText("Tap to speak");

//                                micImage.setVisibility(View.VISIBLE);
//                                    micImage.setImageResource(R.drawable.white_mic_icon);
                                }

                            }
                        },2000);



                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(SelfMadeConversation.this, "Error Occured", Toast.LENGTH_LONG).show();

            }
        });








    }

    private void checkPermission() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            ActivityCompat.requestPermissions(SelfMadeConversation.this,new String[]{
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.rcId:
                // do your code

                Intent rcIntent = new Intent(SelfMadeConversation.this, RandomConversation.class);
//                SMCIntent.putExtra("key","add new");
                startActivity(rcIntent);


                return true;
            case R.id.vcId:
                // do your code

                Intent commandIntent = new Intent(SelfMadeConversation.this, MainActivity.class);
                commandIntent.putExtra("key", "command");
                startActivity(commandIntent);

                return true;
            default:
                return false;
        }
    }

    @Override
    public void PronounceClick(int position) {


        ConversationModule selected = conversationModuleArrayList.get(position);

        String text = selected.getQuestion();




        float speed = (float) seekBar.getProgress()/50;
        if(speed<0.1) speed = 0.1f;
        textToSpeech.setSpeechRate(speed);
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);




    }

    @Override
    public void TranslateClick(int position) {


        ConversationModule selected = conversationModuleArrayList.get(position);

        String text = selected.getQuestion();

        String[] translatorLanguageString = getResources().getStringArray(R.array.translator_language);


        final AlertDialog.Builder builder = new AlertDialog.Builder(SelfMadeConversation.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        LayoutInflater layoutInflater = this.getLayoutInflater();//need this layoutinflater to inflate the layout
        View dialogView = layoutInflater.inflate(R.layout.translator_languages_layout, viewGroup, false);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();


        EditText searchEditText = dialogView.findViewById(R.id.searchTLId);
        ListView languageList = dialogView.findViewById(R.id.recyclerViewTLId);
        TextView TranslatedText = dialogView.findViewById(R.id.translatedTextId);
        TextView languageName = dialogView.findViewById(R.id.languageNameId);



        TranslatedText.setText(text);


        ArrayAdapter<String> translatorLanguageAdapter = new ArrayAdapter<String>(SelfMadeConversation.this,
                R.layout.smc_list_layout,R.id.conversationNameId,
                translatorLanguageString);

        languageList.setAdapter(translatorLanguageAdapter);


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Filter array

                translatorLanguageAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        languageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                translatorLanguage = translatorLanguageAdapter.getItem(position);

                Toast.makeText(SelfMadeConversation.this,translatorLanguage , Toast.LENGTH_SHORT).show();



                switch (translatorLanguage) {
                    case "English": {

                        TranslateAPI translateAPI = new TranslateAPI(
                                Language.AUTO_DETECT,   //Source Language
                                Language.ENGLISH,         //Target Language
                                text);           //Query Text


                        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                            @Override
                            public void onSuccess(String translatedText) {


                                TranslatedText.setText(translatedText);
                                languageName.setText(translatorLanguage);


                            }

                            @Override
                            public void onFailure(String ErrorText) {

                            }
                        });

                        break;
                    }
                    case "Germany": {

                        TranslateAPI translateAPI = new TranslateAPI(
                                Language.AUTO_DETECT,   //Source Language
                                Language.GERMAN,         //Target Language
                                text);           //Query Text


                        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                            @Override
                            public void onSuccess(String translatedText) {


                                TranslatedText.setText(translatedText);
                                languageName.setText(translatorLanguage);



                            }

                            @Override
                            public void onFailure(String ErrorText) {

                            }
                        });

                        break;
                    }
                    case "Bengali": {

                        TranslateAPI translateAPI = new TranslateAPI(
                                Language.AUTO_DETECT,   //Source Language
                                Language.BENGALI,         //Target Language
                                text);           //Query Text


                        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
                            @Override
                            public void onSuccess(String translatedText) {


                                TranslatedText.setText(translatedText);
                                languageName.setText(translatorLanguage);


                            }

                            @Override
                            public void onFailure(String ErrorText) {

                            }
                        });
                        break;
                    }
                }

            }
        });




        alertDialog.show();




    }
}