package com.example.maisha_supermarket_management;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE =101 ;
    private static final int REQUEST_CODE_IMAGE1 =101 ;
    private static final int REQUEST_CODE_IMAGE2 =101 ;
    private static final int REQUEST_CODE_IMAGE3 =101 ;
    private ImageView imageViewAdd;
    private ImageView imageViewAdd1;
    private ImageView imageViewAdd2;
    private ImageView imageViewAdd3;
    private EditText inputImageName;
    private EditText price;
    private EditText description,product_id,product_count;
    private TextView textViewProgress;
    private ProgressBar progressBar;
    private Button btnUpload;

    private ArrayList<Uri> uriList = new ArrayList<Uri>();
    Uri imageUri;
    Uri imageUri1;
    Uri imageUri2;
    Uri imageUri3;
    boolean isImageAdded=false;
    int loopCounter = 0;

    DatabaseReference Dataref;
    StorageReference  StorageRef;
    String category;
    private Uri[] uriArray = new Uri[0];
    private String myUri;
    private String myUri1;
    private String myUri2;
    private String myUri3;

    private Spinner item_category;
    private boolean mSpinnerInitialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewAdd=findViewById(R.id.imageVIewAdd);
//        imageViewAdd1=findViewById(R.id.imageVIewAdd1);
//        imageViewAdd2=findViewById(R.id.imageVIewAdd2);
//        imageViewAdd3=findViewById(R.id.imageVIewAdd3);

        inputImageName=findViewById(R.id.inputImageName);
        price = findViewById(R.id.mainPrice);
        description = findViewById(R.id.description);
        product_id = findViewById(R.id.item_id);
        product_count = findViewById(R.id.item_count);
        textViewProgress=findViewById(R.id.textViewProgress);
        progressBar=findViewById(R.id.progressBar);
        btnUpload=findViewById(R.id.btnUpload);
        item_category = findViewById(R.id.item_category);


        textViewProgress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);


        StorageRef= FirebaseStorage.getInstance().getReference().child("CarImage");

        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });
//        imageViewAdd1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,REQUEST_CODE_IMAGE1);
//            }
//        });
//        imageViewAdd2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,REQUEST_CODE_IMAGE2);
//            }
//        });
//        imageViewAdd3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,REQUEST_CODE_IMAGE3);
//            }
//        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String imageName=inputImageName.getText().toString();
                final String imagePrice=price.getText().toString();
                final String imageDescription=description.getText().toString();
//                final String item_id=product_id.getText().toString();
                final int item_id=Integer.parseInt(product_id.getText().toString());
                final String item_count=product_count.getText().toString();
                if (isImageAdded!=false && imageName!=null && /*item_id!=null*/item_id!=0 && item_count!=null && category!=null && category!="")
                {
//                    category = imageCategory.getText().toString();
                    Dataref= FirebaseDatabase.getInstance().getReference().child(category);
                    uploadImage(imageName,imagePrice,imageDescription,item_id,item_count);
                }else {
                    Toast.makeText(MainActivity.this,"Fill correctly all fields",Toast.LENGTH_LONG).show();
                }
            }
        });
        item_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mSpinnerInitialized) {
                    mSpinnerInitialized = true;
                    return;
                }
                String selectedDistrict = (String) parent.getItemAtPosition(position);

                if (selectedDistrict.equals("DRINKS")||selectedDistrict.equals("FOOD")||selectedDistrict.equals("HOME")
                        ||selectedDistrict.equals("SPORTS") ||selectedDistrict.equals("SNACKS & BITES")
                        ||selectedDistrict.equals("Category")/*||selectedDistrict.equals("home")*/||selectedDistrict.equals("----"))
                {
                    category = "";
                    Toast.makeText(MainActivity.this,"Wrong category selected!",Toast.LENGTH_LONG).show();
                }else{
                    category = selectedDistrict;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void uploadImage(final String imageName,final String price,final String description,int item_id,String item_count) {
        textViewProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

       final  String key=Dataref.push().getKey();
        StorageRef.child(key+".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageRef.child(key +".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        myUri = uri.toString();

                        HashMap hashMap = new HashMap();
                        hashMap.put("CarName",imageName);
                        hashMap.put("ImageUrl", myUri);
                        hashMap.put("itemId", item_id);
                        hashMap.put("itemCount", Integer.parseInt(item_count));
//                        hashMap.put("ImageUrl3", myUri3);
                        hashMap.put("ItemPrice",price);
                        hashMap.put("ItemDescription",description);
                        hashMap.put("time_stamp", Timestamp.now().toDate().toString());

                        Dataref.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                //Toast.makeText(MainActivity.this, "Data Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                textViewProgress.setText(progress +" %");
            }
        });

//        //first
//        final  String key1=Dataref.push().getKey();
//        StorageRef.child(key1+".jpg").putFile(imageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                StorageRef.child(key1 +".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        myUri1 = uri.toString();
//                    }
//                });
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
//                progressBar.setProgress((int) progress);
//                textViewProgress.setText(progress +" %");
//            }
//        });
//        // end first
//        //second
//        final  String key2=Dataref.push().getKey();
//        StorageRef.child(key2+".jpg").putFile(imageUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                StorageRef.child(key2 +".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        myUri2 = uri.toString();
//                    }
//                });
//
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
//                progressBar.setProgress((int) progress);
//                textViewProgress.setText(progress +" %");
//            }
//        });
        // end second
        //third
//        final  String key3=Dataref.push().getKey();
//        StorageRef.child(key3+".jpg").putFile(imageUri3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                StorageRef.child(key3 +".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        myUri3 = uri.toString();
//                        HashMap hashMap = new HashMap();
//                        hashMap.put("CarName",imageName);
//                        hashMap.put("ImageUrl", myUri);
//                        hashMap.put("ImageUrl1", myUri1);
//                        hashMap.put("ImageUrl2", myUri2);
//                        hashMap.put("ImageUrl3", myUri3);
//                        hashMap.put("ItemPrice",price);
//                        hashMap.put("ItemDescription",description);
//
//                        Dataref.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                                //Toast.makeText(MainActivity.this, "Data Successfully Uploaded!", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
//                progressBar.setProgress((int) progress);
//                textViewProgress.setText(progress +" %");
//            }
//        });
        // end third

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_IMAGE && data!=null)
        {
            uriList.add(data.getData());
            uriArray = uriList.toArray(new Uri[0]);

            switch (loopCounter)
            {
                case 0:
                    imageUri = uriArray[loopCounter];
                    isImageAdded=true;
                    imageViewAdd.setImageURI(imageUri);
                    break;
//                case 1:
//                    imageUri1  =uriArray[loopCounter];
//                    isImageAdded=true;
//                    imageViewAdd1.setImageURI(imageUri1);
//                    break;
//                case 2:
//                    imageUri2  =uriArray[loopCounter];
//                    isImageAdded=true;
//                    imageViewAdd2.setImageURI(imageUri2);
//                    break;
//                case 3:
//                    imageUri3 = uriArray[loopCounter];
//                    isImageAdded=true;
//                    imageViewAdd3.setImageURI(imageUri3);
//                    break;
            }
            Log.d("Loopcounter ", loopCounter +" "+ uriArray[loopCounter]);
            loopCounter++;

        }
    }
}
