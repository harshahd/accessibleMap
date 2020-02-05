package com.hackathon.accessiblemap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReceiveOtpActivity extends AppCompatActivity {

    /* renamed from: db */
    private FirebaseDatabase f50db;
    /* access modifiers changed from: private */

    /* renamed from: n */
    public int f51n = 0;
    private OtpManager otpManager;
    private TextView otpView;
    private DatabaseReference receiverReference;
    private DatabaseReference reference;

    /* access modifiers changed from: protected */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_otp);
        this.otpView = (TextView) findViewById(R.id.otp);
        this.f50db = FirebaseDatabase.getInstance();
        this.otpManager = new OtpManager();
    }

    public void createOtp(View view) {
        final int otp = this.otpManager.generateOtp();
        String str = "rc";
        this.reference = this.f50db.getReference(str);
        DatabaseReference databaseReference = this.reference;
        StringBuilder sb = new StringBuilder();
        String str2 = "otp";
        sb.append(str2);
        sb.append(otp);
        this.reference = databaseReference.child(sb.toString());
        DatabaseReference databaseReference2 = this.reference;
        StringBuilder sb2 = new StringBuilder();
        String str3 = "lat";
        sb2.append(str3);
        sb2.append(otp);
        DatabaseReference child = databaseReference2.child(sb2.toString());
        String str4 = "1234";
        child.setValue(str4);
        DatabaseReference databaseReference3 = this.reference;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("lng");
        sb3.append(otp);
        databaseReference3.child(sb3.toString()).setValue(str4);
        this.reference.child(str2).setValue(String.valueOf(otp));
        this.receiverReference = this.f50db.getReference(str);
        DatabaseReference databaseReference4 = this.receiverReference;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(str2);
        sb4.append(otp);
        this.receiverReference = databaseReference4.child(sb4.toString());
        DatabaseReference databaseReference5 = this.receiverReference;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(str3);
        sb5.append(otp);
        this.receiverReference = databaseReference5.child(sb5.toString());
        StringBuilder sb6 = new StringBuilder();
        sb6.append("OTP has been generated. OTP to share: ");
        sb6.append(otp);
        view.announceForAccessibility(sb6.toString());
        this.receiverReference.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (ReceiveOtpActivity.this.f51n == 1) {
                    ReceiveOtpActivity.this.startReciever(otp);
                }
                ReceiveOtpActivity.this.f51n = ReceiveOtpActivity.this.f51n + 1;
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        TextView textView = this.otpView;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("OTP: ");
        sb7.append(otp);
        textView.setText(sb7.toString());
    }

    public void startReciever(int otp) {
        Toast.makeText(this, "OTP has been verified.", 0).show();
        Intent i = new Intent(this, ReceiverActivity.class);
        i.putExtra("verified_otp", otp);
        startActivity(i);
    }
}
