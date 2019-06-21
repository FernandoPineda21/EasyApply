package com.example.easyapply;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements RecycleViewAdapter.OnItemClickListener, FragmentcComments.Listener, FragmentUploadImage.Listener, LocationListener {
    private RecyclerView mRecyclerView;
    private RecycleViewAdapter mRecycleViewAdapter;
    public static final String EXTRA_PUBLISH_ID = "PUBLISHID";
    public static final String EXTRA_URL = "URL";
    public static final String EXTRA_USER = "USER";
    public static final String EXTRA_DESCRIPTION = "DESCRIPTION";
    private Bundle bundle;
    private Authenticationfirebase authenticationfirebase = new Authenticationfirebase();
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private List<Publishing> publishinglist = new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferencelikes;
    private FirebaseDatabase firebaseDatabase;
    private ProgressBar progressBar;
    private Button picupload, buttonlogout;
    private Fragment fragment;
    private Intent intent = null;
private   String Fullname="";
    private FragmentcComments fragmentcComments = FragmentcComments.getInstance();
    private FragmentUploadImage fragmentUploadImage;
    private FusedLocationProviderClient fusedLocationClient;
    private double longitud = 0, latitud = 0;
    private int REQUEST_LOCATION_PERMISSION = 100;
    private LocationManager locationManager;
    private String countryname, locality, adminarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        inicializerdb();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = findViewById(R.id.pbuser);
        progressBar.setVisibility(View.VISIBLE);
        fragmentcComments.setListener(this);

        mRecyclerView = findViewById(R.id.recycleview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        picupload = findViewById(R.id.btnpublicar);
        buttonlogout = findViewById(R.id.btnlogout);

        buttonlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticationfirebase.Signout(getApplicationContext())
                ;
                inicActivity();
            }
        });
        picupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                fragmentUploadImage = FragmentUploadImage.getInstance();
                fragmentUploadImage.setListener(MainActivity.this);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layoutmainactivity, fragmentUploadImage)
                        .commit();

            }
        });


        databaseReference.child("Publishing").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Publishing publishing = dataSnapshot.getValue(Publishing.class);

                publishinglist.add(publishing);

                fillRecicleviewpublish();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("User").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Fullname=dataSnapshot.child("firstname").getValue().toString()+" "+
                dataSnapshot.child("lastname").getValue().toString();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fillRecicleviewpublish() {
        Collections.reverse(publishinglist);
        mRecycleViewAdapter = new RecycleViewAdapter(getApplicationContext(), publishinglist);
        mRecyclerView.setAdapter(mRecycleViewAdapter);
        mRecycleViewAdapter.setOnItemClickListener(MainActivity.this);
        progressBar.setVisibility(View.INVISIBLE);

    }


    private void inicializerdb() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReferencelikes = firebaseDatabase.getReference("Publishing");
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
    }

    @Override
    public void onStart() {
        super.onStart();


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void inicActivity() {
        if (intent != null) {
            startActivity(intent);
            this.finish();

        } else {
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            this.finish();
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
    public void gettotalcomments(int posicion,final TextView textView) {
        final String[] cantidad = {""};
        databaseReference.child("Publishing")
                .child(publishinglist.get(posicion).getPublishid()).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    cantidad[0] = String.valueOf(dataSnapshot.getChildrenCount());
                    textView.setText(cantidad[0]);
                }else{
                    textView.setText("0 ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    @Override
    public void onItenclick(int i) {

        fragment = FragmentcComments.getInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.layoutmainactivity, fragment)
                .commit();

        bundle = new Bundle();

        bundle.putString(EXTRA_USER, firebaseUser.getEmail());
        bundle.putString(EXTRA_DESCRIPTION, publishinglist.get(i).getPicdescription());
        bundle.putString(EXTRA_URL, publishinglist.get(i).getPictureurl());
        bundle.putString(EXTRA_PUBLISH_ID, publishinglist.get(i).getPublishid());

        fragment.setArguments(bundle);
    }

    @Override
    public void countlike(int posicion, final TextView textView, final Button button) {
        final String[] cantidad = {""};


        databaseReference.child("Publishing")
                .child(publishinglist.get(posicion).getPublishid()).child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    cantidad[0] = String.valueOf(dataSnapshot.getChildrenCount());
                    textView.setText(cantidad[0]);
                }else{
                    textView.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

/*        databaseReference
                .child("Publishing")
                .child(publishinglist.get(posicion).getPublishid()).child("Likes")
                .child(publishinglist.get(posicion).getUserid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    button.setBackgroundResource(R.drawable.likemarked);
                } else {
                    button.setBackgroundResource(R.drawable.circle);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/



    }

    @Override
    public void addlike(final int porsicion, final Button button) {


        databaseReference
                .child("Publishing")
                .child(publishinglist.get(porsicion).getPublishid()).child("Likes")
                .child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    button.setBackgroundResource(R.drawable.circle);

                    databaseReference
                            .child("Publishing")
                            .child(publishinglist.get(porsicion).getPublishid()).child("Likes")
                            .child(firebaseUser.getUid()).removeValue();


                } else {
                    button.setBackgroundResource(R.drawable.likemarked);

                    databaseReference
                            .child("Publishing")
                            .child(publishinglist.get(porsicion).getPublishid()).child("Likes")
                            .child(firebaseUser.getUid()).setValue(firebaseUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public void compartir(int posicion) {
        String name = publishinglist.get(posicion).getPicturename();
        String url = publishinglist.get(posicion).getPictureurl();
        String des = publishinglist.get(posicion).getPicdescription();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, name);
        intent.putExtra(Intent.EXTRA_TEXT, "This is a links of image " + url + " " + des);


        startActivity(Intent.createChooser(intent, "Share with"));
    }

    @Override
    public void onClicked() {
        closekeyboard();
    }

    public void closekeyboard() {
        View v = this.getCurrentFocus();

        if (v != null) {

            InputMethodManager inm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }


    }

    @Override
    public String[] getlongitud() {
        // if (!hasPermission()) requestForPermission();
        String[] latiud_longitud = new String[5];

        //obtener el location manager desde el sistema
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        showLastKnowLocation();
        latiud_longitud[0] = String.valueOf(latitud);
        latiud_longitud[1] = String.valueOf(longitud);
        latiud_longitud[2] = countryname;
        latiud_longitud[3] = adminarea;
        latiud_longitud[4] = locality;

        return latiud_longitud;
    }

    @Override
    public String getnameofuser() {

        return Fullname;

    }



    private void requestForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    REQUEST_LOCATION_PERMISSION
            );
        }
    }

    //verificar si tengo permisos
    private boolean hasPermission() {
        return ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    //mostrar el location en el textview
    private void showLocation(Location location) {
        longitud = location.getLongitude();
        latitud = location.getLatitude();

        Geocoder geocoder = new Geocoder(this);
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(latitud, longitud, 10);

        } catch (Throwable e) {
            e.printStackTrace();

        }
        countryname = list.get(0).getCountryName();
        locality = list.get(0).getLocality();
        adminarea = list.get(0).getAdminArea();


    }

    //mostrar la ultima ubicacion
    @SuppressLint("MissingPermission")
    public void showLastKnowLocation() {

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        showLocation(location);
    }

    //actualizar loction
    @SuppressLint("MissingPermission")
    public void updateLocation(View view) {
        long minTime = 1000;
        float minDistance = 0.f;
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                this
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // asumimos que el usuario acepto los permisso
        // de querer manejar esto mejor ver la siguiente documentacion
        // https://developer.android.com/guide/topics/permissions/overview
        showLastKnowLocation();
    }

    //BEGIN METHODS LISTENER LOCATION
    //================================================================================
    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {


    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
