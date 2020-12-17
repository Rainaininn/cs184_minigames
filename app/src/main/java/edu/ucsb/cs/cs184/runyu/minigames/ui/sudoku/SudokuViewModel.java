package edu.ucsb.cs.cs184.runyu.minigames.ui.sudoku;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SudokuViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SudokuViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is sudoku fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}