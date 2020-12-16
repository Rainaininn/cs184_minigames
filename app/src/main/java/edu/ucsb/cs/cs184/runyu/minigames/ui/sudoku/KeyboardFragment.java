package edu.ucsb.cs.cs184.runyu.minigames.ui.sudoku;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import edu.ucsb.cs.cs184.runyu.minigames.R;

public class KeyboardFragment extends Fragment {
    private int groupId;
    private View view;
    private OnKeyboardFragmentInteractionListener mListener;

    public KeyboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_keyboard, container, false);

//        Set textview click listeners
        int KeyboardButtons[] = new int[]{R.id.buttonOne, R.id.buttonTwo, R.id.buttonThree, R.id.buttonFour,
                R.id.buttonFive, R.id.buttonSix, R.id.buttonSeven, R.id.buttonEight, R.id.buttonNine, R.id.buttonDelete};
        for (int kb1 : KeyboardButtons) {
            Button button = view.findViewById(kb1);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Keyboard ", "the button is " + button.getText().toString());
                    if(button.getText().toString().equals("Delete")){
                        mListener.onKeyboardFragmentInteraction(0);
                    }else {
                        mListener.onKeyboardFragmentInteraction(Integer.parseInt(button.getText().toString()));
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnKeyboardFragmentInteractionListener) {
            mListener = (OnKeyboardFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnKeyboardFragmentInteractionListener {
        void onKeyboardFragmentInteraction(int buttonNum);
    }

}
