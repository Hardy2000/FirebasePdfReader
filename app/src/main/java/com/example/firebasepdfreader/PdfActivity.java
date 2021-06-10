package com.example.firebasepdfreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class PdfActivity extends AppCompatActivity {
 FloatingActionButton fb;
// private Toolbar toolbar;
 ImageView logoutbtn;
  RecyclerView recview;
  myadapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
      //Toolbar toolbar=findViewById(R.id.myToolbar);
        logoutbtn=(ImageView)findViewById(R.id.logout_btn);
        fb=(FloatingActionButton)findViewById(R.id.floatingActionButton);

    fb.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
       startActivity(new Intent(PdfActivity.this,uploadfile.class));

        }
    });

       logoutbtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

             FirebaseAuth.getInstance().signOut();
               Toast.makeText(PdfActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
               startActivity(new Intent(PdfActivity.this,MainActivity.class));
               finish();

           }
       });


       recview=(RecyclerView)findViewById(R.id.recview);
       recview.setLayoutManager(new LinearLayoutManager(this));

     FirebaseRecyclerOptions<model>options=
             new FirebaseRecyclerOptions.Builder<model>()
             .setQuery(FirebaseDatabase.getInstance().getReference().child("mydocuments"),model.class)
             .build();

     adapter=new myadapter(options);
     recview.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
    adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}