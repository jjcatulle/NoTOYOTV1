package com.example.oyotv1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText reg_email_field;
    private EditText reg_pass_field;
    private EditText reg_confirm_pass_field;
    private Button reg_btn;
    private Button reg_login_btn;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();

        reg_email_field=(EditText) findViewById(R.id.reg_email);
        reg_pass_field=(EditText) findViewById(R.id.reg_pass);
        reg_confirm_pass_field=(EditText) findViewById(R.id.reg_confirm_pass);
        reg_btn=(Button) findViewById(R.id.reg_btn);
        reg_login_btn=(Button) findViewById(R.id.reg_login_btn);
        /////////////////////////////go back to the login page///////////
        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogin();
            }
        });
///////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////Register
        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=reg_email_field.getText().toString();
                String pass=reg_pass_field.getText().toString();
                String confirm_pass=reg_confirm_pass_field.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) & !TextUtils.isEmpty(confirm_pass) ){

                    if (pass.equals(confirm_pass)){
                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
//
//                                    Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
//                                    startActivity(setupIntent);
//                                    finish();
                                    Toast.makeText(RegisterActivity.this," Registering",Toast.LENGTH_LONG).show();
                                    sendToMain();

                                }else {

                                    String errorMessage=task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this,"error:"+errorMessage, Toast.LENGTH_LONG).show();


                                }
                            }
                        });
                    }else{
                        Toast.makeText(RegisterActivity.this,"Password does not mach", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////

    }

    private void sendToLogin() {
        Intent mainIntent=new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser=mAuth.getCurrentUser();
        if (currentUser != null){
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent=new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }


}
