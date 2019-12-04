package com.hackathon.accessiblemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ContinuousLocationActivity extends AppCompatActivity {
        private GoogleApiClient gClient;
private FirebaseAuth auth;
private EditText email,password;
private FirebaseDatabase db;
private String[] permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_location);
        db=FirebaseDatabase.getInstance();
        reference=db.getReference();
gClient=new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if(ContextCompat.checkSelfPermission(ContinuousLocationActivity.this,permissions[0])!= PackageManager.PERMISSION_GRANTED)
                requestPermissions(permissions,0);
            }

            @Override
            public void onConnectionSuspended(int i) {
            }
        }).build();





            }

    @Override
    protected void onStart() {
        super.onStart();
        gClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
         gClient.disconnect();
    }

    public void retrieveLocation(View view) {
        if (ContextCompat.checkSelfPermission(this,permissions[0])!=PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this, "Please grant location permission and try again.", Toast.LENGTH_SHORT).show();
            requestPermissions(permissions,0);
            return;
        }

LocationRequest lRequest=LocationRequest.create();
        lRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        lRequest.setNumUpdates(1);
        lRequest.setInterval(0);
        LocationServices.FusedLocationApi.requestLocationUpdates(gClient, lRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Geocoder geocoder=new Geocoder(ContinuousLocationActivity.this, Locale.getDefault());
                try
                {
                    List<Address> addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Address address=addresses.get(0);
                    String exactAddress=address.getAddressLine(0);
                    if (exactAddress!=null)
                                                                                reference.setValue("addresses");
                                        reference.child("addresses").setValue(exactAddress);
                    Toast.makeText(ContinuousLocationActivity.this, "Kept address in database.", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(ContinuousLocationActivity.this, "Cant fetch location. sorry.", Toast.LENGTH_SHORT).show();
                }
                            }
        });
    }


    public void deleteAllData(View view) {
        DatabaseReference reference=db.getReference();
        reference.getRoot().removeValue();
    }


}

