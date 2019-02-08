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

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginBtn;
    private Button loginRegBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
////////////finding the fields
        loginEmailText=(EditText) findViewById(R.id.login_email);
        loginPassText=(EditText) findViewById(R.id.login_password);
        loginBtn=(Button) findViewById(R.id.login_btn);
        loginRegBtn=(Button) findViewById(R.id.login_reg_btn);

        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regIntent);
            }
        });

        ///what happened when user click login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail=loginEmailText.getText().toString();
                String loginPass=loginPassText.getText().toString();

                ////if statement to see if they not empty
                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)){
                    //signin
                    mAuth.signInWithEmailAndPassword(loginEmail,loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            ///if user em nd pass is good
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Login in",Toast.LENGTH_LONG).show();

                                sendToMain();

                            }else {
                                String errorMessage=task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"error"+errorMessage,Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
            }
        });

    }
    //////when its start
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser= mAuth.getCurrentUser();
        if (currentUser !=null){
            sendToMain();

        }

    }

    private void sendToMain() {


        Intent mainIntent= new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
