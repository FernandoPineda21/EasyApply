package com.example.easyapply;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


public class FragmentUploadImage extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static FragmentUploadImage instance = null;
    private int PICK_IMAGE_REQUEST = 210;
    private Uri urimage;
    private Button btnupload, btnchoosepic;
    private EditText editTextname, editTextdescription;
    private ImageView imageViewpic;
    private ProgressBar progressBar;


    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private String mParam1;
    private String mParam2;


    public interface Listener {
        String[] getlongitud();

        String getnameofuser();
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public FragmentUploadImage() {
        // Required empty public constructor
    }


    public static FragmentUploadImage getInstance() {
        if (instance == null) {
            instance = new FragmentUploadImage();
        }
        return instance;
    }

    @Override
    public void onDestroyView() {
        instance = null;
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_upload_image, container, false);
        btnchoosepic = view.findViewById(R.id.btnchoosepic);
        btnupload = view.findViewById(R.id.btnuploadpic);
        editTextname = view.findViewById(R.id.edittextpicname);
        editTextdescription = view.findViewById(R.id.edittextdescription);
        imageViewpic = view.findViewById(R.id.imageviewpictoup);
        progressBar = view.findViewById(R.id.progressbaruploadingimg);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btnchoosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                upLoadPic();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;

    }


    public void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtenstion(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void
    upLoadPic() {

        if (urimage != null) {

            final String currenttime = String.valueOf(System.currentTimeMillis());

            StorageReference fileReference = storageReference.child(currenttime + firebaseUser.getEmail() + firebaseUser.getUid() + "." + getFileExtenstion(urimage));

            fileReference.putFile(urimage).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.child(currenttime + firebaseUser.getEmail() + firebaseUser.getUid() + "." + getFileExtenstion(urimage)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String publish = databaseReference.push().getKey();

                                    String[] obtenerubicacion= new String[5];String fullname="";
                                    if (listener!=null){
                                        fullname= listener.getnameofuser();
                                        obtenerubicacion =listener.getlongitud();
                                    }


                                    Publishing publishing = new Publishing(
                                            publish,
                                            firebaseUser.getUid(),
                                            task.getResult().toString(),
                                            fullname,
                                            editTextdescription.getText().toString(),
                                            obtenerubicacion[0],
                                            obtenerubicacion[1],
                                            obtenerubicacion[2],
                                            obtenerubicacion[3],
                                            obtenerubicacion[4]


                                    );

                                    databaseReference
                                            .child("Publishing")
                                            .child(publish).setValue(publishing);

                                    progressBar.setVisibility(View.INVISIBLE);

                                    Toast.makeText(getContext(), "Published", Toast.LENGTH_SHORT).show();

                                    getActivity().onBackPressed();


                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Uploading Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });


        } else {
            progressBar.setVisibility(View.INVISIBLE);

            Toast.makeText(this.getContext(), "Picture is not exists", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            urimage = data.getData();
            imageViewpic.setImageURI(urimage);
        }
    }



}
