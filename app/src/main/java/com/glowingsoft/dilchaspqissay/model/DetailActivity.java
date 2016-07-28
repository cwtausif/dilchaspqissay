package com.glowingsoft.dilchaspqissay.model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.glowingsoft.dilchaspqissay.R;
import com.glowingsoft.dilchaspqissay.database.DBHandler;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class DetailActivity extends AppCompatActivity {
    CustomModel customModel;
    DBHandler dbHandler;
    TextView contentTv;
    ImageButton shareBtnStory;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_detail);
        dbHandler = new DBHandler(getApplicationContext());
        customModel = new CustomModel();
        contentTv = (TextView) findViewById(R.id.contentTextview);
        shareBtnStory = (ImageButton) findViewById(R.id.shareBtnStory);

        Animation bottomUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_to_up);
        contentTv.startAnimation(bottomUp);



        mAdView = (AdView) findViewById(R.id.adViewdetail);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                // Check the LogCat to get your test device ID
//                .addTestDevice("9EE8A96E491740F1636B15A84C6E2386")
//                .build();
       mAdView.loadAd(adRequest);


        shareBtnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        });

        Intent intent = getIntent();

        try {
            int story_id = intent.getIntExtra("story",0);
            Log.d("story_id: ",story_id+"");
            int thestory = story_id;
            if (thestory>0){
                fetchStoryContent(thestory);
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Network problem",Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void fetchStoryContent(int story_id) {
        customModel = dbHandler.getStoryContent(story_id);
        Log.d("content: ",+story_id+" "+customModel.getContent()+"");
        String content = customModel.getContent();
        contentTv.setMovementMethod(new ScrollingMovementMethod());
        contentTv.setText(content);
    }
    public void shareIt(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        String story =  contentTv.getText().toString();
        i.putExtra(Intent.EXTRA_SUBJECT,"Amazing Qissay");
        String sAux = "\n"+story+"\n\n";
        sAux = sAux + "https://play.google.com/store/apps/details?id=com.glowingsoft.dilchaspqissay \n\n";
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        startActivity(Intent.createChooser(i, "choose one"));

    }

}
