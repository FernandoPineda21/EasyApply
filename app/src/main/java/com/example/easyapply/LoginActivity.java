package com.example.easyapply;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Authenticationfirebase.Listener {

    private Intent intent;
    private Authenticationfirebase authenticationfirebase = new Authenticationfirebase();
    private ProgressBar progressBar;
    private EditText email, pass;
    private Button signup, signin;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Fragment fragment;
    private String x, y;
    private Bundle bundle;

    DatabaseReference databaseuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.pg);
        authenticationfirebase.setListener(this);
        databaseuser = FirebaseDatabase.getInstance().getReference("user");
        email = findViewById(R.id.edtemail);
        pass = findViewById(R.id.edtpass);
        signup = findViewById(R.id.btnsignup);
        signup.setOnClickListener(this);
        signin = findViewById(R.id.btnsignin);
        signin.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    public void onStart() {
        super.onStart();

        authenticationfirebase.Userverify(this.getApplicationContext());
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btnsignup:
                callfragmentsignup();
                closekeyboard();
                break;

            case R.id.btnsignin:
                validarsignin();
                closekeyboard();
                break;


        }
        closekeyboard();
    }

    public void callfragmentsignup() {
        x = email.getText().toString();
        y = pass.getText().toString();


        bundle = new Bundle();
        bundle.putString("email", x);
        bundle.putString("pass", y);


        fragment = FragmentSignup.getInstance();
        fragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lyologin, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void validarsignin() {
        x = email.getText().toString();
        y = pass.getText().toString();

        if (authenticationfirebase.edittextvalidator(email, getApplicationContext())) {
            if (authenticationfirebase.edittextvalidator(pass, getApplicationContext())) {
                authenticationfirebase.signin(x, y, this.getApplicationContext());
            }
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public void closekeyboard() {
        View v = this.getCurrentFocus();

        if (v != null) {

            InputMethodManager inm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }


    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onLoading() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void onFinish() {
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void inicActivity() {

        if (intent==null) {
            intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else {
            startActivity(intent);
        }
    }

}
