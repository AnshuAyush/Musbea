package com.example.musbea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 101;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(MainActivity.this , MainActivity2.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // UI Effect
        Animation opacity_for_bubble = AnimationUtils.loadAnimation(MainActivity.this , R.anim.opacity_for_bubble);
        Animation opacity_0_to_100 = AnimationUtils.loadAnimation(MainActivity.this , R.anim.opacity_for_0_to_100);
        LottieAnimationView login_circle = findViewById(R.id.login_circle);
        LottieAnimationView bubble = findViewById(R.id.bubble);
        LottieAnimationView spark = findViewById(R.id.spark);
        bubble.setVisibility(View.GONE);
        spark.setVisibility(View.VISIBLE);
        LottieAnimationView loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        LottieAnimationView sign_up = findViewById(R.id.sign_UP);
        sign_up.setVisibility(View.VISIBLE);

        login_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spark.playAnimation();
                Runnable hide = new Runnable() {
                    @Override
                    public void run() {
                        bubble.setVisibility(View.GONE);

                    }
                };

                bubble.setVisibility(View.VISIBLE);

                bubble.setAnimation(opacity_0_to_100);
                bubble.postDelayed(hide , 1000);
                bubble.setAnimation(opacity_for_bubble);

            }

        });


        // Sign with Google
        mAuth = FirebaseAuth.getInstance();
        LottieAnimationView sign_in_with_google = findViewById(R.id.sign_in_with_google);
        sign_in_with_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessRequest();
                ProcessLogin();
                login_circle.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);

            }
        });

        // Sign Up here
        LottieAnimationView Sign_up = findViewById(R.id.sign_UP);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent create_your_account = new Intent(MainActivity.this , createYourAccount.class);
                startActivity(create_your_account);
            }
        });

    }


    private void ProcessRequest(){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void ProcessLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent , RC_SIGN_IN);
    }
    // Email connect to fireBase and authentication
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Going to main app after login
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(MainActivity.this , MainActivity2.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Error !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}