package ipp.estg.restaurantfinder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AuthenticationActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;
    private TextView forgotPassword;
    private TextInputEditText loginEmail;
    private TextInputEditText loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        forgotPassword = findViewById(R.id.forgotPasswordTextView);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
}