package com.example.andappbydmitriipinzari;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.andappbydmitriipinzari.Fragments.AnimeSearchFragment;
import com.example.andappbydmitriipinzari.Fragments.LoginFragment;
import com.example.andappbydmitriipinzari.Fragments.RegisterFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginFragmentButton = findViewById(R.id.goToLogin);
        Button registerFragmentButton = findViewById(R.id.goToRegister);

        NavController navController = Navigation.findNavController(this,R.id.fragmentContainerView);


        loginFragmentButton.setOnClickListener(v-> navController.navigate(R.id.searchFragment));
        registerFragmentButton.setOnClickListener(v-> navController.navigate(R.id.registerFragment));

    }
}