package com.example.dinesh.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner spinner;
    ImageButton query_button;
    MyView1 v1;
    MyView2 v2;
    LinearLayout layout1;
    LinearLayout layout2;
    public int route=0;
    public String[] Central;
    public String[] Harbour;
    public String[] Western;
    String results="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);

        v1 = new MyView1(this);
        layout1.addView(v1,500,4900);
        v2 = new MyView2(this);
        layout2.addView(v2,500,4900);

        spinner = (Spinner) findViewById(R.id.line_spinner);
        query_button = (ImageButton) findViewById(R.id.query);
        query_button.setOnClickListener(this);

        Resources res = getResources();
        Central = res.getStringArray(R.array.Central);
        Harbour = res.getStringArray(R.array.Harbour);
        Western = res.getStringArray(R.array.Western);

        /* add stations lat long to object of class StationMap */
        InputStream is;
        is = getResources().openRawResource(R.raw.stn);
        insertToStationMap(is);
        // ---------------------------------------------------- //

        // start fingerprint collection //
        this.startService(new Intent(this, DataCollector.class));
        Toast.makeText(getApplicationContext(), "Data Collection Started on Background!!!", Toast.LENGTH_SHORT).show();
        // ---------------------------- //
    }

    /* add stations lat long to object of class StationMap */
    private void insertToStationMap(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] stn = csvLine.split(",");
                StationMap.mapping.add(
                        new StationMap(
                                Integer.parseInt(stn[0]),
                                Integer.parseInt(stn[1]),
                                stn[2],
                                Double.parseDouble(stn[3]),
                                Double.parseDouble(stn[4]),
                                Double.parseDouble(stn[5]),
                                Double.parseDouble(stn[6])
                        )
                );
            }
        } catch (IOException ex) {
            Toast.makeText(getApplicationContext(), "Error in database file loading!!!", Toast.LENGTH_SHORT).show();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Error in database file loading!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
 /* ------------------------------------------------------------*/

    @Override
    public void onClick(View v) {


        //String sql = "SELECT * FROM " + DatabaseHandler.locationMAP;


        if(v.getId()==R.id.query){
            int spinner_pos = spinner.getSelectedItemPosition();
            ((LinearLayout)v1.getParent()).removeView(v1);
            ((LinearLayout)v2.getParent()).removeView(v2);
            route=spinner_pos;
            v1 = new MyView1(this);
            layout1.addView(v1, 500, 4900);
            v2 = new MyView2(this);
            layout2.addView(v2,500,4900);

        }

    }

    private class MyView1 extends View {
        public MyView1(Context context) {
            super(context);
        }
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (route == 1) {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                paint.setTextSize(15);
                canvas.drawLine(0, 5, 20, 5, paint);
                canvas.drawText(Central[0],25,12,paint);
                for (int i = 1; i <= 11; i++) {
                    canvas.drawLine(0, 200 * i, 20, 200 * i, paint);
                    canvas.drawText(Central[i],25,200*i,paint);
                }
                int d=0;
                for (int i = 12; i <= 24; i++) {
                    d=d+2;

                    canvas.drawLine(0, 200 * i, 20, 200 * i, paint);
                    canvas.drawText(Central[i]+" : "+d+" min",25,200*i,paint);
                }

                results = new RequestHandler().sendPostRequest("http://10.129.28.97:8007/FindMyTrain/Reporter", 0 + "");
                results = "20000 --> 23000";
                int initp = 4800*20000/50000;
                int finalp = 4800*23000/50000;
                int p = (initp+finalp)/2;
                paint.setColor(Color.parseColor("#000000"));
//                int l = finalp-initp<50?20:60;
//                canvas.drawLine(15,initp,15,initp+l,paint);
//                canvas.drawLine(15,initp+l,10,initp+l-10,paint);
//                canvas.drawLine(15,initp+l,20,initp+l-10,paint);
                Bitmap b1= BitmapFactory.decodeResource(getResources(), R.drawable.trn4);
//                canvas.drawBitmap(b1, 0, initp-13, paint);
//                canvas.drawBitmap(b1, 0, finalp-13, paint);
                canvas.drawBitmap(b1, 0, p-10, paint);
            } else if (route == 2) {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                paint.setTextSize(15);
                canvas.drawLine(0, 5, 20, 5, paint);
                canvas.drawText(Harbour[0], 25, 12, paint);
                for (int i = 1; i <= 8; i++) {
                    canvas.drawLine(0, 200 * i, 20, 200 * i, paint);
                    canvas.drawText(Harbour[i],25,200*i,paint);
                }
                int d=0;
                canvas.drawLine(0, 200 * 9, 20, 200 * 9, paint);
                canvas.drawText(Harbour[9]+" : "+40+" sec",25,200*9,paint);
                for (int i = 10; i <= 24; i++) {
                    d=d+2;
                    canvas.drawLine(0, 200 * i, 20, 200 * i, paint);
                    canvas.drawText(Harbour[i]+" : "+d+" min",25,200*i,paint);
                }

                results = new RequestHandler().sendPostRequest("http://10.129.28.97:8007/FindMyTrain/Reporter", 1 + "");
                results = "16000 --> 17000";
                int initp = 4800*16000/46000;
                int finalp = 4800*17000/46000;
                paint.setColor(Color.parseColor("#000000"));
                int p = (initp+finalp)/2;
//                int l = finalp-initp<50?20:60;
//                canvas.drawLine(15,initp,15,initp+l,paint);
//                canvas.drawLine(15,initp+l,10,initp+l-10,paint);
//                canvas.drawLine(15,initp+l,20,initp+l-10,paint);
                Bitmap b1= BitmapFactory.decodeResource(getResources(), R.drawable.trn4);
//                canvas.drawBitmap(b1, 0, initp-13, paint);
//                canvas.drawBitmap(b1, 0, finalp-13, paint);
                canvas.drawBitmap(b1, 0, p-10, paint);

            } else if (route == 3) {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                paint.setTextSize(15);
                canvas.drawLine(0, 5, 20, 5, paint);
                canvas.drawText(Western[0],25,12,paint);
                int x = 4800/35;
                for (int i = 1; i <= 11; i++) {
                    canvas.drawLine(0, x * i, 20, x * i, paint);
                    canvas.drawText(Western[i],25,x*i,paint);
                }
                int d=0;
                for (int i = 12; i <= 35; i++) {
                    d=d+2;
                    canvas.drawLine(0, x * i, 20, x * i, paint);
                    canvas.drawText(Western[i]+" : "+d+" min",25,x*i,paint);
                }

                results = new RequestHandler().sendPostRequest("http://10.129.28.97:8007/FindMyTrain/Reporter", 2 + "");
                results = "36000 --> 39000";
                int initp = 4800*36000/122000;
                int finalp = 4800*39000/122000;
                paint.setColor(Color.parseColor("#000000"));
                int p = (initp+finalp)/2;
//                int l = finalp-initp<50?20:60;
//                canvas.drawLine(15,initp,15,initp+l,paint);
//                canvas.drawLine(15,initp+l,10,initp+l-10,paint);
//                canvas.drawLine(15,initp+l,20,initp+l-10,paint);
                Bitmap b1= BitmapFactory.decodeResource(getResources(), R.drawable.trn4);
//                canvas.drawBitmap(b1, 0, initp-13, paint);
//                canvas.drawBitmap(b1, 0, finalp-13, paint);
                canvas.drawBitmap(b1, 0, p-10, paint);

            } else {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                for (int i = 0; i <= 24; i++) {
                    canvas.drawLine(0, 200 * i, 20, 200 * i, paint);
                }
            }
        }
    }
    private class MyView2 extends View {
        public MyView2(Context context) {
            super(context);
        }
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (route == 1) {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                paint.setTextSize(15);
                canvas.drawLine(0, 5, 20, 5, paint);
                canvas.drawText(Central[0], 25, 12, paint);
                for (int i = 1; i <= 11; i++) {
                    canvas.drawLine(0, 200 * i, 20, 200 * i, paint);
                    canvas.drawText(Central[i], 25, 200 * i, paint);
                }
                int d = 0;
                for (int i = 12; i <= 24; i++) {
                    d = d + 2;

                    canvas.drawLine(0, 200 * i, 20, 200 * i, paint);
                    canvas.drawText(Central[i] + " : " + d + " min", 25, 200 * i, paint);
                }

                results = new RequestHandler().sendPostRequest("http://10.129.28.97:8007/FindMyTrain/Reporter", 0 + "");
                results = "20000 --> 23000";
                int initp = 4800 * 20000 / 50000;
                int finalp = 4800 * 23000 / 50000;
                int p = (initp + finalp) / 2;
                paint.setColor(Color.parseColor("#000000"));
//                int l = finalp-initp<50?20:60;
//                canvas.drawLine(15,initp,15,initp+l,paint);
//                canvas.drawLine(15,initp+l,10,initp+l-10,paint);
//                canvas.drawLine(15,initp+l,20,initp+l-10,paint);
                Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.drawable.trn5);
//                canvas.drawBitmap(b1, 0, initp-13, paint);
//                canvas.drawBitmap(b1, 0, finalp-13, paint);
                canvas.drawBitmap(b1, 0, p - 10, paint);
            } else if (route == 2) {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                paint.setTextSize(15);
                canvas.drawLine(0, 5, 20, 5, paint);
                canvas.drawText(Harbour[0], 25, 12, paint);
                for (int i = 1; i <= 8; i++) {
                    canvas.drawLine(0, 200 * i, 20, 200 * i, paint);
                    canvas.drawText(Harbour[i], 25, 200 * i, paint);
                }
                int d = 0;
                canvas.drawLine(0, 200 * 9, 20, 200 * 9, paint);
                canvas.drawText(Harbour[9] + " : " + 40 + " sec", 25, 200 * 9, paint);
                for (int i = 10; i <= 24; i++) {
                    d = d + 2;
                    canvas.drawLine(0, 200 * i, 20, 200 * i, paint);
                    canvas.drawText(Harbour[i] + " : " + d + " min", 25, 200 * i, paint);
                }

                results = new RequestHandler().sendPostRequest("http://10.129.28.97:8007/FindMyTrain/Reporter", 1 + "");
                results = "16000 --> 17000";
                int initp = 4800 * 16000 / 46000;
                int finalp = 4800 * 17000 / 46000;
                paint.setColor(Color.parseColor("#000000"));
                int p = (initp + finalp) / 2;
//                int l = finalp-initp<50?20:60;
//                canvas.drawLine(15,initp,15,initp+l,paint);
//                canvas.drawLine(15,initp+l,10,initp+l-10,paint);
//                canvas.drawLine(15,initp+l,20,initp+l-10,paint);
                Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.drawable.trn5);
