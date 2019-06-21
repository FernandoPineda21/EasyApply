package com.example.easyapply;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FragmentSignup extends Fragment implements  Authenticationfirebase.Listener{

private Intent intent;
    EditText correo, pass, repeated, nombre, apellido, profesion;
    private ProgressBar progressBar;
    private String mParam1;
    private String mParam2;
    private Button register;
    private static FragmentSignup instance = null;
    private View rootview;
    private Authenticationfirebase authenticationfirebase;


    public static FragmentSignup getInstance() {
        if (instance == null) {
            instance = new FragmentSignup();
        }
        return instance;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString("email", "");
            mParam2 = getArguments().getString("pass", "");
        }

        authenticationfirebase = new Authenticationfirebase();
        authenticationfirebase.setListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_fragment_login, container, false);
        correo = rootview.findViewById(R.id.edtemail);
        progressBar=rootview.findViewById(R.id.pgsingup);
        pass = rootview.findViewById(R.id.edtpass);
        repeated = rootview.findViewById(R.id.edtpassrepeated);
        nombre = rootview.findViewById(R.id.edtnombre);
        apellido = rootview.findViewById(R.id.edtapellido);
        profesion = rootview.findViewById(R.id.edtprofesion);
        register = rootview.findViewById(R.id.btnregister);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnregister:
                        if(
                                authenticationfirebase.edittextvalidator(nombre,getActivity().getApplicationContext()) &&
                                authenticationfirebase.edittextvalidator(apellido,getActivity().getApplicationContext()) &&
                                authenticationfirebase.edittextvalidator(profesion,getActivity().getApplicationContext()) &&
                                authenticationfirebase.edittextvalidator(correo,getActivity().getApplicationContext()) &&
                                authenticationfirebase.edittextvalidator(pass,getActivity().getApplicationContext()) &&
                                authenticationfirebase.edittextvalidator(repeated,getActivity().getApplicationContext())
                        )
                        {
                            if (pass.getText().toString().equals(repeated.getText().toString())) {
                                authenticationfirebase.signup(
                                        correo.getText().toString(),
                                        pass.getText().toString(),
                                        nombre.getText().toString(),
                                        apellido.getText().toString(),
                                        profesion.getText().toString(),
                                        getActivity().getApplicationContext());
                            }
                            else{
                                Toast.makeText(getContext(), "La contraseña debe coincidir", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
            }
        });

        correo.setText(mParam1);
        repeated.setText(mParam2);
        pass.setText(mParam2);


        return rootview;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
            intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }
        else {
            startActivity(intent);
        }
    }
}