package edu.ucsb.cs.cs184.runyu.minigames.ui.speedmath;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;
import edu.ucsb.cs.cs184.runyu.minigames.R;

public class StartGame extends AppCompatActivity{
    int op1, op2, correctAnswer, incorrectAnswer, timeLeft, points, numberOfQuestions, correctAnswerPosition;
    TextView timer, progress, answer, result;
    Button btn1, btn2, btn3, btn0;
    CountDownTimer countDownTimer;
    long millis;
    Random random;
    int[] btnIDs;
    ArrayList<Integer> incorrectAnswers;
    String[] operatorArray;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);
        op1 = 0;
        op2 = 0;
        correctAnswer = 0;
        incorrectAnswer = 0;
        timer = findViewById(R.id.Timer);
        progress = findViewById(R.id.problemNum);
        answer = findViewById(R.id.tvSum);
        result = findViewById(R.id.tvResult);
        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        points = 1;
        timeLeft = 0;
        random = new Random();
        btnIDs = new int[] {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3};
        correctAnswerPosition = 0;
        numberOfQuestions = getIntent().getExtras().getInt("questions");
        millis = 100100;
        incorrectAnswers = new ArrayList<>();
        operatorArray = new String[]{"+", "-", "*", "÷"};
        startGame();

    }

    private void startGame() {
        timer.setText(""+ ((100100 - millis) / 1000) + "s");
        progress.setText(""+ points + "/" + numberOfQuestions);
        generateQuestion();

        countDownTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millis) {
                timer.setText(""+ ((100100 - millis) / 1000) + "s");
                timeLeft++;
            }


            @Override
            public void onFinish() {
                btn0.setClickable(false);
                btn1.setClickable(false);
                btn2.setClickable(false);
                btn3.setClickable(false);
                Intent intent = new Intent(StartGame.this, GameOver.class);
                intent.putExtra("points", 0);
                intent.putExtra("newGameQNum", numberOfQuestions);
                startActivity(intent);
                finish();
            }

        }.start();
    }


    private void generateQuestion() {
        //numberOfQuestions++;
        op1 = random.nextInt(10);
        op2 = random.nextInt(10);
        String selectedOperator = operatorArray[random.nextInt(4)];
        if (selectedOperator == "÷") {
            op2 = 1 + random.nextInt(9);
            op1 = op1 * op2;
        }
        correctAnswer = getAnswer(selectedOperator);
        answer.setText(op1 + " " + selectedOperator + " " + op2 + " = ");
        correctAnswerPosition = random.nextInt(4);
        ((Button) findViewById(btnIDs[correctAnswerPosition])).setText("" + correctAnswer);
        while (true) {
            if (incorrectAnswers.size() > 3)
                break;
            op1 = random.nextInt(10);
            op2 = 1 + random.nextInt(9);
            selectedOperator = operatorArray[random.nextInt(4)];
            if (selectedOperator == "÷") {
                op2 = 1 + random.nextInt(9);
                op1 = op1 * op2;
            }
            incorrectAnswer = getAnswer(selectedOperator);
            if (incorrectAnswer == correctAnswer)
                continue;
            incorrectAnswers.add(incorrectAnswer);
        }

        for (int i = 0; i < 3; i++) {
            if (i == correctAnswerPosition)
                continue;
            ((Button) findViewById(btnIDs[i])).setText("" + incorrectAnswers.get(i));
        }
        incorrectAnswers.clear();

    }

    private int getAnswer(String selectedOperator) {
        int answer = 0;
        switch (selectedOperator){
            case "+":
                answer = op1 + op2;
                break;
            case "-":
                answer = op1 - op2;
                break;
            case "*":
                answer = op1 * op2;
                break;
            case "÷":
                answer = op1 / op2;
                break;
        }
        return answer;
    }

    public void chooseAnswer(View view) {
        int answer = Integer.parseInt(((Button)view).getText().toString());
        if(answer == correctAnswer)
        {
            points++;
            result.setText("Correct!");
        }
        else{
            result.setText("Incorrect!");
        }
        if (points > numberOfQuestions) {
            btn0.setClickable(false);
            btn1.setClickable(false);
            btn2.setClickable(false);
            btn3.setClickable(false);
            countDownTimer.cancel();
            Intent intent = new Intent(this, GameOver.class);
            intent.putExtra("newGameQNum", numberOfQuestions);
            intent.putExtra("points", timeLeft);
            startActivity(intent);
            finish();
        }
        else {
            progress.setText(points + "/" + numberOfQuestions);
            generateQuestion();
        }
    }
}
