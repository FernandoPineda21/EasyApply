package com.example.easyapply;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecycleViewAdapter
        extends RecyclerView.Adapter<RecycleViewAdapter.ExampleViewHolder> {

    public OnItemClickListener listener;
    private int layout = 0;
    private List<Publishing> listpublishing;
    private Context context;

    public interface OnItemClickListener {
        void gettotalcomments(int posicion, TextView textView);

        void onItenclick(int i);

        void addlike(int posicion, Button button);

        void compartir(int posicion);

        void countlike(int posicion, TextView textView, Button button);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public RecycleViewAdapter(Context context, List<Publishing> items) {
        this.listpublishing = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        layout = R.layout.layout_type_publish;

        View view = LayoutInflater.from(context).inflate(layout, viewGroup, false);


        ExampleViewHolder evh = new ExampleViewHolder(view);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int i) {
        Publishing currentpublising = listpublishing.get(i);
        String publishid = currentpublising.getPublishid();
        String userid = currentpublising.getUserid();
        String urlpic = currentpublising.getPictureurl();
        String picname = currentpublising.getPicturename();
        String picdescription = currentpublising.getPicdescription();
        String localidad = currentpublising.getCountryname() + ", " + currentpublising.getAdminareaname() + ",\n" + currentpublising.getLocalityname();

        listener.countlike(i, holder.like, holder.buttonlike);
        listener.gettotalcomments(i,holder.textviewtotalcomments);


        holder.txtname.setText(picname);
        holder.textViewlocation.setText(localidad);
        holder.txtdescription.setText(picdescription);
        Picasso.get()
                .load(urlpic)
                .into(holder.imageView);


    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView txtdescription;
        public TextView txtname, like;
        public Button buttoncomment, buttonlike, buttonshare, buttonmap;
        public int position = getAdapterPosition();
        public TextView textViewlocation,textviewtotalcomments;


        public ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageviewpicpublished);
            txtdescription = itemView.findViewById(R.id.txtdescripcion);
            txtname = itemView.findViewById(R.id.txtuser);
            buttoncomment = itemView.findViewById(R.id.buttoncoment);
            buttonlike = itemView.findViewById(R.id.buttonlikes);
            buttonshare = itemView.findViewById(R.id.buttonshar);
            like = itemView.findViewById(R.id.textviewlikes);
            buttonmap = itemView.findViewById(R.id.buttonlocation);
            textViewlocation = itemView.findViewById(R.id.textviewlocalidad);
            textviewtotalcomments= itemView.findViewById(R.id.textviewtotalcomments);

            buttoncomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) ;
                        listener.onItenclick(position);
                        listener.gettotalcomments(position,textviewtotalcomments);
                    }
                }
            });

            buttonlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) ;
                        listener.addlike(position, buttonlike);
                        listener.countlike(position, like, buttonlike);


                    }
                }
            });
            buttonshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) ;
                        listener.compartir(position);
                    }
                }
            });
            buttonmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) ;

                        Uri mUri = Uri.parse("geo:0,0?q="
                                + listpublishing.get(position).getLactitud()
                                + ","
                                + listpublishing.get(position).getLongiud()
                                + "(" + "Mi Ubicacion" + ")");
                        Intent intent = new Intent(Intent.ACTION_VIEW, mUri);
                        intent.setPackage("com.google.android.apps.maps");
                        if (intent.resolveActivity(context.getPackageManager()) != null) {

                            context.startActivity(intent);

                        }
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return listpublishing.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
