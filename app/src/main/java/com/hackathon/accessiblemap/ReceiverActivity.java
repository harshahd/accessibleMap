package com.hackathon.accessiblemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Locale;

public class ReceiverActivity extends AppCompatActivity implements LocationListener {
    private FirebaseDatabase db;
    private DatabaseReference reference;
    private TextView destination,distance;
    private Location presentLocation,targetLocation;
    private double lat=0.0,lng=0.0;
    private LocationManager lm;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        lm=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
                destination=findViewById(R.id.destination);
        distance=findViewById(R.id.distance);
            db=FirebaseDatabase.getInstance();
            reference=db.getReference("");
reference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lat=Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
        lng=Double.parseDouble(dataSnapshot.child("lng").getValue().toString());
                Geocoder geocoder=new Geocoder(ReceiverActivity.this, Locale.getDefault());
        try
        {
            List<Address> addresses=geocoder.getFromLocation(lat, lng, 1);
            Address address=addresses.get(0);
            String exactAddress=address.getAddressLine(0);
            if (exactAddress!=null) {
                targetLocation=new Location("");
                targetLocation.setLatitude(lat);
                targetLocation.setLongitude(lng);
                destination.setText(exactAddress);
                Toast.makeText(ReceiverActivity.this, "Location: " +exactAddress, Toast.LENGTH_SHORT).show();
                if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                {
                    lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, ReceiverActivity.this, Looper.getMainLooper());
                                    }
            }
                 }
        catch (Exception e)
        {
                        Toast.makeText(ReceiverActivity.this, "Cant fetch location. sorry.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Toast.makeText(ReceiverActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
    }
});
        }



    public void retrieveLocation(View view) {
            DatabaseReference reference=this.reference;
        Toast.makeText(this, "Not implemented yet ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        presentLocation=location;
        if (presentLocation!=null)
        {
            float meters=presentLocation.distanceTo(targetLocation);
distance.setText(meters+" meters.");
targetLocation=null;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

