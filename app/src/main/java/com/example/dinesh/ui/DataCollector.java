package com.example.dinesh.ui;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class
        DataCollector extends Service    {

    public String operator = "", TAG = "DataCollector";
    public int cellID = 0, rssi = 0, count=0, flag=100000, dataArraySizeThresh = 20;
    public static double GPSLat, GPSLong, GSMLat, GSMLong;
    public double x=0, y=0, z=0;
    public long tsLoc;
    public SensorManager sensormanager;
    public TelephonyManager tm,tm2;
    public int seconds=0,a=0;
    public String[] stn = new String[0];
    public LocationManager GPSmgr;
    public LocationManager locationManagerNET;
    @Override
    public void onCreate() {
        super.onCreate();



        InputStream is;
        is = getResources().openRawResource(R.raw.journey);

        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        seconds=0;
        a=0;
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
               // Log.d("run", String.valueOf(seconds));

                try {
                    String csvLine;
                    if(a==0){
                        if ((csvLine = reader.readLine()) != null){
                            stn = csvLine.split(",");
                            if(Integer.parseInt(stn[0])==seconds){
                               // Log.d("seconds", String.valueOf(seconds));
                                Data.location.add(new Data(stn[1],Integer.parseInt(stn[2]),Integer.parseInt(stn[3]),Double.parseDouble(stn[4]),Double.parseDouble(stn[5]),tsLoc,Double.parseDouble(stn[6]),Double.parseDouble(stn[7])));
                            }
                            else a=1;
                        }
                    }
                    else{
                        if(Integer.parseInt(stn[0])==seconds){
                           // Log.d("seconds", String.valueOf(seconds));
                            Data.location.add(new Data(stn[1],Integer.parseInt(stn[2]),Integer.parseInt(stn[3]),Double.parseDouble(stn[4]),Double.parseDouble(stn[5]),tsLoc,Double.parseDouble(stn[6]),Double.parseDouble(stn[7])));
                            a=0;
                        }

                    }
                    seconds=seconds+1;
                    /*if ((csvLine = reader.readLine()) != null) {
                        Log.d("csvline",csvLine);
                        String[] stn = csvLine.split(",");
                        if(Integer.parseInt(stn[0])==seconds){
                            Data.location.add(new Data(stn[1],Integer.parseInt(stn[2]),Integer.parseInt(stn[3]),Double.parseDouble(stn[4]),Double.parseDouble(stn[5]),tsLoc,Double.parseDouble(stn[6]),Double.parseDouble(stn[7])));
                        }
                        //Thread.sleep(1000);
                        seconds=seconds+1;
                    }*/
                } catch (IOException ex) {
                    Toast.makeText(getApplicationContext(), "Error in database file loading!!!", Toast.LENGTH_SHORT).show();
                }
            }
        };

        timer.schedule(hourlyTask, 0l, 1000);

    }




    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        final Analyzer analyze = new Analyzer(this);
        final int timeInterval = 60000;

        /** Thread to :
         ***** Reorient Data every minute
         ***** Send data for "Analysis" every 5 mins
         *************************************************/



        final Thread report = new Thread(){
            @Override
            public void run() {
            while(flag-->0) {
                try {
                    Thread.sleep(timeInterval);
                } catch (InterruptedException e) { Log.d(TAG, "Thread Timer Error!!!"); }


                /****** Send data for analysis every 5 mins ******/
                if(++count%1==0)    {
                   // Log.d("Inside","1 minute");
                    if(analyze.getGpsFromGsm()) {
                        Data.location.clear();
                        analyze.isOnTrain();
                    }
                    else    {
//                        Log.d(TAG, "Not in train!!!!!");
                    }
                }
            }
            }
        };

        /********* Start the thread ***********/
        report.start();

        return super.onStartCommand(intent, flags, startId);
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}