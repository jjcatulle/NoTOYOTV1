package com.example.oyotv1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {



    private CircleImageView setupimage;
    private Uri resultUri =null;

    private EditText setupName;
    private EditText setupEmail;
    private Button setupBtn;
    private Button setupCancelbtn;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
//    private android.util.Log Log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Toolbar setupToolbar =findViewById(R.id.setupToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Profile");

        firebaseAuth= FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();

        setupimage=findViewById(R.id.setup_image);
        setupName=findViewById(R.id.setup_name);
        setupEmail=findViewById(R.id.setup_email);
        setupBtn=findViewById(R.id.setup_btn);
        setupCancelbtn=findViewById(R.id.setup_cancel_btn);



        /////////////////////////////

        setupCancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent=new Intent(SetupActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

//////////////////////////////////
        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
////////// EVERYTHING TO FIREEBASE STORAGE
                String user_name=setupName.getText().toString();
                String user_email=setupEmail.getText().toString();


                if (!TextUtils.isEmpty(user_name)){  /////check if image is empty;

                    String user_id=firebaseAuth.getCurrentUser().getUid();

//                    StorageReference image_path=storageReference.child("profile_image").child(user_id+ ".jpg");
//                    image_path.putFile(bitmap).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                            if(task.isSuccessful()){
//
//                            }else {
//                                String error=task.getException().getMessage();
//                                Toast.makeText(SetupActivity.this, "error: "+error,Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//                    });

                    ///////////////////////////////////////////////////////
                    ///save on firebaseFirestore
                    ////////schema

                    Map <String, String> userMap= new HashMap<>();
                    userMap.put("name", user_name);
                    userMap.put("email", user_email);

                    firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(SetupActivity.this, "Profile Save ",Toast.LENGTH_LONG).show();
                                Intent mainIntent= new Intent(SetupActivity.this,MainActivity.class);
                                startActivity(mainIntent);
                                finish();

                            }else{
                                String error=task.getException().getMessage();
                                Toast.makeText(SetupActivity.this, "error: "+error,Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    ////////////////////////////////////////////////////////////////////////
                }

            }
        });


/////////////////////////////////////////////////////////////////////
        setupimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                        Toast.makeText(SetupActivity.this,"Permission denied",Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                    }else{
                        Toast.makeText(SetupActivity.this,"Pick an image",Toast.LENGTH_LONG).show();



                    }
                }

            }
        });


    }

}
