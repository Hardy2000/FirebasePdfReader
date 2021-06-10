package com.example.firebasepdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    EditText t1,t2;
    ProgressBar bar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();

        //auto login process
        //move to main activity if user already sign in
        if (mAuth.getCurrentUser() != null) {
            // User is logged in
            startActivity(new Intent(MainActivity.this, PdfActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);


        t1=(EditText)findViewById(R.id.email_login);
        t2=(EditText) findViewById(R.id.pwd_login);
        bar=(ProgressBar)findViewById(R.id.progressBar3);

        mAuth= FirebaseAuth.getInstance();



    }

    public void signinhere(View view) {


        String email=t1.getText().toString();
        String password=t2.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this.getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this.getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        bar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {   bar.setVisibility(View.INVISIBLE);

                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(MainActivity.this,PdfActivity.class);
                            intent.putExtra("email",mAuth.getCurrentUser().getEmail());
                            intent.putExtra("uid",mAuth.getCurrentUser().getUid());
                            startActivity(intent);
                            finish();

                        }
                        else
                        {
                            bar.setVisibility(View.INVISIBLE);

                            Toast.makeText(MainActivity.this, "Login Failed Invalid Email/Password", Toast.LENGTH_LONG).show();

                        }


                    }
                });


    }

    public void gotosignin (View view){
        startActivity(new Intent(MainActivity.this,SignUpActivity.class));
    }


}


