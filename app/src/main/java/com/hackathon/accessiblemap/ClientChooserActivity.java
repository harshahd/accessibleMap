package com.hackathon.accessiblemap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class ClientChooserActivity extends AppCompatActivity {
private RadioButton sender,receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_chooser);
        sender=findViewById(R.id.sender);
        receiver=findViewById(R.id.receiver);
sender.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener()
{
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            Intent i = new Intent(ClientChooserActivity.this, ContinuousLocationActivity.class);
            startActivity(i);
        }
    }
});
        receiver.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent i = new Intent(ClientChooserActivity.this, ReceiverActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}

