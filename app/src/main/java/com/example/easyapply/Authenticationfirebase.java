package com.example.easyapply;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Authenticationfirebase {

    public interface Listener{
        void onLoading();
        void onFinish();
        void inicActivity();
    }

    private Listener listener;
    public void setListener(Listener listener) {
        this.listener = listener;
    }




    private Context context;
    public FirebaseAuth mAuth=FirebaseAuth.getInstance();
    public FirebaseUser user=mAuth.getCurrentUser();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private user usuario;

    public void databaseconnectiononline() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseDatabase.goOnline();
    }

    public void databaseconnectionoffline() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        firebaseDatabase.goOffline();
    }

    public void signup(final String email, String password, final String nombre, final String apellido, final String profesion, final Context myContext) {
        if(listener != null) listener.onLoading();
        context = myContext;
        mAuth = FirebaseAuth.getInstance();

        databaseconnectiononline();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(listener != null) listener.onFinish();

                        if (task.isSuccessful()) {

                            user = mAuth.getCurrentUser();
                            if (task.getResult() != null) {

                                usuario = new user(user.getUid(),
                                        nombre,apellido,email,"");

                                databaseReference
                                        .child("User")
                                        .child(mAuth.getCurrentUser().getUid())
                                        .setValue(usuario)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(listener != null) listener.onFinish();
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(context, "Correo registrado: " + email, Toast.LENGTH_SHORT).show();
                                                    if(listener != null) listener.inicActivity();
                                                    databaseconnectionoffline();
                                                } else {
                                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                            } else {

                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }


                        } else {
                            Toast.makeText(context, (task.getException().getMessage()), Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }


    public void signin(String email, String password, Context myContext) {
        if(listener != null) listener.onLoading();

        context = myContext;
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(listener != null) listener.onFinish();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            if (task.getResult() != null) {
                                Toast.makeText(context, "Sign In: " + task.getResult().getUser().getEmail(), Toast.LENGTH_SHORT).show();
                                if(listener != null) listener.inicActivity();

                            } else {

                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }


                        } else {
                            Toast.makeText(context, (task.getException().getMessage()), Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }

    public void Signout(Context myContext) {
        context = myContext;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {

            Toast.makeText(context, "Hasta Luego: " + user.getEmail(), Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }

    }


    public void Userverify(Context myContext) {
        context = myContext;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        if (user != null) {

            if(listener != null) {
                listener.inicActivity();

            }
        }


    }


    public boolean edittextvalidator(EditText editText, Context context) {

        String validate = editText.getText().toString();

        if (TextUtils.isEmpty(validate)) {
            editText.setError(context.getString(R.string.Edit_Text_Campo_Oblogatorio));
            editText.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}



