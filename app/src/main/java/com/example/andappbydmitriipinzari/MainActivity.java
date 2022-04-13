package com.example.andappbydmitriipinzari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ActionBarDrawerToggle actionBarDrawerToggle;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        auth = FirebaseAuth.getInstance();
        Button loginFragmentButton = findViewById(R.id.goToLogin);
        Button registerFragmentButton = findViewById(R.id.goToRegister);
        DrawerLayout drawable = findViewById(R.id.drawerLayout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawable, R.string.open, R.string.close);
        drawable.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.item1) {
                navController.navigate(R.id.profileFragment);
            } else if (item.getItemId() == R.id.item2) {
                Toast.makeText(this, "item2", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.item3) {
                Toast.makeText(this, "item3", Toast.LENGTH_SHORT).show();
            }
            return true;
        });


        loginFragmentButton.setOnClickListener(v -> navController.navigate(R.id.searchFragment));

        registerFragmentButton.setOnClickListener(v -> navController.navigate(R.id.registerFragment));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.signOut();
    }
}