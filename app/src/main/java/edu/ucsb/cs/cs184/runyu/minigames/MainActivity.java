package edu.ucsb.cs.cs184.runyu.minigames;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import edu.ucsb.cs.cs184.runyu.minigames.ui.home.HomeFragment;
import edu.ucsb.cs.cs184.runyu.minigames.ui.home.HomeViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Object InstructionsActivity;
    //Google Sign In
    SignInButton signin;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    //After Sign In
    ImageView imageView;
    TextView name, email, score1, score2, score3, scorePrompt, scoreF, scoreS, scoreM;
    Button signOut;
    boolean isSignedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        imageView = findViewById(R.id.imageProfile);
//        name = findViewById(R.id.textName);
//        email = findViewById(R.id.textEmail);
//        score1 = findViewById(R.id.textViewScore1);
//        score2 = findViewById(R.id.textViewScore2);
//        score3 = findViewById(R.id.textViewScore3);
//        scorePrompt = findViewById(R.id.textViewScore);
//        scoreF = findViewById(R.id.textViewScoreF);
//        scoreS = findViewById(R.id.textViewScoreS);
//        scoreM = findViewById(R.id.textViewScoreM);
//        signOut = findViewById(R.id.buttonSignOut);


//        View homeView = findFragmentById(R.id.fragment_home);
//        signin = getView().findViewById(R.id.sign_in_button);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onShowSudokuInstructionsButtonClicked(View view) {
        Log.d("slideshow", "show instruction button clicked in main");
        Intent intent = new Intent("edu.ucsb.cs.cs184.runyu.InstructionsActivity");
        startActivity(intent);
    }

    public void onStartNewSudokuButtonClicked(View view) {
        Intent intent = new Intent("edu.ucsb.cs.cs184.runyu.GameDifficultyActivity");
        startActivity(intent);
    }

    //? Remove delete?
    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
    }

//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
//    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
////            updateUI(account);
//
//
//
//            Intent intent = new Intent(MainActivity.this, HomeFragment.class);
//            signin.setVisibility(View.INVISIBLE);
//            startActivity(intent);
//
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
//        }
//    }

//    private void makeVisible(){
//        imageView.setVisibility(View.VISIBLE);
//        name.setVisibility(View.VISIBLE);
//        email.setVisibility(View.VISIBLE);
//        score1.setVisibility(View.VISIBLE);
//        score2.setVisibility(View.VISIBLE);
//        score3.setVisibility(View.VISIBLE);
//        scorePrompt.setVisibility(View.VISIBLE);
//        scoreF.setVisibility(View.VISIBLE);
//        scoreS.setVisibility(View.VISIBLE);
//        scoreM.setVisibility(View.VISIBLE);
//        signOut.setVisibility(View.VISIBLE);
//        signin.setVisibility(View.INVISIBLE);
//    }

}