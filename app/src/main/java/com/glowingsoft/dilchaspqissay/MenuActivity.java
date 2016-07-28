package com.glowingsoft.dilchaspqissay;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

/**
 * Created by mg on 6/23/2016.
 */
public class MenuActivity extends Activity implements View.OnClickListener{
    TextView hamaryBazurg,sabaqAmoz,islamiWaqyat,hamaraMuashra,safarNama,aapBeeti;
    FloatingActionButton shareBtn,feedbackBtn,aboutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        getViews();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hamaryBazurg:
                moveForward(MenuActivity.this,MainActivity.class,1);
                break;
            case R.id.sabaqAmoz:
                moveForward(MenuActivity.this,MainActivity.class,2);
                break;
            case R.id.islamiWaqyat:
                moveForward(MenuActivity.this,MainActivity.class,3);
                break;
            case R.id.hamaraMuashra:
                moveForward(MenuActivity.this,MainActivity.class,4);
                break;
            case R.id.safarNama:
                moveForward(MenuActivity.this,MainActivity.class,5);
                break;
            case R.id.aapBeeti:
                moveForward(MenuActivity.this,MainActivity.class,6);
                break;
            case R.id.share_app:
                shareIt();
                break;
            case R.id.feedback_app:
                feedBack(getApplicationContext());
                break;
            case R.id.about_us:
                moveForward(MenuActivity.this,AboutActivity.class);
                break;
        }
    }

    private void moveForward(Context context, Class<MainActivity> mainActivityClass, int category) {
        Intent intent = new Intent(context,mainActivityClass);
        intent.putExtra("category",category);
        startActivity(intent);
    }
    private void moveForward(Context context, Class<AboutActivity> mainActivityClass) {
        Intent intent = new Intent(context,mainActivityClass);
        startActivity(intent);
    }

    private void getViews() {
        hamaryBazurg = (TextView) findViewById(R.id.hamaryBazurg);
        sabaqAmoz = (TextView) findViewById(R.id.sabaqAmoz);
        islamiWaqyat = (TextView) findViewById(R.id.islamiWaqyat);
        hamaraMuashra = (TextView) findViewById(R.id.hamaraMuashra);
        safarNama = (TextView) findViewById(R.id.safarNama);
        aapBeeti = (TextView) findViewById(R.id.aapBeeti);

        shareBtn = (FloatingActionButton) findViewById(R.id.share_app);
        feedbackBtn = (FloatingActionButton) findViewById(R.id.feedback_app);
        aboutBtn = (FloatingActionButton) findViewById(R.id.about_us);


        hamaryBazurg.setOnClickListener(this);
        sabaqAmoz.setOnClickListener(this);
        islamiWaqyat.setOnClickListener(this);
        hamaraMuashra.setOnClickListener(this);
        safarNama.setOnClickListener(this);
        aapBeeti.setOnClickListener(this);

        shareBtn.setOnClickListener(this);
        feedbackBtn.setOnClickListener(this);
        aboutBtn.setOnClickListener(this);
    }
    public void shareIt(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Very amazing application for knowledge and fun\n");
        String sAux = "https://play.google.com/store/apps/details?id=com.glowingsoft.dilchaspqissay";
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        startActivity(Intent.createChooser(i, "Share Dilchasp Qissay"));
    }
    public void feedBack(Context context){
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }
}
