package edu.ucsb.cs.cs184.runyu.minigames.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import edu.ucsb.cs.cs184.runyu.minigames.MainActivity;
import edu.ucsb.cs.cs184.runyu.minigames.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ImageView imageView;
    TextView name, email, score1, score2, score3, scorePrompt, scoreF, scoreS, scoreM;
    Button signOut;
    //Google Sign in
    private static GoogleSignInClient mGoogleSignInClient;
    SignInButton signin;
    int RC_SIGN_IN = 0;
    private static boolean isSignedIn = false;
    private static GoogleSignInAccount acct;
    private static Task<GoogleSignInAccount> task;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        imageView = (ImageView) root.findViewById(R.id.imageProfile);
        name = (TextView) root.findViewById(R.id.textName);
        email = (TextView) root.findViewById(R.id.textEmail);
        score1 = (TextView) root.findViewById(R.id.textViewScore1);
        score2 = (TextView) root.findViewById(R.id.textViewScore2);
        score3 = (TextView) root.findViewById(R.id.textViewScore3);
        scorePrompt = (TextView) root.findViewById(R.id.textViewScore);
        scoreF = (TextView) root.findViewById(R.id.textViewScoreF);
        scoreS = (TextView) root.findViewById(R.id.textViewScoreS);
        scoreM = (TextView) root.findViewById(R.id.textViewScoreM);
        signOut = (Button) root.findViewById(R.id.buttonSignOut);
        signin = root.findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        if(isSignedIn == false){
//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestEmail()
//                    .build();
//            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
            makeInvisible();
            signin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.sign_in_button:
                            signIn();
                            break;
                        // ...
                    }
                }
            });
        }
        else{
            makeVisible();
            handleSignInResult(task);
            signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.buttonSignOut:
                            signOut();
                            break;
                    }
                }
            });
        }

//        signin.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.sign_in_button:
//                        signIn();
//                        break;
//                    // ...
//                }
//            }
//        });
//        signOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.buttonSignOut:
//                        signOut();
//                        break;
//                }
//            }
//        });

        return root;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            makeVisible();

            acct = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                Uri personPhoto = acct.getPhotoUrl();

                name.setText(personName);
                email.setText(personEmail);
                Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);

                isSignedIn = true;
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getActivity(), "Signed out successfully!", Toast.LENGTH_SHORT).show();
                    makeInvisible();
                    isSignedIn = false;
                }
            });
    }

    private void makeInvisible(){
        imageView.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        score1.setVisibility(View.INVISIBLE);
        score2.setVisibility(View.INVISIBLE);
        score3.setVisibility(View.INVISIBLE);
        scorePrompt.setVisibility(View.INVISIBLE);
        scoreF.setVisibility(View.INVISIBLE);
        scoreS.setVisibility(View.INVISIBLE);
        scoreM.setVisibility(View.INVISIBLE);
        signOut.setVisibility(View.INVISIBLE);
        signin.setVisibility(View.VISIBLE);

    }

    private void makeVisible(){
        imageView.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        score1.setVisibility(View.VISIBLE);
        score2.setVisibility(View.VISIBLE);
        score3.setVisibility(View.VISIBLE);
        scorePrompt.setVisibility(View.VISIBLE);
        scoreF.setVisibility(View.VISIBLE);
        scoreS.setVisibility(View.VISIBLE);
        scoreM.setVisibility(View.VISIBLE);
        signOut.setVisibility(View.VISIBLE);
        signin.setVisibility(View.INVISIBLE);
    }

}