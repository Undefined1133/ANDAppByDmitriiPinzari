package com.example.andappbydmitriipinzari;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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

        DrawerLayout drawable = findViewById(R.id.drawerLayout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    navController.navigate(R.id.searchFragment);
                } else if (item.getItemId() == R.id.registration) {
                    navController.navigate(R.id.registerFragment);
                } else if (item.getItemId() == R.id.login) {
                    navController.navigate(R.id.loginFragment);
                }
                return true;
            }
        });
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawable, R.string.open, R.string.close);
        drawable.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = findViewById(R.id.navView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.my_profile) {
                navController.navigate(R.id.profileFragment);
            } else if (item.getItemId() == R.id.settings) {
                navController.navigate(R.id.groupChatFragment);
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == R.id.friends) {
                Toast.makeText(this, "Friends", Toast.LENGTH_SHORT).show();
            }
            return true;
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        auth.signOut();
//    }

}