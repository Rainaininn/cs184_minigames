package edu.ucsb.cs.cs184.runyu.minigames.ui.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import edu.ucsb.cs.cs184.runyu.minigames.R;

public class GameView extends View{
    Bitmap background, tank;
    Rect rect;
    static int dWidth, dHeight;
    ArrayList<Plane> planes;
    ArrayList<Missile> missiles;
    int planeWidth;

    Handler handler;
    Runnable runnable;
    final long UPDATE_MILLIS = 30;
    static int tankWidth, tankHeight;
    Context context;
    int count = 0;
    SoundPool sp;
    int fire = 0, point = 0;
    Paint scorePaint, healthPaint;
    final int TEXT_SIZE = 100;
    int life = 10;

    @RequiresApi(api = Build.VERSION_CODES.R)
    public GameView(Context context) {
        super(context);
        this.context = context;
        //super(context);
        //this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        tank = BitmapFactory.decodeResource(getResources(), R.drawable.tank);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rect = new Rect(0, 0, dWidth, dHeight);
        planes = new ArrayList<>();
        missiles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Plane plane = new Plane(context);
            planes.add(plane);
        }


        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        tankWidth = tank.getWidth();
        tankHeight = tank.getHeight();
        sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        fire = sp.load(context, R.raw.fire, 1);
        point = sp.load(context, R.raw.fire, 1);
        scorePaint = new Paint();
        scorePaint.setColor(Color.RED);
        scorePaint.setTextSize(TEXT_SIZE);
        scorePaint.setTextAlign(Paint.Align.LEFT);
        healthPaint = new Paint();
        healthPaint.setColor(Color.GREEN);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(life==0){
            Intent intent = new Intent(context,GameOver.class);
            intent.putExtra("score",(count * 10));
            context.startActivity(intent);
            ((Activity)context).finish();
        }
        canvas.drawBitmap(background, null, rect, null);

        for (int i = 0; i < planes.size(); i++) {
            canvas.drawBitmap(planes.get(i).getBitmap(), planes.get(i).planeX, planes.get(i).planeY, null);
            planes.get(i).planeFrame++;
            if (planes.get(i).planeFrame > 4) {
                planes.get(i).planeFrame = 0;
            }
            planes.get(i).planeX -= planes.get(i).velocity;
            if (planes.get(i).planeX < -planes.get(i).getWidth()) {
                planes.get(i).resetPosition();
                life--;
                if(life==0){
                    Intent intent = new Intent(context,GameOver.class);
                    intent.putExtra("score",(count * 10));
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        }
        for (int i = 0; i < missiles.size(); i++) {
            if (missiles.get(i).y > -missiles.get(i).getMissileHeight()) {
                missiles.get(i).y -= missiles.get(i).mVelocity;
                canvas.drawBitmap(missiles.get(i).missile, missiles.get(i).x, missiles.get(i).y, null);
                if (missiles.get(i).x >= planes.get(0).planeX && (missiles.get(i).x + missiles.get(i).getMissileWidth())
                        < (planes.get(0).planeX + planes.get(0).getWidth()) && missiles.get(i).y >= planes.get(0).planeY && missiles.get(i).y
                        <= (planes.get(0).planeY + planes.get(0).getHeight())) {
                    planes.get(0).resetPosition();
                    count++;
                    missiles.remove(i);
                    if(point != 0){
                        sp.play(point,1,1,0,0,1);
                    }
                } else if (missiles.get(i).x >= planes.get(1).planeX && (missiles.get(i).x + missiles.get(i).getMissileWidth())
                        < (planes.get(1).planeX + planes.get(1).getWidth()) && missiles.get(i).y >= planes.get(1).planeY && missiles.get(i).y
                        <= (planes.get(1).planeY + planes.get(1).getHeight())) {
                    planes.get(1).resetPosition();
                    count++;
                    missiles.remove(i);
                    if(point != 0){
                        sp.play(point,1,1,0,0,1);
                    }
                }
            } else {
                missiles.remove(i);
            }
        }
        canvas.drawBitmap(tank, (dWidth / 2 - tankWidth / 2), dHeight - tankHeight, null);
        canvas.drawText("Point: "+(count * 10),0,TEXT_SIZE,scorePaint);
        canvas.drawRect(dWidth-110,20,dWidth-110 + 10*life,TEXT_SIZE,healthPaint);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }
    @Override
    public boolean onTouchEvent (MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            if (touchX >= (dWidth / 2 - tankWidth / 2) && touchX <= (dWidth / 2 + tankWidth / 2) && touchY >= (-tankHeight)) {
                Log.i("Tank", "is tapped");
                if (missiles.size() < 3) {
                    Missile m = new Missile(context);
                    missiles.add(m);
                    if(fire != 0){
                        sp.play(fire, 1, 1,0,0,1);
                    }
                }
            }

        }
        return true;
    }
}
