package com.example.dinesh.ui;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
import android.content.pm.PackageManager;

public class
        DataCollector extends Service implements SensorEventListener   {

    public String operator = "", TAG = "DataCollector";
    public int cellID = 0, rssi = 0, count=0, flag=100000, dataArraySizeThresh = 20;
    public static double GPSLat, GPSLong, GSMLat, GSMLong;
    public double x=0, y=0, z=0;
    public long tsLoc;
    public SensorManager sensormanager;
    public TelephonyManager tm,tm2;
    public MyPhoneStateListener MyListener;
    public LocationManager GPSmgr;
    public LocationManager locationManagerNET;
    @Override
    public void onCreate() {
        super.onCreate();

        /****** Initialise variables required to collect GSM Information ******/
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //tm2 = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        MyListener = new MyPhoneStateListener();
        tm.listen(MyListener, MyPhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        //tm2.listen(MyListener, MyPhoneStateListener.LISTEN_CELL_LOCATION);

        // DKM //

        locationManagerNET = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        locationManagerNET.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNET);

        // ----- //

        /****** Initialise & register variables required to collect Motion Information ******/
        sensormanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensormanager.registerListener(this,
                sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);

        /***** Code to register location manager for GPS *******/

        GPSmgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        GPSmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, GPSlistener);
    }

    LocationListener GPSlistener=new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onLocationChanged(Location location) {
            GPSLat=location.getLatitude();
            GPSLong=location.getLongitude();
//            Log.d(TAG, "onLocationChanged: "+GPSLat+","+GPSLong);
        }
    };

    LocationListener locationListenerNET=new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onLocationChanged(Location location) {
            GSMLat=location.getLatitude();
            GSMLong=location.getLongitude();

//           Log.d(TAG, "onLocationChanged: "+GSMLat+","+GSMLong);
        }
    };

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

                /****** Reorient Accelerometer Data Every minute *******/
                analyze.reorient(MotionData.motion);
                MotionData.motion.clear();

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
                    MotionData.reorientedMotion.clear();
                }
            }
            }
        };

        /********* Start the thread ***********/
        report.start();

        return super.onStartCommand(intent, flags, startId);
    }


    /****** Collects accelerometer (X,Y,Z) values only when it updates *******/
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            MotionData.motion.add(new MotionData(x, y, z));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}


    /****** Collects cellID and rssi values only when it updates *******/
    private class MyPhoneStateListener extends PhoneStateListener {
        GsmCellLocation loc;

      /* @Override
        public void onCellLocationChanged(CellLocation location) {
            super.onCellLocationChanged(location);
           loc = (GsmCellLocation) tm.getCellLocation();
           operator=tm.getNetworkOperatorName();
            try {
              cellID = loc.getCid() & 0xffff;
               tsLoc = System.currentTimeMillis()/1000;
               Data.location.add(new Data(operator,cellID,rssi,GPSLat,GPSLong,tsLoc,GSMLat,GSMLat));
                Log.d(TAG, "onCellLocationChanged: " + cellID + "," + rssi);
           }
           catch(NullPointerException ne) { cellID = 0; }
        }*/

        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            rssi=-113+2*signalStrength.getGsmSignalStrength();
            loc = (GsmCellLocation) tm.getCellLocation();
            operator=tm.getNetworkOperatorName();
            if(loc!=null) {
                cellID = loc.getCid() & 0xffff;
                Data.location.add(new Data(operator,cellID,rssi,GPSLat,GPSLong,tsLoc,GSMLat,GSMLat));
            }
            //tsLoc = System.currentTimeMillis()/1000;
//            Log.d(TAG, "SignalstrengthChanged: "+cellID+","+rssi);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}