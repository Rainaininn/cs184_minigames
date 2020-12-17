package edu.ucsb.cs.cs184.runyu.minigames.ui.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import edu.ucsb.cs.cs184.runyu.minigames.R;

public class Missile {
    int x,y;
    int mVelocity;
    Bitmap missile;
    public Missile(Context context){
        missile = BitmapFactory.decodeResource(context.getResources(), R.drawable.missile);
        x = GameView.dWidth/2 - getMissileWidth()/2;
        y = GameView.dHeight - GameView.tankHeight - getMissileHeight()/2;
        mVelocity = 50;
    }
    public int getMissileWidth(){
        return missile.getWidth();

    }
    public int getMissileHeight(){
        return missile.getHeight();
    }
}
