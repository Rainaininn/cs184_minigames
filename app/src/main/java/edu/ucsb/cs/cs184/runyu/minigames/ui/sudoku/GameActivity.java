package edu.ucsb.cs.cs184.runyu.minigames.ui.sudoku;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.ucsb.cs.cs184.runyu.minigames.R;
import edu.ucsb.cs.cs184.runyu.minigames.ui.home.HomeFragment;

public class GameActivity extends AppCompatActivity implements CellGroupFragment.OnFragmentInteractionListener, KeyboardFragment.OnKeyboardFragmentInteractionListener{
    private final String TAG = "GameActivity";
    private TextView clickedCell;
    private int clickedGroup;
    private int clickedCellId;
    private Board startBoard;
    private Board currentBoard;
    private Chronometer chronometer;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        int difficulty = getIntent().getIntExtra("difficulty", 0);
        ArrayList<Board> boards = readGameBoards(difficulty);
        startBoard = chooseRandomBoard(boards);
        currentBoard = new Board();
        currentBoard.copyValues(startBoard.getGameCells());

        int cellGroupFragments[] = new int[]{R.id.cellGroupFragment, R.id.cellGroupFragment2, R.id.cellGroupFragment3, R.id.cellGroupFragment4,
                R.id.cellGroupFragment5, R.id.cellGroupFragment6, R.id.cellGroupFragment7, R.id.cellGroupFragment8, R.id.cellGroupFragment9};
        for (int i = 1; i < 10; i++) {
            CellGroupFragment thisCellGroupFragment = (CellGroupFragment) getSupportFragmentManager().findFragmentById(cellGroupFragments[i-1]);
            thisCellGroupFragment.setGroupId(i);
        }

        //Appear all values from the current board
        CellGroupFragment tempCellGroupFragment;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int column = j / 3;
                int row = i / 3;

                int fragmentNumber = (row * 3) + column;
                tempCellGroupFragment = (CellGroupFragment) getSupportFragmentManager().findFragmentById(cellGroupFragments[fragmentNumber]);
                int groupColumn = j % 3;
                int groupRow = i % 3;

                int groupPosition = (groupRow * 3) + groupColumn;
                int currentValue = currentBoard.getValue(i, j);

