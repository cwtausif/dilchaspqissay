package com.glowingsoft.dilchaspqissay;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView tausifEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_about);
        tausifEmail = (ImageView) findViewById(R.id.tausifEmail);
        tausifEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tausifEmail:
                openGmail("cwtausif@gmail.com");
                break;
        }
    }

    private void openGmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hi Tausif");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "I want to say some thing.");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
