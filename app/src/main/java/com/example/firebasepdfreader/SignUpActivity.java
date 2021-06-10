package com.example.firebasepdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    EditText t1, t2,t3;
    ProgressBar bar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        t1 = (EditText) findViewById(R.id.email);
        t2 = (EditText) findViewById(R.id.pwd);
        t3 = (EditText) findViewById(R.id.rpwd);
        bar = (ProgressBar) findViewById(R.id.progressBar);

    }
        public void signuphere (View view){

            String email = t1.getText().toString().trim();
            String password = t2.getText().toString().trim();
            String repassword = t3.getText().toString().trim();


            if (TextUtils.isEmpty(email)) {
                Toast.makeText(SignUpActivity.this,"Enter email address!",Toast.LENGTH_SHORT).show();
                return;
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(SignUpActivity.this,"Enter Password!",Toast.LENGTH_SHORT).show();
                return;
            }

            if(password.length() < 6 ){
                Toast.makeText(SignUpActivity.this,"Password too short, enter minimum 6 characters",Toast.LENGTH_SHORT).show();
                return;
            }

            if( password.compareTo(repassword)!=0)
            {
                Toast.makeText(SignUpActivity.this,"Passwords doesn't Match",Toast.LENGTH_SHORT).show();
                return;
            }


            bar.setVisibility(View.VISIBLE);

            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                bar.setVisibility(View.INVISIBLE);
                                t1.setText("");
                                t2.setText("");
                                Toast.makeText(SignUpActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                finish();
                            } else {
                                bar.setVisibility(View.INVISIBLE);
                                t1.setText("");
                                t2.setText("");
                                Toast.makeText(SignUpActivity.this, "Not Successful", Toast.LENGTH_LONG).show();

                            }

                        }
                    });


        }




    }
