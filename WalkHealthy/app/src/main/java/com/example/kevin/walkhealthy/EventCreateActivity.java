package com.example.kevin.walkhealthy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventCreateActivity extends AppCompatActivity {

    DatabaseReference mDataRef;

    EditText eventDay, eventMonth, eventName, eventStartingLocation, eventEndingLocation;
    Button eventCreateBtn;
    String eDay, eMonth, eName, eStart, eEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);


        //Assign ID's
        eventDay = (EditText) findViewById(R.id.txtEventDay);
        eventMonth = (EditText)findViewById(R.id.txtEventMonth);
        eventName = (EditText) findViewById(R.id.txtEventName);
        eventStartingLocation = (EditText)findViewById(R.id.txtEventStartingLocation);
        eventEndingLocation = (EditText)findViewById(R.id.txtEventEndingLocation);
        eventCreateBtn = (Button) findViewById(R.id.btnCreateEvent);


        mDataRef = FirebaseDatabase.getInstance().getReference().child("Event");


        eventCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eDay = eventDay.getText().toString().trim();
                eMonth = eventMonth.getText().toString().trim();
                eName = eventName.getText().toString().trim();
                eStart = eventStartingLocation.getText().toString().trim();
                eEnd = eventEndingLocation.getText().toString().trim();

                if(!TextUtils.isEmpty(eDay) && !TextUtils.isEmpty(eMonth) && !TextUtils.isEmpty(eName) && !TextUtils.isEmpty(eStart))
                {
                    DatabaseReference mChildDatabase = mDataRef.push();
                    mChildDatabase.child("EventDay").setValue(eDay);
                    mChildDatabase.child("EventMonth").setValue(eMonth);
                    mChildDatabase.child("EventName").setValue(eName);
                    mChildDatabase.child("EventStartingLocation").setValue(eStart);

                    if(!TextUtils.isEmpty(eEnd)){
                        mChildDatabase.child("EventEndingLocation").setValue(eEnd);
                    }
                    else
                        mChildDatabase.child("EventEndingLocation").setValue("N/A");

                    Toast.makeText(EventCreateActivity.this, "Event Created", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(EventCreateActivity.this, EventActivity.class));
                }
            }
        });
        //Create Event Button Logic
        /*
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!TextUtils.isEmpty(eDay) && !TextUtils.isEmpty(eMonth) && !TextUtils.isEmpty(eName) && !TextUtils.isEmpty(eStart))
                {
                    DatabaseReference mChildDatabase = mDataRef.child("Event").push();
                    mChildDatabase.child("EventDay").setValue(eDay);
                    mChildDatabase.child("EventMonth").setValue(eMonth);
                    mChildDatabase.child("EventName").setValue(eName);
                    mChildDatabase.child("EventStartingLocation").setValue(eStart);

                    if(!TextUtils.isEmpty(eEnd)){
                        mChildDatabase.child("EventEndingLocation").setValue(eEnd);
                    }
                    else
                        mChildDatabase.child("EventEndingLocation").setValue("N/A");

                    Toast.makeText(EventCreateActivity.this, "Event Created", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(EventCreateActivity.this, EventActivity.class));
                }
            }
        });
        */

    }//onCreate
}//EventCreateActivity
