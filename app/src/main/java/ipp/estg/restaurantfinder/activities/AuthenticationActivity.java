package ipp.estg.restaurantfinder.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ipp.estg.restaurantfinder.R;

public class AuthenticationActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;
    private TextView forgotPassword;
    private TextInputEditText loginEmail;
    private TextInputEditText loginPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException ignored) {
        }

        setContentView(R.layout.login_layout);

        this.loginEmail = findViewById(R.id.loginEmail);
        this.loginPassword = findViewById(R.id.loginPassword);
        this.loginButton = findViewById(R.id.loginButton);
        this.signupButton = findViewById(R.id.signupButton);
        this.forgotPassword = findViewById(R.id.forgotPasswordTextView);
        this.firebaseAuth = FirebaseAuth.getInstance();

        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(loginEmail.getText().toString(), loginPassword.getText().toString());
            }
        });

        this.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordDialog();
            }
        });

        this.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    private void forgotPasswordDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alert.setTitle("Request new password");

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.forgot_password_layout, null);

        TextInputEditText forgotPasswordEmail = view.findViewById(R.id.forgotPasswordEmail);
        Button newPasswordButton = view.findViewById(R.id.requestPasswordEmail);

        alert.setView(view);

        AlertDialog dialog = alert.create();
        dialog.show();

        newPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword(forgotPasswordEmail.getText().toString());
            }
        });
    }

    private void signIn(String email, String password) {
        this.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Intent nearbyRestaurantsIntent = new Intent(getApplicationContext(), NearbyRestaurants.class);
                            startActivity(nearbyRestaurantsIntent);
                        } else {
                            loginDialog("Login error!", getString(R.string.login_error), getString(R.string.ok_button));
                        }
                    }
                });
    }

    private void loginDialog(String titleMsg, String textMsg, String textButton) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alert.setTitle(titleMsg);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.login_dialog, null);

        Button dialogButton = view.findViewById(R.id.loginDialogButton);
        dialogButton.setText(textButton);

        TextView loginMsg = view.findViewById(R.id.loginMsg);
        loginMsg.setText(textMsg);

        alert.setView(view);

        AlertDialog dialog = alert.create();
        dialog.show();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void resetPassword(String email) {
        this.firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "To reset your password, check your email.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid email, please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}