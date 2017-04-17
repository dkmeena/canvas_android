package com.example.dinesh.ui;

import android.app.Activity;
import android.content.Context;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Spinner spinner;
    ImageButton query_button;
    MyView1 v1;
    MyView2 v2;
    LinearLayout layout1;
    LinearLayout layout2;
    public int route=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);

        v1 = new MyView1(this);
        layout1.addView(v1,500,4800);
        v2 = new MyView2(this);
        layout2.addView(v2,500,4800);

        spinner = (Spinner) findViewById(R.id.line_spinner);
        query_button = (ImageButton) findViewById(R.id.query);
        query_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.query){
            Log.d("asdas","asdas");
            int spinner_pos = spinner.getSelectedItemPosition();
            Log.d("asdas", String.valueOf(spinner_pos));
            switch (spinner_pos){
               case 1:
                    ((LinearLayout)v1.getParent()).removeView(v1);
                    ((LinearLayout)v2.getParent()).removeView(v2);
                    route=1;
                    v1 = new MyView1(this);
                    layout1.addView(v1, 500, 4800);
                    v2 = new MyView2(this);
                    layout2.addView(v2,500,4800);
                   break;
                case 2:
                    ((LinearLayout)v1.getParent()).removeView(v1);
                    ((LinearLayout)v2.getParent()).removeView(v2);
                    route=2;
                    v1 = new MyView1(this);
                    layout1.addView(v1, 500, 4800);
                    v2 = new MyView2(this);
                    layout2.addView(v2,500,4800);
                    break;
                case 3:
                    ((LinearLayout)v1.getParent()).removeView(v1);
                    ((LinearLayout)v2.getParent()).removeView(v2);
                    route=3;
                    v1 = new MyView1(this);
                    layout1.addView(v1, 500, 4800);
                    v2 = new MyView2(this);
                    layout2.addView(v2,500,4800);
                    break;
                case 0:
                    ((LinearLayout)v1.getParent()).removeView(v1);
                    ((LinearLayout)v2.getParent()).removeView(v2);
                    route=0;
                    v1 = new MyView1(this);
                    layout1.addView(v1, 500, 4800);
                    v2 = new MyView2(this);
                    layout2.addView(v2,500,4800);
                    break;
            }
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
                for (int i = 0; i <= 24; i++) {
                    canvas.drawLine(0, 200 * i, 60, 200 * i, paint);
                }
            } else if (route == 2) {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                for (int i = 0; i <= 24; i++) {
                    canvas.drawLine(0, 200 * i, 60, 200 * i, paint);
                }
            } else if (route == 3) {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                int x = 4800/36;
                for (int i = 0; i <= 36; i++) {
                    canvas.drawLine(0, x * i, 60, x * i, paint);
                }
            } else {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                for (int i = 0; i <= 24; i++) {
                    canvas.drawLine(0, 200 * i, 60, 200 * i, paint);
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
                for (int i = 0; i <= 24; i++) {
                    canvas.drawLine(0, 200 * i, 60, 200 * i, paint);
                }
            } else if (route == 2) {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                for (int i = 0; i <= 24; i++) {
                    canvas.drawLine(0, 200 * i, 60, 200 * i, paint);
                }
            } else if (route == 3) {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                int x = 4800/36;
                for (int i = 0; i <= 36; i++) {
                    canvas.drawLine(0, x * i, 60, x * i, paint);
                }
            } else {
                Paint paint = new Paint();
                paint.setColor(Color.parseColor("#d10000"));
                paint.setAntiAlias(true);
                //canvas.drawRect(0, j,50,50, paint);
                paint.setStrokeWidth(5);
                canvas.drawLine(0, 0, 0, 4800, paint);
                paint.setStrokeWidth(3);
                for (int i = 0; i <= 24; i++) {
                    canvas.drawLine(0, 200 * i, 60, 200 * i, paint);
                }
            }
        }
    }
}
