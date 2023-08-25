package com.zerotwotalkingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sanojpunchihewa.updatemanager.UpdateManager;
import com.sanojpunchihewa.updatemanager.UpdateManagerConstant;

import java.util.ArrayList;



public class HomeActivity extends AppCompatActivity {

    CardView commandCard, searchCard, RConversationCard, adminCard, SMCCard, logoutCard;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar mtoolbar;


    UpdateManager mUpdateManager;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();


        commandCard = findViewById(R.id.voiceCommandId);
        searchCard = findViewById(R.id.vociceSearchId);
        RConversationCard = findViewById(R.id.randomconversationId);
        adminCard = findViewById(R.id.adminCardId);
        SMCCard = findViewById(R.id.smConversationId);
        logoutCard = findViewById(R.id.logoutCardId);


        drawerLayout =(DrawerLayout) findViewById(R.id.drawable_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mtoolbar = (Toolbar) findViewById(R.id.mainpage_bar);




        mUpdateManager = UpdateManager.Builder(this).mode(UpdateManagerConstant.FLEXIBLE);
        mUpdateManager.start();


        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .threshold(4)
                .session(3)
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {

                    }
                }).build();

        ratingDialog.show();



        //to use action bar toggle first we need to set actionbar
        //else we will get null pointer exception
        setSupportActionBar(mtoolbar); //setting actionbar
        getSupportActionBar().setTitle("MSN Max");//setting actionbar name


        //to use action bar toggle first we need to set actionbar
        //else we will get null pointer exception
        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Usermenuselector(item);
                return false;
            }
        });


        RConversationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent randomCIntent = new Intent(HomeActivity.this, RandomConversation.class);
                startActivity(randomCIntent);
            }
        });


        SMCCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent SMCIntent = new Intent(HomeActivity.this, LogIn.class);
//                SMCIntent.putExtra("key","add new");
                startActivity(SMCIntent);
            }
        });


        commandCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent commandIntent = new Intent(HomeActivity.this, MainActivity.class);
                commandIntent.putExtra("key", "command");
                startActivity(commandIntent);
            }
        });

        searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent searchCIntent = new Intent(HomeActivity.this, MainActivity.class);
                searchCIntent.putExtra("key", "search");
                startActivity(searchCIntent);
            }
        });

        adminCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                Intent searchCIntent = new Intent(HomeActivity.this,Admin.class);
//                startActivity(searchCIntent);
            }
        });

        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                logoutCard.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, "You have been logged out successfully", Toast.LENGTH_SHORT).show();

            }
        });

    }



    //MENU
    //Must Needed for ActionBarToggle to Work After Clicking
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null)
        {

            logoutCard.setVisibility(View.VISIBLE);

        }
        else
        {
            logoutCard.setVisibility(View.GONE);
        }
    }


    private void Usermenuselector(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.smcId:

                Intent SMCIntent = new Intent(HomeActivity.this, LogIn.class);
//                SMCIntent.putExtra("key","add new");
                startActivity(SMCIntent);

                break;

            case R.id.vcId:

                Intent commandIntent = new Intent(HomeActivity.this, MainActivity.class);
                commandIntent.putExtra("key", "command");
                startActivity(commandIntent);

                break;

            case R.id.rcId:

                Intent randomIntent = new Intent(HomeActivity.this, RandomConversation.class);
                randomIntent.putExtra("key", "command");
                startActivity(randomIntent);

                break;

            case R.id.contributeMenuId:

                Intent contributeIntent = new Intent(HomeActivity.this, Contribution.class);
                contributeIntent.putExtra("key", "Users");
                startActivity(contributeIntent);

                break;

            case R.id.rateMenuId:

//                Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + HomeActivity.this.getPackageName()));
//                startActivity(rateIntent);



                final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                        .threshold(4)
                        .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                            @Override
                            public void onFormSubmitted(String feedback) {

                            }
                        }).build();

                ratingDialog.show();

                break;

            case R.id.contactMenuId:

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:myworldappandweb@gmail.com")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, "myworldappandweb@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "contact");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;

        }

    }

}