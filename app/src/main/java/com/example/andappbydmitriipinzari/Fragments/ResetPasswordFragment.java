package com.example.andappbydmitriipinzari.Fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.andappbydmitriipinzari.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {
    private EditText emailEditText;
    private Button resetButton;

    FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reset_password_layout, container, false);

        emailEditText = view.findViewById(R.id.emailEnteredForReset);
        resetButton = view.findViewById(R.id.resetPasswordButton);

        auth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(view1 -> {
            resetPassword();
        });


        return view;
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Provide a valid email please");
            emailEditText.requestFocus();
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    emailEditText.setText("");
                    Toast.makeText(getContext(), "A reset letter has been sent to your email", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getContext(), "This email is not registered, try again", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
