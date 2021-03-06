package com.example.kevin.walkhealthy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    //views and widget fields to assign id's
    Button createUser, moveToLogin;
    EditText userEmailEdit, userPasswordEdit;

    //Will create a progress loader when registering users
    private ProgressDialog progressDialog;

    //Firebase authentication fields
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mDatabaseRef, mUserCheckData;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);

        //Assign ID's
        createUser = (Button) findViewById(R.id.createButton);
        moveToLogin = (Button) findViewById(R.id.moveToLogin);
        userEmailEdit = (EditText) findViewById(R.id.emailEditTextCreate);
        userPasswordEdit = (EditText) findViewById(R.id.passwordEditTextCreate);


        //Assign Instances

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mUserCheckData = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null)
                {

                    //startActivity(new Intent(MainActivity.this, WelcomeActivity.class));


                    final String email = user.getEmail();

                    mUserCheckData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            checkUserValidation(dataSnapshot, email);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
                else
                {

                }

            }
        };


        //Creating the onclick listeners

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String userEmailString, userPassString;

                userEmailString = userEmailEdit.getText().toString().trim();
                userPassString = userPasswordEdit.getText().toString().trim();

                if(!TextUtils.isEmpty(userEmailString) && !TextUtils.isEmpty(userPassString))
                {
                    progressDialog.setMessage("Registering User....");
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(userEmailString, userPassString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful())
                            {

                                DatabaseReference mChildDatabase = mDatabaseRef.child("Users").push();

                                String key_user = mChildDatabase.getKey();

                                mChildDatabase.child("isVerified").setValue("unverified");
                                mChildDatabase.child("userKey").setValue(key_user);
                                mChildDatabase.child("emailUser").setValue(userEmailString);
                                mChildDatabase.child("passWordUser").setValue(userPassString);


                                Toast.makeText(MainActivity.this, "User Account Created", Toast.LENGTH_LONG).show();
                                finish();

                                //Don't uncomment this, will crash program
                                //startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Failed to create the User Account", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }


            }
        });

        //Move to login onclick listener

        moveToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });







    }

    private void checkUserValidation(DataSnapshot dataSnapshot, String passedEmail)
    {

        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext())
        {

            DataSnapshot dataUser = (DataSnapshot) iterator.next();

            if (dataUser.child("emailUser").getValue().toString().equals(passedEmail))
            {
                if (dataUser.child("isVerified").getValue().toString().equals("unverified"))
                {
                    Intent in = new Intent(MainActivity.this, ProfileActivity.class);
                    in.putExtra("USER_KEY", dataUser.child("userKey").getValue().toString());
                    startActivity(in);

                }
                else
                {
                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                }
            }

        }

    }



}
