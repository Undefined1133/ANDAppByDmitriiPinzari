package com.example.andappbydmitriipinzari;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.andappbydmitriipinzari.Fragments.LoginFragment;
import com.example.andappbydmitriipinzari.Fragments.RegisterFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginFragmentButton = findViewById(R.id.goToLogin);
        Button registerFragmentButton = findViewById(R.id.goToRegister);

        loginFragmentButton.setOnClickListener(view -> {
            FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
transaction.replace(R.id.container,new LoginFragment()).commit();
        });
        registerFragmentButton.setOnClickListener(view -> {
            FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container,new RegisterFragment()).commit();
        });

    }
}