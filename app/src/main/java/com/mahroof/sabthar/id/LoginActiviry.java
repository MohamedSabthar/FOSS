package com.mahroof.sabthar.id;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActiviry extends AppCompatActivity {

    private TextInputLayout TextLoyout_email, TextLayout_password; //variables for EditText

    private FirebaseAuth mAuth;  //declaring firebase authentication

    private Button Button_login;

//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activiry);

        //Initializing firebase auth
        mAuth = FirebaseAuth.getInstance();

        //link between XML and variables
        Button_login = findViewById(R.id.loginid);



        TextLoyout_email = findViewById(R.id.InputLaoyout_mailid);
        TextLayout_password = findViewById(R.id.ImputLaoyout_passwordid);

        //Initializing functions to Button_login
        Button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = TextLoyout_email.getEditText().getText().toString();
                String password = TextLayout_password.getEditText().getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                    Toast.makeText(LoginActiviry.this, "Fields Can't be Empty", Toast.LENGTH_SHORT).show();
                else {
//

                    //checking the network status
                    if (checkNetWork())
                        loginUser(email, password);
                    else
                        Toast.makeText(LoginActiviry.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    //function to check the network status
    private boolean checkNetWork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfos = connectivityManager.getActiveNetworkInfo();

        if (networkInfos != null)
            return true;

        return false;
    }


    //


    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {
                    Intent mainIntent = new Intent(LoginActiviry.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Toast.makeText(LoginActiviry.this, "Conflicting Credentials", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


}
