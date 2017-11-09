package com.example.kevin.walkhealthy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Declare Fields
    Button logoutBtn;
    Button createGroupBtn;
    Button navigateToSearchBtn;
    TextView textViewUserEmail;
    Spinner spinner;

    //Spinner options
    private static final String[]spinnerOptions = {"Event Actions", "Create New Event", "Search for an Event", "Create a New Group"};

    //Firebase authentication fields
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Assign ID's
        logoutBtn = (Button) findViewById(R.id.logoutButton);
        textViewUserEmail = (TextView) findViewById(R.id.welcomeUserEmailTextView) ;



        //Deleting Buttons causes the app to crash. Set invisible instead
        createGroupBtn = findViewById(R.id.createGroupButton);
        navigateToSearchBtn = findViewById(R.id.btnNavigateToSearch);
        createGroupBtn.setVisibility(View.GONE);
        navigateToSearchBtn.setVisibility(View.GONE);



        //Spinner options
        spinner = findViewById(R.id.mainActivitySpinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(WelcomeActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Assign Instances
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                user = firebaseAuth.getCurrentUser();

                if (user != null)
                {
                    Toast.makeText(WelcomeActivity.this, "Welcome Back "+user.getEmail().toString(), Toast.LENGTH_SHORT).show();
                    textViewUserEmail.setText("Welcome " + user.getEmail().toString());
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                }
                else
                {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
            }
        };

        //Button to navigate to Search Event page


        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(WelcomeActivity.this, GroupActivity.class));
            }
        });

        navigateToSearchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(WelcomeActivity.this, EventActivity.class));
            }
        });




        //Set on click listener to logout
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                finish();
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch(i){
            case 0:
                break;
            case 1:
                startActivity(new Intent(WelcomeActivity.this, EventCreateActivity.class));
                break;
            case 2:
                startActivity(new Intent(WelcomeActivity.this, EventActivity.class));
                break;
            case 3:
                startActivity(new Intent(WelcomeActivity.this, GroupActivity.class));

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
