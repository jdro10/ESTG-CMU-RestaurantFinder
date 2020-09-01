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

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        forgotPassword = findViewById(R.id.forgotPasswordTextView);
        this.firebaseAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(loginEmail.getText().toString(), loginPassword.getText().toString());
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswordDialog();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    private void forgotPasswordDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_NoActionBar);
        alert.setTitle("Request new password");

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.forgot_password_layout, null);

        TextInputEditText forgotPasswordEmail = findViewById(R.id.forgotPasswordEmail);
        Button newPasswordButton = findViewById(R.id.requestPasswordEmail);

        alert.setView(view);

        AlertDialog dialog = alert.create();
        dialog.show();
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
        AlertDialog.Builder alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_NoActionBar);
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
}