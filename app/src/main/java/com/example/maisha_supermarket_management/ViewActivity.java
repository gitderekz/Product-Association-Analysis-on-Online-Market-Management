package com.example.maisha_supermarket_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewActivity extends AppCompatActivity {

    private ImageView imageView;
    TextView textView;
    TextView loadedDescription;
    TextView loadedPrice;
    Button btnDelete;
    String categories;

    DatabaseReference ref,DataRef;
    StorageReference StorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        imageView=findViewById(R.id.image_single_view_Activity);
        textView=findViewById(R.id.textView_single_view_activity);
        loadedPrice=findViewById(R.id.loadedPrice);
        loadedDescription=findViewById(R.id.loadedDescription);
        btnDelete=findViewById(R.id.btnDelete);

        categories = getIntent().getStringExtra("category");
        String CarKey=getIntent().getStringExtra("CarKey");

        ref= FirebaseDatabase.getInstance().getReference().child(categories);

        DataRef=FirebaseDatabase.getInstance().getReference().child(categories).child(CarKey);
        StorageRef=FirebaseStorage.getInstance().getReference().child("CarImage").child(CarKey+".jpg");

        ref.child(CarKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    String carName=dataSnapshot.child("CarName").getValue().toString();
                    String loadPrice=dataSnapshot.child("ItemPrice").getValue().toString();
                    String loadDescription=dataSnapshot.child("ItemDescription").getValue().toString();
                    String ImageUrl=dataSnapshot.child("ImageUrl").getValue().toString();

                    Picasso.get().load(ImageUrl).into(imageView);
                    textView.setText(carName);
                    loadedPrice.setText(loadPrice);
                    loadedDescription.setText(loadDescription);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        StorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            }
                        });
                    }
                });
            }
        });
    }
}
