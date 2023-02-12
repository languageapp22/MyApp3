package com.example.myapp.Cards;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    TextInputEditText edtUser, edtEmail, edtPass1, edtPass2;
    TextView btnRegister;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private ArrayList<Deck> allDecks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cards);

        allDecks = (ArrayList<Deck>) getIntent().getSerializableExtra("allDecks");

        mAuth = FirebaseAuth.getInstance();
        btnRegister = (TextView) findViewById(R.id.tvRegisterUser);
        edtUser = (TextInputEditText) findViewById(R.id.edtLoginEmail);
        edtEmail = (TextInputEditText) findViewById(R.id.edtEmail);
        edtPass1 = (TextInputEditText) findViewById(R.id.edtPassword1);
        edtPass2 = (TextInputEditText) findViewById(R.id.edtPassword2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){

        switch (v.getId()){
            case R.id.tvRegisterUser:
                registerUser();
                break;
        }
    }
    //checks if user info is valid, then registers user with firebase:
    private void registerUser(){
        String email = edtEmail.getText().toString().trim();
        String password1 = edtPass1.getText().toString();
        String password2 = edtPass2.getText().toString();
        String username = edtUser.getText().toString().trim();

        if (username.isEmpty()){
            edtUser.setError("Please enter a username.");
            edtUser.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Please enter a valid email.");
            edtEmail.requestFocus();
            return;
        }
        else if(password1.isEmpty()){
            edtPass1.setError("Please enter a valid password.");
            edtPass1.requestFocus();
            return;
        }
        else if(!password1.equals(password2)){
            edtPass1.setError("Passwords must match.");
            edtPass1.requestFocus();
            return;
        }
        else if(password1.length() < 6){
            edtPass1.setError("Password must be atleast 6 characters.");
            edtPass1.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();


                            //update username
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username).build();
                            user1.updateProfile(profileUpdates);



                            User user = new User (username, email, new ArrayList<String>());

                            //add user to firebase database:
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Successfully registered " + username, Toast.LENGTH_LONG).show();

                                        //create user object and upload it:
                                        User newUser = new User();

                                        progressBar.setVisibility(View.GONE);
                                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                        i.putExtra("allDecks", allDecks);
                                        startActivity(i);
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "Failed to register (Database)", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "Failed to register (Authentication)", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }
}