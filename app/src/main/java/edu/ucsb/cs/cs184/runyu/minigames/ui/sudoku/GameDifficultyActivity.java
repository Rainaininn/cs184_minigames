package edu.ucsb.cs.cs184.runyu.minigames.ui.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import edu.ucsb.cs.cs184.runyu.minigames.R;

public class GameDifficultyActivity extends AppCompatActivity {
    private boolean newBoard = false;
    private int selectedDifficulty = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_difficulty);
        newBoard = getIntent().getBooleanExtra("newBoard", false);
        if (newBoard) {
            Button buttonContinue = findViewById(R.id.buttonContinue);
            buttonContinue.setText(getString(R.string.add_new_board));
        }
    }

    public void onGoBackButtonClicked(View view) {
        finish();
    }

    public void onDifficultyRadioButtonsClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.radioButtonEasy:
                if (checked) {
                    selectedDifficulty = 0;
                }
                break;
            case R.id.radioButtonNormal:
                if (checked) {
                    selectedDifficulty = 1;
                }
                break;
            case R.id.radioButtonHard:
                if (checked) {
                    selectedDifficulty = 2;
                }
                break;
        }
    }

    public void onStartGameButtonClicked(View view) {
        if (newBoard) {
            Intent intent = new Intent();
            intent.putExtra("boardSaved", true);
            intent.putExtra("difficulty", selectedDifficulty);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Intent intent = new Intent("edu.ucsb.cs.cs184.runyu.GameActivity");
            intent.putExtra("difficulty", selectedDifficulty);
            startActivity(intent);
        }
    }

}
