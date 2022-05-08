package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import dk.au.mad22spring.appproject.trivialtrivia.Models.User;
import dk.au.mad22spring.appproject.trivialtrivia.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView banner;
    private Button registerUser, buttonBack;
    private EditText editTextUsername, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        //region Widgets

        //region TextViews
        banner = (TextView) findViewById(R.id.textRegisterHeader);
        //endregion

        //region Buttons
        registerUser = (Button) findViewById(R.id.buttonRegister);
        registerUser.setOnClickListener(this);

        buttonBack = (Button) findViewById(R.id.button_register_back);
        buttonBack.setOnClickListener(this);
        //endregion

        //region EditTexts
        editTextUsername = (EditText) findViewById(R.id.editTextUsernameRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmailAddressRegister);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordRegister);
        //endregion

        //region ProgressBar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //endregion

        //endregion
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textRegisterHeader:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.buttonRegister:
                registerUser();
                break;
            case R.id.button_register_back:
                finish();
        }
    }

    private void registerUser() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //region Register Form Validators
        if (username.isEmpty()) {
            editTextUsername.setError("Username is required!");
            editTextUsername.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        //Patterns enforces the criteria for a valid email adress containing '@' etc...
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password should be at least 6 characters!");
            editTextPassword.requestFocus();
            return;

        }
        //endregion

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Construct a new User object to store in Firebase
                            User user = new User(username, email);

                            FirebaseDatabase.getInstance("https://trivialtrivia-group20-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "User has been registered succesfully", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                        //redirect to login
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}