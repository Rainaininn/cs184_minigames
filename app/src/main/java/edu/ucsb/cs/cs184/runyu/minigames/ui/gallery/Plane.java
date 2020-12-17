package edu.ucsb.cs.cs184.runyu.minigames.ui.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

import edu.ucsb.cs.cs184.runyu.minigames.R;

public class Plane {
    Bitmap plane [] = new Bitmap[5];
    int planeX,planeY,velocity,planeFrame;
    Random random;


    public Plane(Context context) {
        plane[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.plane1);
        plane[1] = BitmapFactory.decodeResource(context.getResources(),R.drawable.plane2);
        plane[2] = BitmapFactory.decodeResource(context.getResources(),R.drawable.plane3);
        plane[3] = BitmapFactory.decodeResource(context.getResources(),R.drawable.plane4);
        plane[4] = BitmapFactory.decodeResource(context.getResources(),R.drawable.plane5);

        random = new Random();
        resetPosition();
    }
    public Bitmap getBitmap(){
        return plane[planeFrame];
    }
    public int getWidth(){
        return plane[0].getWidth();

    }
    public int getHeight(){
        return  plane[0].getHeight();
    }
    public void resetPosition(){
        planeX = GameView.dWidth+random.nextInt(1200);
        planeY = random.nextInt(300);
        velocity = 8 + random.nextInt(13);
        planeFrame = 0;
    }

}
