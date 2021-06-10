package com.example.firebasepdfreader;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.w3c.dom.Text;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder> {


    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model) {

        holder.header.setText(model.getFilename());

        holder.readcount.setText(String.valueOf(model.getNov()));
        holder.upcount.setText(String.valueOf(model.getNol()));
        holder.downcount.setText(String.valueOf(model.getNod()));

                holder.img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(holder.img1.getContext(),viewpdf.class);
                        intent.putExtra("filename",model.getFilename());
                        intent.putExtra("fileurl",model.getFileurl());

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        holder.img1.getContext().startActivity(intent);

                    }
                });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
    return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{

        ImageView img1;
        TextView header;
        ImageView readbook,upvote,downvote;
        TextView readcount,upcount,downcount;


        public myviewholder(@NonNull View itemView) {

            super(itemView);

            img1=itemView.findViewById(R.id.img1);
            header=itemView.findViewById(R.id.header);

            readbook=itemView.findViewById(R.id.watch);
            upvote=itemView.findViewById(R.id.upvote);
            downvote=itemView.findViewById(R.id.downvote);

            readcount=itemView.findViewById(R.id.watchcount);
            upcount=itemView.findViewById(R.id.upvotecount);
            downcount=itemView.findViewById(R.id.downvotecount);




        }
    }

}
