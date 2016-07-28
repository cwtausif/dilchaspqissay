package com.glowingsoft.dilchaspqissay.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.glowingsoft.dilchaspqissay.R;
import com.glowingsoft.dilchaspqissay.model.CustomModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mg on 6/18/2016.
 */
public class CustomTitlesAdapter extends BaseAdapter {
    Context context;
    ArrayList<CustomModel> titleList;
    LayoutInflater inflater;
    CustomModel customModel;
    int lastPosition;
    public HashMap<Integer,Integer> myList;
    public CustomTitlesAdapter(Context applicationContext, ArrayList<CustomModel> titelsList) {
        this.context = applicationContext;
        this.titleList=titelsList;
        myList=new HashMap<Integer,Integer>();
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return titleList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.titlerow, null);

        TextView title = (TextView) convertView.findViewById(R.id.titlesTitle);
        ImageView titlesImage = (ImageView) convertView.findViewById(R.id.titlesImage);
        TextView counterTv = (TextView) convertView.findViewById(R.id.counterTv);
        int counter=1;
        int pos =position+1;
        if(position<9){
            counterTv.setText("0"+pos);
        }else{
            counterTv.setText(""+pos);
        }

        try {
            int[] ourimage = new int[]{R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,R.drawable.f,R.drawable.g,R.drawable.h,R.drawable.i,R.drawable.j,R.drawable.k,R.drawable.l,R.drawable.m,R.drawable.n,R.drawable.o,R.drawable.p,R.drawable.q,R.drawable.r,R.drawable.s,R.drawable.t,R.drawable.u,R.drawable.v,R.drawable.w,R.drawable.x,R.drawable.y,R.drawable.z};
            int id = ourimage[(int) (Math.random() * 25)];
            titlesImage.setImageResource(Integer.parseInt(String.valueOf(id)));
        }catch (Exception e){
            e.printStackTrace();
        }
        if(lastPosition>position) {
            Animation bottomUp = AnimationUtils.loadAnimation(context, R.anim.up_to_bottom);
            convertView.startAnimation(bottomUp);
        }else {
            Animation bottomdown = AnimationUtils.loadAnimation(context, R.anim.bottom_to_up);
            convertView.startAnimation(bottomdown);
        }
        lastPosition = position;
        CustomModel data = titleList.get(position);
        String titleText = data.getTitle();
        if(titleText.length()>20){
            titleText = titleText.substring(0,20)+"...";
        }
        title.setText(titleText);

        //Store Story Id against position
        myList.put(position,data.getTitle_id());
        Log.d("Adap","Position: "+position+" ID: "+data.getTitle_id());

        return convertView;
    }
}
