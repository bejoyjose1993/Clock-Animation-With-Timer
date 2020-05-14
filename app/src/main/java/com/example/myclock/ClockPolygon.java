package com.example.myclock;

import android.graphics.Canvas;
import android.graphics.Paint;

public class ClockPolygon {

    private float x0, y0, r;
    private int n;
    private float x[], y[];
    private Canvas c = null;
    private Paint p = null;
    public ClockPolygon(int n, float r, float x0, float y0, Canvas canvas, Paint paint) {
        this.n =n;
        this.r = r;
        this.x0 = x0;
        this.y0 = y0;
        this.c = canvas;
        this.p = paint;
        x = new float[n];
        y = new float[n];
        for(int i =0;i<n;i++){
            x[i] = (float)(x0 + r*Math.cos(2*Math.PI*i/n));
            y[i] = (float)(y0 + r*Math.sin(2*Math.PI*i/n));
        }
    }

    public float getCordX(int i){
        return x[i%n];
    }
    public float getCordY(int i){
        return y[i%n];
    }
    public void circlePoly(int n, float r, float x0, float y0, Canvas canvas, Paint paint){

    }

    public void drawcirClePoly(){

        for(int i=0; i<n; i++){
            c.drawCircle(x[i],y[i],4,p);
        }
    }
    public void drawPoints(){

        for(int i=0; i<n; i++){
            c.drawCircle(x[i],y[i],4,p);
        }
    }

    public void drawClockDigits(){

        for(int i=0; i<n; i++){
            String digits []= {"3","4","5","6","7","8","9","10","11","12","1","2"};
            String digit = digits[i];
            c.drawText(digit,x[i]-10,y[i]+10,p);
        }
    }
    public void drawRadius(int i){
            c.drawLine(x0,y0, x[i%n],y[i%n],p);
    }
}