                if (currentValue != 0) {
                    tempCellGroupFragment.setValue(groupPosition, currentValue);
                }
            }
        }

        //chronometer
        chronometer = findViewById(R.id.chronometer);
        startChronometer();
    }

    public void startChronometer(){
        if(!running){
            resetChronometer();
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(){
        if(running){
            chronometer.stop();
            running = false;
        }
    }

    public void resetChronometer(){
        chronometer.setBase(SystemClock.elapsedRealtime());
    }

    private ArrayList<Board> readGameBoards(int difficulty) {
        ArrayList<Board> boards = new ArrayList<>();
        int fileId;
        if (difficulty == 1) {
            fileId = R.raw.normal;
        } else if (difficulty == 0) {
            fileId = R.raw.easy;
        } else {
            fileId = R.raw.hard;
        }

        InputStream inputStream = getResources().openRawResource(fileId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                Board board = new Board();
                // read all lines in the board
                for (int i = 0; i < 9; i++) {
                    String rowCells[] = line.split(" ");
                    for (int j = 0; j < 9; j++) {
                        if (rowCells[j].equals("-")) {
                            board.setValue(i, j, 0);
                        } else {
                            board.setValue(i, j, Integer.parseInt(rowCells[j]));
                        }
                    }
                    line = bufferedReader.readLine();
                }
                boards.add(board);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        //reading from internal storage (/data/data/<package-name>/files)
        String fileName = "boards-";
        if (difficulty == 0) {
            fileName += "easy";
        } else if (difficulty == 1) {
            fileName += "normal";
        } else {
            fileName += "hard";
        }

        FileInputStream fileInputStream;
        try {
            fileInputStream = this.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader internalBufferedReader = new BufferedReader(inputStreamReader);
            String line = internalBufferedReader.readLine();
            line = internalBufferedReader.readLine();
            while (line != null) {
                Board board = new Board();
                // read all lines in the board
                for (int i = 0; i < 9; i++) {
                    String rowCells[] = line.split(" ");
                    for (int j = 0; j < 9; j++) {
                        if (rowCells[j].equals("-")) {
                            board.setValue(i, j, 0);
                        } else {
                            board.setValue(i, j, Integer.parseInt(rowCells[j]));
                        }
                    }
                    line = internalBufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                }
                boards.add(board);
                line = internalBufferedReader.readLine();
            }
            internalBufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return boards;
    }

    private Board chooseRandomBoard(ArrayList<Board> boards) {
        int randomNumber = (int) (Math.random() * boards.size());
        return boards.get(randomNumber);
    }

    private boolean isStartPiece(int group, int cell) {
        int row = ((group-1)/3)*3 + (cell/3);
        int column = ((group-1)%3)*3 + ((cell)%3);
        return startBoard.getValue(row, column) != 0;
    }

    private boolean checkAllGroups() {
        int cellGroupFragments[] = new int[]{R.id.cellGroupFragment, R.id.cellGroupFragment2, R.id.cellGroupFragment3, R.id.cellGroupFragment4,
                R.id.cellGroupFragment5, R.id.cellGroupFragment6, R.id.cellGroupFragment7, R.id.cellGroupFragment8, R.id.cellGroupFragment9};
        for (int i = 0; i < 9; i++) {
            CellGroupFragment thisCellGroupFragment = (CellGroupFragment) getSupportFragmentManager().findFragmentById(cellGroupFragments[i]);
            if (!thisCellGroupFragment.checkGroupCorrect()) {
                return false;
            }
        }
        return true;
    }

    public void onCheckBoardButtonClicked(View view) {
        currentBoard.isBoardCorrect();
        if(checkAllGroups() && currentBoard.isBoardCorrect()) {
            //User completes the sudoku!
            Toast.makeText(this, getString(R.string.board_correct), Toast.LENGTH_SHORT).show();
            pauseChronometer();
            TextView sudokuCongrats = findViewById(R.id.sudokuComplete);
            sudokuCongrats.setText("Congratulations! You have successfully solved this Sudoku!");
            sudokuCongrats.setVisibility(View.VISIBLE);
            View keyboardFrag = findViewById(R.id.keyboardFragment);
            keyboardFrag.setVisibility(View.INVISIBLE);

            //update to Firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String userID = HomeFragment.userID;
            Log.d("Sudoku", "userID : " + userID);
            if(userID.equals("n/a") == false) {
                DatabaseReference myRef = database.getReference(userID);
                DatabaseReference myRefSudoku = myRef.child("Sudoku");
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                Log.d("Sudoku", "The time taken is :" + elapsedMillis);

                myRefSudoku.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.getValue(String.class);
                        Log.d(TAG, "Value is: " + value);
                        if (value == null) {
                            myRefSudoku.setValue(elapsedMillis + "");
                        } else {
                            long prevBestTime = Long.parseLong(value);
                            if (prevBestTime > elapsedMillis) {
                                myRefSudoku.setValue(elapsedMillis + "");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }

        } else {
            Toast.makeText(this, getString(R.string.board_incorrect), Toast.LENGTH_SHORT).show();
        }
    }

    public void onGoBackButtonClicked(View view) {
        finish();
    }

    public void onShowInstructionsButtonClicked(View view) {
        Intent intent = new Intent("edu.ucsb.cs.cs184.runyu.InstructionsActivity");
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(int groupId, int cellId, View view) {
        if(!isStartPiece(groupId, cellId)) {
            if (clickedCell != null) {
                clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell));
            }

            clickedCell = (TextView) view;
            clickedGroup = groupId;
            clickedCellId = cellId;
            clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell_unsure));
        }
        else{
            Toast.makeText(this, getString(R.string.start_piece_error), Toast.LENGTH_SHORT).show();
        }
        Log.i(TAG, "Clicked group " + groupId + ", cell " + cellId);

    }

    @Override
    public void onKeyboardFragmentInteraction(int kb1) {
        Log.i("game activity", "Clicked on "+ kb1);

        if(clickedCell == null){
            Toast.makeText(this, "Choose a cell first", Toast.LENGTH_SHORT).show();
            return;
        }

        Button buttonCheckBoard = findViewById(R.id.buttonCheckBoard);
        int row = ((clickedGroup - 1) / 3) * 3 + (clickedCellId / 3);
        int column = ((clickedGroup - 1) % 3) * 3 + ((clickedCellId) % 3);

        if(kb1 == 0) {
            clickedCell.setText("");
//            clickedCell.setBackground(getResources().getDrawable(R.drawable.table_border_cell));
            currentBoard.setValue(row, column, 0);
            buttonCheckBoard.setVisibility(View.INVISIBLE);
        }else{
            clickedCell.setText(String.valueOf(kb1));
            currentBoard.setValue(row, column, kb1);
            if (currentBoard.isBoardFull()) {
                buttonCheckBoard.setVisibility(View.VISIBLE);
            } else {
                buttonCheckBoard.setVisibility(View.INVISIBLE);
            }
        }
    }
}
