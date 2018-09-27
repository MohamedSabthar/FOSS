package com.mahroof.sabthar.id;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    //Firebase Authentication Declaration
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing Firebase Authentication
        mAuth = FirebaseAuth.getInstance();


    }

    public void onStart() {
        super.onStart();

        //reading current user
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //checking whether any user loged in
        if (currentUser == null) {

            //redirecting to Welcome Intent
            Intent startIntent = new Intent(MainActivity.this, WellcomeActiviry.class);
            startActivity(startIntent);
            finish();
        } else {
            //Redirecting to ViewID intent
            Intent viewIdIntent = new Intent(MainActivity.this, ViewIdActiviry.class);
            startActivity(viewIdIntent);
            finish();

        }


    }


}
