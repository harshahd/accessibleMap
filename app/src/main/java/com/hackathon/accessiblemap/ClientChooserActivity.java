package com.hackathon.accessiblemap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;

public class ClientChooserActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public RadioButton receiver;
    /* access modifiers changed from: private */
    public RadioButton sender;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_chooser);
        this.sender = (RadioButton) findViewById(R.id.sender);
        this.receiver = (RadioButton) findViewById(R.id.receiver);
        this.sender.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ClientChooserActivity.this.sender.announceForAccessibility("Redirecting to sender. Please wait...");
                    ClientChooserActivity.this.startActivity(new Intent(ClientChooserActivity.this, SenderOtpActivity.class));
                }
            }
        });
        this.receiver.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ClientChooserActivity.this.receiver.announceForAccessibility("Redirecting to receiver. Please wait...");
                    ClientChooserActivity.this.startActivity(new Intent(ClientChooserActivity.this, ReceiveOtpActivity.class));
                }
            }
        });
    }
}
