package com.hackathon.accessiblemap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SenderOtpActivity extends AppCompatActivity {

    /* renamed from: db */
    private FirebaseDatabase f54db;
    private EditText inputOtp;
    private DatabaseReference reference;

    /* access modifiers changed from: protected */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_otp);
        this.inputOtp = (EditText) findViewById(R.id.input_otp);
    }

    public void verifyOtp(View view) {
        if (!TextUtils.isEmpty(this.inputOtp.getText().toString()) || TextUtils.isDigitsOnly(this.inputOtp.getText().toString()) || this.inputOtp.getText().toString().length() == 4) {
            final int otp = Integer.parseInt(String.valueOf(this.inputOtp.getText().toString()));
            this.f54db = FirebaseDatabase.getInstance();
            this.reference = this.f54db.getReference("rc");
            DatabaseReference databaseReference = this.reference;
            StringBuilder sb = new StringBuilder();
            String str = "otp";
            sb.append(str);
            sb.append(otp);
            this.reference = databaseReference.child(sb.toString());
            this.reference = this.reference.child(str);
            this.reference.addValueEventListener(new ValueEventListener() {
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        SenderOtpActivity.this.startSender(otp);
                    }
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            });
            return;
        }
        Toast.makeText(this, "Please enter valid OTP", 0).show();
    }

    public void startSender(int otp) {
        Intent i = new Intent(this, ContinuousLocationActivity.class);
        i.putExtra("otp_verified", otp);
        startActivity(i);
    }
}
