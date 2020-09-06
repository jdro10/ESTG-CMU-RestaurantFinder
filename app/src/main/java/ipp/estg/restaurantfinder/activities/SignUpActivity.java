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

import ipp.estg.restaurantfinder.R;

public class SignUpActivity extends AppCompatActivity {

    private Button signupButton;
    private TextInputEditText signupEmail;
    private TextInputEditText signupPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException ignored) {
        }

        setContentView(R.layout.sign_up_layout);

        this.firebaseAuth = FirebaseAuth.getInstance();
        this.signupButton = findViewById(R.id.signupButton);
        this.signupEmail = findViewById(R.id.signupEmail);
        this.signupPassword = findViewById(R.id.signupPassword);

        this.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(signupEmail.getText().toString(), signupPassword.getText().toString());
            }
        });
    }

    private void createAccount(String email, String password) {
        this.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signupDialog("Successfully registered!", getString(R.string.success_signup), getString(R.string.success_signup_button));
                        } else {
                            signupDialog("Error!", getString(R.string.error_signup), getString(R.string.ok_button));
                        }
                    }
                });
    }

    private void signupDialog(String titleMsg, String textMsg, String textButton) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alert.setTitle(titleMsg);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.signup_dialog, null);

        Button dialogButton = view.findViewById(R.id.signupDialogButton);
        dialogButton.setText(textButton);

        TextView signupMsg = view.findViewById(R.id.signup_msg);
        signupMsg.setText(textMsg);

        alert.setView(view);

        AlertDialog dialog = alert.create();
        dialog.show();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogButton.getText().equals(getString(R.string.success_signup_button))) {
                    Intent loginIntent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                    startActivity(loginIntent);
                } else if (dialogButton.getText().equals(getString(R.string.ok_button))) {
                    dialog.dismiss();
                }
            }
        });
    }
}