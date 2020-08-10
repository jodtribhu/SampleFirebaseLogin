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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private Button signin_button;
    private TextInputLayout Email;
    private TextInputLayout Password;
    private FirebaseAuth mAuth;
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signin_button=findViewById(R.id.signin_button);
        Email=findViewById(R.id.email_inputlayout);
        Password=findViewById(R.id.password_inputlayout);
        mAuth = FirebaseAuth.getInstance();

        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=Email.getEditText().getText().toString();
                String password=Password.getEditText().getText().toString();
                SignInstart(email,password);
    }
        });
    }
    private void SignInstart(String email,String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    public void newRegister(View view)
    {
        Intent i=new Intent(this,RegisterActivity.class);
        startActivity(i);
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this,MainActivity.class));
        } else {
            Toast.makeText(LoginActivity.this,"Unable to Sign In",Toast.LENGTH_SHORT).show();
        }
    }
}
