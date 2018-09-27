package com.mahroof.sabthar.id;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WellcomeActiviry extends AppCompatActivity {

    private Button mLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome_activiry);

        mLoginBtn = findViewById(R.id.welcomelogin);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(WellcomeActiviry.this, LoginActiviry.class);
                startActivity(loginActivity);
                finish();
            }
        });


    }


}
