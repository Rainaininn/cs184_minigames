package edu.ucsb.cs.cs184.runyu.minigames.ui.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsb.cs.cs184.runyu.minigames.R;

import static android.app.Activity.RESULT_OK;

public class SudokuFragment extends Fragment {

    private SudokuViewModel sudokuViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sudokuViewModel =
                new ViewModelProvider(this).get(SudokuViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sudoku, container, false);

        return root;
    }

}