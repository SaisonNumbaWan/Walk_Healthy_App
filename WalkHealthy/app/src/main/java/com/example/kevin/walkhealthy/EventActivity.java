package com.example.kevin.walkhealthy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventActivity extends AppCompatActivity {

    private DatabaseReference mDataRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);



        //Assign IDs
        Button searchEventBtn = (Button) findViewById(R.id.btnSearchEvent);
        Button createEventBtn = (Button) findViewById(R.id.btnCreateEvent);
        final TextView eventNameView = (TextView)findViewById(R.id.eventNameTextView);
        final TextView eventStartView = (TextView) findViewById(R.id.eventStartingLocationTextView);
        final TextView eventDateView = (TextView)findViewById(R.id.eventDateTextView);
        final EditText editTextLocation = (EditText) findViewById(R.id.editTextLocation);



        String strLocation = editTextLocation.getText().toString();

        searchEventBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //get the location to search
                final String strLocation = editTextLocation.getText().toString();


                mDataRef = FirebaseDatabase.getInstance().getReference().child("Event");

                mDataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            //eventNameView.setText(dataSnapshot.getValue().toString());
                            for(DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                if(ds.child("EventStartingLocation").getValue().equals(strLocation))
                                {
                                    eventNameView.setText(ds.child("EventName").getValue().toString());
                                    eventStartView.setText("Starts At: "+ds.child("EventStartingLocation").getValue().toString());
                                    eventDateView.setText("On: "+ds.child("EventMonth").getValue().toString()+", "+ds.child("EventDay").getValue().toString());
                                }
                            }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






                /*addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       //eventNameView.setText(dataSnapshot.getKey());
                        Event event = dataSnapshot.getValue(Event.class);
                       eventNameView.setText(event.getEventName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

            }
        });

        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(EventActivity.this, MainActivity.class));
                startActivity(new Intent(EventActivity.this, EventCreateActivity.class));
            }
        });

    }//OnCreate
}
