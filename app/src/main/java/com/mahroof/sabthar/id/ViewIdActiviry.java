package com.mahroof.sabthar.id;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ViewIdActiviry extends AppCompatActivity {

    private DatabaseReference mUserDatabase;     //to retrive Firebase database link
    private FirebaseUser mCurrentUser;           //to get the current user

    private String current_uid;               //variable to store the id of the user
    private String Name,NIC,RegNo,Address;
    private TextView TextView_name, TextView_address, TextView_RegNo, TextView_NIC;              //variables for text view
    private ImageView ImageView_pic, ImageView_barcode, ImageView_signatre,ImageView_Qrcode;            //variables for image view

    private Button Button_logout,Button_share;                //logout button


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_id_activiry);

        Button_logout = findViewById(R.id.logoutid);
        //initializing a function to button
        Button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutMeOut();
            }
        });

        Button_share=findViewById(R.id.shareid);
        //Initializing share intent to the button
        Button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent=new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"MY DETAILS\n---------------------");
                shareIntent.putExtra(Intent.EXTRA_TEXT,Name+"\n"+RegNo+"\n"+Address);
                startActivity(Intent.createChooser(shareIntent,"SHARE VIA"));
            }
        });



        //chain between XML and variables
        TextView_name = findViewById(R.id.nameid1);
        TextView_address = findViewById(R.id.addrid);
        TextView_RegNo = findViewById(R.id.RegNoid);
        TextView_NIC = findViewById(R.id.Nicid);


        ImageView_barcode = findViewById(R.id.barcodeid);
        ImageView_signatre = findViewById(R.id.sigid);
        ImageView_pic = findViewById(R.id.imagelinkid);
        ImageView_Qrcode=findViewById(R.id.Qrcodeid);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        //retrieving current user id
        current_uid = mCurrentUser.getUid();

        //setting up the link to database reference
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);

        //retrieving the child details from database snapshots
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //get the string values of children
                 Name = dataSnapshot.child("Name").getValue().toString();
                NIC = dataSnapshot.child("NIC").getValue().toString();
                 RegNo = dataSnapshot.child("RegNo").getValue().toString();
                Address = dataSnapshot.child("Address").getValue().toString();

                //image links
                final String ID_image = dataSnapshot.child("Image").getValue().toString();         //1
                final String Signature = dataSnapshot.child("signature").getValue().toString();    //2


                //setting the string to text view
                TextView_name.setText(Name);
                TextView_NIC.setText(NIC);
                TextView_RegNo.setText(RegNo);
                TextView_address.setText(Address);

                //Including details in qr
                StringBuilder buffer=new StringBuilder();
                buffer.append(Name+"\n"+RegNo+"\n"+Address);
                String qr=buffer.toString();

                String bar;
                if(NIC.length()==10)
                bar=NIC.substring(0,9);
                else
                    bar=NIC;

                //generating Qrcode
                MultiFormatWriter Qrcode_Writer=new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix=Qrcode_Writer.encode(qr, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder=new BarcodeEncoder() ;
                    Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                    ImageView_Qrcode.setImageBitmap(bitmap);


                }
                catch (WriterException e)
                {
                    e.printStackTrace();
                }

            //generating Barcode
                MultiFormatWriter Barcode_Writer=new MultiFormatWriter();
                try{
                    BitMatrix bitMatrix=Barcode_Writer.encode(bar, BarcodeFormat.CODE_39,1000,200);
                    BarcodeEncoder barcodeEncoder=new BarcodeEncoder() ;
                    Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                    ImageView_barcode.setImageBitmap(bitmap);

                }
                catch (WriterException e)
                {
                    e.printStackTrace();
                }







                //setting the image links to image view          //networkPolicy(NetworkPolicy.OFFLINE) for load image from offline which are previously downloaded if user already loged in
                //Callback for if user not loged in previously

                //1
                Picasso.get().load(ID_image).networkPolicy(NetworkPolicy.OFFLINE).into(ImageView_pic, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(ID_image).into(ImageView_pic);
                    }
                });




                //2
                Picasso.get().load(Signature).networkPolicy(NetworkPolicy.OFFLINE).into(ImageView_signatre, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(Signature).into(ImageView_signatre);
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }


        });


    }


    //function to logout
    public void logoutMeOut() {
        FirebaseAuth.getInstance().signOut();
        Intent welcomeIntent = new Intent(ViewIdActiviry.this, WellcomeActiviry.class);
        startActivity(welcomeIntent);
        finish();

    }
}
