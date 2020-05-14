package com.example.myclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Calendar;

public class ClockSurfaceView extends SurfaceView implements Runnable {
    private Thread thread = null;
    private SurfaceHolder surfaceHolder;
    private  boolean running = false;
    private float length;
    Paint paint = null;
    Paint paintHour = null;
    Bitmap bmp = null;
    public ClockSurfaceView(Context context, float l){
        super(context);
        this.length = l;
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paintHour = new Paint();
        SharedPreferences prefs = context.getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE);
        int selectedColor = prefs.getInt("selectedColor", 0);
        System.out.println("selectedColor"+selectedColor);
        int selectedColorIn = prefs.getInt("selectedColorIn", 0);
        paintHour.setColor(selectedColorIn);
        paint.setColor(selectedColor);
        paint.setStrokeWidth(5);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.arsenal_fc);
    }

    public void  onResumeMySurfaceView(){
        running = true;
        thread = new Thread(this);
        thread.start();;
    }
    public void  onPauseMySurfaceView(){
        boolean retry = true;
        running = false;
        while (retry) {
            try {
                thread.join();;
                retry = false;
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }

    public void run(){
        int hour = 0, min = 0, sec = 0, mili=0;
        surfaceHolder = getHolder();
        while (running){

            if(surfaceHolder.getSurface().isValid()){

                Canvas canvas = surfaceHolder.lockCanvas();

                Paint backgroundClear = new Paint();
                Paint millHandPaimt = new Paint();
                Paint circlePaint = new Paint();
                Paint miliCirclePaint = new Paint();
                Paint outerCirPaint = new Paint();


                backgroundClear.setColor(Color.parseColor("#232526"));
                millHandPaimt.setColor(Color.WHITE);
                circlePaint.setColor(Color.BLACK);
                outerCirPaint.setColor(Color.BLACK);
                miliCirclePaint.setColor(Color.RED);
                //backgroundClear.setShader(new LinearGradient(0, 0, 0, getHeight(), Color.parseColor("#414345"), Color.parseColor("#232526"), Shader.TileMode.MIRROR));
                canvas.drawPaint(backgroundClear);

                outerCirPaint.setStrokeWidth(5);
                millHandPaimt.setStrokeWidth(4);
                miliCirclePaint.setStrokeWidth(2);

                paintHour.setTextSize(30);
                outerCirPaint.setStyle(Paint.Style.STROKE);
                miliCirclePaint.setStyle(Paint.Style.STROKE);
                int px = getWidth()/2+10;
                float py = getHeight()/2.5f;
                float radius = Math.min(px, py);
                canvas.drawCircle(getWidth()/2, getHeight()/2,length+30, outerCirPaint);
                canvas.drawCircle(getWidth()/2, getHeight()/2,length+20, circlePaint);
                canvas.drawCircle(getWidth()/2, getHeight()/1.7f,length-230, miliCirclePaint);
                ClockPolygon secMarks  = new ClockPolygon(60, length, getWidth()/2, getHeight()/2, canvas, paint);
                ClockPolygon hourMarks = new ClockPolygon(12, length-40, getWidth()/2, getHeight()/2, canvas, paintHour);
                ClockPolygon hourHand  = new ClockPolygon(60, length-175, getWidth()/2, getHeight()/2, canvas, paint);
                ClockPolygon minHand   = new ClockPolygon(60, length-100, getWidth()/2, getHeight()/2, canvas, paint);
                ClockPolygon secHand   = new ClockPolygon(60, length-80, getWidth()/2, getHeight()/2, canvas, paint);
                ClockPolygon milHand   = new ClockPolygon(60, length-270, getWidth()/2, getHeight()/1.7f, canvas, paintHour);
                ClockPolygon milMark   = new ClockPolygon(12, length-250, getWidth()/2, getHeight()/1.7f, canvas, paintHour);

                //Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.arsenal_fc);
                Bitmap scaled = Bitmap.createScaledBitmap(bmp, (int) (radius / 6),(int) (radius / 6), true);
                canvas.drawBitmap(scaled, px - scaled.getWidth() / 2,
                        py - scaled.getHeight() / 2, null);
                // get the date
                Calendar calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR);
                min = calendar.get(Calendar.MINUTE);
                sec = calendar.get(Calendar.SECOND);
                //mili = calendar.get(Calendar.MILLISECOND);
                // draw the hands
                secHand.drawRadius(sec+45);
                minHand.drawRadius(min+45);
                hourHand.drawRadius(45+hour*5+ min/12);
                secMarks.drawPoints();
                milMark.drawPoints();
                hourMarks.drawClockDigits();

                milHand.drawRadius(mili+45);
                try {
                    Thread.sleep(1000/990);
                    mili++;
                } catch (InterruptedException e) {e.printStackTrace();}


                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void changeColor(int selectedColor) {
        paint.setColor(selectedColor);

    }
    public void changeInColor(int selectedColor) {
        paintHour.setColor(selectedColor);

    }


    public void changeLogo(String logo, LinearLayout tobtab, ImageView changeImg) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("MY_PREFS_NAME", Context.MODE_PRIVATE).edit();
        int selectedColor =0;
        int selectedColorIn = 0;
        if(logo.equalsIgnoreCase("ARSENAL")){
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.arsenal_fc);
            paint.setColor(Color.parseColor("#DB0007"));
            paintHour.setColor(Color.parseColor("#FFFFFF"));
            tobtab.setBackgroundColor(Color.parseColor("#DB0007"));
            changeImg.setImageBitmap(bmp);
            selectedColor = Color.parseColor("#DB0007");
            selectedColorIn = Color.parseColor("#FFFFFF");
        }else if(logo.equalsIgnoreCase("BARCALONA")){
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.barcalona_fc);
            paint.setColor(Color.parseColor("#EDBC00"));
            tobtab.setBackgroundColor(Color.parseColor("#EDBC00"));
            changeImg.setImageBitmap(bmp);
            selectedColor = Color.parseColor("#EDBC00");
            selectedColorIn = Color.parseColor("#A70042");
            paintHour.setColor(Color.parseColor("#A70042"));
        }else if(logo.equalsIgnoreCase("LIVERPOOL")){
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.liverpool_fc);
            paint.setColor(Color.parseColor("#D00027"));
            tobtab.setBackgroundColor(Color.parseColor("#D00027"));
            changeImg.setImageBitmap(bmp);
            selectedColor = Color.parseColor("#D00027");
            selectedColorIn = Color.parseColor("#00A398");
            paintHour.setColor(Color.parseColor("#00A398"));
        }else if(logo.equalsIgnoreCase("MANUNITED")){
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.manu_pg2);
            paint.setColor(Color.parseColor("#DA020E"));
            tobtab.setBackgroundColor(Color.parseColor("#DA020E"));
            changeImg.setImageBitmap(bmp);
            selectedColor = Color.parseColor("#DA020E");
            selectedColorIn = Color.parseColor("#FFE500");
            paintHour.setColor(Color.parseColor("#FFE500"));
        }else if(logo.equalsIgnoreCase("REAL MADRID")){
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.real_madrid_fc);
            paint.setColor(Color.parseColor("#FEBE10"));
            tobtab.setBackgroundColor(Color.parseColor("#FEBE10"));
            changeImg.setImageBitmap(bmp);
            selectedColor = Color.parseColor("#FEBE10");
            selectedColorIn = Color.parseColor("#00529F");
            paintHour.setColor(Color.parseColor("#00529F"));
        }
        editor.putInt("selectedColor", selectedColor);
        editor.putInt("selectedColorIn", selectedColorIn);
        editor.apply();
    }
}
