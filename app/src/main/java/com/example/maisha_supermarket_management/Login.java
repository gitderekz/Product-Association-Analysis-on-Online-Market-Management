package com.example.maisha_supermarket_management;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private static final int RC_SIGN_IN = 102;
    TextView register;
    EditText email;
    EditText password;
    Button logIn;
    Button googleBtn;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = new Intent(Login.this,HomeActivity.class);
        if (user != null) {
            Log.d("user not null ","Imepita");
            // User is signed in  open next activity
            final String[] userName = {""};
            final String[] photo = {""};
            String email = "";
            if (user.getDisplayName() != "")
            {
                Log.d("if ","Imepita");
                SharebleResources.imageUrl = String.valueOf(user.getPhotoUrl());
                SharebleResources.contactName = user.getDisplayName();
                SharebleResources.phoneNo = user.getPhoneNumber();
                userName[0] = user.getDisplayName();
                photo[0] = String.valueOf(user.getPhotoUrl());
                email = user.getEmail();
                intent.putExtra("email",email).putExtra("photo", photo[0]).putExtra("userName", userName[0]);
            }
            else {
                Log.d("Else ","Imepita");
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                String user_id = firebaseAuth.getCurrentUser().getUid();
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().exists()){
                                SharebleResources.imageUrl = task.getResult().getString("image");
                                SharebleResources.contactName = task.getResult().getString("name");
                                userName[0] = task.getResult().getString("name");
                                photo[0] = task.getResult().getString("image");
                                Log.d("passed user name  ",SharebleResources.contactName);
                                intent.putExtra("photo", photo[0]).putExtra("userName", SharebleResources.contactName);
                            }
                        } else {
                            String error = task.getException().getMessage();
//                    Toast.makeText(MainActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            startActivity(intent);
            finish();
        } else {
            // No user is signed in
        }

        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        logIn = findViewById(R.id.loginBtn);
        register = findViewById(R.id.signUp);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Login.this);
        googleBtn = findViewById(R.id.googleBtn);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredetials();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        startActivity(new Intent(Login.this,HomeActivity.class));
        finish();
    }

    private void checkCredetials()
    {
        String rEmail = email.getText().toString();
        String rPassword = password.getText().toString();

        if (rEmail.isEmpty() || !rEmail.contains("@"))
        {
            showError(email,"invalid email!");
        }
        else if (rPassword.isEmpty() || rPassword.length()<7)
        {
            showError(password,"must have at least 7 characters");
        }
        else {
            progressDialog.setTitle("Login");
            progressDialog.setMessage("authenticating");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(rEmail,rPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        final String[] userName = {""};
                        final String[] photo = {""};
                        String email = "";
//                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        String user_id = firebaseAuth.getCurrentUser().getUid();
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    if(task.getResult().exists()){
                                        SharebleResources.imageUrl = task.getResult().getString("image");
                                        SharebleResources.contactName = task.getResult().getString("name");
                                        SharebleResources.email = rEmail;
                                        Log.d("name passed",SharebleResources.contactName);
                                        userName[0] = task.getResult().getString("name");
                                        photo[0] = task.getResult().getString("image");
                                        Log.d("name passed2",userName[0]);
                                    }
                                } else {
                                    String error = task.getException().getMessage();
//                    Toast.makeText(MainActivity.this, "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        Toast.makeText(Login.this,"login successful",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this,HomeActivity.class)
                                .putExtra("email",email)
                                .putExtra("photo", photo[0])
                                .putExtra("userName", SharebleResources.contactName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(Login.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void showError(EditText editText, String message)
    {
        editText.setError(message);
        editText.requestFocus();
    }
}