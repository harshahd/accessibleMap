package com.hackathon.accessiblemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReceiverActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    private DatabaseReference reference;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
            db=FirebaseDatabase.getInstance();
            reference=db.getReference();
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Toast.makeText(ReceiverActivity.this, "Location: "+dataSnapshot.getValue().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(ReceiverActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
    }
});
        }



    public void retrieveLocation(View view) {
            DatabaseReference reference=this.reference;
        Toast.makeText(this, " "+reference.child("addresses").toString(), Toast.LENGTH_SHORT).show();
    }

    }

