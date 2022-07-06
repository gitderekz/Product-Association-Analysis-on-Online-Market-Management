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

public class Register extends AppCompatActivity {
    TextView login;
    EditText userName;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button signUp,signUp_withGoogle;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 102;
    private static final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUp = findViewById(R.id.signUpBtn);
        signUp_withGoogle = findViewById(R.id.signUpBtn_withGoogle);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Register.this);


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredetials();
            }
        });
        signUp_withGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        login = findViewById(R.id.logIn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });
    }

    private void checkCredetials()
    {
        String rUserName = userName.getText().toString();
        String rEmail = email.getText().toString();
        String rPassword = password.getText().toString();
        String rConfirmPassword = confirmPassword.getText().toString();

        if (rUserName.isEmpty() || rUserName.length()<3)
        {
            showError(userName,"must have at least 3 characters");
        }
        else if (rEmail.isEmpty() || !rEmail.contains("@"))
        {
            showError(email,"invalid email!");
        }
        else if (rPassword.isEmpty() || rPassword.length()<7)
        {
            showError(password,"must have at least 7 characters");
        }
        else if (rConfirmPassword.isEmpty() || !rConfirmPassword.equals(rPassword))
        {
            showError(confirmPassword,"password miss-match!");
        }
        else {
            progressDialog.setTitle("Registration");
            progressDialog.setMessage("authenticating");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(rEmail,rPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        final String[] userName = {""};
                        final String[] photo = {""};
                        String email = "";
                        SharebleResources.email = rEmail;
                        Toast.makeText(Register.this,"confirmed, new user added",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Register.this,Login.class)
                                .putExtra("email",rEmail)
                                .putExtra("photo", photo[0])
                                .putExtra("userName", rUserName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else {

                        Toast.makeText(Register.this,task.getException().toString(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
    private void showError(EditText editText,String message)
    {
        editText.setError(message);
        editText.requestFocus();
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
                Toast.makeText(this, "Failed to Login", Toast.LENGTH_LONG).show();
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
        Toast.makeText(Register.this,"confirmed, new user added",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Register.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}