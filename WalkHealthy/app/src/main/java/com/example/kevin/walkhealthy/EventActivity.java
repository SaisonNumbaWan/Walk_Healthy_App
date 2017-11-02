package com.example.kevin.walkhealthy;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
    public int clickCount;
    public String timeStart;
    public String duration;
    public String intensity;
    private ListView mDrawerList;
    private ListView mItemList;
    private ArrayAdapter<String> mAdapter;
    private ArrayAdapter<String> dsAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        //Assign IDs
        mDrawerList = (ListView)findViewById(R.id.navList);
        mItemList = (ListView)findViewById(R.id.eventList);
        addDrawerItems();
        final DrawerLayout mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        final Button searchRefinement = (Button) findViewById(R.id.searchRefinement);
        searchRefinement.setVisibility(View.INVISIBLE);
        final Button searchEventBtn = (Button) findViewById(R.id.btnSearchEvent);
        final Button createEventBtn = (Button) findViewById(R.id.btnCreateEvent);
        final TextView eventNameView = (TextView)findViewById(R.id.eventNameTextView);
        final TextView eventStartView = (TextView) findViewById(R.id.eventStartingLocationTextView);
        final TextView eventDateView = (TextView)findViewById(R.id.eventDateTextView);
        final EditText editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        String strLocation = editTextLocation.getText().toString();


        searchEventBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //get the location to search
                createEventBtn.setVisibility(View.INVISIBLE);
                editTextLocation.setVisibility(View.INVISIBLE);
                searchRefinement.setVisibility(View.VISIBLE);

                final String strLocation = editTextLocation.getText().toString();


                mDataRef = FirebaseDatabase.getInstance().getReference().child("Event");

                mDataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        addEventItem(dataSnapshot, strLocation);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        searchRefinement.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mDrawerLayout.openDrawer(Gravity.START);
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

    private void addEventItem(DataSnapshot dataSnapshot, String strLocation){
        ArrayList<String> eventItems = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            if(ds.child("EventStartingLocation").getValue().equals(strLocation))
            {
                eventItems.add(ds.child("EventName").getValue().toString() + "\n" +
                        "Starts At: "+ds.child("EventStartingLocation").getValue().toString() + "\n" +
                        "On: "+ds.child("EventMonth").getValue().toString()+", "+ds.child("EventDay").getValue().toString() + "\n");
            }
        }
        dsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventItems);
        mItemList.setAdapter(dsAdapter);
    }
    private void addDrawerItems(){
        String[] filterNames = {"Duration", "Time", "Intensity"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, filterNames);
        mDrawerList.setAdapter(mAdapter);
    }
}
