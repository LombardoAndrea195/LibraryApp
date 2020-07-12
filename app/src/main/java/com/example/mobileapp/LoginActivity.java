package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    EditText emailId,password;
    Button butSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG="LoginActivity";
    private int RC_SIGN_IN=1;
    String name_surname, email;

    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth=FirebaseAuth.getInstance();
        emailId=findViewById(R.id.email);
        tvSignUp=findViewById(R.id.sign_up);
        password=findViewById(R.id.password);
        butSignIn=findViewById(R.id.sign_in);
        signInButton=findViewById(R.id.GoogleSignIn);
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent=mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent,RC_SIGN_IN);

            }

        });
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser =mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser!=null){
                    Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT);

                    Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                  //  i.putExtra("email", email);
                   // i.putExtra("Surname", name_surname);

                }
                else{
                    Toast.makeText(LoginActivity.this,"Please login",Toast.LENGTH_SHORT);

                }
            }
        };
        butSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        String email = emailId.getText().toString();
                        String pws = password.getText().toString();
                        if (email.isEmpty()) {
                            emailId.setError("Please enter your email:");
                            emailId.requestFocus();
                            Toast.makeText(LoginActivity.this, "Field is empty", Toast.LENGTH_LONG).show();
                            }
                        else if (pws.isEmpty()) {
                            password.setError("Please enter your email:");
                            password.requestFocus();
                            Toast.makeText(LoginActivity.this, "Field is empty", Toast.LENGTH_LONG).show();
                            }
                        else if (pws.isEmpty() && email.isEmpty()) {
                            Toast.makeText(LoginActivity.this, "Fields is empty", Toast.LENGTH_LONG).show();
                        } else if (!(pws.isEmpty() && email.isEmpty())) {
                            mFirebaseAuth.signInWithEmailAndPassword(email,pws).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(!task.isSuccessful()){
                                            Toast.makeText(LoginActivity.this, "Login error, please try again", Toast.LENGTH_LONG).show();

                                        }
                                        else{

                                            Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                                            startActivity(i);

                                        }

                                }
                            });
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();
                        }

                }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i= new Intent(LoginActivity.this,SignupActivity.class);

                    startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount ac=completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this,"Signed in Successfully",Toast.LENGTH_SHORT).show();

            FirebaseGoogleAuth(ac);
            Intent s=new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(s);

        }catch (ApiException e){


            Toast.makeText(LoginActivity.this,"Signed in Failed",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);
        }
    }
    private void FirebaseGoogleAuth(GoogleSignInAccount idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(String.valueOf(idToken), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();




                            update(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            update(null);
                        }

                        // ...


                    }
                });
    }

    private void update(FirebaseUser fUser){
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account!=null){
            String personName=account.getDisplayName();
            String personGivenName=account.getGivenName();
            String personFamilyName=account.getFamilyName();
            String personEmail=account.getEmail();
            String personId=account.getId();
            Uri personPhoto=account.getPhotoUrl();
        }

    }
    @Override
    protected void onStart(){
            super.onStart();;
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
            }
}