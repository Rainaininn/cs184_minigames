package edu.ucsb.cs.cs184.runyu.minigames.ui.speedmath;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.ucsb.cs.cs184.runyu.minigames.MainActivity;
import edu.ucsb.cs.cs184.runyu.minigames.R;
import edu.ucsb.cs.cs184.runyu.minigames.ui.home.HomeFragment;

public class GameOver extends AppCompatActivity {
    TextView tvPoints;
    int numberOfQuestions;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speedmath_game_over);
        int score = getIntent().getExtras().getInt("points");
        numberOfQuestions = getIntent().getExtras().getInt("newGameQNum");
        tvPoints = findViewById(R.id.BestTime);
        tvPoints.setText(""+ score + ", " + numberOfQuestions + " Questions");
//        tvPoints.setText("12");

        //Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userID = HomeFragment.userID;
        Log.d("Math", "userID : " + userID);
        if(userID.equals("n/a") == false) {
            DatabaseReference myRef = database.getReference(userID);
            DatabaseReference myRefMath = myRef.child("Math");

            myRefMath.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    Log.d("Math", "Value is: " + value);
                    if (value == null) {
                        myRefMath.setValue(score + "");
                    } else {
                        int prevBest = Integer.parseInt(value);
                        if (score < prevBest) {
                            myRefMath.setValue(score + "");
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("Math", "Failed to read value.", error.toException());
                }
            });
        }
    }

    public void restart(View view) {
        Intent intent = new Intent("edu.ucsb.cs.cs184.runyu.StartGame");
        intent.putExtra("questions", numberOfQuestions);
        startActivity(intent);
        finish();
    }

    public void returnToTitle(View view) {
        Intent intent = new Intent(edu.ucsb.cs.cs184.runyu.minigames.ui.speedmath.GameOver.this, MainActivity.class);
        startActivity(intent);
    }
}
