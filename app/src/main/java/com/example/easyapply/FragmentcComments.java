package com.example.easyapply;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.example.easyapply.MainActivity.EXTRA_DESCRIPTION;
import static com.example.easyapply.MainActivity.EXTRA_PUBLISH_ID;
import static com.example.easyapply.MainActivity.EXTRA_URL;
import static com.example.easyapply.MainActivity.EXTRA_USER;

public class FragmentcComments extends Fragment {
    private ImageView imageView;
    private EditText editTextComment;
    private List<Comments> listcomments = new ArrayList<>();
    private String url;
    private String description;
    private String user;
    private String publishid;
    private Button btnsendcomment;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SimpleDateFormat dateFormat;
    public static FragmentcComments instance = null;
    private RecyclerView mRecyclerView;
    private RecycleViewAdapterComments mRecycleViewAdapterComments;
    private Utilities utilities = new Utilities();


    public FragmentcComments() {
    }

    public static FragmentcComments getInstance() {

        if (instance == null) {
            instance = new FragmentcComments();

        }
        return instance;
    }

    private void inicializerdb() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(EXTRA_URL);
            description = getArguments().getString(EXTRA_DESCRIPTION);
            user = getArguments().getString(EXTRA_USER);
            publishid = getArguments().getString(EXTRA_PUBLISH_ID);

        }
        inicializerdb();
        databaseReference.child("Publishing").child(publishid).child("Comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listcomments.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Comments comments = postSnapshot.getValue(Comments.class);

                    listcomments.add(comments);

                }
                fillrecyclwviewcomments();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fragmentc_comments, container, false);
        mRecyclerView = v.findViewById(R.id.recyclleviewcomments);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnsendcomment = v.findViewById(R.id.btnenviar);
        imageView = v.findViewById(R.id.imageviewpictocomment);
        editTextComment = v.findViewById(R.id.edtcomentar);
        Picasso.get()
                .load(url)
                .into(imageView);


        btnsendcomment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (utilities.edittextvalidator(editTextComment)) {
                    Comments comments = new Comments(
                            databaseReference.push().getKey(),
                            publishid,
                            user,
                            editTextComment.getText().toString()

                    );

                    databaseReference
                            .child("Publishing")
                            .child(publishid)
                            .child("Comments")
                            .child(databaseReference.push().getKey()).setValue(comments);

                    editTextComment.setText("");

                    if (listener != null) listener.onClicked();


                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClicked();

            }
        });
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (listener != null) listener.onClicked();

                return false;
            }
        }) ;

        return v;
    }

    public void fillrecyclwviewcomments() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecycleViewAdapterComments = new RecycleViewAdapterComments(instance.getContext(), listcomments);
                mRecyclerView.setAdapter(mRecycleViewAdapterComments);

            }
        }, 1000);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public interface Listener {
        void onClicked();
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }


}
