package com.example.andappbydmitriipinzari.Fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.andappbydmitriipinzari.AnimeApiClasses.TopAnimeResult;
import com.example.andappbydmitriipinzari.R;
import com.example.andappbydmitriipinzari.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;
import java.util.Locale;

public class RegisterFragment extends Fragment {
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_layout, container, false);

        EditText email = view.findViewById(R.id.emailRegisterField);
        EditText password = view.findViewById(R.id.passwordRegisterField);
        EditText username = view.findViewById(R.id.usernameRegisterField);
        Button registerButton = view.findViewById(R.id.registerButton);
        TextView banner = view.findViewById(R.id.banner);
        EditText fullName = view.findViewById(R.id.fullNameRegisterField);


        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() ==null) {

            banner.setOnClickListener(view1 -> {
                // return back to activity i guess
            });

            registerButton.setOnClickListener(view1 -> {

                String fullNameText = fullName.getText().toString().trim();
                String usernameText = username.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                String emailText = email.getText().toString().trim();

                if (fullNameText.isEmpty()) {
                    fullName.setError("Full name is required");
                    fullName.requestFocus();
                    return;
                }
                if (usernameText.isEmpty()) {
                    username.setError("Username is required");
                    username.requestFocus();
                    return;
                }
                if (emailText.isEmpty()) {
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    email.setError("Please provide valid email");
                }
                if (passwordText.isEmpty()) {
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                }
                if (password.length() < 5) {
                    password.setError("Min password length has to be 5 characters");
                    password.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

// WHEN CLICKING AN ANIME FAVORITE BUTTON, CREATE HASHMAP AS SHOWN BELOW

//                            Map<String, Object> userUpdates = new HashMap<>();
//                            userUpdates.put("alanisawesome/nickname", "Alan The Machine");
//                            userUpdates.put("gracehop/nickname", "Amazing Grace");
//                            usersRef.updateChildrenAsync(userUpdates);

                                User user = new User(fullNameText, passwordText, emailText,
                                        usernameText, "", null, null);

                                FirebaseDatabase.getInstance("https://andappbydmitriipinzari-default-rtdb.europe-west1.firebasedatabase.app/").getReference("User")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "User is registered Succesfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
            });
        }else {
            Toast.makeText(getContext(), "You are already registered and signed in!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }


}
