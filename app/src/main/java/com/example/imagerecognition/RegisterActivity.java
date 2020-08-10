package com.example.imagerecognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    private Button Registerbutton;
    private TextInputLayout usernameLabel;
    private TextInputLayout telephonelabel;
    private TextInputLayout OccupationLabel;
    private TextInputLayout Email;
    private TextInputLayout passlabel;
    private String email;
    private Button Verifybutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.loginlayout);
        Registerbutton=findViewById(R.id.Registerbutton);
        Email=findViewById(R.id.InputLayout_email);
        usernameLabel=findViewById(R.id.InputLayout_username);
        passlabel=findViewById(R.id.InputLayout_password);
        Verifybutton=findViewById(R.id.Verifybutton);
        Verifybutton.setVisibility(View.INVISIBLE);
        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        Verifybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Boolean isEmailVerified = user.isEmailVerified();
                        if(isEmailVerified==true)
                        {
                            Toast.makeText(RegisterActivity.this,"Your Email has been: Successfully Verified ",Toast.LENGTH_SHORT).show();
                            startnewActivity();
                        }

                    }
                });


            }
        });

        Registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //start
                email=Email.getEditText().getText().toString();
                String password=passlabel.getEditText().getText().toString();
                validateForm();
                String username=usernameLabel.getEditText().getText().toString();
                String telephone=telephonelabel.getEditText().getText().toString();
                String occupation=OccupationLabel.getEditText().getText().toString();
                if(validateForm()) {
                    startRegister(email, password,username,telephone,occupation);
                }
                //end
            }

        });
    }

    private void startnewActivity()
    {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
    }
    private boolean validateForm()
    {
        if(validateUsername() && validatePassword()&& validateEmail())
            return true;
        else
            return false;
    }

    private boolean validateEmail()
    {
        String label=passlabel.getEditText().getText().toString().trim();
        if(label.isEmpty())
        {
            passlabel.setError("Field cannot be empty.");
            return false;
        }
        else{
            passlabel.setError(null);
            return true;
        }
    }


    //Validating the password in the label box
    private boolean validatePassword()
    {
        String label=passlabel.getEditText().getText().toString().trim();
        if(label.isEmpty())
        {
            passlabel.setError("Field cannot be empty.");
            return false;
        }
        if(label.length()<6)
        {
            passlabel.setError("Password should be of length greater than six");
            return false;
        }
        else{
            passlabel.setError(null);
            return true;
        }
    }

    //Validating the username in the label box
    private boolean validateUsername()
    {
        String label=usernameLabel.getEditText().getText().toString().trim();
        if(label.isEmpty())
        {
            usernameLabel.setError("Field cannot be empty.");
            return false;
        }
        if(label.length()>15)
        {
            usernameLabel.setError("Label name is too long");
            return false;
        }
        else{
            usernameLabel.setError(null);
            return true;
        }

    }

    private void startRegister(String email, String password, final String username,String telephone,String Occupation)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            UserProfileChangeRequest profileupdates=new UserProfileChangeRequest.Builder().setDisplayName(username).build();
                            user.updateProfile(profileupdates);
                            Registerbutton.setVisibility(View.INVISIBLE);
                            Verifybutton.setVisibility(View.VISIBLE);
                            sendEmailVerification(user);


                        } else {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(RegisterActivity.this,"Please Verify email",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegisterActivity.this,"Unable to create",Toast.LENGTH_SHORT).show();
        }
    }


    private void sendEmailVerification(final FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(RegisterActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
