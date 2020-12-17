package edu.ucsb.cs.cs184.runyu.minigames.ui.tictactoe;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import edu.ucsb.cs.cs184.runyu.minigames.R;

public class TicTacToe extends Fragment implements View.OnClickListener{

    private TicTacToeViewModel mViewModel;

    public static TicTacToe newInstance() {
        return new TicTacToe();
    }

    //Arrays for 9 buttons, and reset Button
    private Button[][] buttons = new Button[3][3];
    private Button resetButton;

    //When turn is true, this is player 1's turn. else this is player 2's turn
    private boolean player1Turn = true;

    //There is total 5 rounds. players who win 3 rounds will win the game.
    //If there is no player win 3 rounds, players who win more round is the winner.
    //Otherwise, there will be no winner
    private int round = 1;

    //This timer record total turns in this round
    private int timer = 0;

    //Record scores for 2 players
    private int player1Score = 0;
    private int player2Score = 0;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    private TextView textViewTurn;
    private TextView textViewRound;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tictactoe, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TicTacToeViewModel.class);
        // TODO: Use the ViewModel

        textViewPlayer1 = requireActivity().findViewById(R.id.text_view_p1);
        textViewPlayer2 = requireActivity().findViewById(R.id.text_view_p2);
        textViewTurn = requireActivity().findViewById(R.id.text_view_turn);
        textViewRound = requireActivity().findViewById(R.id.text_view_round);

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                String buttonName = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonName, "id",
                        requireActivity().getPackageName());
                buttons[i][j] = requireActivity().findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        resetButton = requireActivity().findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        if(savedInstanceState != null){
            round = savedInstanceState.getInt("round");
            timer = savedInstanceState.getInt("timer");
            player1Score = savedInstanceState.getInt("player1Score");
            player2Score = savedInstanceState.getInt("player2Score");
            player1Turn = savedInstanceState.getBoolean("player1Turn");
        }
    }

    @Override
    public void onClick(View v) {
        if(!((Button)v).getText().toString().equals("")){
            return;
        }
        if(player1Turn){
            ((Button)v).setText("x");
            ((Button)v).setTextColor(Color.parseColor("#FF0000"));
            textViewTurn.setText("This turn: Player 2");
            textViewTurn.setTextColor(Color.parseColor("#0000FF"));
        }
        else{
            ((Button)v).setText("o");
            ((Button)v).setTextColor(Color.parseColor("#0000FF"));

            textViewTurn.setText("This turn: Player 1");
            textViewTurn.setTextColor(Color.parseColor("#FF0000"));
        }

        timer ++;


        if(checkWin()){
            if(player1Turn){
                round++;
                player1Win();
            }
            else{
                round++;
                player2Win();
            }
        }
        else if(timer == 9){
            round++;
            draw();
        }
        else{
            player1Turn = !player1Turn;
        }
    }

    private boolean checkWin() {
        String[][] sign = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sign[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (sign[i][0].equals(sign[i][1]) && sign[i][0].equals(sign[i][2])
                    && !sign[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (sign[0][i].equals(sign[1][i]) && sign[0][i].equals(sign[2][i])
                    && !sign[0][i].equals("")) {
                return true;
            }
        }

        if (sign[0][0].equals(sign[1][1]) && sign[0][0].equals(sign[2][2])
                && !sign[0][0].equals("")) {
            return true;
        }

        if (sign[0][2].equals(sign[1][1]) && sign[0][2].equals(sign[2][0])
                && !sign[0][2].equals("")) {
            return true;
        }
        return false;
    }

    private void player1Win(){
        player1Score++;
        Context context = requireActivity().getApplicationContext();
        Toast.makeText(context, "Player 1 wins this round!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void player2Win(){
        player2Score++;
        Context context = requireActivity().getApplicationContext();
        Toast.makeText(context, "Player 2 wins this round!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        resetBoard();
    }

    private void draw(){
        Context context = requireActivity().getApplicationContext();
        Toast.makeText(context, "Draw!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    public void updatePointsText(){
        textViewPlayer1.setText("Player 1: "+ player1Score);
        textViewPlayer2.setText("Player 2: "+ player2Score);
    }

    public void resetBoard() {
        checkRound();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                buttons[i][j].setText("");
            }
        }
        timer = 0;
//        player1Turn = true;
    }

    public void resetGame(){
        player1Score = 0;
        player2Score = 0;
        round = 1;
        textViewRound.setText("Round: 1");
        textViewTurn.setText("This turn: Player 1");
        textViewTurn.setTextColor(Color.parseColor("#FF0000"));
        updatePointsText();
        resetBoard();
    }

    public void checkRound(){
        if(player1Score == 3){
            Context context = requireActivity().getApplicationContext();
            Toast.makeText(context, "Player 1 wins this game! Game is over", Toast.LENGTH_SHORT).show();
            resetGame();
        }
        else if(player2Score == 3){
            Context context = requireActivity().getApplicationContext();
            Toast.makeText(context, "Player 2 wins this game! Game is over", Toast.LENGTH_SHORT).show();
            resetGame();
        }
        else if(round == 6){
            if(player1Score > player2Score){
                Context context = requireActivity().getApplicationContext();
                Toast.makeText(context, "Player 1 wins this game! Game is over", Toast.LENGTH_SHORT).show();
                resetGame();
            }
            else if(player1Score > player2Score){
                Context context = requireActivity().getApplicationContext();
                Toast.makeText(context, "Player 2 wins this game! Game is over", Toast.LENGTH_SHORT).show();
                resetGame();
            }
            else{
                Context context = requireActivity().getApplicationContext();
                Toast.makeText(context, "No player wins this game! Game is over", Toast.LENGTH_SHORT).show();
                resetGame();
            }
        }
        else{
            textViewRound.setText("Round: "+round);
            if(round % 2 == 1){
                player1Turn = true;
                textViewTurn.setText("This turn: Player 1");
                textViewTurn.setTextColor(Color.parseColor("#FF0000"));
            }
            else{
                player1Turn = false;
                textViewTurn.setText("This turn: Player 2");
                textViewTurn.setTextColor(Color.parseColor("#0000FF"));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("round",round);
        outState.putInt("timer",timer);
        outState.putInt("player1Score",player1Score);
        outState.putInt("player2Score",player2Score);
        outState.putBoolean("player1Turn",player1Turn);
    }

}