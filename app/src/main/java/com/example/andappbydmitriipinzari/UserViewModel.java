package com.example.andappbydmitriipinzari;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.andappbydmitriipinzari.AnimeApiClasses.Aired;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserViewModel extends ViewModel {
    FirebaseAuth firebaseAuth;


    public void logout() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
    }
//    public void signIn(String email, String password){
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(this, "User logged in", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
}
