package com.example.andappbydmitriipinzari.Fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.andappbydmitriipinzari.MainActivity;
import com.example.andappbydmitriipinzari.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;


public class LoginFragment extends Fragment {
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_layout, container, false);
        auth = FirebaseAuth.getInstance();
        EditText email = view.findViewById(R.id.emailLogin);
        EditText password = view.findViewById(R.id.passwordLogin);
        Button loginButton = view.findViewById(R.id.loginButton);
        TextView banner = view.findViewById(R.id.myApp);
        TextView registerRedirect = view.findViewById(R.id.registerButton);
        TextView forgotPassword = view.findViewById(R.id.forgotPassword);


        loginButton.setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(view);
            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("SuccessLogin",
                                        "singInWithEmail Success");
                                navController.navigate(R.id.searchFragment);

                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                            if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });


        forgotPassword.setOnClickListener(view2 -> {

            NavController navController1 = Navigation.findNavController(view);
            navController1.navigate(R.id.resetPasswordFragment);

        });
        registerRedirect.setOnClickListener(view1 -> {
            NavController navController = Navigation.findNavController(view);

            navController.navigate(R.id.registerFragment);
        });


        return view;
    }
}

