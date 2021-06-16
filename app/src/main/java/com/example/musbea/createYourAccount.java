package com.example.musbea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class createYourAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceate_your_accout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // Create Account
        FirebaseAuth fAuth;
        fAuth = FirebaseAuth.getInstance();
        Button create_account = findViewById(R.id.create_account);



        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email = findViewById(R.id.email);
                EditText password = findViewById(R.id.password);
                String email_str = email.getText().toString().trim();
                String password_str = password.getText().toString().trim();

                if(TextUtils.isEmpty(email_str)){
                    Toast.makeText(createYourAccount.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password_str)){
                    Toast.makeText(createYourAccount.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password_str.length() < 6){
                    Toast.makeText(createYourAccount.this, "Password too short", Toast.LENGTH_SHORT).show();
                }

                fAuth.createUserWithEmailAndPassword(email_str , password_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(createYourAccount.this, "User created", Toast.LENGTH_SHORT).show();
                            Intent go_app = new Intent(createYourAccount.this , MainActivity2.class);
                            startActivity(go_app);
                            finish();
                        }
                        else {
                            Toast.makeText(createYourAccount.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });




        }
    }
