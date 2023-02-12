 package com.example.myapp.Cards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

 public class LoginActivity extends AppCompatActivity {

     private TextInputEditText edtEmail;
     private TextInputEditText edtPassword;
     private Button btnLogin;
     private TextView btnGuest, btnSignup;
     private ProgressBar progressBar;
     private FirebaseAuth mAuth;
     private ArrayList<Deck> allDecks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cards);

        edtEmail = (TextInputEditText) findViewById(R.id.edtLoginEmail);
        edtPassword = (TextInputEditText) findViewById(R.id.edtPass);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGuest = (TextView) findViewById(R.id.tvGuest);
        btnSignup = (TextView)findViewById(R.id.tvSignup);

        progressBar = (ProgressBar)findViewById(R.id.loginPgBr);
        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        allDecks = (ArrayList<Deck>) getIntent().getSerializableExtra("allDecks");



        btnGuest.setClickable(true);
        btnSignup.setClickable(true);

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String pss = edtPassword.getText().toString();

                if (email.isEmpty()){
                    edtEmail.setError("Please enter an email.");
                    edtEmail.requestFocus();
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edtEmail.setError("Enter a valid email.");
                    edtEmail.requestFocus();
                    return;
                }
                else if(pss.isEmpty()){
                    edtPassword.setError("Please enter a password.");
                    edtPassword.requestFocus();
                    return;
                }
                else if(pss.length() < 6){
                    edtPassword.setError("Invalid password.");
                    edtPassword.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, pss).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(LoginActivity.this, PublicDecks.class);
                            i.putExtra("allDecks", allDecks);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Failed to login. Please retry.", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        btnGuest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent(getApplicationContext(), PublicDecks.class);
                i.putExtra("allDecks", allDecks);
                startActivity(i);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                i.putExtra("allDecks", allDecks);
                startActivity(i);
            }
        });
    }

}