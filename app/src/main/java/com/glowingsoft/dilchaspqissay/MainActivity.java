package com.glowingsoft.dilchaspqissay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.glowingsoft.dilchaspqissay.adapter.CustomTitlesAdapter;
import com.glowingsoft.dilchaspqissay.database.DBHandler;
import com.glowingsoft.dilchaspqissay.database.TinyDB;
import com.glowingsoft.dilchaspqissay.global.GlobalConfig;
import com.glowingsoft.dilchaspqissay.model.CustomModel;
import com.glowingsoft.dilchaspqissay.model.DetailActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Context context;
    GlobalConfig globalConfig;
    String url,TAG="Main";
    ListView titlesListview;
    ArrayList<CustomModel> titelsList,detailsList;
    CustomModel customModel;
    CustomTitlesAdapter customTitlesAdapter;
    DBHandler dbHandler;
    TinyDB tinyDB;
    ProgressDialog pDialog;
    //ImageButton shareBtn;
    InterstitialAd mInterstitialAd;
    View footerView;
    RequestQueue queue;
    int category;
    boolean first = false; //Check this category data already exists
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //Receive Intent Value for Menu
        Intent intent = getIntent();
        category = intent.getIntExtra("category",1);
        Log.d("category: ", String.valueOf(category));

        /*
        1. Hamary Bazurg
        2. Sabaq Amoz
        3. Islami Waqyat
        4. Hamara Muashra
        5. Safar Nama
        6. App Beeti
         */


        Log.d("Main: ","oncreate");

        context = getApplicationContext();
        globalConfig = new GlobalConfig();
        getViews();
        titelsList = new ArrayList<>();
        detailsList = new ArrayList<>();
        dbHandler = new DBHandler(context);


        //code to set adapter to populate list
        footerView =  ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.listview_footer, null, false);
        titlesListview.addFooterView(footerView,null,false);
        TextView morestories = (TextView) footerView.findViewById(R.id.morestories);
        morestories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadMore();
            }
        });

        //Fetch data from Local Database
        loadLocalData(category);



        //Add work
      //  mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
      //  mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        //region Test Add
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                // Check the LogCat to get your test device ID
//                .addTestDevice("9EE8A96E491740F1636B15A84C6E2386")
//                .build();

        //endregion


        // Load ads into Interstitial Ads

//
//        mInterstitialAd.loadAd(adRequest);
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                showInterstitial();
//            }
//        });

        //Onclick Listener Listview
        titlesListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 int story_id = customTitlesAdapter.myList.get(position);
                //Toast.makeText(getApplicationContext(),"storyID: "+pos,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("story",story_id);
                startActivity(intent);
            }
        });



    }
    private void downloadMore(){
        titelsList = dbHandler.getAllStories(category);
        CustomModel custommod = titelsList.get(titelsList.size()-1);
        int id = custommod.getTitle_id();
        theServerRequest(1,id,category,id);
    }

    private void theServerRequest(int more,int id,int category,int datasize) {

        String storyurl  =globalConfig.STORY_URL;
        //add category
        storyurl = storyurl+"?category="+category;
        if (more==1){
            storyurl = storyurl+"&id="+id;
        }
        queue = Volley.newRequestQueue(this);
        serverRequest(storyurl,more,datasize);
    }

    private void loadLocalData(int category) {
        Log.d("Main loadlocaldata:", String.valueOf(category));
        titelsList = dbHandler.getAllStories(category);
        int size = titelsList.size();
        if (titelsList.size()==0){
             /*
            @param 1: Is it more request
            @param 2: Maximum Id for more request
            @param 3: Story category
             */
            theServerRequest(0,0,category,titelsList.size());
            return;
        }
        customTitlesAdapter = new CustomTitlesAdapter(getApplicationContext(),titelsList);
        titlesListview.setAdapter(customTitlesAdapter);
    }

    private void getViews() {
        titlesListview = (ListView) findViewById(R.id.titlesList);
       // shareBtn = (ImageButton) findViewById(R.id.shareBtn);

    }

    private void serverRequest(String url, final int more, final int datasize) {
        // Instantiate the RequestQueue.
        Log.d("url: ",url);

        //check network
        if(isNetworkAvailable()==false){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Network Problem")
                    .setCancelable(false)
                    .setMessage("Internet connection is not available")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (datasize==0){
                            finish();
                            }
                        }
                    })
                    .show();
                    return;
        }

        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Fetching data..");
        pDialog.setCancelable(false);
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        if (response.length()<1){
                            Snackbar.make(getWindow().getDecorView().getRootView(), "No more stories", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            titlesListview.removeFooterView(footerView);
                        }

                        for (int i=0; i<response.length(); i++){
                            try {
                                customModel = new CustomModel();
                                JSONArray person = (JSONArray) response.get(i);
                                customModel.setTitle_id(person.getInt(0));
                                customModel.setTitle(person.getString(1));
                                customModel.setCategory(person.getInt(2));
                                customModel.setContent_id(person.getInt(3));
                                customModel.setContent(person.getString(4));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            titelsList.add(customModel);
                            dbHandler.addStory(customModel);
                            dbHandler.adddetail(customModel);
                        }
                        //now pass data to adaper
                        customTitlesAdapter = new CustomTitlesAdapter(getApplicationContext(),titelsList);
                        titlesListview.setAdapter(customTitlesAdapter);
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Network Problem", Toast.LENGTH_SHORT).show();
                if (more==0){
                    finish();
                }

            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


}
