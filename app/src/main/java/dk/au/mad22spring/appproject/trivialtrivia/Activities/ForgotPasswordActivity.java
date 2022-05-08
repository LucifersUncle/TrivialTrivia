package dk.au.mad22spring.appproject.trivialtrivia.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import dk.au.mad22spring.appproject.trivialtrivia.R;


//*******************************************************//
//https://www.youtube.com/watch?v=Z-RE1QuUWPg - PART1
//https://www.youtube.com/c/CodeWithMazn - PART2
//...and the rest of the series
//******************************************************//
public class ForgotPasswordActivity extends AppCompatActivity {

    //region Widgets
    private EditText editTextEmailAddress;
    private Button buttonResetPassword;
    private ProgressBar progressBar;
    //endregion

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //region Find the UI elements
        editTextEmailAddress = (EditText) findViewById(R.id.editTextEmailAddressRetrieval);
        progressBar = (ProgressBar) findViewById(R.id.progressBarForgotPassword);
        //endregion

        //region Buttons
        buttonResetPassword = (Button) findViewById(R.id.buttonResetPassword);
        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
        //endregion

        mAuth = FirebaseAuth.getInstance();
    }

    private void resetPassword() {
        String email = editTextEmailAddress.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmailAddress.setError("Email is required!");
            editTextEmailAddress.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailAddress.setError("Please provide a valid email!");
            editTextEmailAddress.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Try again! Something went wrong!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}