package com.example.easyapply;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RecycleViewAdapterComments extends RecyclerView.Adapter<RecycleViewAdapterComments.ExampleViewHolder> {


    private int layout = 0;
    private Context context;
    private List<Comments> listcomments;


    public RecycleViewAdapterComments(Context context, List<Comments> items) {
        this.listcomments = items;
        this.context = context;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        layout = R.layout.layout_comment;
        View view = LayoutInflater.from(context).inflate(layout, viewGroup, false);
        ExampleViewHolder evh = new ExampleViewHolder(view);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, int i) {
        Utilities utilities = new Utilities();
        Comments comments = listcomments.get(i);
        String usuario = comments.getUserid();
        String comentario = comments.getComentario();
        Long fecha = comments.getDateofpublication();

        String dateresult= utilities.timeBeforePublication(fecha);
        exampleViewHolder.comentario.setText(comentario);

        exampleViewHolder.username.setText(usuario);
        exampleViewHolder.today.setText(dateresult);

    }

    @Override
    public int getItemCount() {
        return listcomments.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView comentario;
        private TextView today;


        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.txtusuariocomentar);
            comentario = itemView.findViewById(R.id.txtcomentario);
            today = itemView.findViewById(R.id.txtfecha);
        }
    }
}
