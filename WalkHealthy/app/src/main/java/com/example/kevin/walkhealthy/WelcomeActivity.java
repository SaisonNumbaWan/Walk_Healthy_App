package com.example.kevin.walkhealthy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeActivity extends AppCompatActivity {

    //Declare Fields
    Button logoutBtn;
    TextView textViewUserEmail;

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

        //Assign Instances
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                user = firebaseAuth.getCurrentUser();

                if (user != null)
                {
                    textViewUserEmail.setText("Welcome " + user.getEmail());
                    databaseReference = FirebaseDatabase.getInstance().getReference();

                    //finish();
                    //startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }
                else
                {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                }






            }
        };



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

}
