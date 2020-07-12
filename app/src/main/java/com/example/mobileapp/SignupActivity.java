package com.example.mobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity {

    EditText emailId,password,passwordconf;
    Button butSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mFirebaseAuth=FirebaseAuth.getInstance();
        emailId=findViewById(R.id.email);
        tvSignIn=findViewById(R.id.signin);
        password=findViewById(R.id.password);
        passwordconf=findViewById(R.id.passwordconf);
        butSignUp=findViewById(R.id.signup);


        butSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pws = password.getText().toString();
                String pwsconf=passwordconf.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Please enter your email:");
                    emailId.requestFocus();
                    Toast.makeText(SignupActivity.this, "Field is empty", Toast.LENGTH_LONG).show();
                } else if (pws.isEmpty()) {
                    password.setError("Please enter your password:");
                    password.requestFocus();
                    Toast.makeText(SignupActivity.this, "Field is empty", Toast.LENGTH_LONG).show();

                } else if (pwsconf.isEmpty()) {
                    passwordconf.setError("Please confirm your password:");
                    passwordconf.requestFocus();
                    Toast.makeText(SignupActivity.this, "Field is empty", Toast.LENGTH_LONG).show();

                }
                else if (!pws.contentEquals(pwsconf)) {
                    passwordconf.setError("Password are not the same:");
                    passwordconf.requestFocus();
                    Toast.makeText(SignupActivity.this, "Passowrd fields are not the same", Toast.LENGTH_LONG).show();

                }
                else if (pws.isEmpty() && email.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Fields is empty", Toast.LENGTH_LONG).show();
                } else if (!(pws.isEmpty() && email.isEmpty())) {

                    mFirebaseAuth.createUserWithEmailAndPassword(email,pws).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignupActivity.this, "Sign up unsucessful, please try again", Toast.LENGTH_LONG).show();

                            }
                            else{
                                Intent i= new Intent(SignupActivity.this,HomeActivity.class);
                        //        i.putExtra("email", email);
                              //  i.putExtra("Surname", name_surname);

                                startActivity(i);
                            }
                        }
                    });

                }
                else{
                    Toast.makeText(SignupActivity.this, "Error Occurred", Toast.LENGTH_LONG).show();

                }
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }

}