package com.example.weatherapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private Context context;
    private ArrayList<WeatherReportModel> arrayList;

    Calendar cal=Calendar.getInstance();
    SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
    String month_name = month_date.format(cal.getTime());




    public CustomAdapter(Context context, ArrayList<WeatherReportModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d(TAG,"onCreateViewHolder");

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.futureweatherreport,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Log.d(TAG,"OnBindViewHolderCalled");

        String d = month_name+context.getString(R.string.comma)+arrayList.get(position).getApplicable_date().substring(8);
        holder.day.setText(d);

        String maxtmp = context.getString(R.string.max)+(int)arrayList.get(position).getMax_temp()+context.getString(R.string.degreeSign);
        holder.maxTmp.setText(maxtmp);

        String mintmp = context.getString(R.string.min)+(int)arrayList.get(position).getMin_temp()+context.getString(R.string.degreeSign);
        holder.minTmp.setText(mintmp);

        holder.weatherStatus.setText(arrayList.get(position).getWeather_state_name());

        String abr = arrayList.get(position).getWeather_state_abbr();
        String url = "https://www.metaweather.com/static/img/weather/png/"+abr+".png";

        Glide.with(context).load(url).into(holder.weatherImage);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView day,minTmp,maxTmp,weatherStatus;
        ImageView weatherImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.futureDay);
            minTmp = itemView.findViewById(R.id.futureMinTemp);
            maxTmp = itemView.findViewById(R.id.futureMaxTemp);
            weatherImage = itemView.findViewById(R.id.futureWeatherImage);
            weatherStatus = itemView.findViewById(R.id.futureWeatherStatus);

        }
    }
}