//                canvas.drawBitmap(b1, 0, initp-13, paint);
//                canvas.drawBitmap(b1, 0, finalp-13, paint);
                canvas.drawBitmap(b1, 0, p - 10, paint);

            } else if (route == 3) {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                paint.setTextSize(15);
                canvas.drawLine(0, 5, 20, 5, paint);
                canvas.drawText(Western[0], 25, 12, paint);
                int x = 4800 / 35;
                for (int i = 1; i <= 11; i++) {
                    canvas.drawLine(0, x * i, 20, x * i, paint);
                    canvas.drawText(Western[i], 25, x * i, paint);
                }
                int d = 0;
                for (int i = 12; i <= 35; i++) {
                    d = d + 2;
                    canvas.drawLine(0, x * i, 20, x * i, paint);
                    canvas.drawText(Western[i] + " : " + d + " min", 25, x * i, paint);
                }

                results = new RequestHandler().sendPostRequest("http://10.129.28.97:8007/FindMyTrain/Reporter", 2 + "");
                results = "36000 --> 39000";
                int initp = 4800 * 36000 / 122000;
                int finalp = 4800 * 39000 / 122000;
                paint.setColor(Color.parseColor("#000000"));
                int p = (initp + finalp) / 2;
//                int l = finalp-initp<50?20:60;
//                canvas.drawLine(15,initp,15,initp+l,paint);
//                canvas.drawLine(15,initp+l,10,initp+l-10,paint);
//                canvas.drawLine(15,initp+l,20,initp+l-10,paint);
                Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.drawable.trn5);
//                canvas.drawBitmap(b1, 0, initp-13, paint);
//                canvas.drawBitmap(b1, 0, finalp-13, paint);
                canvas.drawBitmap(b1, 0, p - 10, paint);

            } else {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                for (int i = 0; i <= 24; i++) {
                    canvas.drawLine(0, 200 * i, 20, 200 * i, paint);
                }
            }
        }
    }
}
