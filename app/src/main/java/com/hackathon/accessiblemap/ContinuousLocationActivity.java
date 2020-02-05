package com.hackathon.accessiblemap;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContinuousLocationActivity extends AppCompatActivity {

    /* renamed from: db */
    private FirebaseDatabase f46db;
    private GoogleApiClient gClient;

    /* renamed from: lc */
    private LocationCallback f47lc;
    /* access modifiers changed from: private */
    public int otp;
    /* access modifiers changed from: private */
    public String[] permissions = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_BACKGROUND_LOCATION"};
    /* access modifiers changed from: private */
    public DatabaseReference reference;
    private boolean started = false;


    /* access modifiers changed from: protected */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_location);
        this.f46db = FirebaseDatabase.getInstance();
        this.otp = getIntent().getIntExtra("otp_verified", 0);
        this.reference = this.f46db.getReference("rc");
        DatabaseReference databaseReference = this.reference;
        StringBuilder sb = new StringBuilder();
        sb.append("otp");
        sb.append(this.otp);
        this.reference = databaseReference.child(sb.toString());
        this.gClient = new Builder(this).addApi(LocationServices.API).addConnectionCallbacks(new ConnectionCallbacks() {
            public void onConnected(Bundle bundle) {
                ContinuousLocationActivity continuousLocationActivity = ContinuousLocationActivity.this;
                if (ContextCompat.checkSelfPermission(continuousLocationActivity, continuousLocationActivity.permissions[0]) != 0) {
                    ContinuousLocationActivity continuousLocationActivity2 = ContinuousLocationActivity.this;
                    continuousLocationActivity2.requestPermissions(continuousLocationActivity2.permissions, 0);
                }
            }

            public void onConnectionSuspended(int i) {
            }
        }).build();
        this.f47lc = new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        DatabaseReference access$200 = ContinuousLocationActivity.this.reference;
                        StringBuilder sb = new StringBuilder();
                        sb.append("lat");
                        sb.append(ContinuousLocationActivity.this.otp);
                        access$200.child(sb.toString()).setValue(String.valueOf(location.getLatitude()));
                        DatabaseReference access$2002 = ContinuousLocationActivity.this.reference;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("lng");
                        sb2.append(ContinuousLocationActivity.this.otp);
                        access$2002.child(sb2.toString()).setValue(String.valueOf(location.getLongitude()));
                    }
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    protected void onStart() {
        super.onStart();
        this.gClient.connect();
    }

    /* access modifiers changed from: protected */
    protected void onStop() {
        super.onStop();
        LocationServices.FusedLocationApi.removeLocationUpdates(this.gClient, this.f47lc);
        this.gClient.disconnect();
    }

    public void retrieveLocation(View view) {
        if (ContextCompat.checkSelfPermission(this, this.permissions[0]) != 0) {
            Toast.makeText(this, "Please grant location permission and try again.", 0).show();
            requestPermissions(this.permissions, 0);
            return;
        }
        LocationRequest lRequest = LocationRequest.create();
        lRequest.setPriority(100);
        lRequest.setFastestInterval(1000);
        lRequest.setInterval(1000);
        if (LocationServices.FusedLocationApi.requestLocationUpdates(this.gClient, lRequest, this.f47lc, getMainLooper()) != null && !this.started) {
            Toast.makeText(this, "Started location sharing.", 0).show();
            this.started = true;
        }
    }

    public void deleteAllData(View view) {
        if (this.f46db.getReference().getRoot().removeValue() != null) {
            Toast.makeText(this, "All data has been deleted", 0).show();
        } else {
            Toast.makeText(this, "Problem in deleting all the data.", 0).show();
        }
    }

    public void stopShare(View view) {
        if (!this.started) {
            Toast.makeText(this, "Location sharing not started yet. cant able to stop.", 0).show();
            return;
        }
        LocationServices.FusedLocationApi.removeLocationUpdates(this.gClient, this.f47lc);
        DatabaseReference reference2 = this.f46db.getReference("rc");
        StringBuilder sb = new StringBuilder();
        sb.append("otp");
        sb.append(this.otp);
        reference2.child(sb.toString()).removeValue().addOnCompleteListener(new OnCompleteListener() {
            public void onComplete(Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ContinuousLocationActivity.this, "Location sharing has been stopped successfully", 0).show();
                } else {
                    Toast.makeText(ContinuousLocationActivity.this, "Problem in stopping the live location.", 0).show();
                }
            }
        });
        this.started = false;
        finish();
    }
}
