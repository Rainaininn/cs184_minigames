package edu.ucsb.cs.cs184.runyu.minigames.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.ucsb.cs.cs184.runyu.minigames.MainActivity;
import edu.ucsb.cs.cs184.runyu.minigames.R;
import edu.ucsb.cs.cs184.runyu.minigames.ui.home.HomeFragment;

public class GameOver extends Activity {
    TextView tvScore,tvPersonalBest;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        int score = getIntent().getExtras().getInt("score");
        SharedPreferences pref = getSharedPreferences("MyPref",0);
        int scoreSP = pref.getInt("scoreSP",0);
        SharedPreferences.Editor editor = pref.edit();
        if(score > scoreSP){
            scoreSP = score;
            editor.putInt("scoreSP",scoreSP);
            editor.commit();
        }
        tvScore = (TextView)findViewById(R.id.tvScore);
        tvPersonalBest = (TextView)findViewById(R.id.tvPersonalBest);
        tvScore.setText(""+score);
        tvPersonalBest.setText(""+scoreSP);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userID = HomeFragment.userID;
        Log.d("Gun", "userID : " + userID);
        if(userID.equals("n/a") == false) {
            DatabaseReference myRef = database.getReference(userID);
            DatabaseReference myRefGun = myRef.child("Gun");

            int finalScoreSP = scoreSP;
            myRefGun.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    Log.d("Gun", "Value is: " + value);
                    if (value == null) {
                        myRefGun.setValue(finalScoreSP + "");
                    } else {
                        int prevBest = Integer.parseInt(value);
                        if (finalScoreSP > prevBest) {
                            myRefGun.setValue(finalScoreSP + "");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("Gun", "Failed to read value.", error.toException());
                }
            });
        }
    }
    public void restart(View view){
        Intent intent = new Intent(GameOver.this, StartGame.class);
        startActivity(intent);
        finish();
    }
    public void exit(View view){
        Intent intent = new Intent(GameOver.this,MainActivity.class);
        startActivity(intent);
        //finish();
    }
}
