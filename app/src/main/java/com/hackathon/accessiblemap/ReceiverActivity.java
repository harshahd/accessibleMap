package com.hackathon.accessiblemap;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Locale;

public class ReceiverActivity extends AppCompatActivity implements LocationListener {
    /* access modifiers changed from: private */
    public static final String[] permissions = {"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_BACKGROUND_LOCATION"};

    /* renamed from: db */
    private FirebaseDatabase f52db;
    /* access modifiers changed from: private */
    public TextView destination;
    private TextView distance;
    /* access modifiers changed from: private */
    public String exactAddress = "";
    private GoogleApiClient gClient;
    private TextView keyView;
    /* access modifiers changed from: private */
    public double lat = 0.0d;
    /* access modifiers changed from: private */

    /* renamed from: lm */
    public LocationManager f53lm;
    /* access modifiers changed from: private */
    public double lng = 0.0d;
    /* access modifiers changed from: private */
    public int otp;
    private Location presentLocation;
    private DatabaseReference reference;
    /* access modifiers changed from: private */
    public Location targetLocation;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        this.f53lm = (LocationManager) getSystemService("location");
        this.destination = (TextView) findViewById(R.id.destination);
        this.distance = (TextView) findViewById(R.id.distance);
        this.keyView = (TextView) findViewById(R.id.keyView);
        this.otp = getIntent().getIntExtra("verified_otp", 0);
        TextView textView = this.keyView;
        StringBuilder sb = new StringBuilder();
        sb.append("OTP to give for sender: ");
        sb.append(this.otp);
        textView.setText(sb.toString());
        this.f52db = FirebaseDatabase.getInstance();
        this.reference = this.f52db.getReference("rc");
        DatabaseReference databaseReference = this.reference;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("otp");
        sb2.append(this.otp);
        this.reference = databaseReference.child(sb2.toString());
        this.reference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String str = "network";
                StringBuilder sb = new StringBuilder();
                sb.append("lat");
                sb.append(ReceiverActivity.this.otp);
                Object latSnapshot = dataSnapshot.child(sb.toString()).getValue();
                StringBuilder sb2 = new StringBuilder();
                sb2.append("lng");
                sb2.append(ReceiverActivity.this.otp);
                Object lngSnapshot = dataSnapshot.child(sb2.toString()).getValue();
                if (latSnapshot == null && lngSnapshot == null) {
                    Toast.makeText(ReceiverActivity.this, "No location found", 0).show();
                    return;
                }
                Geocoder geocoder = new Geocoder(ReceiverActivity.this, Locale.getDefault());
                try {
                    ReceiverActivity.this.lat = Double.parseDouble(latSnapshot.toString());
                    ReceiverActivity.this.lng = Double.parseDouble(lngSnapshot.toString());
                    ReceiverActivity.this.exactAddress = ((Address) geocoder.getFromLocation(ReceiverActivity.this.lat, ReceiverActivity.this.lng, 1).get(0)).getAddressLine(0);
                    if (ReceiverActivity.this.exactAddress != null) {
                        ReceiverActivity.this.targetLocation = new Location("");
                        ReceiverActivity.this.targetLocation.setLatitude(ReceiverActivity.this.lat);
                        ReceiverActivity.this.targetLocation.setLongitude(ReceiverActivity.this.lng);
                        if (!ReceiverActivity.this.exactAddress.contains("not defined")) {
                            ReceiverActivity.this.destination.setText(ReceiverActivity.this.exactAddress);
                        }
                        if (ReceiverActivity.this.f53lm.isProviderEnabled(str)) {
                            try {
                                if (ContextCompat.checkSelfPermission(ReceiverActivity.this, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
                                    ReceiverActivity.this.requestPermissions(ReceiverActivity.permissions, 0);
                                }
                                ReceiverActivity.this.f53lm.requestSingleUpdate(str, ReceiverActivity.this, ReceiverActivity.this.getMainLooper());
                            } catch (Exception e) {
                                Toast.makeText(ReceiverActivity.this, "Cannot able to retrieve the distance. sorry.", 0).show();
                            }
                        }
                    }
                } catch (Exception e2) {
                    Toast.makeText(ReceiverActivity.this, "Cant fetch location. sorry. ", 0).show();
                }
            }

            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReceiverActivity.this, "Failed to retrieve data", 0).show();
            }
        });
    }

    public void retrieveLocation(View view) {
        StringBuilder sb = new StringBuilder();
        sb.append("Location: ");
        sb.append(this.exactAddress);
        Toast.makeText(this, sb.toString(), 0).show();
    }

    public void onLocationChanged(Location location) {
        this.presentLocation = location;
        Location location2 = this.presentLocation;
        if (location2 != null) {
            float meters = this.targetLocation.distanceTo(location2);
            if (((double) meters) > 1000.0d) {
                float meters2 = meters / 1000.0f;
                TextView textView = this.distance;
                StringBuilder sb = new StringBuilder();
                sb.append(meters2);
                sb.append(" Kilometers");
                textView.setText(sb.toString());
                return;
            }
            TextView textView2 = this.distance;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(meters);
            sb2.append(" meters.");
            textView2.setText(sb2.toString());
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }

    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double d = lat2;
        return 60.0d * rad2deg(Math.acos((Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(d))) + (Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(d)) * Math.cos(deg2rad(lon1 - lon2))))) * 1.1515d;
    }

    private double deg2rad(double deg) {
        return (3.141592653589793d * deg) / 180.0d;
    }

    private double rad2deg(double rad) {
        return (180.0d * rad) / 3.141592653589793d;
    }
}
