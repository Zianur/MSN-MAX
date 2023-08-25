package com.zerotwotalkingapp;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SMCList extends AppCompatActivity implements SMCAdapter.OnRecyclerViewClickListener {

    private RecyclerView recyclerView;
    private ArrayList<SMCModule> smcModuleArrayList;
    private SMCAdapter smcAdapter;
    LinearLayout linearLayout;
    TextView addNewButton;


    private DatabaseReference User, DatabaseRef;
    FirebaseAuth mAuth;
    String CurrentUser, CName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_m_c_list);


        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        User = FirebaseDatabase.getInstance().getReference().child("User").child(CurrentUser);



        linearLayout = findViewById(R.id.LLSMCId);
        addNewButton = findViewById(R.id.addButtonId);
        recyclerView = findViewById(R.id.recyclerViewId);





        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        smcModuleArrayList = new ArrayList<>();


        ShowList();


        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent CIntent = new Intent(SMCList.this,ContributionSMC.class);
                CIntent.putExtra("key","add new");
                startActivity(CIntent);

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent homeIntent = new Intent(SMCList.this, HomeActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
        finish();
    }






    private void ShowList() {

        ProgressDialog progress = new ProgressDialog(SMCList.this);
        progress.setTitle("Working on it......");
        progress.show();


        User.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists())
                {

                    linearLayout.setVisibility(View.VISIBLE);

                    smcModuleArrayList.clear();


                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                        SMCModule smcModule = new SMCModule();

                        smcModule.setNodeKey(dataSnapshot.getKey());


                        smcModuleArrayList.add(smcModule);

                    }


                    progress.cancel();


                    smcAdapter = new SMCAdapter(SMCList.this, smcModuleArrayList);
                    recyclerView.setAdapter(smcAdapter);
                    smcAdapter.notifyDataSetChanged();
                    smcAdapter.setOnRecyclerViewClickListener(SMCList.this);

                }
                else
                {
                    linearLayout.setVisibility(View.INVISIBLE);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void OnRecyclerViewClick(int position) {


        SMCModule selected = smcModuleArrayList.get(position);

        CName = selected.getNodeKey();

        Intent SMCIntent = new Intent(SMCList.this,SelfMadeConversation.class);
        SMCIntent.putExtra("key",CName);
        startActivity(SMCIntent);

    }
}