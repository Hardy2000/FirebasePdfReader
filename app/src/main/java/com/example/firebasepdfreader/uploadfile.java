package com.example.firebasepdfreader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class uploadfile extends AppCompatActivity {

    ImageView imagebrowse,imageupload,filelogo,cancelfile;
    EditText filetitle;
    Uri filepath;

    StorageReference storageReference;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadfile);

        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("mydocuments");

        filetitle=findViewById(R.id.filetitle);
        filelogo=findViewById(R.id.filelogo);
        cancelfile=findViewById(R.id.cancelfile);
        imagebrowse=findViewById(R.id.imagebrowse);
        imageupload=findViewById(R.id.imageupload);

        filelogo.setVisibility(View.INVISIBLE);
        cancelfile.setVisibility(View.INVISIBLE);

        cancelfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filepath=null;
                filelogo.setVisibility(View.INVISIBLE);
                cancelfile.setVisibility(View.INVISIBLE);
                imagebrowse.setVisibility(View.VISIBLE);
            }
        });

        imagebrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent,"Select pdf File"),101);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        imageupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(filepath==null)
                {Toast.makeText(uploadfile.this,"Select File Before Upload",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(filetitle.getText().toString().trim()))
                { Toast.makeText(uploadfile.this,"Set Title of the Pdf Before Upload",Toast.LENGTH_SHORT).show();
                    return;
                }

                processupload(filepath);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    if(requestCode==101 && resultCode==RESULT_OK)
    {
        filepath=data.getData();
        filelogo.setVisibility(View.VISIBLE);
        cancelfile.setVisibility(View.VISIBLE);
        imagebrowse.setVisibility(View.INVISIBLE);
    }

    }


    private void processupload(Uri filepath) {


       final ProgressDialog pd= new ProgressDialog(this);
        pd.setTitle("File Uploading....");
        pd.show();

   final StorageReference reference=storageReference.child("upload/"+System.currentTimeMillis()+".pdf");
    reference.putFile(filepath)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                 reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {

                         fileinfomodel obj=new fileinfomodel(filetitle.getText().toString(),uri.toString());

                         databaseReference.child(databaseReference.push().getKey()).setValue(obj);
                         pd.dismiss();
                         Toast.makeText(getApplicationContext(),"File uploaded Successfully",Toast.LENGTH_LONG).show();
                         filelogo.setVisibility(View.INVISIBLE);
                         cancelfile.setVisibility(View.INVISIBLE);
                         imagebrowse.setVisibility(View.VISIBLE);
                         filetitle.setText("");


                     }
                 });
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                   float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    pd.setMessage("Uploaded "+(int)percent+"%");
                }
            });


    }



}